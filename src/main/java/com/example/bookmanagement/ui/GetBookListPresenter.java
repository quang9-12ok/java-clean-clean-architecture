    package com.example.bookmanagement.ui;

    import java.util.ArrayList;
    import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
    import javax.swing.SwingUtilities;

    import com.example.bookmanagement.entity.Book;
    import com.example.bookmanagement.entity.ReferenceBook;
    import com.example.bookmanagement.entity.TextBook;
import com.example.bookmanagement.getListBook.GetListBookOutputBoundary;
import com.example.bookmanagement.getListBook.GetListBookOutputDTO;
import com.example.bookmanagement.getListBook.GetListBookResponseError;
import com.example.bookmanagement.usecase.BookManagementOutputBoundary;
    import com.example.bookmanagement.usecase.ResponseData;
    import com.example.bookmanagement.usecaseEdit.EditBookOutputBoundary;
    import com.example.bookmanagement.usecaseEdit.EditBookOutputDTO;
    import com.example.bookmanagement.usecaseListTB.ListBookPublisherOutputBoundary;
    import com.example.bookmanagement.usecaseListTB.ListBookPublisherOutputDTO;
    import com.example.bookmanagement.themSach.ThemSachOutputBoundary;
    import com.example.bookmanagement.themSach.ThemSachOutputDTO;
import com.example.bookmanagement.themSach.ThemSachResponseError;
import com.example.bookmanagement.timSach.TimSachOutputBoundary;
    import com.example.bookmanagement.tinhTongThanhTien.TinhTongThanhTienOutputBoundary;
    import com.example.bookmanagement.tinhTongThanhTien.TinhTongThanhTienOutputDTO;
    import com.example.bookmanagement.tinhTrungBinhDonGia.TinhTrungBinhDonGiaOutputBoundary;
    import com.example.bookmanagement.tinhTrungBinhDonGia.TinhTrungBinhDonGiaOutputDTO;
    import com.example.bookmanagement.timSach.DataExport;
    public class GetBookListPresenter implements GetListBookOutputBoundary,BookManagementOutputBoundary, EditBookOutputBoundary, ListBookPublisherOutputBoundary, ThemSachOutputBoundary, TimSachOutputBoundary, TinhTongThanhTienOutputBoundary, TinhTrungBinhDonGiaOutputBoundary  {
        private GetBookListView view;
        private GetBookListViewModel viewModel;
        //private TextBookListResultView resultView;
        private List<Book> foundBooks;
      

        public GetBookListPresenter() {
            //this.resultView = new TextBookListResultView();
            this.foundBooks = new ArrayList<>();
        }


        @Override
    public void present(List<GetListBookOutputDTO> bookList) {
        System.out.println("DEBUG: Received books from database: " + bookList.size());
        List<Book> books = new ArrayList<>();

        for (GetListBookOutputDTO dto : bookList) {
            Book book;

            // Log chi tiết thông tin từng sách
            System.out.println("Book Details - Mã sách: " + dto.getMaSach() + 
                               ", Loại sách: " + dto.getLoaiSach() + 
                               ", Tình trạng: " + dto.getTinhTrang());

            if ("Sách Tham Khảo".equals(dto.getLoaiSach())) {
                Double tax = dto.getThue();
                if (tax == null) {
                    tax = 0.0; // If tax is null, set it to 0
                }
                book = new ReferenceBook(
                    dto.getMaSach(),
                    dto.getNgayNhap(),
                    dto.getDonGia(),
                    dto.getSoLuong(),
                    dto.getNhaXuatBan(),
                    dto.getLoaiSach(),
                    tax
                );
            } else {
                String status = dto.getTinhTrang();
                book = new TextBook(
                    dto.getMaSach(),
                    dto.getNgayNhap(),
                    dto.getDonGia(),
                    dto.getSoLuong(),
                    dto.getNhaXuatBan(),
                    dto.getLoaiSach(),
                    status
                );
            }

            books.add(book);
        }

        SwingUtilities.invokeLater(() -> {
            if (viewModel != null) {
                viewModel.setBooks(books);
                System.out.println("DEBUG: Set " + books.size() + " books in ViewModel");
            }

            if (view != null) {
                view.loadBooks();
                System.out.println("DEBUG: Loaded books in View");
            }
        });
    }
        public void setView(GetBookListView view) {
            this.view = view;
        }
    
        public void setViewModel(GetBookListViewModel viewModel) {
            this.viewModel = viewModel;
        }
        @Override
        public void presentResult(TinhTongThanhTienOutputDTO output) {
            // Cập nhật tổng tiền trong viewModel
            viewModel.updateTongThanhTien(output.getTongThanhTienSachGiaoKhoa(), 
                                        output.getTongThanhTienSachThamKhao());
            
            // Hiển thị kết quả trong view
            SwingUtilities.invokeLater(() -> {
                view.showTongTienResult(output.getTongThanhTienSachGiaoKhoa(),
                                    output.getTongThanhTienSachThamKhao());
                view.loadBooks(); 
            });
        }

        public List<Book> getFoundBooks() {
            return foundBooks;
        }
        @Override
public void presentResult(TinhTrungBinhDonGiaOutputDTO output) {
    if (output.isSuccess()) {
        SwingUtilities.invokeLater(() -> {
            int selectedRow = view.getTable().getSelectedRow();
            
            if (selectedRow != -1) {
                // Lấy mã sách của dòng được chọn, chuyển đổi sang int
                Object maSachObj = view.getTable().getValueAt(selectedRow, 0);
                int maSach;
                
                // Kiểm tra và chuyển đổi mã sách sang int
                if (maSachObj instanceof Integer) {
                    maSach = (Integer) maSachObj;
                } else if (maSachObj instanceof String) {
                    try {
                        maSach = Integer.parseInt((String) maSachObj);
                    } catch (NumberFormatException e) {
                        view.showMessage("Lỗi", "Không thể chuyển đổi mã sách", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    view.showMessage("Lỗi", "Mã sách không hợp lệ", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Kiểm tra xem sách được chọn có phải là sách tham khảo không
                Book selectedBook = viewModel.getBooks().get(selectedRow);
                
                if (selectedBook instanceof ReferenceBook) {
                    view.getTable().setValueAt(
                        String.format("%.2f", output.getAveragePrice()), 
                        selectedRow, 
                        view.getTable().getColumnCount() - 1 
                    );
                    
                    view.showMessage("Thành công", 
                        String.format("Đơn giá trung bình sách tham khảo %d: %.2f\nSố lượng: %d", 
                            maSach, 
                            output.getAveragePrice(), 
                            output.getTotalBooks()),
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    view.showMessage("Lỗi", 
                        "Vui lòng chọn mã sách của sách tham khảo", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    } else {
        SwingUtilities.invokeLater(() -> {
            view.showMessage("Lỗi", output.getMessage(), JOptionPane.ERROR_MESSAGE);
        });
    }
}

        

    @Override
    public void presentError(String errorMessage) {
       
        view.showErrorMessage("Error: " + errorMessage);
    }
        @Override
        public void presentSearchResults(DataExport dataExport) {
            if (dataExport.isSuccess()) {
                if (dataExport.hasResults()) {
                    viewModel.setBooks(dataExport.getData());
                    view.showMessage("Kết quả tìm kiếm", 
                                dataExport.getDetailedMessage(), 
                                JOptionPane.INFORMATION_MESSAGE);
                } else {
                    view.showMessage("Thông báo", 
                                dataExport.getMessage(), 
                                JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                view.showMessage("Lỗi", 
                            dataExport.getMessage(), 
                            JOptionPane.ERROR_MESSAGE);
            }
        }

      

        @Override
        public void presentDeleteBookOutput(ResponseData responseData) {
            if (responseData.isSuccess()) {
                // Cập nhật viewModel với danh sách sách mới
                viewModel.setBooks(responseData.getDeletedBooks());
                
                // Hiển thị thông báo thành công với chi tiết sách đã xóa
                view.showMessage("Xóa sách thành công", responseData.getMessage(), JOptionPane.INFORMATION_MESSAGE);
                
                // Cập nhật lại bảng
                 view.loadBooks();
            } else {
                // Hiển thị thông báo lỗi
                view.showMessage("Lỗi", responseData.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }
        @Override
        public void presentBooks(List<Book> books) {
            
            viewModel.setBooks(books);
            
            view.loadBooks();
        }

        

        @Override
public void presentResult(String result) {
    SwingUtilities.invokeLater(() -> {
        if (result.startsWith("Thêm sách thành công")) {
            view.showSuccessMessage(result);
            view.loadBooks(); // Cập nhật lại danh sách sách
        } else {
            view.showErrorMessage("Lỗi: " + result);
        }
    });
} 
@Override
public void presentTextBooks(ListBookPublisherOutputDTO outputDTO) {
    // Kiểm tra nếu cần yêu cầu nhập lại
    if (outputDTO.isRequestingNewInput()) {
        String newInput = view.showInputDialog(
            "Nhập tên nhà xuất bản:",
            "Lọc sách giáo khoa",
            JOptionPane.QUESTION_MESSAGE
        );
        outputDTO.setNewPublisherInput(newInput);
        return; // Kết thúc phương thức nếu đã yêu cầu nhập lại
    }

    // Kiểm tra nếu kết quả thành công
    if (outputDTO.isSuccess()) {
        // Cập nhật viewModel với danh sách sách
        //viewModel.setBooks(outputDTO.getTextBooks());

        // Hiển thị thông báo chi tiết về sách
        String detailMessage = outputDTO.createDetailMessage();
        view.showMessage("Kết quả tìm kiếm", detailMessage, JOptionPane.INFORMATION_MESSAGE);

        // Cập nhật lại bảng hiển thị sách trong view
        view.loadBooks();
    } else {
        // Xử lý trường hợp lỗi
        if (outputDTO.isRetryRequested()) {
            int option = view.showConfirmDialog(
                "Lỗi",
                outputDTO.getMessage() + "\nBạn có muốn thử lại không?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE
            );
            outputDTO.setRetryRequested(option == JOptionPane.YES_OPTION);
        } else {
            view.showMessage("Lỗi", outputDTO.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }
}

       

@Override
public void prepareSuccessView(ThemSachOutputDTO outputDTO) {
    // Refresh lại danh sách sách
    view.loadBooks();

    // Lấy thông tin sách từ outputDTO
    com.example.bookmanagement.themSach.DataExport dataExport = outputDTO.getDataExport();
    String message = dataExport.getMessage();

    // Hiển thị thông báo thành công
    JOptionPane.showMessageDialog(view,
        message,
        "Thêm sách thành công", 
        JOptionPane.INFORMATION_MESSAGE);
}

   @Override
   public void prepareFailView(ThemSachResponseError error) {
       // Hiển thị thông báo lỗi
       JOptionPane.showMessageDialog(view,
           error.getMessage(),
           "Lỗi",
           JOptionPane.ERROR_MESSAGE); 
   }

   

   @Override
public void presentEditResult(String result) {
    SwingUtilities.invokeLater(() -> {
       
        if (result.startsWith("THÔNG TIN SÁCH SAU KHI CẬP NHẬT:")) {
            view.showSuccessMessage(result);
            view.loadBooks(); // Refresh the book list after successful edit
        } else {
            view.showErrorMessage("Lỗi: " + result);
        }
    });
}


@Override
public void presentEditedBook(EditBookOutputDTO outputDTO) {
    SwingUtilities.invokeLater(() -> {
        
        view.updateEditedBook(outputDTO);
    });
}


  
 }
