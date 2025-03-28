package com.example.bookmanagement.usecase;

import java.util.List;

import com.example.bookmanagement.entity.Book;

public interface BookManagementOutputBoundary {
    void presentDeleteBookOutput(ResponseData responseData);
    void presentBooks(List<Book> books);
}
