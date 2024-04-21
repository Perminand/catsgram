package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Collection<Post> findAll() {
        return postService.findAll();
    }

    @GetMapping("/{postId}")
    public Post findById(@PathVariable("postId") Integer postId){
        return postService.findById(postId).orElseThrow(NullPointerException::new);
    }

    @PostMapping
    public Post create(@RequestBody Post post) throws ConditionsNotMetException {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post post) throws ConditionsNotMetException, NotFoundException {
        return postService.update(post);
    }
}
