package siperpus.gui;

import siperpus.dao.BookDAO;
import siperpus.dao.BorrowTransactionDAO;
import siperpus.dao.MemberDAO;
import siperpus.database.Database;
import siperpus.model.Book;
import siperpus.model.BorrowTransaction;
import siperpus.model.Member;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BorrowTransactionForm extends JPanel {
    private final BorrowTransactionDAO transactionDAO;
    private final MemberDAO memberDAO;
    private final BookDAO bookDAO;

    private final JTable table;
    private final DefaultTableModel tableModel;

    private final JTextField txtSearch;
    private final JComboBox<String> memberComboBox;
    private final JComboBox<String> bookComboBox;
    private final JTextField txtBorrowDate;
    private final JTextField txtReturnDate;

    public BorrowTransactionForm() {
        Database database = new Database();
        transactionDAO = new BorrowTransactionDAO(database);
        memberDAO = new MemberDAO(database);
        bookDAO = new BookDAO(database);

        setLayout(new BorderLayout());

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setFont(new Font("Arial", Font.BOLD, 16));
        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 16));
        JButton btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("Arial", Font.BOLD, 16));
        btnSearch.addActionListener(this::searchTransactions);

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        add(searchPanel, BorderLayout.NORTH);

        // Table panel
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Member");
        tableModel.addColumn("Book");
        tableModel.addColumn("Borrow Date");
        tableModel.addColumn("Return Date");
        tableModel.addColumn("Actual Return Date");

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.setRowHeight(20);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Add ListSelectionListener to table
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        String memberName = (String) tableModel.getValueAt(selectedRow, 1);
                        String bookTitle = (String) tableModel.getValueAt(selectedRow, 2);
                        String borrowDate = tableModel.getValueAt(selectedRow, 3).toString();
                        String returnDate = tableModel.getValueAt(selectedRow, 4).toString();

                        memberComboBox.setSelectedItem(memberName);
                        bookComboBox.setSelectedItem(bookTitle);
                        txtBorrowDate.setText(borrowDate);
                        txtReturnDate.setText(returnDate);
                    }
                }
            }
        });

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 2));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around form panel

        JLabel lblMember = new JLabel("Member:");
        lblMember.setFont(new Font("Arial", Font.BOLD, 16));
        memberComboBox = new JComboBox<>();
        memberComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        loadMembersIntoComboBox();

        JLabel lblBook = new JLabel("Book:");
        lblBook.setFont(new Font("Arial", Font.BOLD, 16));
        bookComboBox = new JComboBox<>();
        bookComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        loadBooksIntoComboBox();

        JLabel lblBorrowDate = new JLabel("Borrow Date:");
        lblBorrowDate.setFont(new Font("Arial", Font.BOLD, 16));
        txtBorrowDate = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        txtBorrowDate.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel lblReturnDate = new JLabel("Return Date:");
        lblReturnDate.setFont(new Font("Arial", Font.BOLD, 16));
        txtReturnDate = new JTextField();
        txtReturnDate.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton btnAdd = getBtnAdd();

        JButton btnUpdate = getBtnUpdate();


        JButton btnReturn = getBtnReturn();

        formPanel.add(lblMember);
        formPanel.add(memberComboBox);
        formPanel.add(lblBook);
        formPanel.add(bookComboBox);
        formPanel.add(lblBorrowDate);
        formPanel.add(txtBorrowDate);
        formPanel.add(lblReturnDate);
        formPanel.add(txtReturnDate);
        formPanel.add(btnAdd);
        formPanel.add(btnUpdate);
        formPanel.add(new JLabel());
        formPanel.add(btnReturn);

        add(formPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private JButton getBtnReturn() {
        JButton btnReturn = new JButton("Return");
        btnReturn.setFont(new Font("Arial", Font.BOLD, 16));
        btnReturn.addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String actualReturnDateStr = sdf.format(new Date());

                BorrowTransaction transaction = transactionDAO.getBorrowTransactionById(id);
                if (transaction != null) {
                    transaction.setActualReturnDate(java.sql.Date.valueOf(actualReturnDateStr));
                    transactionDAO.updateBorrowTransaction(transaction);
                    bookDAO.incrementBookStock(id);
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Book returned successfully!");
                }
            }
        });
        return btnReturn;
    }

    private JButton getBtnUpdate() {
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setFont(new Font("Arial", Font.BOLD, 16));
        btnUpdate.addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String memberName = (String) memberComboBox.getSelectedItem();
                String bookTitle = (String) bookComboBox.getSelectedItem();
                String borrowDate = txtBorrowDate.getText();
                String returnDate = txtReturnDate.getText();

                Member selectedMember = memberDAO.getMemberByName(memberName);
                Book selectedBook = bookDAO.getBookByTitle(bookTitle);

                if (selectedMember != null && selectedBook != null) {
                    BorrowTransaction transaction = new BorrowTransaction();
                    transaction.setId(id);
                    transaction.setMemberId(selectedMember.getId());
                    transaction.setBookId(selectedBook.getId());
                    transaction.setBorrowDate(java.sql.Date.valueOf(borrowDate));
                    transaction.setScheduledReturnDate(java.sql.Date.valueOf(returnDate));

                    transactionDAO.updateBorrowTransaction(transaction);
                    refreshTable();
                }
            }
        });
        return btnUpdate;
    }

    private JButton getBtnAdd() {
        JButton btnAdd = new JButton("Add");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 16));
        btnAdd.addActionListener((ActionEvent e) -> {
            String memberName = (String) memberComboBox.getSelectedItem();
            String bookTitle = (String) bookComboBox.getSelectedItem();
            String borrowDate = txtBorrowDate.getText();
            String returnDate = txtReturnDate.getText();

            Member selectedMember = memberDAO.getMemberByName(memberName);
            Book selectedBook = bookDAO.getBookByTitle(bookTitle);

            if (selectedMember != null && selectedBook != null) {
                BorrowTransaction transaction = new BorrowTransaction();
                transaction.setMemberId(selectedMember.getId());
                transaction.setBookId(selectedBook.getId());
                transaction.setBorrowDate(java.sql.Date.valueOf(borrowDate));
                transaction.setScheduledReturnDate(java.sql.Date.valueOf(returnDate));

                transactionDAO.addBorrowTransaction(transaction);
                System.out.println("gas");
                bookDAO.decrementBookStock(selectedBook.getId());
                refreshTable();
            }
        });
        return btnAdd;
    }

    private void loadMembersIntoComboBox() {
        List<Member> members = memberDAO.getAllMembers();
        for (Member member : members) {
            memberComboBox.addItem(member.getName());
        }
    }

    private void loadBooksIntoComboBox() {
        List<Book> books = bookDAO.getAllBooks();
        for (Book book : books) {
            bookComboBox.addItem(book.getTitle());
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<BorrowTransaction> transactions = transactionDAO.getAllBorrowTransactions();
        for (BorrowTransaction transaction : transactions) {
            String memberName = memberDAO.getMemberById(transaction.getMemberId()).getName();
            String bookTitle = bookDAO.getBookById(transaction.getBookId()).getTitle();
            tableModel.addRow(new Object[]{
                    transaction.getId(),
                    memberName,
                    bookTitle,
                    transaction.getBorrowDate(),
                    transaction.getScheduledReturnDate(),
                    transaction.getActualReturnDate() // Display actual return date
            });
        }
    }

    private void searchTransactions(ActionEvent e) {
        String keyword = txtSearch.getText();
        List<BorrowTransaction> transactions = transactionDAO.searchBorrowTransactions(keyword);
        tableModel.setRowCount(0);
        for (BorrowTransaction transaction : transactions) {
            String memberName = memberDAO.getMemberById(transaction.getMemberId()).getName();
            String bookTitle = bookDAO.getBookById(transaction.getBookId()).getTitle();
            tableModel.addRow(new Object[]{
                    transaction.getId(),
                    memberName,
                    bookTitle,
                    transaction.getBorrowDate(),
                    transaction.getScheduledReturnDate(),
                    transaction.getActualReturnDate() // Display actual return date
            });
        }
    }
}
