package com.example.bookmanagement.tinhTrungBinhDonGia;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.example.bookmanagement.entity.*;
import com.example.bookmanagement.database.*;

public class TinhTrungBinhDonGiaUseCase implements TinhTrungBinhDonGiaInputBoundary {
    private final TinhTrungBinhDonGiaOutputBoundary outputBoundary;
    private final TBDatabaseBoundary sachDatabase;

    public TinhTrungBinhDonGiaUseCase(TinhTrungBinhDonGiaOutputBoundary outputBoundary, TBDatabaseBoundary sachDatabase) {
        this.outputBoundary = outputBoundary;
        this.sachDatabase = sachDatabase;
    }
    @Override
public void execute(int selectedRowIndex) {
    System.out.println("Executing tinhTrungBinhDonGia with row index: " + selectedRowIndex);
    DataExport response = new DataExport();

    try {
        List<Book> bookList = sachDatabase.getBooks();

        // Kiểm tra chỉ số hàng
        if (selectedRowIndex < 0 || selectedRowIndex >= bookList.size()) {
            handleInvalidIndex(response);
            return;
        }

        Book selectedBook = bookList.get(selectedRowIndex);

        // In ra thông tin chi tiết của sách được chọn
        System.out.println("Selected Book Details:");
        System.out.println("Mã sách: " + (selectedBook != null ? selectedBook.getMaSach() : "N/A"));
        System.out.println("Loại sách: " + (selectedBook != null ? selectedBook.getLoaiSach() : "N/A"));

        // Kiểm tra xem sách được chọn có phải là sách tham khảo không
        if (!(selectedBook instanceof ReferenceBook)) {
            handleNotReferenceBook(response);
            return;
        }

        // Tính toán cho sách tham khảo được chọn
        calculateSelectedBookPrice((ReferenceBook) selectedBook, response);

    } catch (SQLException e) {
        handleDatabaseError(e, response);
    }
    
}

    
private void calculateSelectedBookPrice(ReferenceBook selectedBook, DataExport response) {
    try {
        // Kiểm tra sách được chọn có phải sách tham khảo không
        if (selectedBook == null) {
            handleNotReferenceBook(response);
            return;
        }

        // Lấy thông tin của sách được chọn
        double donGia = selectedBook.getDonGia();
        int soLuong = selectedBook.getSoLuong();

        // Tính trung bình đơn giá của sách được chọn
        double averagePrice = soLuong > 0 ? donGia / soLuong : 0.0;

        // Chuẩn bị response
        response.setSuccess(true);
        response.setMessage("Tính đơn giá thành công");
        response.setAveragePrice(averagePrice);
        response.setTotalBooks(soLuong);

        // Log thông tin để debug
        System.out.println("Chi tiết sách được chọn:");
        System.out.println("Đơn giá: " + donGia);
        System.out.println("Số lượng: " + soLuong);
        System.out.println("Trung bình đơn giá: " + averagePrice);

        // Gửi kết quả
        outputBoundary.presentResult(new TinhTrungBinhDonGiaOutputDTO(response));

    } catch (Exception e) {
        // Xử lý nếu có lỗi
        response.setSuccess(false);
        response.setMessage("Lỗi khi tính toán: " + e.getMessage());
        response.setAveragePrice(0.0);
        response.setTotalBooks(0);
        
        outputBoundary.presentError("Lỗi khi tính toán: " + e.getMessage());
    }
}

    
    
    private void handleEmptyList(DataExport response) {
        response.setSuccess(false);
        response.setMessage("Danh sách sách tham khảo trống");
        response.setAveragePrice(0.0);
        response.setTotalBooks(0);

        outputBoundary.presentError("Danh sách sách tham khảo trống.");
    }

    private void handleInvalidIndex(DataExport response) {
        response.setSuccess(false);
        response.setMessage("Vui lòng chọn mã sách của sách tham khảo");
        response.setAveragePrice(0.0);
        response.setTotalBooks(0);

        outputBoundary.presentError("Vui lòng chọn mã sách của sách tham khảo");
    }
    private void handleNotReferenceBook(DataExport response) {
        response.setSuccess(false);
        response.setMessage("Sách được chọn không phải sách tham khảo");
        response.setAveragePrice(0.0);
        response.setTotalBooks(0);

        outputBoundary.presentError("Sách được chọn không phải sách tham khảo");
    }

    private void handleDatabaseError(SQLException e, DataExport response) {
        response.setSuccess(false);
        response.setMessage("Lỗi database: " + e.getMessage());
        response.setAveragePrice(0.0);
        response.setTotalBooks(0);

        outputBoundary.presentError("Lỗi khi truy xuất dữ liệu: " + e.getMessage());
    }
}