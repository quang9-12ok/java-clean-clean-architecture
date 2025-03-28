package com.example.bookmanagement.getListBook;

import java.util.ArrayList;
import java.util.List;
import com.example.bookmanagement.database.*;
import com.example.bookmanagement.entity.Book;
import com.example.bookmanagement.entity.ReferenceBook;
import com.example.bookmanagement.entity.TextBook;


public class GetListBookUseCase implements GetListBookInputBoundary {
    private final getListDatabaseBoudary databaseBoudary;
    private final GetListBookOutputBoundary outputBoundary;

    public GetListBookUseCase(getListDatabaseBoudary databaseBoudary, GetListBookOutputBoundary outputBoundary) {
        this.databaseBoudary = databaseBoudary;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void getListBook() {
        try {
            List<Book> books = databaseBoudary.getBooks();
            
            if (books == null || books.isEmpty()) {
                outputBoundary.present(new ArrayList<>());
                return;
            }

            List<GetListBookOutputDTO> outputDTOs = new ArrayList<>();
            
            for (Book book : books) {
                String tinhTrang = "";
                double thue = 0.0;
                
                if (book instanceof TextBook) {
                    TextBook textBook = (TextBook) book;
                    tinhTrang = textBook.getTinhTrang();
                } else if (book instanceof ReferenceBook) {
                    ReferenceBook referenceBook = (ReferenceBook) book;
                    thue = referenceBook.getThue();
                }

                GetListBookOutputDTO dto = new GetListBookOutputDTO(
                    List.of(book),
                    book.getMaSach(),
                    book.getNgayNhap(),
                    book.getDonGia(),
                    book.getSoLuong(),
                    book.getNhaXuatBan(),
                    book.getLoaiSach(),
                    tinhTrang,
                    thue
                );
                outputDTOs.add(dto);
            }

            outputBoundary.present(outputDTOs);

        } catch (Exception e) {
            String errorMessage = GetListBookResponseError.DATABASE_QUERY_ERROR + e.getMessage();
            outputBoundary.present(new ArrayList<>());
        }
    }
}
        
