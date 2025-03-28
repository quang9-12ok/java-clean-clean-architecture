package com.example.bookmanagement;
import static org.junit.Assert.*;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;

import com.example.bookmanagement.database.GetListBookDAO;
import com.example.bookmanagement.entity.Book;
import com.example.bookmanagement.entity.TextBook;
import com.example.bookmanagement.getListBook.GetListBookUseCase;
import com.example.bookmanagement.themSach.ThemSachInputBoundary;
import com.example.bookmanagement.themSach.ThemSachUseCase;
import com.example.bookmanagement.timSach.TimSachDatabaseBoundary;
import com.example.bookmanagement.timSach.TimSachInputBoundary;
import com.example.bookmanagement.timSach.TimSachOutputBoundary;
import com.example.bookmanagement.timSach.TimSachUseCase;
import com.example.bookmanagement.tinhTongThanhTien.TinhTongThanhTienInputBoundary;
import com.example.bookmanagement.tinhTongThanhTien.TinhTongThanhTienUseCase;
import com.example.bookmanagement.tinhTrungBinhDonGia.TinhTrungBinhDonGiaInputBoundary;
import com.example.bookmanagement.tinhTrungBinhDonGia.TinhTrungBinhDonGiaOutputDTO;
import com.example.bookmanagement.tinhTrungBinhDonGia.TinhTrungBinhDonGiaUseCase;
import com.example.bookmanagement.ui.GetBookListController;
import com.example.bookmanagement.ui.GetBookListPresenter;
import com.example.bookmanagement.ui.GetBookListView;
import com.example.bookmanagement.ui.GetBookListViewModel;
import com.example.bookmanagement.entity.ReferenceBook;
import com.example.bookmanagement.usecase.BookManagementInputBoundary;
import com.example.bookmanagement.usecase.BookManagementUseCase;
import com.example.bookmanagement.usecaseEdit.DataExportEdit;
import com.example.bookmanagement.usecaseEdit.EditBookDatabaseBoundary;
import com.example.bookmanagement.usecaseEdit.EditBookOutputBoundary;
import com.example.bookmanagement.usecaseEdit.EditBookUseCase;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherDatabaseBoundary;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherInputBoundary;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherOutputBoundary;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherUseCase;
public class testTrungBinh {
    private GetListBookDAO dao;
    private BookManagementUseCase useCase;
    private GetBookListViewModel viewModel;
    private GetBookListView view;
    private GetBookListPresenter presenter;
    private GetBookListController controller;
    private TinhTrungBinhDonGiaUseCase tinhTrungBinhDonGiaUseCase;
  @Before
public void setUp() {
    // 1. Khởi tạo DAO
    dao = new GetListBookDAO();
    
    // 2. Khởi tạo ViewModel
    viewModel = new GetBookListViewModel();
    
    // 3. Khởi tạo Presenter
    presenter = new GetBookListPresenter();
    presenter.setViewModel(viewModel);
    
    // 4. Khởi tạo các Use Cases với DAO và Presenter
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

    // 5. Khởi tạo Controller với tất cả các dependencies
    controller = new GetBookListController(
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

    // 6. Khởi tạo View và truyền Controller
    view = new GetBookListView(controller, viewModel);
    
    // 7. Thiết lập View cho Presenter
    presenter.setView(view);
}  

@Test
    public void testTinhTrungBinhDonGiaSuccessfully() {
        // 1. Tạo sách tham khảo test
        Date date = new Date();
        ReferenceBook referenceBook1 = new ReferenceBook(1, date, 200000, 5, "NXB A", "Sách Tham Khảo", 10.0);
        ReferenceBook referenceBook2 = new ReferenceBook(2, date, 300000, 10, "NXB B", "Sách Tham Khảo", 15.0);
        
        // 2. Thêm sách vào cơ sở dữ liệu
        try {
            dao.themSach(referenceBook1);
            dao.themSach(referenceBook2);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to add test books to the database: " + e.getMessage());
        }
        TinhTrungBinhDonGiaUseCase tinhTrungBinhDonGiaUseCase = new TinhTrungBinhDonGiaUseCase(presenter, dao);
       
        int selectedRowIndex = 8; 
        tinhTrungBinhDonGiaUseCase.execute(selectedRowIndex);
    }





}