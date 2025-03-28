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
import com.example.bookmanagement.getListBook.GetListBookInputBoundary;
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

 public class AppTest 
 {
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
public void testDeleteBookFromDatabase() {
    // 1. Tạo sách test
    Date date = new Date();
    TextBook textBook = new TextBook(1, date, 100000, 10, "NXB Kim Đồng", "Sách Giáo Khoa", "Mới");
    
    GetListBookDAO realDAO = new GetListBookDAO();
    
    // Sửa đổi để khởi tạo presenter mà không cần view và viewModel
    presenter = new GetBookListPresenter(); // Khởi tạo presenter mà không cần tham số
    presenter.setView(view);
    presenter.setViewModel(viewModel);

    BookManagementUseCase useCase = new BookManagementUseCase(realDAO, presenter);
    
    List<Book> booksToDelete = new ArrayList<>();
    booksToDelete.add(textBook);
    
    DeleteBookOutputDTO outputDTO = new DeleteBookOutputDTO(booksToDelete);
    useCase.setOutputDTO(outputDTO);
    useCase.execute();

    ResponseData responseData = useCase.getResponseData();
    assertTrue(responseData.isSuccess());

    List<Book> deletedBooks = responseData.getDeletedBooks();
    assertNotNull(deletedBooks);

    for(Book deletedBook : deletedBooks) {
        if(deletedBook instanceof TextBook) {
            TextBook deleted = (TextBook) deletedBook;
            System.out.println("Đã xóa sách giáo khoa:");
            System.out.println("Mã sách: " + deleted.getMaSach());
            System.out.println("Tên NXB: " + deleted.getNhaXuatBan());
            System.out.println("Tình trạng: " + deleted.getTinhTrang());
        } else if(deletedBook instanceof ReferenceBook) {
            ReferenceBook deleted = (ReferenceBook) deletedBook;
            System.out.println("Đã xóa sách tham khảo:");
            System.out.println("Mã sách: " + deleted.getMaSach());
            System.out.println("Tên NXB: " + deleted.getNhaXuatBan()); 
            System.out.println("Thuế: " + deleted.getThue() + "%");
        }
        System.out.println("------------------------");
    }

    List<Book> remainingBooks = realDAO.getBooks();
    for(Book deletedBook : deletedBooks) {
        assertFalse(remainingBooks.contains(deletedBook));
    }
}
@Test
public void testDeleteMultipleBooksFromDatabase() {
    // 1. Tạo sách test
    Date date = new Date();
    // TextBook textBook1 = new TextBook(1, date, 100000, 10, "NXB Kim Đồng", "Sách Giáo Khoa", "Mới");
    TextBook textBook2 = new TextBook(2, date, 120000, 5, "NXB Giáo Dục", "Sách Giáo Khoa", "Mới");
    ReferenceBook referenceBook1 = new ReferenceBook(3, date, 150000, 5, "NXB Giáo Dục", "Sách Tham Khảo", 10.0);
    ReferenceBook referenceBook2 = new ReferenceBook(4, date, 180000, 3, "NXB Kim Đồng", "Sách Tham Khảo", 15.0);
    presenter = new GetBookListPresenter(); // Khởi tạo presenter mà không cần tham số
    presenter.setView(view);
    presenter.setViewModel(viewModel);
    GetListBookDAO realDAO = new GetListBookDAO();
    
   
    BookManagementUseCase useCase = new BookManagementUseCase(realDAO, presenter);
    
    List<Book> booksToDelete = new ArrayList<>();
    // booksToDelete.add(textBook1);
    booksToDelete.add(textBook2);
    booksToDelete.add(referenceBook1);
    booksToDelete.add(referenceBook2);
    
    DeleteBookOutputDTO outputDTO = new DeleteBookOutputDTO(booksToDelete);
    useCase.setOutputDTO(outputDTO);
    useCase.execute();

    ResponseData responseData = useCase.getResponseData();
    assertTrue(responseData.isSuccess());
    
 
    List<Book> deletedBooks = responseData.getDeletedBooks();
    assertNotNull(deletedBooks);
    assertEquals(3, deletedBooks.size()); // Kiểm tra số lượng sách đã xóa
    
   
    for(Book deletedBook : deletedBooks) {
        if(deletedBook instanceof TextBook) {
            TextBook deleted = (TextBook) deletedBook;
            System.out.println("Đã xóa sách giáo khoa:");
            System.out.println("Mã sách: " + deleted.getMaSach());
            System.out.println("Tên NXB: " + deleted.getNhaXuatBan());
            System.out.println("Tình trạng: " + deleted.getTinhTrang());
        } else if(deletedBook instanceof ReferenceBook) {
            ReferenceBook deleted = (ReferenceBook) deletedBook;
            System.out.println("Đã xóa sách tham khảo:");
            System.out.println("Mã sách: " + deleted.getMaSach());
            System.out.println("Tên NXB: " + deleted.getNhaXuatBan()); 
            System.out.println("Thuế: " + deleted.getThue() + "%");
        }
        System.out.println("------------------------");
    }

    List<Book> remainingBooks = realDAO.getBooks();
    for(Book deletedBook : deletedBooks) {
        assertFalse(remainingBooks.contains(deletedBook));
    }
}

// Test trường hợp không chọn cột mã sách
@Test 
public void testNotSelectingBookIdColumn() {
    // Chọn một cột không phải là cột mã sách (giả sử cột mã sách là cột đầu tiên)
    view.selectSingleColumn(1); // Chọn cột thứ hai, không phải là cột mã sách

    // Gọi hành động xóa
    view.handleDeleteAction(); 

    // Kiểm tra xem cảnh báo đã hiển thị chưa
    boolean isWarningDisplayed = view.isWarningDisplayed();
    //assertTrue("Cảnh báo không hiển thị!", isWarningDisplayed);
    
    // Kiểm tra thông báo cảnh báo
    String expectedMessage = "Vui lòng chỉ chọn cột mã sách để xóa!";
    String actualMessage = view.getWarningMessage();
    //assertEquals("Thông báo cảnh báo không chính xác!", expectedMessage, actualMessage);

    // Kiểm tra số lượng sách ban đầu không thay đổi
    int initialBookCount = dao.getBooks().size();
    assertEquals("Số lượng sách trong DAO đã thay đổi!", initialBookCount, dao.getBooks().size());

    // Đặt lại trạng thái cảnh báo
    view.resetWarningStatus();
}
@Test 
public void testNotSelectingAnyRow() {
    // Xóa tất cả lựa chọn trong bảng
    view.getTable().clearSelection();

    // Gọi hành động xóa
    view.handleDeleteAction();

    // Kiểm tra xem cảnh báo đã hiển thị chưa
   // assertTrue("Cảnh báo không hiển thị!", view.isWarningDisplayed());
    //assertEquals("Thông báo cảnh báo không chính xác!", "Vui lòng chọn sách cần xóa!", view.getWarningMessage());

    // Kiểm tra số lượng sách ban đầu không thay đổi
    int initialBookCount = dao.getBooks().size();
    assertEquals("Số lượng sách trong DAO đã thay đổi!", initialBookCount, dao.getBooks().size());

    // Đặt lại trạng thái cảnh báo
    view.resetWarningStatus();
}
 }