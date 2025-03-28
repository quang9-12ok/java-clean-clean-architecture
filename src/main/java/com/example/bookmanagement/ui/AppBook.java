package com.example.bookmanagement.ui;

import com.example.bookmanagement.database.GetListBookDAO;
import com.example.bookmanagement.getListBook.GetListBookUseCase;
import com.example.bookmanagement.usecase.BookManagementInputBoundary;
import com.example.bookmanagement.usecase.BookManagementUseCase;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherUseCase;
import com.example.bookmanagement.themSach.ThemSachInputBoundary;
import com.example.bookmanagement.themSach.ThemSachUseCase;
import com.example.bookmanagement.timSach.TimSachDatabaseBoundary;
import com.example.bookmanagement.timSach.TimSachInputBoundary;
import com.example.bookmanagement.timSach.TimSachOutputBoundary;
import com.example.bookmanagement.timSach.TimSachUseCase;
import com.example.bookmanagement.tinhTongThanhTien.TinhTongThanhTienInputBoundary;
import com.example.bookmanagement.tinhTongThanhTien.TinhTongThanhTienUseCase;
import com.example.bookmanagement.tinhTrungBinhDonGia.TinhTrungBinhDonGiaInputBoundary;
import com.example.bookmanagement.tinhTrungBinhDonGia.TinhTrungBinhDonGiaUseCase;
import com.example.bookmanagement.usecaseEdit.EditBookDatabaseBoundary;
import com.example.bookmanagement.usecaseEdit.EditBookOutputBoundary;
import com.example.bookmanagement.usecaseEdit.EditBookUseCase;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherDatabaseBoundary;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherInputBoundary;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherOutputBoundary;

public class AppBook {
    public static void main(String[] args) {
        try {
            GetListBookDAO dao = new GetListBookDAO();

            GetBookListViewModel viewModel = new GetBookListViewModel();
 
            GetBookListPresenter presenter = new GetBookListPresenter();
            presenter.setViewModel(viewModel);
            
          
            BookManagementUseCase bookManagementUseCase = new BookManagementUseCase(dao, presenter);
             GetListBookUseCase getListBookUseCase = new GetListBookUseCase(dao, presenter);
            ThemSachUseCase themSachUseCase = new ThemSachUseCase(dao, presenter);
            TimSachUseCase timSachUseCase = new TimSachUseCase((TimSachDatabaseBoundary) dao, (TimSachOutputBoundary) presenter);
            EditBookUseCase editBookUseCase = new EditBookUseCase((EditBookDatabaseBoundary) dao, (EditBookOutputBoundary) presenter);
            TinhTongThanhTienUseCase tinhTongThanhTienUseCase = new TinhTongThanhTienUseCase(dao, presenter);
            TinhTrungBinhDonGiaUseCase tinhTrungBinhDonGiaUseCase = new TinhTrungBinhDonGiaUseCase(presenter, dao);
            ListBookPublisherUseCase listBookPublisherUseCase = new ListBookPublisherUseCase(
                (ListBookPublisherDatabaseBoundary) dao, 
                (ListBookPublisherOutputBoundary) presenter
            );
            
         
            GetBookListController controller = new GetBookListController(
                bookManagementUseCase,
                viewModel,
                
                presenter,
                getListBookUseCase,
                themSachUseCase,
                themSachUseCase,
                timSachUseCase,
                dao,
                presenter,
                editBookUseCase,
                tinhTrungBinhDonGiaUseCase,
                tinhTongThanhTienUseCase,
                listBookPublisherUseCase
            );

       GetBookListView view = new GetBookListView(controller, viewModel);
            
         
            presenter.setView(view);

          
            javax.swing.SwingUtilities.invokeLater(() -> {
                view.setVisible(true);
               
               
                 controller.loadBooks();
                view.initializeController(controller, viewModel);
            });
            
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Lỗi khởi tạo ứng dụng: " + e.getMessage(),
                "Lỗi",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}