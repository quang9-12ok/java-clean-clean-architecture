package com.example.bookmanagement.ui;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.bookmanagement.database.GetListBookDAO;
import com.example.bookmanagement.timSach.TimSachInputBoundary;
import com.example.bookmanagement.timSach.TimSachOutputBoundary;

public class TimSachDialog extends JDialog {
    private final JComboBox<String> searchCriteriaComboBox;
    private final JTextField searchValueField;
    private final JButton searchButton;
    private final JButton cancelButton;

    private final TimSachInputBoundary timSachUseCase;
    private final TimSachOutputBoundary timSachOutputBoundary;
    private final GetListBookDAO bookDAO;
    private final GetBookListController controller;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private boolean isSearchPerformed = false;

    public TimSachDialog(JFrame parent, 
                         TimSachInputBoundary timSachUseCase, 
                         TimSachOutputBoundary timSachOutputBoundary,
                         GetListBookDAO bookDAO,
                         GetBookListController controller) {
        super(parent, "Tìm Sách", true);
        this.timSachUseCase = timSachUseCase;
        this.timSachOutputBoundary = timSachOutputBoundary;
        this.bookDAO = bookDAO;
        this.controller = controller;

        // Initialize components
        searchCriteriaComboBox = createSearchCriteriaComboBox();
        searchValueField = new JTextField(20);
        searchButton = new JButton("Tìm Kiếm");
        cancelButton = new JButton("Hủy");

        setupUI();
        setupEventHandlers();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(400, 200);
        setLocationRelativeTo(getOwner());

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Search Criteria
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Tìm theo:"), gbc);

        gbc.gridx = 1;
        mainPanel.add(searchCriteriaComboBox, gbc);

        // Search Value
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Giá trị:"), gbc);

        gbc.gridx = 1;
        mainPanel.add(searchValueField, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(searchButton);
        buttonPanel.add(cancelButton);

        // Add panels to dialog
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private JComboBox<String> createSearchCriteriaComboBox() {
        List<String> searchCriteria = bookDAO.getSearchCriteria();
        return new JComboBox<>(searchCriteria.toArray(new String[0]));
    }

    private void setupEventHandlers() {
        searchButton.addActionListener(e -> performSearch());
        
        cancelButton.addActionListener(e -> {
            isSearchPerformed = false;
            dispose();
        });
    }

    private void performSearch() {
        String searchValue = searchValueField.getText().trim();
    
        if (searchValue.isEmpty()) {
            showErrorMessage("Vui lòng nhập giá trị tìm kiếm");
            return;
        }
    
        String selectedCriteria = (String) searchCriteriaComboBox.getSelectedItem();
        try {
            switch (selectedCriteria) {
                case "Mã Sách":
                    timSachUseCase.timSachTheoMa(Integer.parseInt(searchValue));
                    break;
                case "Ngày Nhập":
                    Date date = dateFormat.parse(searchValue);
                    timSachUseCase.timSachTheoNgayNhap(date);
                    break;
                case "Nhà Xuất Bản":
                    timSachUseCase.timSachTheoNhaXuatBan(searchValue);
                    break;
                case "Loại Sách":
                    timSachUseCase.timSachTheoLoai(searchValue);
                    break;
                case "Đơn Giá":
                    timSachUseCase.timSachTheoDonGia(Double.parseDouble(searchValue));
                    break;
            }
            
            // Refresh book list after search
            controller.refreshBookList();
            
            // Clear search value after successful search
            searchValueField.setText("");
            
            // Giữ nguyên dialog để tiếp tục tìm kiếm
            isSearchPerformed = true;
    
        } catch (NumberFormatException e) {
            showErrorMessage("Giá trị không hợp lệ cho tiêu chí " + selectedCriteria);
        } catch (ParseException e) {
            showErrorMessage("Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng yyyy-MM-dd");
        }
    }
    

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Lỗi",
            JOptionPane.ERROR_MESSAGE
        );
    }

    // Static method to show the dialog
    public static void showDialog(JFrame parent, 
                TimSachInputBoundary timSachUseCase, 
                TimSachOutputBoundary timSachOutputBoundary,
                GetListBookDAO bookDAO,
                GetBookListController controller) {
            TimSachDialog dialog = new TimSachDialog(
                    parent, 
                    timSachUseCase, 
                    timSachOutputBoundary, 
                    bookDAO,
                    controller
                    );

            // Đảm bảo dialog luôn hiển thị và không tự động đóng
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
            }

}
