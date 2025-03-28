package com.example.bookmanagement.usecase;

import java.util.List;

import com.example.bookmanagement.entity.Book;

public class DeleteBookOutputDTO {
    private List<Book> booksToDelete;
    private int maSach;

    public DeleteBookOutputDTO(List<Book> booksToDelete) {
        this.booksToDelete = booksToDelete;
    }

    public DeleteBookOutputDTO(int maSach) {
        this.maSach = maSach;
    }

    public List<Book> getBooksToDelete() {
        return booksToDelete;
    }

    public int getMaSach() {
        return maSach;
    }

}
