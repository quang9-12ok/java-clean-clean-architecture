package com.example.bookmanagement.usecaseListTB;

import java.util.List;

import com.example.bookmanagement.entity.TextBook;

public interface ResponseDataListTB {
    List<TextBook> getTextBooks();
    boolean isSuccess();
    String getMessage();
    String getPublisher();
}
