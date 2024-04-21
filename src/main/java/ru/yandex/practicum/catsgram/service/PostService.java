package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import java.util.Optional;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class PostService {
    private final UserService userService;
    private final Map<Long, Post> posts = new HashMap<>();

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public Optional<Post> findById(int id) {
        return Optional.ofNullable(posts.get((long)id));
    }
        public Post create (Post post) throws ConditionsNotMetException {
            Long idAuthor = post.getAuthorId();
            if (idAuthor == null) {
                throw new ConditionsNotMetException("Нет id автора");
            } else {
                if (userService.findUserById(post.getAuthorId()).isEmpty()) {
                    throw new ConditionsNotMetException(String.format("Автор с id %d не найден", idAuthor));
                }
            }
            if (post.getDescription() == null || post.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            post.setId(getNextId());
            post.setPostDate(Instant.now());
            posts.put(post.getId(), post);
            return post;
        }

        public Post update (Post post) throws ConditionsNotMetException, NotFoundException {
            if (posts.containsKey(post.getId())) {
                Post oldPost = posts.get(post.getId());
                Long idAuthor = post.getAuthorId();
                if (idAuthor == null) {
                    throw new ConditionsNotMetException("Нет id автора");
                } else {
                    if (userService.findUserById(post.getAuthorId()).isEmpty()) {
                        throw new ConditionsNotMetException(String.format("Автор с id %d не найден", idAuthor));
                    }
                }
                if (post.getDescription() == null || post.getDescription().isBlank()) {
                    throw new ConditionsNotMetException("Описание не может быть пустым");
                }
                oldPost.setDescription(post.getDescription());
                return oldPost;
            }
            throw new NotFoundException(String.format("Пост с id %d не найден", post.getId()));
        }


        private long getNextId () {
            long currentMaxId = posts.keySet()
                    .stream()
                    .mapToLong(id -> id)
                    .max()
                    .orElse(0);
            return ++currentMaxId;
        }
    }
