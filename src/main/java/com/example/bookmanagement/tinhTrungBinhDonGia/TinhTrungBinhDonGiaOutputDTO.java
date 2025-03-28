package com.example.bookmanagement.tinhTrungBinhDonGia;

public class TinhTrungBinhDonGiaOutputDTO {
    private final DataExport data;

    public TinhTrungBinhDonGiaOutputDTO(DataExport data) {
        this.data = data;
    }

    public DataExport getData() {
        return data;
    }
    public double getAveragePrice() {
        return data.getAveragePrice();
    }

    public boolean isSuccess() {
        return data.isSuccess();
    }

    public String getMessage() {
        return data.getMessage();
    }

    public int getTotalBooks() {
        return data.getTotalBooks();
    }
}
