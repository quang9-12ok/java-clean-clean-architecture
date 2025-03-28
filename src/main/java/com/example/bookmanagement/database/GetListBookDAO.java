package com.example.bookmanagement.database;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.bookmanagement.entity.Book;
import com.example.bookmanagement.entity.TextBook;
import com.example.bookmanagement.getListBook.getListDatabaseBoudary;
import com.example.bookmanagement.themSach.ThemSachDatabaseBoundary;
import com.example.bookmanagement.timSach.TimSachDatabaseBoundary;
import com.example.bookmanagement.tinhTongThanhTien.TinhTongThanhTienDatabaseBoundary;
import com.example.bookmanagement.tinhTrungBinhDonGia.TBDatabaseBoundary;
import com.example.bookmanagement.entity.ReferenceBook;
import com.example.bookmanagement.usecase.BookManagementDatabaseBoundary;
import com.example.bookmanagement.usecaseEdit.EditBookDatabaseBoundary;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherDatabaseBoundary;



public class GetListBookDAO implements getListDatabaseBoudary,BookManagementDatabaseBoundary,EditBookDatabaseBoundary,ListBookPublisherDatabaseBoundary, ThemSachDatabaseBoundary, TimSachDatabaseBoundary, TinhTongThanhTienDatabaseBoundary, TBDatabaseBoundary  { 
    @Override
    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Book"; 
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
    
            while (rs.next()) {
                Book book = createBookFromResultSet(rs);
                if (book != null) {
                    books.add(book);
                }
            }
            System.out.println("Số sách được tải: " + books.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
    
    @Override
    public boolean deleteBook(Book book) {
        String sql = "DELETE FROM Book WHERE MaSach = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, book.getMaSach());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean editBook(Book book) {
        
        Date currentDate = new Date(); 
        
        
        String sql = "UPDATE Book SET NgayNhap = ?, DonGia = ?, SoLuong = ?, " +
                    "NhaXuatBan = ?, LoaiSach = ?";
        
     
        if (book instanceof TextBook) {
            sql += ", TinhTrang = ?, Thue = NULL";
        } else if (book instanceof ReferenceBook) {
            sql += ", TinhTrang = NULL, Thue = ?";
        }
        
        sql += " WHERE MaSach = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
           
            pstmt.setDate(1, new java.sql.Date(currentDate.getTime()));
            pstmt.setDouble(2, book.getDonGia());
            pstmt.setInt(3, book.getSoLuong());
            pstmt.setString(4, book.getNhaXuatBan());
            pstmt.setString(5, book.getLoaiSach());
            
            int paramIndex = 6;
            
          
            if (book instanceof TextBook) {
                pstmt.setString(paramIndex, ((TextBook) book).getTinhTrang());
            } else if (book instanceof ReferenceBook) {
                pstmt.setDouble(paramIndex, ((ReferenceBook) book).getThue());
            }
            
         
            pstmt.setInt(paramIndex + 1, book.getMaSach());
            
   
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean validateBook(Book book) {
        if (book == null) {
            return false;
        }
        
       
        if (book.getMaSach() <= 0 ||
            book.getDonGia() < 0 ||
            book.getSoLuong() < 0 ||
            book.getNhaXuatBan() == null || 
            book.getNhaXuatBan().trim().isEmpty()) {
            return false;
        }
        
      
        if (book instanceof TextBook) {
            TextBook textBook = (TextBook) book;
            return textBook.getTinhTrang() != null && 
                   !textBook.getTinhTrang().trim().isEmpty();
        } else if (book instanceof ReferenceBook) {
            ReferenceBook referenceBook = (ReferenceBook) book;
            return referenceBook.getThue() >= 0;
        }
        
        return false;
    }

    private Book createBookFromResultSet(ResultSet rs) throws SQLException {
        int maSach = rs.getInt("MaSach");
        Date ngayNhap = rs.getDate("NgayNhap");
        double donGia = rs.getDouble("DonGia");
        int soLuong = rs.getInt("SoLuong");
        String nhaXuatBan = rs.getString("NhaXuatBan");
        String loaiSach = rs.getString("LoaiSach");
    
        if (loaiSach.equalsIgnoreCase("Sách Giáo Khoa")) {
            String tinhTrang = rs.getString("TinhTrang");
            return new TextBook(maSach, ngayNhap, donGia, soLuong, nhaXuatBan, loaiSach, tinhTrang);
        } else if (loaiSach.equalsIgnoreCase("Sách Tham Khảo")) {
            double thue = rs.getDouble("Thue");
            return new ReferenceBook(maSach, ngayNhap, donGia, soLuong, nhaXuatBan, loaiSach, thue);
        }
        return null; // Nếu không phải sách giáo khoa hay sách tham khảo
    }

    public List<String> getBookTypes() {
        List<String> types = new ArrayList<>();
        String sql = "SELECT DISTINCT LoaiSach FROM Book";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                types.add(rs.getString("LoaiSach"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }

    public List<String> getBookConditions() {
        List<String> conditions = new ArrayList<>();
        String sql = "SELECT DISTINCT TinhTrang FROM Book WHERE TinhTrang IS NOT NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                conditions.add(rs.getString("TinhTrang"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conditions;
    }
    public List<String> getSearchCriteria() {
        List<String> criteria = new ArrayList<>();
        criteria.add("Mã Sách");
        criteria.add("Ngày Nhập");
        criteria.add("Nhà Xuất Bản");
        criteria.add("Loại Sách");
        criteria.add("Đơn Giá");
        return criteria;
    }

    @Override
    public Book getBookById(int maSach) {
        String sql = "SELECT * FROM Book WHERE MaSach = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maSach);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createBookFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<TextBook> getTextBooksByPublisher(String publisher) {
        List<TextBook> textBooks = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE NhaXuatBan = ? AND LoaiSach = 'Sách Giáo Khoa'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, publisher);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Book book = createBookFromResultSet(rs);
                    if (book instanceof TextBook) {
                        textBooks.add((TextBook) book);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return textBooks;
    }


    @Override
    public int themSach(Book book) throws SQLException {
        String sql = "INSERT INTO Book (NgayNhap, DonGia, SoLuong, NhaXuatBan, LoaiSach, TinhTrang, Thue) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
                     
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setDate(1, new java.sql.Date(book.getNgayNhap().getTime()));
            stmt.setDouble(2, book.getDonGia());
            stmt.setInt(3, book.getSoLuong());
            stmt.setString(4, book.getNhaXuatBan());
            stmt.setString(5, book.getLoaiSach());
            
            // Set TinhTrang and Thue based on book type
            if (book instanceof TextBook) {
                stmt.setString(6, ((TextBook) book).getTinhTrang());
                stmt.setNull(7, java.sql.Types.DOUBLE);
            } else if (book instanceof ReferenceBook) {
                stmt.setNull(6, java.sql.Types.VARCHAR);
                stmt.setDouble(7, ((ReferenceBook) book).getThue());
            } else {
                stmt.setNull(6, java.sql.Types.VARCHAR);
                stmt.setNull(7, java.sql.Types.DOUBLE);
            }
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Thêm sách thất bại, không có dòng nào được thêm.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Thêm sách thất bại, không lấy được mã sách.");
                }
            }
        }
    }

    public int getNextBookId() {
        int maxId = 0;
        String sql = "SELECT MAX(MaSach) FROM Book";
        
        try (Connection conn = DatabaseConnection.getConnection();  
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
                
            if (rs.next()) {
                maxId = rs.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return maxId + 1; // Trả về mã sách tiếp theo
    }

    @Override
    public List<Book> findBookById(Integer maSach) {
        List<Book> results = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE MaSach = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, maSach);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Book book = createBookFromResultSet(rs);
                if (book != null) {
                    results.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public List<Book> findBookByDate(Date ngayNhap) {
        List<Book> results = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE DATE(NgayNhap) = DATE(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, new java.sql.Date(ngayNhap.getTime()));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Book book = createBookFromResultSet(rs);
                if (book != null) {
                    results.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public List<Book> findBookByPublisher(String nhaXuatBan) {
        List<Book> results = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE NhaXuatBan LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nhaXuatBan + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Book book = createBookFromResultSet(rs);
                if (book != null) {
                    results.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public List<Book> findBookByType(String loaiSach) {
        List<Book> results = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE LoaiSach = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, loaiSach);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Book book = createBookFromResultSet(rs);
                if (book != null) {
                    results.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public List<Book> findBookByPrice(Double donGia) {
        List<Book> results = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE DonGia = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, donGia);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Book book = createBookFromResultSet(rs);
                if (book != null) {
                    results.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    // @Override
    // public List<Book> getListSachThamKhao() throws SQLException {
    //     List<Book> sachThamKhaoList = new ArrayList<>();
    //     String query = "SELECT * FROM Book WHERE LoaiSach = ?";
        
    //     try (Connection conn = DatabaseConnection.getConnection();
    //          PreparedStatement stmt = conn.prepareStatement(query)) {
            
    //         stmt.setString(1, "Sách Tham Khảo");
            
    //         try (ResultSet rs = stmt.executeQuery()) {
    //             while (rs.next()) {
    //                 ReferenceBook book = new ReferenceBook(
    //                     rs.getInt("MaSach"),  
    //                     rs.getDate("NgayNhap"),
    //                     rs.getDouble("DonGia"),
    //                     rs.getInt("SoLuong"),
    //                     rs.getString("NhaXuatBan"),
    //                     rs.getString("LoaiSach"),
    //                     rs.getDouble("Thue")
    //                 );
    //                 sachThamKhaoList.add(book);
    //             }
    //         }
    //     }
    //     return sachThamKhaoList;
    // }
    

    public double getTongThanhTienSachGiaoKhoa() {
        double tongThanhTien = 0;
        String sql = "SELECT * FROM Book WHERE LoaiSach = 'Sách Giáo Khoa'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                TextBook book = (TextBook) createBookFromResultSet(rs);
                if (book != null) {
                    tongThanhTien += book.tinhThanhTien();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tongThanhTien;
    }

    // Phương thức tính tổng thành tiền cho sách tham khảo
    public double getTongThanhTienSachThamKhao() {
        double tongThanhTien = 0;
        String sql = "SELECT * FROM Book WHERE LoaiSach = 'Sách Tham Khảo'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                ReferenceBook book = (ReferenceBook) createBookFromResultSet(rs);
                if (book != null) {
                    tongThanhTien += book.tinhThanhTien();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In lỗi ra console để kiểm tra
        }
        return tongThanhTien;
    }
    // Phương thức kiểm tra xem danh sách sách có trống không
    public boolean isBookListEmpty() {
        String sql = "SELECT COUNT(*) as count FROM Book";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("count") == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}