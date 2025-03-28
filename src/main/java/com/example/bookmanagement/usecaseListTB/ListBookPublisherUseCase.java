package com.example.bookmanagement.usecaseListTB;

import java.util.List;
import java.util.stream.Collectors;

import com.example.bookmanagement.entity.TextBook;

public class ListBookPublisherUseCase implements ListBookPublisherInputBoundary {
    private final ListBookPublisherDatabaseBoundary databaseBoundary;
    private final ListBookPublisherOutputBoundary outputBoundary;

    public ListBookPublisherUseCase(ListBookPublisherDatabaseBoundary databaseBoundary, 
                                    ListBookPublisherOutputBoundary outputBoundary) {
        this.databaseBoundary = databaseBoundary;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(String publisher) {
        boolean shouldRetry;
        do {
            shouldRetry = false;
            try {
                // Kiểm tra đầu vào
                if (publisher == null || publisher.trim().isEmpty()) {
                    throw new ResponseErrorListTB("Tên nhà xuất bản không được để trống");
                }

                // Lấy danh sách sách
                List<TextBook> textBooks = databaseBoundary.getTextBooksByPublisher(publisher);
                
                if (textBooks.isEmpty()) {
                    throw new ResponseErrorListTB("Không tìm thấy sách giáo khoa nào của nhà xuất bản: " + publisher);
                }

                // Tạo và gửi kết quả thành công
                ListBookPublisherOutputDTO outputDTO = new ListBookPublisherOutputDTO(
                    textBooks,
                    true,
                    "Tìm thấy " + textBooks.size() + " sách giáo khoa",
                    publisher
                );
                outputBoundary.presentTextBooks(outputDTO);

            } catch (ResponseErrorListTB e) {
                // Xử lý lỗi và hỏi người dùng có muốn thử lại
                ListBookPublisherOutputDTO errorOutput = new ListBookPublisherOutputDTO(
                    null,
                    false,
                    e.getMessage(),
                    publisher
                );
                errorOutput.setRetryRequested(true);
                outputBoundary.presentTextBooks(errorOutput);
                
                // Lấy kết quả từ presenter
                shouldRetry = errorOutput.isRetryRequested();
                
                // Nếu người dùng muốn thử lại, yêu cầu nhập lại thông qua presenter
                if (shouldRetry) {
                    publisher = requestNewPublisher();
                    if (publisher == null) { // Người dùng đã hủy
                        shouldRetry = false;
                    }
                }
            }
        } while (shouldRetry);
    }

    private String requestNewPublisher() {
        // Tạo một DTO đặc biệt để yêu cầu nhập lại
        ListBookPublisherOutputDTO requestInput = new ListBookPublisherOutputDTO(
            null,
            false,
            "Vui lòng nhập lại tên nhà xuất bản",
            null
        );
        requestInput.setRequestingNewInput(true);
        outputBoundary.presentTextBooks(requestInput);
        return requestInput.getNewPublisherInput();
    }
}