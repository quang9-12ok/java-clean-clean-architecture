package com.example.bookmanagement.usecaseEdit;

public interface EditBookOutputBoundary {
    void presentEditResult(String message);
    void presentError(String errorMessage);
    void presentEditedBook(EditBookOutputDTO outputDTO);
}