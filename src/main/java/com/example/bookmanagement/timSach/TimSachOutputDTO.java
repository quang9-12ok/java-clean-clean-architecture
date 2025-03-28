package com.example.bookmanagement.timSach;

import com.example.bookmanagement.entity.Book;
import com.example.bookmanagement.entity.TextBook;
import com.example.bookmanagement.entity.ReferenceBook;
import java.util.List;

public class TimSachOutputDTO implements ResponseData<List<Book>> {
    private final List<Book> foundBooks;
    private final boolean success;
    private final String message;
    private final boolean retryRequested;

    public TimSachOutputDTO(List<Book> foundBooks, boolean success, String message, boolean retryRequested) {
        this.foundBooks = foundBooks;
        this.success = success;
        this.message = message;
        this.retryRequested = retryRequested;
    }

    @Override
    public List<Book> getData() {
        return foundBooks;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public boolean isRetryRequested() {
        return retryRequested;
    }

    public boolean hasResults() {
        return foundBooks != null && !foundBooks.isEmpty();
    }

    public String getDetailedMessage() {
        if (success && hasResults()) {
            StringBuilder details = new StringBuilder();
            details.append("Tìm thấy ").append(foundBooks.size()).append(" kết quả:\n\n");
            
            for (Book book : foundBooks) {
                details.append(book.getDetail()).append("\n");
                details.append("----------------------------------------\n");
            }
            return details.toString();
        } else {
            return message;
        }
    }
}