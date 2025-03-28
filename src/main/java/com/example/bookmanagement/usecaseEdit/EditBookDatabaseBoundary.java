package com.example.bookmanagement.usecaseEdit;

import com.example.bookmanagement.entity.Book;

public interface EditBookDatabaseBoundary {
    Book getBookById(int  maSach); 
    boolean editBook(Book book); 
}