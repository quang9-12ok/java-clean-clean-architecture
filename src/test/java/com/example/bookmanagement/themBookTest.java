package com.example.bookmanagement;

import static org.junit.Assert.*;

import java.util.List;

import javax.swing.JOptionPane;

import java.util.Date;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



import com.example.bookmanagement.database.GetListBookDAO;
import com.example.bookmanagement.entity.Book;
import com.example.bookmanagement.entity.TextBook;
import com.example.bookmanagement.getListBook.GetListBookUseCase;
import com.example.bookmanagement.themSach.ThemSachInputBoundary;
import com.example.bookmanagement.themSach.ThemSachOutputBoundary;
import com.example.bookmanagement.themSach.ThemSachOutputDTO;
import com.example.bookmanagement.themSach.ThemSachUseCase;
import com.example.bookmanagement.timSach.TimSachDatabaseBoundary;
import com.example.bookmanagement.timSach.TimSachInputBoundary;
import com.example.bookmanagement.timSach.TimSachOutputBoundary;
import com.example.bookmanagement.timSach.TimSachUseCase;
import com.example.bookmanagement.tinhTongThanhTien.TinhTongThanhTienInputBoundary;
import com.example.bookmanagement.tinhTongThanhTien.TinhTongThanhTienUseCase;
import com.example.bookmanagement.tinhTrungBinhDonGia.TinhTrungBinhDonGiaInputBoundary;
import com.example.bookmanagement.tinhTrungBinhDonGia.TinhTrungBinhDonGiaUseCase;
import com.example.bookmanagement.ui.GetBookListController;
import com.example.bookmanagement.ui.GetBookListPresenter;
import com.example.bookmanagement.ui.GetBookListView;
import com.example.bookmanagement.ui.GetBookListViewModel;
import com.example.bookmanagement.entity.ReferenceBook;
import com.example.bookmanagement.usecase.BookManagementInputBoundary;
import com.example.bookmanagement.usecase.BookManagementOutputBoundary;
import com.example.bookmanagement.usecase.BookManagementUseCase;
import com.example.bookmanagement.usecase.DeleteBookOutputDTO;
import com.example.bookmanagement.usecase.ResponseData;
import com.example.bookmanagement.usecaseEdit.EditBookDatabaseBoundary;
import com.example.bookmanagement.usecaseEdit.EditBookOutputBoundary;
import com.example.bookmanagement.usecaseEdit.EditBookUseCase;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherDatabaseBoundary;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherInputBoundary;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherOutputBoundary;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherUseCase;
public class themBookTest {
    private GetListBookDAO dao;
    private BookManagementUseCase useCase;
    private GetBookListViewModel viewModel;
    private GetBookListView view;
    private GetBookListPresenter presenter;
    private GetBookListController controller;
    
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
public void testAddTextBookSuccessfully() {
    // 1. Tạo sách test
    Date date = new Date();
    TextBook newTextBook = new TextBook(1, date, 100000, 10, "NXB Messi", "Sách Giáo Khoa", "Cũ");
    
    GetListBookDAO realDAO = new GetListBookDAO();
    
    // Khởi tạo presenter và controller
    presenter = new GetBookListPresenter(); // Khởi tạo presenter mà không cần tham số
    presenter.setView(view);
    presenter.setViewModel(viewModel);
    
    ThemSachUseCase themSachUseCase = new ThemSachUseCase(realDAO, presenter);

    // 2. Thực hiện thêm sách
    themSachUseCase.execute(newTextBook);




}

@Test
public void testAddReferenceBookSuccessfully() {
    // 1. Tạo sách tham khảo test
    Date date = new Date();
    ReferenceBook newReferenceBook = new ReferenceBook(2, date, 200000, 5, "NXB phúc ngu", "Sách Tham Khảo", 10.0);
    
    GetListBookDAO realDAO = new GetListBookDAO();
    
    // Khởi tạo presenter và controller
    presenter = new GetBookListPresenter(); // Khởi tạo presenter mà không cần tham số
    presenter.setView(view);
    presenter.setViewModel(viewModel);
    
    ThemSachUseCase themSachUseCase = new ThemSachUseCase(realDAO, presenter);

    // 2. Thực hiện thêm sách tham khảo
    themSachUseCase.execute(newReferenceBook);
}
}
