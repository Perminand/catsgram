package ru.yandex.practicum.catsgram.exception;

public class ConditionsNotMetException extends RuntimeException{
    public ConditionsNotMetException(String s){
        super(s);
    }
}
