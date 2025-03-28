package com.example.bookmanagement.themSach;

import com.example.bookmanagement.entity.Book;

public class ThemSachOutputDTO {
    private Book book;
    private DataExport dataExport;


    public ThemSachOutputDTO(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public com.example.bookmanagement.themSach.DataExport getDataExport() {
        return dataExport;
    }

    public void setDataExport(com.example.bookmanagement.themSach.DataExport dataExport) {
        this.dataExport = dataExport;
    }
}