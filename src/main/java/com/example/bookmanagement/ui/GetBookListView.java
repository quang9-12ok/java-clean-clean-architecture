package com.example.bookmanagement.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

import com.example.bookmanagement.database.GetListBookDAO;
import com.example.bookmanagement.entity.Book;
import com.example.bookmanagement.entity.ReferenceBook;
import com.example.bookmanagement.entity.TextBook;
import com.example.bookmanagement.themSach.ThemSachDatabaseBoundary;
import com.example.bookmanagement.themSach.ThemSachOutputBoundary;
import com.example.bookmanagement.themSach.ThemSachUseCase;
import com.example.bookmanagement.timSach.TimSachInputBoundary;
import com.example.bookmanagement.tinhTongThanhTien.TinhTongThanhTienOutputDTO;
import com.example.bookmanagement.usecaseEdit.EditBookDatabaseBoundary;
import com.example.bookmanagement.usecaseEdit.EditBookOutputBoundary;
import com.example.bookmanagement.usecaseEdit.EditBookOutputDTO;
import com.example.bookmanagement.usecaseListTB.ListBookPublisherOutputDTO;

public class GetBookListView extends JFrame {
    private GetBookListController controller;
    private GetBookListViewModel viewModel;
    private JTable table;
    private JButton xoaButton;
    private JButton suaButton;
    private JButton themSachButton;
    private JButton listTextBooksButton;
    private JButton timSachButton;
    private JButton tinhTrungBinhDonGiaButton;
    private JPanel buttonPanel;
    private JTextArea resultArea;
    private DefaultTableModel tableModel;
    private GetListBookDAO dao;
    private TimSachInputBoundary timSachUseCase;
    private JButton tinhTongTienButton;
    private boolean updated = false;
    private GetBookListPresenter presenter;
    private ThemSachUseCase themSachUseCase;
    private ThemSachOutputBoundary themSachOutputBoundary;

    public GetBookListView(GetBookListController controller, GetBookListViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        

        String[] columnNames = {
            "Mã Sách",
            "Ngày Nhập",
            "Đơn Giá",
            "Số Lượng",
            "Nhà Xuất Bản",
            "Loại Sách",
            "Tình Trạng",
            "Thuế",
            "Tổng Tiền",
            "Trung bình giá sách tham khảo "
        };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        setTitle("Thư viện X !");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
    
        // Title label
        JLabel tieuDe = new JLabel("THƯ VIỆN X !", SwingConstants.CENTER);
        tieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        tieuDe.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(tieuDe, BorderLayout.NORTH);

      
        
    
        // Table setup
        this.table = new JTable();
        JScrollPane scrollPane = new JScrollPane(this.table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        themSachButton = new JButton("Thêm Sách");
        xoaButton = new JButton("Xóa");
        suaButton = new JButton("Sửa");
        listTextBooksButton = new JButton("Liệt kê sách giáo khoa");
        timSachButton = new JButton("Tìm Sách");
        tinhTongTienButton = new JButton("Tính Tổng Tiền");
        tinhTrungBinhDonGiaButton = new JButton("Tính trung bình đơn giá");
        buttonPanel.add(tinhTrungBinhDonGiaButton);
        buttonPanel.add(tinhTongTienButton);
        buttonPanel.add(themSachButton);
        buttonPanel.add(xoaButton);
        buttonPanel.add(suaButton);
        buttonPanel.add(listTextBooksButton);
        buttonPanel.add(timSachButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
     
       
        setVisible(true);
        loadBooks();
       
        
    }

    public void loadBooks() {
  
        SwingUtilities.invokeLater(() -> {
            System.out.println("DEBUG: loadBooks() called");
            
            // Kiểm tra viewModel
            if (viewModel == null) {
                System.err.println("DEBUG: ViewModel is null in loadBooks()");
                return;
            }
            
            // Lấy danh sách sách từ ViewModel
            List<Book> books = viewModel.getBooks();
            
            // Kiểm tra danh sách sách
            if (books == null || books.isEmpty()) {
                System.out.println("DEBUG: No books to display");
                
                // Hiển thị thông báo cho người dùng
                JOptionPane.showMessageDialog(this, 
                    "Không có sách để hiển thị.", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Đảm bảo bảng được làm mới
                if (tableModel != null) {
                    tableModel.setRowCount(0);
                    tableModel.fireTableDataChanged();
                }
                return;
            }
            
            System.out.println("DEBUG: Books to display: " + books.size());
            
            
            viewModel.updateTableModel();
            
            // Đảm bảo table được cập nhật
            if (table != null) {
                table.setModel(viewModel.getTableModel());
                table.revalidate();
                table.repaint();
            }
        });
    }
    
    
    
    // Thêm phương thức để đảm bảo việc tải lại dữ liệu
    public void reloadBooks() {
        if (controller != null) {
            controller.loadBooks();
        }
        loadBooks();
    }
    private boolean isBookIdColumnSelected() {
        int[] selectedColumns = table.getSelectedColumns();
        return selectedColumns.length == 1 && selectedColumns[0] == 0; 
    }
    public JTable getTable() {
        return table; 
    }
    public JButton getXoaButton() {
        return xoaButton; 
    }
    public JButton getSuaButton() {
        return suaButton;
    }

   
    public void selectSingleColumn(int columnIndex) {
        table.getColumnModel().getSelectionModel().setSelectionInterval(columnIndex, columnIndex);
    }

    public void enableColumnSelection(int columnIndex) {
        table.setColumnSelectionAllowed(true);
        table.setColumnSelectionInterval(columnIndex, columnIndex);
    }

    
    public int getSelectedColumn() {
        return table.getSelectedColumn();
    }

    
    public void clearColumnSelection() {
        table.clearSelection();
    }

    public GetBookListController getController() {
        return this.controller;
    }
    
    

 
    

    public void handleDeleteAction() {
       
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length == 0) {
            showMessage("Cảnh báo", "Vui lòng chọn sách cần xóa!", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        
        if (!isBookIdColumnSelected()) {
            showMessage("Cảnh báo", "Vui lòng chỉ chọn cột mã sách để xóa!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xóa " + selectedRows.length + " cuốn sách đã chọn?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteSelectedBooks(selectedRows);
        }
    }
    public void setPresenter(GetBookListPresenter presenter) {
        this.presenter = presenter;
    }
    
    
    public void initializeController(GetBookListController controller, GetBookListViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

      

        xoaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeleteAction();
            }
        });
        suaButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            
            // Kiểm tra xem có dòng nào được chọn không
            if (selectedRow == -1) {
                showMessage("Cảnh báo", "Vui lòng chọn sách cần sửa!", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Kiểm tra xem cột được chọn có phải là cột Mã Sách không
            int selectedColumn = table.getSelectedColumn();
            if (selectedColumn != 0) {
                showMessage("Cảnh báo", "Vui lòng chỉ chọn cột Mã Sách để sửa!", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Lấy mã sách từ dòng được chọn
            int bookId = (int) viewModel.getTableModel().getValueAt(selectedRow, 0);
            
            // Tìm sách tương ứng
            Book selectedBook = viewModel.getBooks().stream()
                .filter(book -> book.getMaSach() == bookId)
                .findFirst()
                .orElse(null);
            
            // Nếu tìm thấy sách, mở dialog chỉnh sửa
            if (selectedBook != null) {
                controller.showEditBook(selectedBook, this);
            } else {
                showMessage("Lỗi", "Không thể tìm thấy sách để chỉnh sửa", JOptionPane.ERROR_MESSAGE);
            }
        });
        

        themSachButton.addActionListener(e -> {
            controller.showThemSachDialog((JFrame) SwingUtilities.getWindowAncestor(this));
        });
        tinhTongTienButton.addActionListener(e -> {
            // // Check if a row is selected
            // int selectedRow = table.getSelectedRow();
            
            // // Check if the selected column is the Book ID column (index 0)
            // int selectedColumn = table.getSelectedColumn();
            
            // if (selectedRow == -1) {
            //     showMessage("Cảnh báo", "Vui lòng chọn sách!", JOptionPane.WARNING_MESSAGE);
            //     return;
            // }
            
            // if (selectedColumn != 0) {
            //     showMessage("Cảnh báo", "Vui lòng chọn cột Mã Sách để tính tổng tiền!", JOptionPane.WARNING_MESSAGE);
            //     return;
            // }
            
            
            controller.tinhTongTien();
        });
        
        tinhTrungBinhDonGiaButton.addActionListener(e -> {
            try {
                // Check if a row is selected
                int selectedRow = table.getSelectedRow();
                System.out.println("Dòng được chọn: " + selectedRow);
                if (selectedRow == -1) {
                    showMessage("Cảnh báo", "Vui lòng chọn sách!", JOptionPane.WARNING_MESSAGE);
                    return;
                }
        
                // Get book ID and type from the selected row
                Object bookIdObj = viewModel.getTableModel().getValueAt(selectedRow, 0);
                System.out.println("Mã sách: " + bookIdObj);
                Object loaiSachObj = viewModel.getTableModel().getValueAt(selectedRow, 5);
                System.out.println("Loại sách: " + loaiSachObj);
                int bookId;
                String loaiSach;
        
                // Validate book ID
                if (bookIdObj instanceof Integer) {
                    bookId = (Integer) bookIdObj;
                } else if (bookIdObj instanceof String) {
                    bookId = Integer.parseInt((String) bookIdObj);
                } else {
                    showMessage("Cảnh báo", "Mã sách không hợp lệ!", JOptionPane.WARNING_MESSAGE);
                    return;
                }
        
                // Get book type
                loaiSach = loaiSachObj != null ? loaiSachObj.toString() : "";
        
                // Check if the selected book is a reference book
                if (!"Sách Tham Khảo".equals(loaiSach)) {
                    showMessage("Cảnh báo", "Mã sách: " + bookId + " không phải là sách tham khảo. Vui lòng chọn sách tham khảo!", JOptionPane.WARNING_MESSAGE);
                    System.out.println("Loại sách: " + loaiSach);

                    return;
                }
        
                // Execute average price calculation
                controller.tinhTrungBinhDonGia(selectedRow);
        
            } catch (Exception ex) {
                showMessage("Lỗi", "Đã xảy ra lỗi không mong muốn: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        });
        
        
        

        listTextBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String publisher = JOptionPane.showInputDialog(GetBookListView.this, 
                    "Nhập tên nhà xuất bản:", 
                    "Lọc sách giáo khoa", 
                    JOptionPane.QUESTION_MESSAGE);
                
                if (publisher != null) { 
                    controller.listTextBooksByPublisher(publisher.trim());
                }
            }
        });
        timSachButton.addActionListener(e -> {
    controller.showTimSachDialog((JFrame) SwingUtilities.getWindowAncestor(this));
});

        
        tableModel = new DefaultTableModel();
        table.setModel(tableModel);
        table.setModel(viewModel.getTableModel());
        
        controller.loadBooks();
        loadBooks();
    }
    


    
    public void showTongTienResult(double tongTienSGK, double tongTienSTK) {
        DecimalFormat formatter = new DecimalFormat("#,###.## VNĐ");
        String message = String.format("Tổng tiền sách giáo khoa: %s\n" +
                                     "Tổng tiền sách tham khảo: %s\n" ,
                                    
            formatter.format(tongTienSGK),
            formatter.format(tongTienSTK)
           
        );
        
        showMessage("Kết quả tính tổng tiền", message, JOptionPane.INFORMATION_MESSAGE);
    }

    
    public String showInputDialog(String message, String title, int messageType) {
        return JOptionPane.showInputDialog(this, message, title, messageType);
    }
    private String lastWarningMessage; 
    private boolean warningDisplayed = false; 
    public boolean isWarningDisplayed() {
        return warningDisplayed; 
    }
    
    public void resetWarningStatus() {
        warningDisplayed = false; 
        lastWarningMessage = null;
    }
    
    public void showMessage(String title, String message, int messageType) {
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setBackground(new Color(245, 245, 245));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createLineBorder(Color.GRAY)
        ));
        
        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            title,
            messageType
        );
    }
    public int showConfirmDialog(String title, String message, int optionType, int messageType) {
        return JOptionPane.showConfirmDialog(
            this,
            message,
            title,
            optionType,
            messageType
        );
    }
    public String getWarningMessage() {
        return lastWarningMessage;
    }
    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Thành công",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

   
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Lỗi",
            JOptionPane.ERROR_MESSAGE
        );
    }

    
    

    public void updateTongThanhTienDisplay() {
    if (tableModel == null || viewModel == null) return;
    
   
    DecimalFormat formatter = new DecimalFormat("#,###.## VNĐ");
    
    for (int i = 0; i < tableModel.getRowCount(); i++) {
        int maSach = (int) tableModel.getValueAt(i, 0);
        Double tongThanhTien = viewModel.getTongThanhTien(maSach);
        if (tongThanhTien != null) {
        
            String formattedValue = formatter.format(tongThanhTien);
            tableModel.setValueAt(formattedValue, i, 7);
        }
    }
   
    table.repaint();
}
public void showTrungBinhDonGiaResult(double averagePrice) {
    DecimalFormat formatter = new DecimalFormat("#,###.## VNĐ");
    String message = String.format("Trung bình đơn giá sách tham khảo: %s", 
        formatter.format(averagePrice));
 
    showMessage("Kết quả tính trung bình đơn giá", message, JOptionPane.INFORMATION_MESSAGE);
    
   
    table.repaint();
}

public void setUpdated(boolean updated) {
    this.updated = updated;
}


public boolean isUpdated() {
    return updated;
}
public void refreshTable() {
  
    loadBooks();
  
    viewModel.getTableModel().fireTableDataChanged();
}

public void updateEditedBook(EditBookOutputDTO editedBook) {
        // Find and update the specific book row in the table
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            // Assuming first column is book ID
            if (tableModel.getValueAt(row, 0).toString().equals(editedBook.getMaSach())) {
                // Update relevant columns
                tableModel.setValueAt(editedBook.getNhaXuatBan(), row, 2);  // Publisher
                tableModel.setValueAt(editedBook.getDonGia(), row, 3);     // Price
                tableModel.setValueAt(editedBook.getSoLuong(), row, 4);    // Quantity

                // For specific book types
                if (!editedBook.getTinhTrang().isEmpty()) {
                    tableModel.setValueAt(editedBook.getTinhTrang(), row, 5);  // Status for TextBook
                }
                if (editedBook.getThue() > 0) {
                    tableModel.setValueAt(editedBook.getThue(), row, 5);  // Tax for ReferenceBook
                }
                break;
            }
        }
    }



}


