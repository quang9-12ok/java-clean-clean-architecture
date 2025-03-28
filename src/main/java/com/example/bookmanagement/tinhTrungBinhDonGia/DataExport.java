package com.example.bookmanagement.tinhTrungBinhDonGia;

public class DataExport implements ResponseData {
    private boolean success;
    private String message;
    private double averagePrice;
    private int totalBooks;
    private int selectedRowIndex = -1;

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public double getAveragePrice() { return averagePrice; }
    public void setAveragePrice(double averagePrice) { this.averagePrice = averagePrice; }
    public int getTotalBooks() { return totalBooks; }
    public void setTotalBooks(int totalBooks) { this.totalBooks = totalBooks; }
    public int getSelectedRowIndex() {
        return selectedRowIndex;
    }
    
    public void setSelectedRowIndex(int selectedRowIndex) {
        this.selectedRowIndex = selectedRowIndex;
    }
}