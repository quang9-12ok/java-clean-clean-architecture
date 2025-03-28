package com.example.bookmanagement.usecase;

import java.util.List;


import com.example.bookmanagement.entity.Book;

public interface BookManagementDatabaseBoundary {
 boolean deleteBook(Book book);  
List<Book> getBooks();  
Book getBookById(int maSach);

        
}
