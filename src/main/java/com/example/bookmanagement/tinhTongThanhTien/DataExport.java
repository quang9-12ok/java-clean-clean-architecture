package com.example.bookmanagement.tinhTongThanhTien;

public class DataExport implements ResponseData {
    private boolean success;
    private String message;
    private double tongThanhTienSachGiaoKhoa;
    private double tongThanhTienSachThamKhao;

    public DataExport(boolean success, String message, double tongThanhTienSachGiaoKhoa, double tongThanhTienSachThamKhao) {
        this.success = success;
        this.message = message;
        this.tongThanhTienSachGiaoKhoa = tongThanhTienSachGiaoKhoa;
        this.tongThanhTienSachThamKhao = tongThanhTienSachThamKhao;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public double getTongThanhTienSachGiaoKhoa() {
        return tongThanhTienSachGiaoKhoa;
    }

    public double getTongThanhTienSachThamKhao() {
        return tongThanhTienSachThamKhao;
    }

    public double getTongThanhTien() {
        return tongThanhTienSachGiaoKhoa + tongThanhTienSachThamKhao;
    }

    public String getFormattedResult() {
        StringBuilder result = new StringBuilder();
        result.append("Kết quả tính tổng thành tiền:\n\n");
        result.append(String.format("Tổng thành tiền sách giáo khoa: %.2f VNĐ\n", tongThanhTienSachGiaoKhoa));
        result.append(String.format("Tổng thành tiền sách tham khảo: %.2f VNĐ\n", tongThanhTienSachThamKhao));
        result.append(String.format("Tổng thành tiền tất cả sách: %.2f VNĐ", getTongThanhTien()));
        return result.toString();
    }

    // Factory methods để tạo các response
    public static DataExport createSuccessResponse(double tongThanhTienSachGiaoKhoa, double tongThanhTienSachThamKhao) {
        return new DataExport(
            true,
            "Tính tổng thành tiền thành công",
            tongThanhTienSachGiaoKhoa,
            tongThanhTienSachThamKhao
        );
    }

    public static DataExport createErrorResponse(String errorMessage) {
        return new DataExport(
            false,
            errorMessage,
            0,
            0
        );
    }
}