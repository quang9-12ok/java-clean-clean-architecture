package com.example.bookmanagement.tinhTrungBinhDonGia;

import java.sql.SQLException;
import java.util.List;
import com.example.bookmanagement.entity.Book;

public interface TBDatabaseBoundary {
    List<Book> getBooks() throws SQLException;
}
