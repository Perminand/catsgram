package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;
import ru.yandex.practicum.catsgram.staticClass.SortOrder;

import java.util.Collection;
import java.util.Optional;

import static ru.yandex.practicum.catsgram.staticClass.SortOrder.ASCENDING;
import static ru.yandex.practicum.catsgram.staticClass.SortOrder.DESCENDING;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final int SIZE_DEFAULT = 10;
    private final int FROM_DEFAULT = 5;
    private final String SORT_DEFAULT = "asc";

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/search")
    public Collection<Post> findAll(
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<Integer> from,
            @RequestParam Optional<String> sort) {
        if(!SortOrder.from(sort.get()).equals(ASCENDING)||!SortOrder.from(sort.get()).equals(DESCENDING)){
            throw new ParameterNotValidException("sort","Получено: " +sort.get()+". Должно быть: \"ascending\" или \"descending\", \"asc\" или \"desc\" ");
        }
        if(size.get()<=0){
            throw new ParameterNotValidException("size", "Размер должен быть больше нуля");
        }
        if(from.get()<=0){
            throw new ParameterNotValidException("from", "Начало выборки должно быть положительным числом");
        }
        return postService.findAll(size.orElse(SIZE_DEFAULT),from.orElse(FROM_DEFAULT),sort.orElse(SORT_DEFAULT));
    }

    @GetMapping("/{postId}")
    public Post findPostById(@PathVariable("postId") long postId) throws ConditionsNotMetException {
        if (postService.findPostById(postId).isPresent()) {
            return postService.findPostById(postId).get();
        } else {
            throw new ConditionsNotMetException("Нет User с заданным id");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) throws ConditionsNotMetException {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post post) throws ConditionsNotMetException, NotFoundException {
        return postService.update(post);
    }
}
