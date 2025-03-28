package com.example.bookmanagement.usecaseListTB;

import java.util.List;

import com.example.bookmanagement.entity.TextBook;

public class DataExportListTB implements ResponseDataListTB {
     private List<TextBook> textBooks;      
    private boolean success;              
    private String message;               
    private String publisher;  // Thêm trường publisher để lưu tên nhà xuất bản
    
    public DataExportListTB(List<TextBook> textBooks, boolean success, String message, String publisher) {
        this.textBooks = textBooks;
        this.success = success;
        this.message = message;
        this.publisher = publisher;
    }
    
    @Override
    public List<TextBook> getTextBooks() {
        return textBooks;
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
    public String getPublisher() {
        return publisher;
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

        return detailMessage.toString();
    }
}
