package com.example.bookmanagement.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.example.bookmanagement.entity.Book;
import com.example.bookmanagement.entity.ReferenceBook;
import com.example.bookmanagement.entity.TextBook;

public class GetBookListViewModel {
     private DefaultTableModel tableModel;
    private List<Book> books;
     private Map<Integer, Double> tongThanhTienMap = new HashMap<>();
     private double averageReferenceBookPrice;
     private JTable table;
    

     public GetBookListViewModel() {
        String[] columnNames = {
            "Mã Sách", "Ngày Nhập", "Đơn Giá", "Số Lượng", 
            "Nhà Xuất Bản", "Loại Sách", "Tình Trạng", "Thuế", "Tổng Tiền","Trung bình giá sách tham khảo"
        };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
    }
    

    
     

    public void setBooks(List<Book> books) {
        this.books = books;
        updateTableModel();
    }

    

    public List<Book> getBooks() {
        return books;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void updateBooks(List<Book> updatedBooks) {
        this.books = updatedBooks;
     
        updateTableModel();
    }
    public void updateBook(Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getMaSach() == updatedBook.getMaSach()) {
                books.set(i, updatedBook);
                break;
            }
        }
        updateTableModel();
    }

    public void updateTongThanhTien(double tongThanhTienSGK, double tongThanhTienSTK) {
        // Cập nhật giá trị tổng thành tiền cho từng loại sách
        for (Book book : books) {
            double thanhTien;
            if (book instanceof TextBook) {
                TextBook textBook = (TextBook) book;
                thanhTien = book.getDonGia() * book.getSoLuong();
                // Kiểm tra tình trạng sách giáo khoa và áp dụng giảm giá nếu là sách cũ
                if ("Cũ".equalsIgnoreCase(textBook.getTinhTrang())) {
                    thanhTien *= 0.5; // Giảm 50% cho sách cũ
                }
            } else {
                ReferenceBook refBook = (ReferenceBook) book;
                thanhTien = book.getDonGia() * book.getSoLuong() * (1 + refBook.getThue() / 100);
            }
            tongThanhTienMap.put(book.getMaSach(), thanhTien);
        }
    }
    
    public Double getTongThanhTien(int maSach) {
        return tongThanhTienMap.get(maSach);
    }
    public void updateTrungBinhDonGia(double averagePrice) {
        this.averageReferenceBookPrice = averagePrice;
        updateTableModel();
    }

    public void calculateAverageReferenceBookPrice() {
        if (books != null && !books.isEmpty()) {
            double total = 0;
            int count = 0;
    
            for (Book book : books) {
                if (book instanceof ReferenceBook) {
                    total += book.getDonGia();
                    count++;
                }
            }
    
            if (count > 0) {
                averageReferenceBookPrice = total / count;
            } else {
                averageReferenceBookPrice = 0; 
            }
    
            
            updateTableModel();
        }
    }
    public void updateTableModel() {
        SwingUtilities.invokeLater(() -> {
            // Xóa toàn bộ dòng hiện tại
            tableModel.setRowCount(0);
    
            // Kiểm tra danh sách sách
            if (books == null || books.isEmpty()) {
                System.out.println("Không có sách để hiển thị.");
                tableModel.fireTableDataChanged();
                return;
            }
    
            // Duyệt và thêm từng cuốn sách
            for (Book book : books) {
                try {
                    // Khởi tạo các biến
                    String tinhTrang = "";
                    String thueFormatted = "";
                    Double thue = 0.0;
                    Double tongTien = tongThanhTienMap.get(book.getMaSach());
    
                    // Xử lý sách giáo khoa
                    if (book instanceof TextBook) {
                        TextBook textBook = (TextBook) book;
                        tinhTrang = textBook.getTinhTrang();
                    }
    
                    // Xử lý sách tham khảo
                    if (book instanceof ReferenceBook) {
                        ReferenceBook refBook = (ReferenceBook) book;
                        thue = refBook.getThue();
                        thueFormatted = String.format("%.2f%%", thue);
                    }
    
                    // Định dạng tổng tiền
                    String tongTienFormatted = tongTien != null 
                        ? String.format("%,.2f VNĐ", tongTien) 
                        : "";
    
                    // Tạo dòng dữ liệu
                    Object[] row = {
                        book.getMaSach(),
                        book.getNgayNhap(),
                        String.format("%,.2f", book.getDonGia()),
                        book.getSoLuong(),
                        book.getNhaXuatBan(),
                        book.getLoaiSach(),
                        tinhTrang, // Hiển thị tình trạng
                        thueFormatted, // Hiển thị thuế
                        tongTienFormatted,
                        averageReferenceBookPrice > 0 
                        ? String.format("%.2f", averageReferenceBookPrice) 
                        :""
                    };
    
                    // Thêm dòng vào bảng
                    tableModel.addRow(row);
                } catch (Exception e) {
                    System.err.println("Lỗi khi thêm sách vào bảng: " + book.getMaSach());
                    e.printStackTrace();
                }
            }
    
            // Thông báo model đã thay đổi
            tableModel.fireTableDataChanged();
            System.out.println("Đã thêm " + tableModel.getRowCount() + " sách vào bảng");
        });
    }
    public void setAverageReferenceBookPrice(double averagePrice) {
        this.averageReferenceBookPrice = averagePrice;
        updateTableModel();
    }

    public double getAverageReferenceBookPrice() {
        return averageReferenceBookPrice;
    }
    public JTable getTable() {
        return table;
    }
    
    public void setTable(JTable table) {
        this.table = table;
    }
    
}   