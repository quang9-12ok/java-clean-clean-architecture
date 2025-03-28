    package com.example.bookmanagement.ui;

    import java.util.ArrayList;
    import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import java.util.Date;
    import com.example.bookmanagement.database.GetListBookDAO;
    import com.example.bookmanagement.entity.*;
import com.example.bookmanagement.getListBook.GetListBookInputBoundary;
import com.example.bookmanagement.themSach.*;
    import com.example.bookmanagement.usecase.*;
    import com.example.bookmanagement.usecaseEdit.*;
    import com.example.bookmanagement.usecaseListTB.*;
    import com.example.bookmanagement.timSach.*;
    import com.example.bookmanagement.tinhTongThanhTien.*;
    import com.example.bookmanagement.tinhTrungBinhDonGia.*;
    

    public class GetBookListController {
        private final BookManagementInputBoundary inputBoundary;
        private final GetBookListViewModel viewModel;
        private final GetBookListPresenter presenter;
        private final GetListBookInputBoundary getinputBoundary;
        private final ThemSachInputBoundary themSachInputBoundary;
        private final TimSachInputBoundary timSachUseCase;
        private final EditBookDatabaseBoundary databaseBoundary;
        private final EditBookOutputBoundary outputBoundary;
        private final EditBookUseCase editBookUseCase;
        private final TinhTongThanhTienInputBoundary tinhTongThanhTienUseCase;
        private final ListBookPublisherInputBoundary listBookPublisherInputBoundary;
        private final TinhTrungBinhDonGiaInputBoundary tinhTrungBinhDonGiaUseCase;
        private final ThemSachUseCase themSachUseCase;
        private GetBookListView view;
        

     
        public GetBookListController(
                BookManagementInputBoundary inputBoundary,
                GetBookListViewModel viewModel,
               
                GetBookListPresenter presenter,
                GetListBookInputBoundary getinputBoundary,
                ThemSachInputBoundary themSachInputBoundary,
                ThemSachUseCase themSachUseCase,
                TimSachInputBoundary timSachUseCase,
                EditBookDatabaseBoundary databaseBoundary, 
                EditBookOutputBoundary outputBoundary,
                EditBookUseCase editBookUseCase,
                TinhTrungBinhDonGiaInputBoundary tinhTrungBinhDonGiaUseCase,
                TinhTongThanhTienInputBoundary tinhTongThanhTienUseCase,
                ListBookPublisherInputBoundary listBookPublisherInputBoundary) {
            
            this.inputBoundary = inputBoundary;
            this.viewModel = viewModel;
           
            this.presenter = presenter;
            this.getinputBoundary = getinputBoundary;
            this.tinhTrungBinhDonGiaUseCase = tinhTrungBinhDonGiaUseCase;
            this.databaseBoundary = databaseBoundary;
            this.outputBoundary = outputBoundary;
            this.tinhTongThanhTienUseCase = tinhTongThanhTienUseCase;
            this.listBookPublisherInputBoundary = listBookPublisherInputBoundary;
            this.themSachInputBoundary = themSachInputBoundary;
            this.themSachUseCase = themSachUseCase;
            this.timSachUseCase = timSachUseCase;
            this.editBookUseCase = editBookUseCase;
            loadBooks();
        }

        public void loadBooks() {
            try {
                System.out.println("DEBUG: Controller loadBooks() called");
                getinputBoundary.getListBook();
            } catch (Exception e) {
                System.err.println("DEBUG: Error loading books");
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Lỗi khi tải danh sách sách: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        
        
    

        public void listTextBooksByPublisher(String publisher) {
            listBookPublisherInputBoundary.execute(publisher);
        }
                
        public void themSach(Book newBook) {
            try {
            
                themSachInputBoundary.execute(newBook);
                themSachUseCase.execute(newBook);
               
                refreshBookList();
            } catch (Exception e) {
                System.err.println("Lỗi khi thêm sách: " + e.getMessage());
                JOptionPane.showMessageDialog(null,
                    "Lỗi khi thêm sách: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        public void BookEdit(DataExportEdit editData) {
            try {
                
                editBookUseCase.execute(editData);
                
               
                refreshBookList();
            } catch (Exception e) {
                System.err.println("Lỗi khi cập nhật sách: " + e.getMessage());
                JOptionPane.showMessageDialog(
                    null,
                    "Lỗi khi cập nhật sách: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
         public void showThemSachDialog(JFrame parentFrame) {
        Book newBook = ThemSachDialog.showDialog(
            parentFrame,
            themSachUseCase,
            presenter,  
            this
        );
        
        if (newBook != null) {
            loadBooks();  
        }
    }
    public void showEditBook(Book bookToEdit, JFrame parentFrame) {
        try {
            
            EditBookDialog.showDialog(
                parentFrame, 
                bookToEdit, 
                editBookUseCase, 
                outputBoundary, 
                this
            );
        } catch (Exception e) {
            System.err.println("Lỗi khi sửa sách: " + e.getMessage());
            JOptionPane.showMessageDialog(
                null,
                "Lỗi khi sửa sách: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

        
        

        

        

        public void deleteSelectedBooks(int[] selectedRows) {
            if (selectedRows.length == 0) return;

            List<Book> booksToDelete = getBooksToDelete(selectedRows);
            if (!booksToDelete.isEmpty()) {
                processBookDeletion(booksToDelete);
            }
        }

        private List<Book> getBooksToDelete(int[] selectedRows) {
            List<Book> books = viewModel.getBooks();
            List<Book> booksToDelete = new ArrayList<>();
            
            for (int row : selectedRows) {
                int bookId = (int) viewModel.getTableModel().getValueAt(row, 0);
                books.stream()
                    .filter(book -> book.getMaSach() == bookId)
                    .findFirst()
                    .ifPresent(booksToDelete::add);
            }
            
            return booksToDelete;
        }

        private void processBookDeletion(List<Book> booksToDelete) {
            DeleteBookOutputDTO outputDTO = new DeleteBookOutputDTO(booksToDelete);
            ((BookManagementUseCase) inputBoundary).setOutputDTO(outputDTO);
            inputBoundary.execute();
            
            List<Book> updatedBooks = ((BookManagementUseCase) inputBoundary).getUpdatedBooks();
            viewModel.setBooks(updatedBooks);
            loadBooks();
        }

        
        public void showTimSachDialog(JFrame parentFrame) {
            TimSachDialog.showDialog(
                parentFrame, 
                timSachUseCase, 
                presenter,  
                new GetListBookDAO(),
                this
            );
        }
        public GetBookListView getView() {
            return view;
        }
        
       
        public void tinhTrungBinhDonGia(int selectedRowIndex) {
            tinhTrungBinhDonGiaUseCase.execute(selectedRowIndex);
        }
        
        
        public void tinhTongTien() {
            TinhTongThanhTienOutputDTO result = tinhTongThanhTienUseCase.tinhTongTien();
        }

        public void getBooks() {
            getinputBoundary.getListBook();
        }

        public void refreshBookList() {
            getBooks();
        }
    }
