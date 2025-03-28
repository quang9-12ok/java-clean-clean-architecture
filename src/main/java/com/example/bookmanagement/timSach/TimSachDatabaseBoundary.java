package com.example.bookmanagement.timSach;

import com.example.bookmanagement.entity.Book;
import java.util.Date;
import java.util.List;

public interface TimSachDatabaseBoundary {
    List<Book> findBookById(Integer maSach);
    List<Book> findBookByDate(Date ngayNhap);
    List<Book> findBookByPublisher(String nhaXuatBan);
    List<Book> findBookByType(String loaiSach);
    List<Book> findBookByPrice(Double donGia);
}