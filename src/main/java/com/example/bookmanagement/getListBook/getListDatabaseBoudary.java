package com.example.bookmanagement.getListBook;
import java.util.List;
import com.example.bookmanagement.entity.Book;
public interface getListDatabaseBoudary {
    List<Book> getBooks() throws Exception;
}
