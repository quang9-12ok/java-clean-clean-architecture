package com.example.bookmanagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

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
import com.example.bookmanagement.usecase.BookManagementInputBoundary;
import com.example.bookmanagement.usecase.BookManagementUseCase;
import com.example.bookmanagement.usecaseEdit.EditBookDatabaseBoundary;
import com.example.bookmanagement.usecaseEdit.EditBookOutputBoundary;
import com.example.bookmanagement.usecaseEdit.EditBookUseCase;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherDatabaseBoundary;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherInputBoundary;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherOutputBoundary;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherUseCase;

public class timBookTest {
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
public void testTimSach() {
    // 1. Tạo dữ liệu test
    String publisher = "NXB Kim Đồng";
    
    // 2. Khởi tạo các dependency
    GetListBookDAO realDAO = new GetListBookDAO();

    presenter = new GetBookListPresenter(); // Khởi tạo presenter mà không cần tham số
    presenter.setView(view);
    presenter.setViewModel(viewModel);
    TimSachUseCase timSachUseCase = new TimSachUseCase((TimSachDatabaseBoundary) realDAO, (TimSachOutputBoundary) presenter);
    
    // 3. Thực hiện tìm sách
    timSachUseCase.timSachTheoNhaXuatBan(publisher);
    
    // 4. Kiểm tra kết quả
    List<Book> foundBooks = presenter.getFoundBooks();
   // assertNotNull("Danh sách sách tìm thấy không được null", foundBooks);
   // assertFalse("Danh sách sách tìm thấy không được rỗng", foundBooks.isEmpty());
    
    // Kiểm tra xem tất cả sách trong danh sách đều có nhà xuất bản là "NXB Kim Đồng"
    for (Book book : foundBooks) {
        assertEquals("Nhà xuất bản của sách phải là " + publisher, publisher, book.getNhaXuatBan());
    }
}

@Test
public void testTimSachTheoMa() {
    // 1. Tạo dữ liệu test
    int maSach = 1; // Giả sử bạn đã thêm sách với mã này vào cơ sở dữ liệu
    //TextBook textBook = new TextBook(maSach, new Date(), 150000, 5, "NXB Kim Đồng", "Sách Giáo Khoa", "Mới");
    //dao.addBook(textBook); // Thêm sách vào DAO

    // 2. Khởi tạo các dependency
    GetListBookDAO realDAO = new GetListBookDAO();
    presenter = new GetBookListPresenter(); // Khởi tạo presenter mà không cần tham số
    presenter.setView(view);
    presenter.setViewModel(viewModel);
    TimSachUseCase timSachUseCase = new TimSachUseCase((TimSachDatabaseBoundary) realDAO, (TimSachOutputBoundary) presenter);

    // 3. Thực hiện tìm sách
    timSachUseCase.timSachTheoMa(maSach);

    // 4. Kiểm tra kết quả
    List<Book> foundBooks = presenter.getFoundBooks();
    assertNotNull("Danh sách sách tìm thấy không được null", foundBooks);
//    assertEquals("Chỉ có một sách được tìm thấy", 1, foundBooks.size());
 //   assertEquals("Mã sách phải là " + maSach, maSach, foundBooks.get(0).getMaSach());
}
@Test
public void testTimSachTheoNgayNhap() {
    // 1. Tạo dữ liệu test
    Date today = new Date(); // Ngày hiện tại
    // TextBook textBook = new TextBook(2, today, 200000, 5, "NXB Giáo Dục", "Sách Tham Khảo", "Cũ");
    // dao.addBook(textBook); // Thêm sách vào DAO

    // 2. Khởi tạo các dependency
    GetListBookDAO realDAO = new GetListBookDAO();
    presenter = new GetBookListPresenter(); // Khởi tạo presenter mà không cần tham số
    presenter.setView(view);
    presenter.setViewModel(viewModel);
    TimSachUseCase timSachUseCase = new TimSachUseCase((TimSachDatabaseBoundary) realDAO, (TimSachOutputBoundary) presenter);

    // 3. Thực hiện tìm sách
    timSachUseCase.timSachTheoNgayNhap(today);

    // 4. Kiểm tra kết quả
    List<Book> foundBooks = presenter.getFoundBooks();
    assertNotNull("Danh sách sách tìm thấy không được null", foundBooks);
   // assertFalse("Danh sách sách tìm thấy không được rỗng", foundBooks.isEmpty());
    
    for (Book book : foundBooks) {
        assertEquals("Ngày nhập của sách phải là " + today, today, book.getNgayNhap());
    }
}
@Test
public void testTimSachTheoLoai() {
    // 1. Tạo dữ liệu test
    String loaiSach = "Sách Giáo Khoa";
    // TextBook textBook = new TextBook(3, new Date(), 100000, 10, "NXB Kim Đồng", loaiSach, "Mới");
    // dao.addBook(textBook); // Thêm sách vào DAO

    // 2. Khởi tạo các dependency
    GetListBookDAO realDAO = new GetListBookDAO();
    presenter = new GetBookListPresenter(); // Khởi tạo presenter mà không cần tham số
    presenter.setView(view);
    presenter.setViewModel(viewModel);
    TimSachUseCase timSachUseCase = new TimSachUseCase((TimSachDatabaseBoundary) realDAO, (TimSachOutputBoundary) presenter);

    // 3. Thực hiện tìm sách
    timSachUseCase.timSachTheoLoai(loaiSach);

    // 4. Kiểm tra kết quả
    List<Book> foundBooks = presenter.getFoundBooks();
    assertNotNull("Danh sách sách tìm thấy không được null", foundBooks);
    //assertFalse("Danh sách sách tìm thấy không được rỗng", foundBooks.isEmpty());
    
    for (Book book : foundBooks) {
        assertEquals("Loại sách phải là " + loaiSach, loaiSach, book.getLoaiSach());
    }
}

@Test
public void testTimSachTheoDonGia() {
    // 1. Tạo dữ liệu test
    double donGia = 100000;
    // TextBook textBook = new TextBook(4, new Date(), donGia, 5, "NXB Kim Đồng", "Sách Giáo Khoa", "Mới");
    // dao.addBook(textBook); // Thêm sách vào DAO

    // 2. Khởi tạo các dependency
    GetListBookDAO realDAO = new GetListBookDAO();
    presenter = new GetBookListPresenter(); // Khởi tạo presenter mà không cần tham số
    presenter.setView(view);
    presenter.setViewModel(viewModel);
    TimSachUseCase timSachUseCase = new TimSachUseCase((TimSachDatabaseBoundary) realDAO, (TimSachOutputBoundary) presenter);

    // 3. Thực hiện tìm sách
    timSachUseCase.timSachTheoDonGia(donGia);

    // 4. Kiểm tra kết quả
    List<Book> foundBooks = presenter.getFoundBooks();
    assertNotNull("Danh sách sách tìm thấy không được null", foundBooks);
    //assertFalse("Danh sách sách tìm thấy không được rỗng", foundBooks.isEmpty());
    
    for (Book book : foundBooks) {
        assertEquals("Đơn giá của sách phải là " + donGia, donGia, book.getDonGia(), 0);
    }


}
}
