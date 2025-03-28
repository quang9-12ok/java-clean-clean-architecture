package com.example.bookmanagement.themSach;

import java.sql.SQLException;
import com.example.bookmanagement.database.*;
import com.example.bookmanagement.entity.*;

public class ThemSachUseCase implements ThemSachInputBoundary {
    private final ThemSachDatabaseBoundary themSachdatabaseBoundary;
    private final ThemSachOutputBoundary themSachoutputBoundary;
    private ThemSachResponseError responseError;

    public ThemSachUseCase(ThemSachDatabaseBoundary themSachdatabaseBoundary, ThemSachOutputBoundary themSachoutputBoundary) {
        this.themSachdatabaseBoundary = themSachdatabaseBoundary;
        this.themSachoutputBoundary = themSachoutputBoundary;
    }

    public class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }
    
    @Override
    public void execute(Book book) {
        try {
            // Kiểm tra thông tin sách
            validateBook(book);  // Remove return check
    
            // Thêm sách vào cơ sở dữ liệu
            int result = themSachdatabaseBoundary.themSach(book);
    
            if (result > 0) {
                // Tạo DTO và thông báo thành công
                ThemSachOutputDTO outputDTO = new ThemSachOutputDTO(book);
                outputDTO.setDataExport(new DataExport(true, createBookInformation(book)));
                themSachoutputBoundary.prepareSuccessView(outputDTO);
            } else {
                // Thông báo lỗi nếu không thể thêm sách
                throw new ValidationException("Không thể thêm sách vào cơ sở dữ liệu.");
            }
        } catch (Exception e) {
            handleException(e);
        }
    }
    
    public void validateBook(Book book) {
        // Kiểm tra sách null
        if (book == null) {
            throw new ValidationException("Thông tin sách không được để trống.");
        }
    
        // Kiểm tra nhà xuất bản
        if (!isValidPublisher(book.getNhaXuatBan())) {
            throw new ValidationException("Nhà xuất bản không hợp lệ.");
        }
    
        // Kiểm tra đơn giá
        if (book.getDonGia() <= 0) {
            throw new ValidationException("Đơn giá phải lớn hơn 0.");
        }
    
        // Kiểm tra số lượng
        if (book.getSoLuong() < 0) {
            throw new ValidationException("Số lượng không được âm.");
        }
    
        // Kiểm tra theo loại sách
        if (book instanceof TextBook) {
            validateSachGiaoKhoa((TextBook) book);
        } else if (book instanceof ReferenceBook) {
            validateSachThamKhao((ReferenceBook) book);
        } else {
            throw new ValidationException("Loại sách không hợp lệ.");
        }
    }
    
    private boolean isValidPublisher(String publisher) {
        if (publisher == null || publisher.trim().isEmpty()) {
            throw new ValidationException("Nhà xuất bản không được để trống.");
        }
        
        if (publisher.matches(".*\\d+.*")) {
            throw new ValidationException("Nhà xuất bản không được chứa số.");
        }
        
        return true;
    }
    
    public void validateSachGiaoKhoa(TextBook textBook) {
        if (textBook.getTinhTrang() == null || textBook.getTinhTrang().trim().isEmpty()) {
            throw new ValidationException("Tình trạng sách giáo khoa không được để trống.");
        }
    }
    
    
    public void validateSachThamKhao(ReferenceBook referenceBook) {
        double thue = referenceBook.getThue();
        if (thue < 0 || thue > 100) {
            throw new ValidationException("Thuế phải nằm trong khoảng từ 0 đến 100.");
        }
    }
    
    public void handleException(Exception e) {
        if (e instanceof ValidationException) {
            themSachoutputBoundary.prepareFailView(
                new ThemSachResponseError(e.getMessage())
            );
        } else if (e instanceof SQLException) {
            themSachoutputBoundary.prepareFailView(ThemSachResponseError.databaseError());
        } else {
            themSachoutputBoundary.prepareFailView(
                new ThemSachResponseError("Lỗi hệ thống: " + e.getMessage())
            );
        }
    }
    

    public String createBookInformation(Book book) {
        // Tạo thông tin chi tiết sách
        StringBuilder info = new StringBuilder("THÔNG TIN SÁCH:\n--------------------\n");
        info.append(String.format(
            "Mã sách: %d\nLoại sách: %s\nĐơn giá: %.2f\nSố lượng: %d\nNgày nhập: %s\nNhà Xuất Bản: %s\n",
            book.getMaSach(), book.getLoaiSach(), book.getDonGia(), 
            book.getSoLuong(), book.getNgayNhap(), book.getNhaXuatBan()
        ));

        if (book instanceof TextBook) {
            info.append(String.format("Tình trạng: %s\n", ((TextBook) book).getTinhTrang()));
        } else if (book instanceof ReferenceBook) {
            info.append(String.format("Thuế: %.2f%%\n", ((ReferenceBook) book).getThue()));
        }

        info.append("--------------------\n");
        return info.toString();
    }

    // public void handleException(Exception e) {
    //     // Xử lý ngoại lệ
    //     if (e instanceof SQLException) {
    //         themSachoutputBoundary.prepareFailView(ThemSachResponseError.databaseError());
    //     } else {
    //         themSachoutputBoundary.prepareFailView(
    //             new ThemSachResponseError("Lỗi hệ thống: " + e.getMessage())
    //         );
    //     }
    // }
}
