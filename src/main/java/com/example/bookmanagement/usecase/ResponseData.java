package com.example.bookmanagement.usecase;

import java.util.List;

import com.example.bookmanagement.entity.Book;

public interface ResponseData {
    List<Book> getDeletedBooks();
    boolean isSuccess();
    String getMessage();
}
