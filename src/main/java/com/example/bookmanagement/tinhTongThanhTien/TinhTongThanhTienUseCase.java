package com.example.bookmanagement.tinhTongThanhTien;

import java.util.List;
import com.example.bookmanagement.entity.Book;
import com.example.bookmanagement.entity.TextBook;
import com.example.bookmanagement.entity.ReferenceBook;

public class TinhTongThanhTienUseCase implements TinhTongThanhTienInputBoundary {
    private final TinhTongThanhTienDatabaseBoundary sachDatabase;
    private final TinhTongThanhTienOutputBoundary outputBoundary;

    public TinhTongThanhTienUseCase(TinhTongThanhTienDatabaseBoundary sachDatabase, 
                                   TinhTongThanhTienOutputBoundary outputBoundary) {
        this.sachDatabase = sachDatabase;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public TinhTongThanhTienOutputDTO tinhTongTien() {
        try {
            List<Book> allBooks = sachDatabase.getBooks();
            if (allBooks.isEmpty()) {
                return new TinhTongThanhTienOutputDTO(0, 0);
            }

            double tongThanhTienGiaoKhoa = tinhTongThanhTienSachGiaoKhoa(allBooks);
            double tongThanhTienThamKhao = tinhTongThanhTienSachThamKhao(allBooks);

            TinhTongThanhTienOutputDTO outputDTO = new TinhTongThanhTienOutputDTO(
                tongThanhTienGiaoKhoa,
                tongThanhTienThamKhao
            );
            outputBoundary.presentResult(outputDTO);
            return outputDTO;
        } catch (Exception e) {
            e.printStackTrace();
            TinhTongThanhTienOutputDTO errorDTO = new TinhTongThanhTienOutputDTO(0, 0);
            outputBoundary.presentResult("Lỗi khi tính tổng thành tiền: " + e.getMessage());
            return errorDTO;
        }
    }

    private double tinhTongThanhTienSachGiaoKhoa(List<Book> books) {
        double tongThanhTien = 0;
        for (Book book : books) {
            if (book instanceof TextBook) {
                tongThanhTien += tinhThanhTienSachGiaoKhoa((TextBook) book);
            }
        }
        return tongThanhTien;
    }

    private double tinhTongThanhTienSachThamKhao(List<Book> books) {
        double tongThanhTien = 0;
        for (Book book : books) {
            if (book instanceof ReferenceBook) {
                tongThanhTien += tinhThanhTienSachThamKhao((ReferenceBook) book);
            }
        }
        return tongThanhTien;
    }

    private double tinhThanhTienSachGiaoKhoa(TextBook sachGiaoKhoa) {
        double thanhTien = sachGiaoKhoa.getDonGia() * sachGiaoKhoa.getSoLuong();
        
        if ("Mới".equalsIgnoreCase(sachGiaoKhoa.getTinhTrang())) {
            return thanhTien; // Sách mới không giảm giá
        } else if ("Cũ".equalsIgnoreCase(sachGiaoKhoa.getTinhTrang())) {
            return thanhTien * 0.5; // Sách cũ giảm 50%
        }
        
        return thanhTien;
    }

    private double tinhThanhTienSachThamKhao(ReferenceBook sachThamKhao) {
        double thanhTien = sachThamKhao.getDonGia() * sachThamKhao.getSoLuong();
        return thanhTien + (thanhTien * sachThamKhao.getThue() / 100);
    }
}