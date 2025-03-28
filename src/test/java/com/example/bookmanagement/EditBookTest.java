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

public class EditBookTest {
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
public void testEditTextBookSuccessfully() {
  // 1. Tạo sách test
  Date date = new Date();
  TextBook originalTextBook = new TextBook(1, date, 100000, 10, "NXB Kim Đồng", "Sách Giáo Khoa", "Mới");
  
  GetListBookDAO realDAO = new GetListBookDAO();
  
  // Thêm sách test vào database
  

  // Khởi tạo presenter và controller
  presenter = new GetBookListPresenter(); // Khởi tạo presenter mà không cần tham số
  presenter.setView(view);
  presenter.setViewModel(viewModel);
  
  BookManagementUseCase useCase = new BookManagementUseCase(realDAO, presenter);
  
  // 2. Chuẩn bị dữ liệu để chỉnh sửa
  DataExportEdit editData = new DataExportEdit(
      1,                  // maSach
      "Sách Giáo Khoa",   // loaiSach
    "Cũ",              // tinhTrang
      null,               // thue (không áp dụng cho sách giáo khoa)
      50000.0,            // donGia
      5,                  // soLuong
      "NXB quần què"      // nhaXuatBan
  );

  // 3. Thực hiện chỉnh sửa
  controller.BookEdit(editData);


    
    
}

@Test
public void testEditReferenceBookSuccessfully() {
    // 1. Tạo sách tham khảo test
    Date date = new Date();
    ReferenceBook originalReferenceBook = new ReferenceBook(6, date, 200000, 5, "NXB Tư Pháp", "Sách Tham Khảo", 10.0);
    
    GetListBookDAO realDAO = new GetListBookDAO();
    
    // Thêm sách tham khảo test vào database
    //realDAO.themSach(originalReferenceBook); // Giả sử phương thức này thêm sách vào cơ sở dữ liệu

    // Khởi tạo presenter và controller
    presenter = new GetBookListPresenter(); // Khởi tạo presenter mà không cần tham số
    presenter.setView(view);
    presenter.setViewModel(viewModel);
    
    BookManagementUseCase useCase = new BookManagementUseCase(realDAO, presenter);
    
    // 2. Chuẩn bị dữ liệu để chỉnh sửa
    DataExportEdit editData = new DataExportEdit(
        6,                  // maSach
        "Sách Tham Khảo",   // loaiSach
        null,               // tinhTrang (không áp dụng cho sách tham khảo)
        15.0,              // thue
        150000.0,          // donGia
        103,                // soLuong
        "NXB Duy Quang"      // nhaXuatBan
    );

    // 3. Thực hiện chỉnh sửa
    controller.BookEdit(editData);
    


}
}