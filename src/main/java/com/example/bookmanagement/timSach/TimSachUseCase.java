package com.example.bookmanagement.timSach;

import com.example.bookmanagement.entity.Book;
import java.util.Date;
import java.util.List;

public class TimSachUseCase implements TimSachInputBoundary {
    private final TimSachDatabaseBoundary databaseBoundary;
    private final TimSachOutputBoundary outputBoundary;

    public TimSachUseCase(TimSachDatabaseBoundary databaseBoundary, TimSachOutputBoundary outputBoundary) {
        this.databaseBoundary = databaseBoundary;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void timSachTheoMa(Integer maSach) {
        try {
            if (maSach == null || maSach <= 0) {
                throw new TimSachResponseError("Mã sách không hợp lệ");
            }
            List<Book> results = databaseBoundary.findBookById(maSach);
            presentResults(results, "mã sách " + maSach);
        } catch (TimSachResponseError e) {
            handleError(e);
        }
    }

    @Override
    public void timSachTheoNgayNhap(Date ngayNhap) {
        try {
            if (ngayNhap == null) {
                throw new TimSachResponseError("Ngày nhập không hợp lệ");
            }
            List<Book> results = databaseBoundary.findBookByDate(ngayNhap);
            presentResults(results, "ngày nhập " + ngayNhap);
        } catch (TimSachResponseError e) {
            handleError(e);
        }
    }

    @Override
    public void timSachTheoNhaXuatBan(String nhaXuatBan) {
        try {
            if (nhaXuatBan == null || nhaXuatBan.trim().isEmpty()) {
                throw new TimSachResponseError("Nhà xuất bản không hợp lệ");
            }
            List<Book> results = databaseBoundary.findBookByPublisher(nhaXuatBan);
            presentResults(results, "nhà xuất bản " + nhaXuatBan);
        } catch (TimSachResponseError e) {
            handleError(e);
        }
    }

    @Override
    public void timSachTheoLoai(String loaiSach) {
        try {
            if (loaiSach == null || loaiSach.trim().isEmpty()) {
                throw new TimSachResponseError("Loại sách không hợp lệ");
            }
            List<Book> results = databaseBoundary.findBookByType(loaiSach);
            presentResults(results, "loại sách " + loaiSach);
        } catch (TimSachResponseError e) {
            handleError(e);
        }
    }

    @Override
    public void timSachTheoDonGia(Double donGia) {
        try {
            if (donGia == null || donGia < 0) {
                throw new TimSachResponseError("Đơn giá không hợp lệ");
            }
            List<Book> results = databaseBoundary.findBookByPrice(donGia);
            presentResults(results, "đơn giá " + donGia);
        } catch (TimSachResponseError e) {
            handleError(e);
        }
    }

    private void presentResults(List<Book> results, String searchCriteria) {
        DataExport dataExport;
        if (results.isEmpty()) {
            dataExport = DataExport.createErrorResponse("Không tìm thấy sách nào với " + searchCriteria, false);
        } else {
            dataExport = DataExport.createSuccessResponse(results, "Tìm thấy " + results.size() + " sách với " + searchCriteria);
        }
        outputBoundary.presentSearchResults(dataExport);
    }

    private void handleError(TimSachResponseError error) {
        DataExport dataExport = DataExport.createErrorResponse(error.getMessage(), true);
        outputBoundary.presentSearchResults(dataExport);
    }
}