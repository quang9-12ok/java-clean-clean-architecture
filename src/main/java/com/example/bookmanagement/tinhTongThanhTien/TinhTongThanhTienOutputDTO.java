package com.example.bookmanagement.tinhTongThanhTien;

public class TinhTongThanhTienOutputDTO {
    private double tongThanhTienSachGiaoKhoa;
    private double tongThanhTienSachThamKhao;


    public TinhTongThanhTienOutputDTO(double tongThanhTienSachGiaoKhoa, double tongThanhTienSachThamKhao) {
        this.tongThanhTienSachGiaoKhoa = tongThanhTienSachGiaoKhoa;
        this.tongThanhTienSachThamKhao = tongThanhTienSachThamKhao;
    }


    public double getTongThanhTienSachGiaoKhoa() {
        return tongThanhTienSachGiaoKhoa;
    }


    public double getTongThanhTienSachThamKhao() {
        return tongThanhTienSachThamKhao;
    }
}

    // public double getTongThanhTien(){
    //     return tongThanhTien;
    // }

    
    

