package com.example.bookmanagement.usecase;

import java.util.ArrayList;
import java.util.List;

import com.example.bookmanagement.entity.Book;
import com.example.bookmanagement.entity.ReferenceBook;
import com.example.bookmanagement.entity.TextBook;


public class BookManagementUseCase implements BookManagementInputBoundary {
     private final BookManagementDatabaseBoundary databaseBoundary;
    private final BookManagementOutputBoundary outputBoundary;
    private ResponseData responseData;
    private DeleteBookOutputDTO outputDTO;
   
    public BookManagementUseCase(
        BookManagementDatabaseBoundary databaseBoundary,
        BookManagementOutputBoundary outputBoundary
    ) {
        this.databaseBoundary = databaseBoundary;
        this.outputBoundary = outputBoundary;
        
    }
    public void retrieveBooks() {
        List<Book> books = databaseBoundary.getBooks();
        outputBoundary.presentBooks(books);
    }

    @Override
    public void execute() {
        try {
            // Trường hợp xóa một cuốn sách
            if (outputDTO.getMaSach() != 0) {
                handleSingleBookDeletion();
                return;
            }

            // Trường hợp xóa nhiều sách
            handleMultipleBooksDeletion();

        } catch (Exception e) {
            handleError(e);
        }

        // Gửi kết quả ra output boundary
        outputBoundary.presentDeleteBookOutput(responseData);
    }

    private void handleSingleBookDeletion() {
        List<Book> books = databaseBoundary.getBooks();
        List<Book> deletedBooks = new ArrayList<>();
        
        for (Book book : books) {
            if (book.getMaSach() == outputDTO.getMaSach()) {
                if (deleteBookAndUpdateList(book, deletedBooks)) {
                    createSuccessResponse(deletedBooks);
                } else {
                    createFailureResponse(deletedBooks, "Không thể xóa sách có mã " + outputDTO.getMaSach());
                }
                return;
            }
        }
        
        createFailureResponse(deletedBooks, "Không tìm thấy sách có mã " + outputDTO.getMaSach());
    }

    private void handleMultipleBooksDeletion() {
        List<Book> booksToDelete = outputDTO.getBooksToDelete();
        List<Book> deletedBooks = new ArrayList<>();

        if (booksToDelete == null || booksToDelete.isEmpty()) {
            createFailureResponse(deletedBooks, "Không có sách nào được chọn để xóa");
            return;
        }

        for (Book book : booksToDelete) {
            deleteBookAndUpdateList(book, deletedBooks);
        }

        if (!deletedBooks.isEmpty()) {
            createSuccessResponse(deletedBooks);
        } else {
            createFailureResponse(deletedBooks, "Không thể xóa bất kỳ cuốn sách nào");
        }
    }

    private boolean deleteBookAndUpdateList(Book book, List<Book> deletedBooks) {
        if (databaseBoundary.deleteBook(book)) {
            deletedBooks.add(book);
            return true;
        }
        return false;
    }

    private void createSuccessResponse(List<Book> deletedBooks) {
    StringBuilder message = new StringBuilder("Đã xóa thành công " + deletedBooks.size() + " cuốn sách:\n\n");
    for (Book book : deletedBooks) {
        message.append("Mã sách: ").append(book.getMaSach())
               .append("\nNgày nhập: ").append(book.getNgayNhap())
               .append("\nĐơn giá: ").append(String.format("%.2f", book.getDonGia()))
               .append("\nSố lượng: ").append(book.getSoLuong())
               .append("\nNhà xuất bản: ").append(book.getNhaXuatBan())
               .append("\nLoại sách: ").append(book.getLoaiSach());

        // Kiểm tra và hiển thị thông tin đặc thù của từng loại sách
        if (book instanceof TextBook) {
            message.append("\nTình trạng: ").append(((TextBook) book).getTinhTrang());
        } else if (book instanceof ReferenceBook) {
            message.append("\nThuế: ").append(String.format("%.2f%%", ((ReferenceBook) book).getThue()));
        }
        
        message.append("\n----------------------------------------\n");
    }
    
    responseData = new DataExport(
        deletedBooks,
        true,
        message.toString()
    );
}

    private void createFailureResponse(List<Book> deletedBooks, String message) {
        responseData = new DataExport(
            deletedBooks,
            false,
            message
        );
    }

    private void handleError(Exception e) {
        responseData = new DataExport(
            new ArrayList<>(),
            false,
            "Lỗi hệ thống: " + e.getMessage()
        );
    }

    public void setOutputDTO(DeleteBookOutputDTO outputDTO) {
        this.outputDTO = outputDTO;
    }

    public ResponseData getResponseData() {
        return responseData;
    }
    public List<Book> getUpdatedBooks() {
        return databaseBoundary.getBooks();
    }
    

}
