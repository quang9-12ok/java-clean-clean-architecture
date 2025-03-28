package com.example.bookmanagement.usecaseEdit;


public class DataExportEdit implements ResponseDataEdit {
    private int maSach;
    private String loaiSach;
    private String tinhTrang;
    private Double thue;
    private Double donGia; 
    private Integer soLuong; 
    private String nhaXuatBan;

    public DataExportEdit(int maSach, String loaiSach, String tinhTrang, Double thue,Double donGia, Integer soLuong, String nhaXuatBan ) {
        this.maSach = maSach;
        this.loaiSach = loaiSach;
        this.tinhTrang = tinhTrang;
        this.thue = thue;
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.nhaXuatBan = nhaXuatBan;
    }

    @Override
    public int getMaSach() {
        return maSach;
    }

    @Override
    public String getLoaiSach() {
        return loaiSach;
    }

    @Override
    public String getTinhTrang() {
        return tinhTrang;
    }

    @Override
    public Double getThue() {
        return thue;
    }

    @Override
    public void setMaSach(int maSach) {
        this.maSach = maSach;
    }

    @Override
    public void setLoaiSach(String loaiSach) {
        this.loaiSach = loaiSach;
    }

    @Override
    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    @Override
    public void setThue(Double thue) {
        this.thue = thue;
    }
    public Double getDonGia() {
        return donGia;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public String getNhaXuatBan() {
        return nhaXuatBan;
    }
    public void setDonGia(Double donGia) {
        this.donGia = donGia;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public void setNhaXuatBan(String nhaXuatBan) {
        this.nhaXuatBan = nhaXuatBan;
    }

}
