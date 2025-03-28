package com.example.bookmanagement.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import com.example.bookmanagement.database.GetListBookDAO;
import com.example.bookmanagement.entity.Book;
import com.example.bookmanagement.entity.ReferenceBook;
import com.example.bookmanagement.entity.TextBook;
import com.example.bookmanagement.usecaseEdit.*;

public class EditBookDialog extends JDialog {
    private final JTextField txtNhaXuatBan;
    private final JComboBox<String> cboLoaiSach;
    private final JTextField txtDonGia;
    private final JTextField txtSoLuong;

    private final JPanel pnlSachGiaoKhoa;
    private final JComboBox<String> cboTinhTrang;

    private final JPanel pnlSachThamKhao;
    private final JTextField txtThue;

    private final JButton btnCapNhat;
    private final JButton btnHuy;

    private boolean isConfirmed = false;
    private final GetListBookDAO bookDAO;
    private final EditBookUseCase editBookUseCase;
    private final EditBookOutputBoundary outputBoundary;
    private final GetBookListController controller;
    private Book bookToEdit;

    public EditBookDialog(JFrame parent, Book book, 
                          EditBookUseCase editBookUseCase, 
                          EditBookOutputBoundary outputBoundary,
                          GetBookListController controller) {
        super(parent, "Chỉnh Sửa Sách", true);

        this.bookToEdit = book;
        this.editBookUseCase = editBookUseCase;
        this.outputBoundary = outputBoundary;
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

        btnCapNhat = new JButton("Cập Nhật");
        btnHuy = new JButton("Hủy");

        initComponents();
        populateBookData();
        setupEventHandlers();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addBasicComponents(mainPanel, gbc);
        setupBookTypePanels();

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        mainPanel.add(pnlSachGiaoKhoa, gbc);
        mainPanel.add(pnlSachThamKhao, gbc);

        JPanel buttonPanel = createButtonPanel();

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void addBasicComponents(JPanel panel, GridBagConstraints gbc) {

        // Nhà Xuất Bản
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nhà xuất bản:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNhaXuatBan, gbc);

        // Loại Sách
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Loại sách:"), gbc);
        gbc.gridx = 1;
        panel.add(cboLoaiSach, gbc);

        // Đơn Giá
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Đơn giá:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDonGia, gbc);

        // Số Lượng
        gbc.gridx = 0; gbc.gridy = 4;
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
        buttonPanel.add(btnCapNhat);
        buttonPanel.add(btnHuy);
        return buttonPanel;
    }

    public class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }

    private void populateBookData() {
        txtNhaXuatBan.setText(bookToEdit.getNhaXuatBan());
        cboLoaiSach.setSelectedItem(bookToEdit.getLoaiSach());
        txtDonGia.setText(String.valueOf(bookToEdit.getDonGia()));
        txtSoLuong.setText(String.valueOf(bookToEdit.getSoLuong()));
    
        // Ẩn/hiện panel phù hợp với loại sách
        if (bookToEdit instanceof TextBook) {
            cboTinhTrang.setSelectedItem(((TextBook) bookToEdit).getTinhTrang());
            pnlSachGiaoKhoa.setVisible(true);
            pnlSachThamKhao.setVisible(false);
        } else if (bookToEdit instanceof ReferenceBook) {
            txtThue.setText(String.valueOf(((ReferenceBook) bookToEdit).getThue()));
            pnlSachGiaoKhoa.setVisible(false);
            pnlSachThamKhao.setVisible(true);
        }
    
        // Gọi phương thức để đảm bảo panel được hiển thị đúng
        handleBookTypeSelection();
    }
    
    private void setupEventHandlers() {
        cboLoaiSach.addActionListener(e -> handleBookTypeSelection());
        
        btnCapNhat.addActionListener(e -> handleUpdateBookAction());
        
        btnHuy.addActionListener(e -> {
            isConfirmed = false;
            dispose();
        });
    }

    private void handleBookTypeSelection() {
        String selectedType = (String) cboLoaiSach.getSelectedItem();
        pnlSachGiaoKhoa.setVisible("Sách Giáo Khoa".equals(selectedType));
        pnlSachThamKhao.setVisible("Sách Tham Khảo".equals(selectedType));
        pack();
    }

    private void handleUpdateBookAction() {
        try {
            DataExportEdit editData = createEditData();
            
            try {
                editBookUseCase.execute(editData);
                
                controller.refreshBookList();
                
                isConfirmed = true;
                dispose();
            } catch (ValidationException ex) {
                // Hiển thị thông báo lỗi nhưng không đóng dialog
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Lỗi Nhập Liệu",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Lỗi: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    private DataExportEdit createEditData() {
        // Lấy loại sách mới được chọn
        String newBookType = (String) cboLoaiSach.getSelectedItem();
    
        // Prepare default values for optional parameters
        String tinhTrang = "";
        Double thue = null; // Thay đổi thành null
    
        // Xử lý các trường hợp loại sách khác nhau
        if (newBookType.equals("Sách Giáo Khoa")) {
            tinhTrang = (String) cboTinhTrang.getSelectedItem();
        } else if (newBookType.equals("Sách Tham Khảo")) {
            // Chuyển đổi sang Double
            thue = Double.parseDouble(txtThue.getText());
        }
    
        return new DataExportEdit(
            bookToEdit.getMaSach(),           // maSach
            newBookType,                      // loaiSach (đã cập nhật)
            tinhTrang,                        // tinhTrang
            thue,                             // thue (giờ là Double)
            Double.parseDouble(txtDonGia.getText()),  // donGia
            Integer.parseInt(txtSoLuong.getText()),   // soLuong
            txtNhaXuatBan.getText()           // nhaXuatBan
        );
    }
    
    


    public static void showDialog(JFrame parent, Book book, 
                                   EditBookUseCase editBookUseCase, 
                                   EditBookOutputBoundary outputBoundary,
                                   GetBookListController controller) {
        EditBookDialog dialog = new EditBookDialog(parent, book, 
                                                    editBookUseCase, 
                                                    outputBoundary, 
                                                    controller);
        dialog.setVisible(true);
    }
}
