package com.example.bookmanagement.themSach;

import java.sql.SQLException;
import com.example.bookmanagement.entity.Book;

public interface ThemSachDatabaseBoundary {
    int themSach(Book book) throws Exception; 
}