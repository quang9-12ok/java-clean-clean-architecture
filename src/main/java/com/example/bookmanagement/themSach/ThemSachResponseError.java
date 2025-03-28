package com.example.bookmanagement.themSach;

public class ThemSachResponseError {
    private String errorMessage;
    

    // Các mã lỗi cụ thể
    public static final String ERROR_DON_GIA = "Đơn giá không hợp lệ.";
    public static final String ERROR_SO_LUONG = "Số lượng không hợp lệ.";
    public static final String ERROR_DATABASE = "Lỗi kết nối cơ sở dữ liệu.";
    public static final String ERROR_SACH_RONG = "Thông tin sách không được để trống.";
    public static final String ERROR_LOAI_SACH = "Loại sách không hợp lệ.";
    public static final String ERROR_TINH_TRANG_SACH = "Tình trạng sách giáo khoa không được để trống.";
    public static final String ERROR_THUE = "Thuế phải nằm trong khoảng từ 0 đến 100.";
    public static final String ERROR_THEM_SACH = "Không thể thêm sách vào cơ sở dữ liệu.";

    public ThemSachResponseError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        return errorMessage;
    }


    public static ThemSachResponseError invalidPriceError() {
        return new ThemSachResponseError(ERROR_DON_GIA);
    }

    public static ThemSachResponseError invalidQuantityError() {
        return new ThemSachResponseError(ERROR_SO_LUONG);
    }

    public static ThemSachResponseError databaseError() {
        return new ThemSachResponseError(ERROR_DATABASE);
    }

      // Thêm các phương thức error factory mới
      public static ThemSachResponseError emptyBookInfoError() {
        return new ThemSachResponseError(ERROR_SACH_RONG);
    }

    public static ThemSachResponseError invalidBookTypeError() {
        return new ThemSachResponseError(ERROR_LOAI_SACH);
    }

    public static ThemSachResponseError emptyTextBookStatusError() {
        return new ThemSachResponseError(ERROR_TINH_TRANG_SACH);
    }

    public static ThemSachResponseError invalidTaxError() {
        return new ThemSachResponseError(ERROR_THUE);
    }

    public static ThemSachResponseError cannotAddBookError() {
        return new ThemSachResponseError(ERROR_THEM_SACH);
    }

    public static ThemSachResponseError invalidPublisherError() {
        return new ThemSachResponseError("Tên nhà xuất bản không được chứa số");
    }
    
}
