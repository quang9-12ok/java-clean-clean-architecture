package com.example.bookmanagement.tinhTongThanhTien;

import java.sql.SQLException;
import java.util.List;
import com.example.bookmanagement.entity.Book;

public interface TinhTongThanhTienDatabaseBoundary {
    List<Book> getBooks() throws SQLException;
   
    
}