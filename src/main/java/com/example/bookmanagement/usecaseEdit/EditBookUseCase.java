package com.example.bookmanagement.usecaseEdit;

import com.example.bookmanagement.entity.Book;
import com.example.bookmanagement.entity.ReferenceBook;
import com.example.bookmanagement.entity.TextBook;
import com.example.bookmanagement.ui.EditBookDialog.ValidationException;

import java.util.Date;

public class EditBookUseCase implements EditBookInputBoundary {
    private final EditBookDatabaseBoundary databaseBoundary;
    private final EditBookOutputBoundary outputBoundary;

    public EditBookUseCase(EditBookDatabaseBoundary databaseBoundary, EditBookOutputBoundary outputBoundary) {
        this.databaseBoundary = databaseBoundary;
        this.outputBoundary = outputBoundary;
    }

    private String createBookEditInformation(Book book) {
        StringBuilder bookInfo = new StringBuilder("THÔNG TIN SÁCH SAU KHI CẬP NHẬT:\n");
        bookInfo.append("--------------------\n");
        bookInfo.append(String.format("Mã sách: %d\n", book.getMaSach()));
        bookInfo.append(String.format("Loại sách: %s\n", book.getLoaiSach()));
        bookInfo.append(String.format("Đơn giá: %.2f\n", book.getDonGia()));
        bookInfo.append(String.format("Số lượng: %d\n", book.getSoLuong()));
        bookInfo.append(String.format("Ngày nhập: %s\n", book.getNgayNhap()));
        bookInfo.append(String.format("Nhà Xuất Bản: %s\n", book.getNhaXuatBan()));

        if (book instanceof TextBook) {
            bookInfo.append(String.format("Tình trạng: %s\n", ((TextBook) book).getTinhTrang()));
        } else if (book instanceof ReferenceBook) {
            bookInfo.append(String.format("Thuế: %.2f%%\n", ((ReferenceBook) book).getThue()));
        }
        
        bookInfo.append("--------------------\n");
        return bookInfo.toString();
    }

    public class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }

    @Override
public void execute(DataExportEdit data) {
    Book bookToEdit = databaseBoundary.getBookById(data.getMaSach());

    if (bookToEdit == null) {
        throw new ValidationException("Không tìm thấy sách với mã: " + data.getMaSach());
    }

    // Kiểm tra nhà xuất bản
    if (data.getNhaXuatBan() == null || data.getNhaXuatBan().trim().isEmpty()) {
        throw new ValidationException("Vui lòng nhập nhà xuất bản.");
    }

    // Kiểm tra loại sách
    if (data.getLoaiSach() == null || data.getLoaiSach().trim().isEmpty()) {
        throw new ValidationException("Vui lòng chọn loại sách.");
    }

    // Kiểm tra nhà xuất bản không chứa số
    if (data.getNhaXuatBan().matches(".*\\d.*")) {
        throw new ValidationException("Tên nhà xuất bản không được chứa số.");
    }

    // Kiểm tra đơn giá
    if (data.getDonGia() == null || data.getDonGia() <= 0) {
        throw new ValidationException("Đơn giá phải lớn hơn 0.");
    }

    // Kiểm tra số lượng
    if (data.getSoLuong() == null || data.getSoLuong() < 0) {
        throw new ValidationException("Số lượng không được là số âm.");
    }

    // Kiểm tra thông tin cụ thể theo loại sách
    if ("Sách Giáo Khoa".equals(data.getLoaiSach())) {
        // Kiểm tra tình trạng sách giáo khoa
        if (data.getTinhTrang() == null || data.getTinhTrang().trim().isEmpty()) {
            throw new ValidationException("Vui lòng chọn tình trạng sách giáo khoa.");
        }
    } else if ("Sách Tham Khảo".equals(data.getLoaiSach())) {
        // Kiểm tra thuế sách tham khảo
        if (data.getThue() == null || data.getThue() < 0 || data.getThue() > 100) {
            throw new ValidationException("Vui lòng nhập thuế từ 0 đến 100.");
        }
    }
    
        // Xử lý chuyển đổi loại sách
        Book updatedBook;
        if ("Sách Giáo Khoa".equals(data.getLoaiSach())) {
            // Nếu đang là sách tham khảo, chuyển sang sách giáo khoa
            updatedBook = new TextBook(
                bookToEdit.getMaSach(), 
                new Date(), 
                data.getDonGia(), 
                data.getSoLuong(), 
                data.getNhaXuatBan(), 
                data.getLoaiSach(), 
                data.getTinhTrang()
            );
        } else if ("Sách Tham Khảo".equals(data.getLoaiSach())) {
            // Nếu đang là sách giáo khoa, chuyển sang sách tham khảo
            updatedBook = new ReferenceBook(
                bookToEdit.getMaSach(), 
                new Date(), 
                data.getDonGia(), 
                data.getSoLuong(), 
                data.getNhaXuatBan(), 
                data.getLoaiSach(), 
                data.getThue()
            );
        } else {
            // Nếu không chuyển đổi loại sách
            updatedBook = bookToEdit;
            updatedBook.setDonGia(data.getDonGia());
            updatedBook.setSoLuong(data.getSoLuong());
            updatedBook.setNhaXuatBan(data.getNhaXuatBan());
            updatedBook.setLoaiSach(data.getLoaiSach());
            
            // Cập nhật thông tin riêng của từng loại sách
            if (updatedBook instanceof TextBook) {
                ((TextBook) updatedBook).setTinhTrang(data.getTinhTrang());
            } else if (updatedBook instanceof ReferenceBook) {
                ((ReferenceBook) updatedBook).setThue(data.getThue());
            }
        }
    
        // Lưu sách đã chỉnh sửa
        databaseBoundary.editBook(updatedBook);
    
        outputBoundary.presentEditResult(createBookEditInformation(updatedBook));
        outputBoundary.presentEditedBook(new EditBookOutputDTO(
            String.valueOf(updatedBook.getMaSach()),
            updatedBook.getLoaiSach(),
            updatedBook.getNhaXuatBan(),
            updatedBook.getDonGia(),
            updatedBook.getSoLuong(),
            (updatedBook instanceof TextBook) ? ((TextBook) updatedBook).getTinhTrang() : "",
            (updatedBook instanceof ReferenceBook) ? ((ReferenceBook) updatedBook).getThue() : 0.0
        ));
    }
    
}
