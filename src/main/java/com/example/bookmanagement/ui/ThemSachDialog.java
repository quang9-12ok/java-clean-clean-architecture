package com.example.bookmanagement.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.example.bookmanagement.database.GetListBookDAO;
import com.example.bookmanagement.entity.Book;
import com.example.bookmanagement.entity.ReferenceBook;
import com.example.bookmanagement.entity.TextBook;
import com.example.bookmanagement.themSach.ThemSachOutputBoundary;
import com.example.bookmanagement.themSach.ThemSachUseCase;

public class ThemSachDialog extends JDialog {
    private final JTextField txtNhaXuatBan;
    private final JComboBox<String> cboLoaiSach;
    private final JTextField txtDonGia;
    private final JTextField txtSoLuong;
    
    private final JPanel pnlSachGiaoKhoa;
    private final JComboBox<String> cboTinhTrang;
    
    private final JPanel pnlSachThamKhao;
    private final JTextField txtThue;
    
    private final JButton btnThem;
    private final JButton btnHuy;
    
    private boolean isConfirmed = false;
    private final GetListBookDAO bookDAO;
    private final ThemSachUseCase themSachUseCase;
    private final ThemSachOutputBoundary themSachOutputBoundary;
    private final GetBookListController controller;
    private Book resultBook;

    private static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }

    public ThemSachDialog(JFrame parent, ThemSachUseCase themSachUseCase, 
                         ThemSachOutputBoundary themSachOutputBoundary,
                         GetBookListController controller) {
        super(parent, "Thêm Sách Mới", true);
        
        this.themSachUseCase = themSachUseCase;
        this.themSachOutputBoundary = themSachOutputBoundary;
        this.controller = controller;
        this.bookDAO = new GetListBookDAO();

        // Initialize components
        txtNhaXuatBan = new JTextField(20);
        txtDonGia = new JTextField(20);
        txtSoLuong = new JTextField(20);
        
        List<String> bookTypes = bookDAO.getBookTypes();
        cboLoaiSach = new JComboBox<>(bookTypes.toArray(new String[0]));
        
        pnlSachGiaoKhoa = new JPanel(new GridBagLayout());
        List<String> conditions = bookDAO.getBookConditions();
        cboTinhTrang = new JComboBox<>(conditions.toArray(new String[0]));
        
        pnlSachThamKhao = new JPanel(new GridBagLayout());
        txtThue = new JTextField(20);
        
        btnThem = new JButton("Thêm");
        btnHuy = new JButton("Hủy");

        initComponents();
        setupEventHandlers();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
      
        addBasicComponents(mainPanel, gbc);
        
        setupBookTypePanels();
        
 
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        mainPanel.add(pnlSachGiaoKhoa, gbc);
        mainPanel.add(pnlSachThamKhao, gbc);
        pnlSachThamKhao.setVisible(false);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();

       
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Configure dialog
        pack();
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void addBasicComponents(JPanel panel, GridBagConstraints gbc) {
        // Publisher
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nhà xuất bản:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNhaXuatBan, gbc);

        // Book Type
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Loại sách:"), gbc);
        gbc.gridx = 1;
        panel.add(cboLoaiSach, gbc);

        // Price
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Đơn giá:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDonGia, gbc);

        // Quantity
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Số lượng:"), gbc);
        gbc.gridx = 1;
        panel.add(txtSoLuong, gbc);
    }

    private void setupBookTypePanels() {
        // TextBook Panel
        GridBagConstraints gbcSGK = new GridBagConstraints();
        gbcSGK.insets = new Insets(5, 5, 5, 5);
        gbcSGK.fill = GridBagConstraints.HORIZONTAL;
        
        gbcSGK.gridx = 0; gbcSGK.gridy = 0;
        pnlSachGiaoKhoa.add(new JLabel("Tình trạng:"), gbcSGK);
        gbcSGK.gridx = 1;
        pnlSachGiaoKhoa.add(cboTinhTrang, gbcSGK);

        // ReferenceBook Panel
        GridBagConstraints gbcSTK = new GridBagConstraints();
        gbcSTK.insets = new Insets(5, 5, 5, 5);
        gbcSTK.fill = GridBagConstraints.HORIZONTAL;
        
        gbcSTK.gridx = 0; gbcSTK.gridy = 0;
        pnlSachThamKhao.add(new JLabel("Thuế (%):"), gbcSTK);
        gbcSTK.gridx = 1;
        pnlSachThamKhao.add(txtThue, gbcSTK);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnThem);
        buttonPanel.add(btnHuy);
        return buttonPanel;
    }

    private void setupEventHandlers() {
        cboLoaiSach.addActionListener(e -> handleBookTypeSelection());
        btnThem.addActionListener(e -> handleAddBookAction());
        btnHuy.addActionListener(e -> {
            isConfirmed = false;
            dispose();
        });
    }

    private void handleBookTypeSelection() {
        String selectedType = (String) cboLoaiSach.getSelectedItem();
        pnlSachGiaoKhoa.setVisible("Sách Giáo Khoa".equals(selectedType));
        pnlSachThamKhao.setVisible("Sách Tham Khảo".equals(selectedType));

        if ("Sách Giáo Khoa".equals(selectedType)) {
            cboTinhTrang.setSelectedIndex(0);
            txtThue.setText("");
        } else if ("Sách Tham Khảo".equals(selectedType)) {
            txtThue.setText("");
            cboTinhTrang.setSelectedIndex(0);
        }

        pack();
    }
    private void handleAddBookAction() {
        try {
            // Tạo sách từ input
            Book book = createBookFromInput();
            
            // Thực thi thêm sách qua UseCase
            themSachUseCase.execute(book);
            
            // Nếu không có ngoại lệ, cập nhật danh sách và đóng dialog
            this.resultBook = book;
            controller.refreshBookList();
            
            isConfirmed = true;
            dispose();  // Chỉ đóng dialog nếu không có lỗi
        } catch (IllegalArgumentException ex) {
            // Hiển thị thông báo lỗi chi tiết mà không đóng dialog
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
            );
            // Giữ dialog mở bằng cách không gọi dispose()
            isConfirmed = false;
        } catch (Exception ex) {
            // Các lỗi khác
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    
    
    private Book createBookFromInput() throws SQLException {
        // Kiểm tra nhà xuất bản
        String nhaXuatBan = txtNhaXuatBan.getText().trim();
        if (nhaXuatBan.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập nhà xuất bản");
        }
        // Kiểm tra nhà xuất bản không chứa số
        if (nhaXuatBan.matches(".*\\d+.*")) {
        throw new IllegalArgumentException("Nhà xuất bản không được chứa số");
        }

    
        // Kiểm tra loại sách
        String loaiSach = (String) cboLoaiSach.getSelectedItem();
        if (loaiSach == null || loaiSach.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn loại sách");
        }
    
        // Kiểm tra đơn giá
        String donGiaStr = txtDonGia.getText().trim();
        if (donGiaStr.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập đơn giá");
        }
        double donGia;
        try {
            donGia = Double.parseDouble(donGiaStr);
            if (donGia <= 0) {
                throw new IllegalArgumentException("Đơn giá phải lớn hơn 0");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Đơn giá không hợp lệ");
        }
    
        // Kiểm tra số lượng
        String soLuongStr = txtSoLuong.getText().trim();
        if (soLuongStr.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập số lượng");
        }
        int soLuong;
        try {
            soLuong = Integer.parseInt(soLuongStr);
            if (soLuong < 0) {
                throw new IllegalArgumentException("Số lượng không được âm");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Số lượng không hợp lệ");
        }
    
        int maSach = bookDAO.getNextBookId();
        Date ngayNhap = new Date();
    
        // Tạo sách theo loại
        if ("Sách Giáo Khoa".equals(loaiSach)) {
            // Kiểm tra tình trạng sách giáo khoa
            String tinhTrang = (String) cboTinhTrang.getSelectedItem();
            if (tinhTrang == null || tinhTrang.trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng chọn tình trạng sách giáo khoa");
            }
            return createTextBook(maSach, ngayNhap, donGia, soLuong, nhaXuatBan, loaiSach);
        } else if ("Sách Tham Khảo".equals(loaiSach)) {
            // Kiểm tra thuế
            String thueStr = txtThue.getText().trim();
            if (thueStr.isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập thuế");
            }
            double thue;
            try {
                thue = Double.parseDouble(thueStr);
                if (thue < 0 || thue > 100) {
                    throw new IllegalArgumentException("Thuế phải nằm trong khoảng từ 0 đến 100");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Thuế không hợp lệ");
            }
            return createReferenceBook(maSach, ngayNhap, donGia, soLuong, nhaXuatBan, loaiSach);
        }
    
        throw new IllegalArgumentException("Loại sách không hợp lệ");
    }
    
    
    private TextBook createTextBook(int maSach, Date ngayNhap, double donGia, 
                                  int soLuong, String nhaXuatBan, String loaiSach) {
        String tinhTrang = (String) cboTinhTrang.getSelectedItem();
        return new TextBook(maSach, ngayNhap, donGia, soLuong, nhaXuatBan, loaiSach, tinhTrang);
    }
    
    private ReferenceBook createReferenceBook(int maSach, Date ngayNhap, double donGia, 
                                            int soLuong, String nhaXuatBan, String loaiSach) {
        String thueStr = txtThue.getText().trim();
        double thue = Double.parseDouble(thueStr);
        return new ReferenceBook(maSach, ngayNhap, donGia, soLuong, nhaXuatBan, loaiSach, thue);
    }
    public static Book showDialog(JFrame parent, ThemSachUseCase themSachUseCase,
                                ThemSachOutputBoundary themSachOutputBoundary,
                                GetBookListController controller) {
        ThemSachDialog dialog = new ThemSachDialog(parent, themSachUseCase, 
                                                 themSachOutputBoundary, controller);
        dialog.setVisible(true);
        
        // Trả về kết quả nếu người dùng xác nhận
        if (dialog.isConfirmed) {
            return dialog.resultBook;
        }
        return null;
    }
}