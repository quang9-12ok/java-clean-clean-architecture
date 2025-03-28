package com.example.bookmanagement.usecaseListTB;

import java.util.List;

import com.example.bookmanagement.entity.TextBook;

public interface ListBookPublisherDatabaseBoundary {
    // List<TextBook> getAllTextBooks();
    List<TextBook> getTextBooksByPublisher(String publisher);
}
