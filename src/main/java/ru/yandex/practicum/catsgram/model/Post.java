package ru.yandex.practicum.catsgram.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
@Data
@EqualsAndHashCode(of = {"id"})
@Builder
public class Post {
    Long id;
    Long authorId;
    String description;
    Instant postDate;
}
