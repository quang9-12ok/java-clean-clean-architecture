package com.example.bookmanagement.usecaseListTB;

import com.example.bookmanagement.entity.TextBook;
import java.util.List;

public class ListBookPublisherOutputDTO {
    private List<TextBook> textBooks;
    private boolean success;
    private String message;
    private String publisher;
    private int totalBooks;
    private boolean retryRequested;
    private boolean requestingNewInput;
    private String newPublisherInput;

    // Constructor đầy đủ
    public ListBookPublisherOutputDTO(List<TextBook> textBooks, boolean success, 
                                    String message, String publisher) {
        this.textBooks = textBooks;
        this.success = success;
        this.message = message;
        this.publisher = publisher;
        this.totalBooks = textBooks != null ? textBooks.size() : 0;
        this.retryRequested = false;
    }

    // Constructor cho trường hợp lỗi
    public ListBookPublisherOutputDTO(String errorMessage, String publisher) {
        this(null, false, errorMessage, publisher);
    }

    // Getters
    public List<TextBook> getTextBooks() {
        return textBooks;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getTotalBooks() {
        return totalBooks;
    }

    public boolean isRetryRequested() {
        return retryRequested;
    }

    // Setters
    public void setRetryRequested(boolean retryRequested) {
        this.retryRequested = retryRequested;
    }
    public boolean isRequestingNewInput() {
        return requestingNewInput;
    }

    public void setRequestingNewInput(boolean requestingNewInput) {
        this.requestingNewInput = requestingNewInput;
    }

    public String getNewPublisherInput() {
        return newPublisherInput;
    }

    public void setNewPublisherInput(String newPublisherInput) {
        this.newPublisherInput = newPublisherInput;
    }

    // Phương thức tạo thông báo chi tiết
    public String createDetailMessage() {
        if (!success || textBooks == null || textBooks.isEmpty()) {
            return message;
        }

        StringBuilder detailMessage = new StringBuilder();
        detailMessage.append("Danh sách sách giáo khoa của nhà xuất bản ")
                    .append(publisher)
                    .append(":\n\n");

        for (TextBook book : textBooks) {
            detailMessage.append("Mã sách: ").append(book.getMaSach())
                        .append("\nNgày nhập: ").append(book.getNgayNhap())
                        .append("\nĐơn giá: ").append(String.format("%.2f", book.getDonGia()))
                        .append("\nSố lượng: ").append(book.getSoLuong())
                        .append("\nNhà xuất bản: ").append(book.getNhaXuatBan())
                        .append("\nTình trạng: ").append(book.getTinhTrang())
                        .append("\n----------------------------------------\n");
        }

        detailMessage.append("\nTổng số sách: ").append(totalBooks);
        return detailMessage.toString();
    }

    // Phương thức kiểm tra có sách hay không
    public boolean hasBooks() {
        return textBooks != null && !textBooks.isEmpty();
    }

    // Phương thức tính tổng giá trị sách
    public double calculateTotalValue() {
        if (!hasBooks()) {
            return 0.0;
        }
        return textBooks.stream()
                       .mapToDouble(book -> book.getDonGia() * book.getSoLuong())
                       .sum();
    }

    // Phương thức tạo summary ngắn gọn
    public String createSummary() {
        if (!success) {
            return message;
        }
        return String.format("Tìm thấy %d sách giáo khoa của nhà xuất bản %s\n" +
                           "Tổng giá trị: %.2f VNĐ", 
                           totalBooks, publisher, calculateTotalValue());
    }

    // Override toString để hiển thị thông tin tổng quan
    @Override
    public String toString() {
        return String.format("ListBookPublisherOutputDTO{success=%s, publisher='%s', " +
                           "totalBooks=%d, message='%s'}", 
                           success, publisher, totalBooks, message);
    }
}