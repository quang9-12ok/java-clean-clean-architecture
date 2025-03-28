package com.example.bookmanagement.tinhTrungBinhDonGia;

public interface TinhTrungBinhDonGiaOutputBoundary {
    void presentResult(TinhTrungBinhDonGiaOutputDTO outputDTO);
    void presentError(String errorMessage);
}
