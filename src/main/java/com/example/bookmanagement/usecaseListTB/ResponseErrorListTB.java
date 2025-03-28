package com.example.bookmanagement.usecaseListTB;

public class ResponseErrorListTB extends Exception {
    private String errorMessage;
    private String errorCode;
    private String publisher;

    // Constructor đầy đủ
    public ResponseErrorListTB(String errorMessage, String errorCode, String publisher) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.publisher = publisher;
    }

    // Constructor với message và code
    public ResponseErrorListTB(String errorMessage, String errorCode) {
        this(errorMessage, errorCode, null);
    }

    // Constructor chỉ với message
    public ResponseErrorListTB(String errorMessage) {
        this(errorMessage, "UNKNOWN_ERROR", null);
    }

    // Getters
    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getPublisher() {
        return publisher;
    }

    // Các hằng số định nghĩa mã lỗi
    public static final String PUBLISHER_NOT_FOUND = "PUBLISHER_NOT_FOUND";
    public static final String EMPTY_PUBLISHER = "EMPTY_PUBLISHER";
    public static final String DATABASE_ERROR = "DATABASE_ERROR";
    public static final String INVALID_PUBLISHER = "INVALID_PUBLISHER";
    public static final String NO_TEXTBOOKS_FOUND = "NO_TEXTBOOKS_FOUND";

    // Các phương thức tạo lỗi cụ thể
    public static ResponseErrorListTB createPublisherNotFoundError(String publisher) {
        return new ResponseErrorListTB(
            "Không tìm thấy nhà xuất bản: " + publisher,
            PUBLISHER_NOT_FOUND,
            publisher
        );
    }

    public static ResponseErrorListTB createEmptyPublisherError() {
        return new ResponseErrorListTB(
            "Tên nhà xuất bản không được để trống",
            EMPTY_PUBLISHER
        );
    }

    public static ResponseErrorListTB createDatabaseError(String details) {
        return new ResponseErrorListTB(
            "Lỗi cơ sở dữ liệu: " + details,
            DATABASE_ERROR
        );
    }

    public static ResponseErrorListTB createInvalidPublisherError(String publisher) {
        return new ResponseErrorListTB(
            "Tên nhà xuất bản không hợp lệ: " + publisher,
            INVALID_PUBLISHER,
            publisher
        );
    }

    public static ResponseErrorListTB createNoTextBooksFoundError(String publisher) {
        return new ResponseErrorListTB(
            "Không tìm thấy sách giáo khoa nào của nhà xuất bản: " + publisher,
            NO_TEXTBOOKS_FOUND,
            publisher
        );
    }

    // Override toString để hiển thị thông tin lỗi đầy đủ
    @Override
    public String toString() {
        StringBuilder error = new StringBuilder();
        error.append("Error Code: ").append(errorCode)
             .append("\nMessage: ").append(errorMessage);
        
        if (publisher != null) {
            error.append("\nPublisher: ").append(publisher);
        }
        
        return error.toString();
    }
}