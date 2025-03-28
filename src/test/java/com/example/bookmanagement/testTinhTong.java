package com.example.bookmanagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.example.bookmanagement.database.GetListBookDAO;
import com.example.bookmanagement.entity.Book;
import com.example.bookmanagement.entity.ReferenceBook;
import com.example.bookmanagement.entity.TextBook;
import com.example.bookmanagement.getListBook.GetListBookUseCase;
import com.example.bookmanagement.themSach.ThemSachInputBoundary;
import com.example.bookmanagement.themSach.ThemSachUseCase;
import com.example.bookmanagement.timSach.TimSachDatabaseBoundary;
import com.example.bookmanagement.timSach.TimSachInputBoundary;
import com.example.bookmanagement.timSach.TimSachOutputBoundary;
import com.example.bookmanagement.timSach.TimSachUseCase;
import com.example.bookmanagement.tinhTongThanhTien.TinhTongThanhTienInputBoundary;
import com.example.bookmanagement.tinhTongThanhTien.TinhTongThanhTienOutputBoundary;
import com.example.bookmanagement.tinhTongThanhTien.TinhTongThanhTienOutputDTO;
import com.example.bookmanagement.tinhTongThanhTien.TinhTongThanhTienUseCase;
import com.example.bookmanagement.tinhTrungBinhDonGia.TinhTrungBinhDonGiaInputBoundary;
import com.example.bookmanagement.tinhTrungBinhDonGia.TinhTrungBinhDonGiaUseCase;
import com.example.bookmanagement.ui.GetBookListController;
import com.example.bookmanagement.ui.GetBookListPresenter;
import com.example.bookmanagement.ui.GetBookListView;
import com.example.bookmanagement.ui.GetBookListViewModel;
import com.example.bookmanagement.usecase.BookManagementInputBoundary;
import com.example.bookmanagement.usecase.BookManagementUseCase;
import com.example.bookmanagement.usecaseEdit.EditBookDatabaseBoundary;
import com.example.bookmanagement.usecaseEdit.EditBookOutputBoundary;
import com.example.bookmanagement.usecaseEdit.EditBookUseCase;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherDatabaseBoundary;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherInputBoundary;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherOutputBoundary;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherUseCase;

public class testTinhTong {

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
public void testTinhTongThanhTienSuccessfully() {
    // 1. Tạo sách test
    Date date1 = new Date();
    TextBook textBook1 = new TextBook(1, date1, 100000, 5, "NXB Kim Đồng", "Sách Giáo Khoa", "Mới");
    TextBook textBook2 = new TextBook(2, date1, 150000, 3, "NXB Giáo Dục", "Sách Tham Khảo", "Cũ");
    ReferenceBook referenceBook1 = new ReferenceBook(3, date1, 200000, 2, "NXB Tư Pháp", "Sách Tham Khảo", 10.0);
    
    GetListBookDAO realDAO = new GetListBookDAO();
    
    // Thêm sách test vào database
    try {
        realDAO.themSach(textBook1);
        realDAO.themSach(textBook2);
        realDAO.themSach(referenceBook1);
    } catch (SQLException e) {
        e.printStackTrace();
        fail("Failed to add test books to the database: " + e.getMessage());
    }

    // Khởi tạo presenter và controller
    presenter = new GetBookListPresenter(); // Khởi tạo presenter mà không cần tham số
    presenter.setView(view);
    presenter.setViewModel(viewModel);
    
    TinhTongThanhTienUseCase tinhTongUseCase = new TinhTongThanhTienUseCase(realDAO, presenter);

    // 2. Thực hiện tính tổng thành tiền
    TinhTongThanhTienOutputDTO outputDTO = tinhTongUseCase.tinhTongTien(); // Giả sử phương thức này trả về kết quả

    // 3. In ra kết quả tổng tiền
    if (outputDTO != null) {
        System.out.println("Tổng thành tiền sách giáo khoa: " + outputDTO.getTongThanhTienSachGiaoKhoa());
        System.out.println("Tổng thành tiền sách tham khảo: " + outputDTO.getTongThanhTienSachThamKhao());
    } else {
        System.out.println("Không có kết quả tổng tiền.");
    }

    // Kết thúc test case mà không có xác minh
}
    
    
    
}
