package com.example.bookmanagement.timSach;

import com.example.bookmanagement.entity.Book;
import java.util.List;

public class DataExport implements ResponseData<List<Book>> {
    private final List<Book> foundBooks;
    private final boolean success;
    private final String message;
    private final boolean retryRequested;

    public DataExport(List<Book> foundBooks, boolean success, String message, boolean retryRequested) {
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

    public static DataExport createSuccessResponse(List<Book> books, String message) {
        return new DataExport(books, true, message, false);
    }

    public static DataExport createErrorResponse(String errorMessage, boolean needRetry) {
        return new DataExport(null, false, errorMessage, needRetry);
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