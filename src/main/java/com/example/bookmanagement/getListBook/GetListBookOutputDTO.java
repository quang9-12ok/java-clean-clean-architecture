package com.example.bookmanagement.getListBook;

import java.util.Date;
import java.util.List;

import com.example.bookmanagement.entity.Book;


public class GetListBookOutputDTO  {
     private List<Book> books;
    private int maSach;
    private Date ngayNhap;
    private double donGia;
    private int soLuong;
    private String nhaXuatBan;
    private String tinhTrang; 
    private Double thue; 
    private String loaiSach; 

    // Constructor
    public GetListBookOutputDTO(List<Book> books,int maSach, Date ngayNhap, double donGia, int soLuong, String nhaXuatBan, String loaiSach ,String tinhTrang, Double thue) {
        this.maSach = maSach;
        this.ngayNhap = ngayNhap;
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.nhaXuatBan = nhaXuatBan;
        this.books = books;
        this.tinhTrang = tinhTrang;
        this.thue = thue;
        this.loaiSach = loaiSach; 
    }

    public List<Book> getBooks() {
        return books;
    }

    // Getters
    public int getMaSach() {
        return maSach;
    }

    public Date getNgayNhap() {
        return ngayNhap;
    }

    public double getDonGia() {
        return donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public String getNhaXuatBan() {
        return nhaXuatBan;
    }

    public String getTinhTrang() {
        System.out.println("DEBUG: DTO getTinhTrang called: " + tinhTrang);
        return tinhTrang;
    }
    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public Double getThue() {
        System.out.println("DEBUG: DTO getThue called for maSach " + maSach + ": " + thue);
        return thue;
    }
    public void setThue(Double thue) {
        System.out.println("DEBUG: DTO getThue called for maSach " + maSach + ": " + thue);
        this.thue = thue;
    }

    public String getLoaiSach() {
        return loaiSach; 
    }
}
