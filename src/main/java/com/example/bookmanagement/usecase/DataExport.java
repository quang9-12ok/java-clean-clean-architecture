package com.example.bookmanagement.usecase;
import java.util.List;

import com.example.bookmanagement.entity.Book;
public class DataExport implements ResponseData{

     private List<Book> deletedBooks;      
    private boolean success;              
    private String message;               
    
    public DataExport(List<Book> deletedBooks, boolean success, String message) {
        this.deletedBooks = deletedBooks;
        this.success = success;
        this.message = message;
    }
    
    
    public List<Book> getDeletedBooks() {
        return deletedBooks;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
}
