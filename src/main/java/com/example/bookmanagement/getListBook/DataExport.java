package com.example.bookmanagement.getListBook;

import java.util.List;

public class DataExport implements ResponseData {
    private List<GetListBookOutputDTO> bookList;
    private String errorMessage;
    private boolean success;

    public DataExport(List<GetListBookOutputDTO> bookList) {
        this.bookList = bookList;
        this.success = true;
        this.errorMessage = null;
    }

    public DataExport(String errorMessage) {
        this.errorMessage = errorMessage;
        this.success = false;
        this.bookList = null;
    }

    public List<GetListBookOutputDTO> getBookList() {
        return bookList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }
}