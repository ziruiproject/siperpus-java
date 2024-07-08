package siperpus.gui;

import siperpus.dao.AuthorDAO;
import siperpus.dao.BookDAO;
import siperpus.dao.PublisherDAO;
import siperpus.database.Database;
import siperpus.model.Author;
import siperpus.model.Book;
import siperpus.model.Publisher;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class BookForm extends JPanel {
    private final BookDAO bookDAO;
    private final AuthorDAO authorDAO;
    private final PublisherDAO publisherDAO;

    private final JTable table;
    private final DefaultTableModel tableModel;

    private final JTextField txtSearch;
    private final JComboBox<String> authorComboBox;
    private final JComboBox<String> publisherComboBox;

    public BookForm() {
        Database database = new Database();
        bookDAO = new BookDAO(database);
        authorDAO = new AuthorDAO(database);
        publisherDAO = new PublisherDAO(database);

        setLayout(new BorderLayout());

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setFont(new Font("Arial", Font.BOLD, 16));
        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 16));
        JButton btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("Arial", Font.BOLD, 16));
        btnSearch.addActionListener(this::searchBooks);

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        add(searchPanel, BorderLayout.NORTH);

        // Table panel
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Title");
        tableModel.addColumn("Call Number");
        tableModel.addColumn("Stock");
        tableModel.addColumn("Author");
        tableModel.addColumn("Publisher");
        tableModel.addColumn("Created At");
        tableModel.addColumn("Updated At");

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.setRowHeight(20);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(7, 2));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around form panel

        JLabel lblTitle = new JLabel("Title:");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        JTextField txtTitle = new JTextField();
        txtTitle.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel lblCallNumber = new JLabel("Call Number:");
        lblCallNumber.setFont(new Font("Arial", Font.BOLD, 16));
        JTextField txtCallNumber = new JTextField();
        txtCallNumber.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel lblStock = new JLabel("Stock:");
        lblStock.setFont(new Font("Arial", Font.BOLD, 16));
        JTextField txtStock = new JTextField();
        txtStock.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel lblAuthor = new JLabel("Author:");
        lblAuthor.setFont(new Font("Arial", Font.BOLD, 16));
        authorComboBox = new JComboBox<>();
        authorComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        loadAuthorsIntoComboBox();

        JLabel lblPublisher = new JLabel("Publisher:");
        lblPublisher.setFont(new Font("Arial", Font.BOLD, 16));
        publisherComboBox = new JComboBox<>();
        publisherComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        loadPublishersIntoComboBox();

        // Add ListSelectionListener to table
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        String bookTitle = (String) tableModel.getValueAt(selectedRow, 1);
                        String callNumber = (String) tableModel.getValueAt(selectedRow, 2);
                        String stock = tableModel.getValueAt(selectedRow, 3).toString();
                        String author = tableModel.getValueAt(selectedRow, 4).toString();
                        String publisher = tableModel.getValueAt(selectedRow, 5).toString();

                        txtTitle.setText(bookTitle);
                        txtCallNumber.setText(callNumber);
                        txtStock.setText(stock);
                        authorComboBox.setSelectedItem(author);
                        publisherComboBox.setSelectedItem(publisher);
                    }
                }
            }
        });

        JButton btnAdd = new JButton("Add");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 16));
        btnAdd.addActionListener((ActionEvent e) -> {
            String title = txtTitle.getText();
            String callNumber = txtCallNumber.getText();
            int stock = Integer.parseInt(txtStock.getText());
            String authorName = (String) authorComboBox.getSelectedItem();
            String publisherName = (String) publisherComboBox.getSelectedItem();

            Author selectedAuthor = authorDAO.getAuthorByName(authorName);
            Publisher selectedPublisher = publisherDAO.getPublisherByName(publisherName);

            if (selectedAuthor != null && selectedPublisher != null) {
                Book book = new Book();
                book.setTitle(title);
                book.setCallNumber(callNumber);
                book.setStock(stock);

                // Check if the author-publisher combination exists, if not, create it
                int authorPublisherId = getAuthorPublisherId(selectedAuthor.getId(), selectedPublisher.getId());
                book.setAuthorPublisherId(authorPublisherId);

                bookDAO.addBook(book);
                loadBooks();
            } else {
                JOptionPane.showMessageDialog(this, "Author or Publisher not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setFont(new Font("Arial", Font.BOLD, 16));
        btnUpdate.addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String title = txtTitle.getText();
                String callNumber = txtCallNumber.getText();
                int stock = Integer.parseInt(txtStock.getText());
                String authorName = (String) authorComboBox.getSelectedItem();
                String publisherName = (String) publisherComboBox.getSelectedItem();

                Author selectedAuthor = authorDAO.getAuthorByName(authorName);
                Publisher selectedPublisher = publisherDAO.getPublisherByName(publisherName);

                if (selectedAuthor != null && selectedPublisher != null) {
                    Book book = new Book();
                    book.setId(id);
                    book.setTitle(title);
                    book.setCallNumber(callNumber);
                    book.setStock(stock);

                    // Check if the author-publisher combination exists, if not, create it
                    int authorPublisherId = getAuthorPublisherId(selectedAuthor.getId(), selectedPublisher.getId());
                    book.setAuthorPublisherId(authorPublisherId);

                    bookDAO.updateBook(book);
                    loadBooks();
                } else {
                    JOptionPane.showMessageDialog(this, "Author or Publisher not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnDelete = new JButton("Delete");
        btnDelete.setFont(new Font("Arial", Font.BOLD, 16));
        btnDelete.addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                bookDAO.deleteBook(id);
                loadBooks();
            }
        });

        formPanel.add(lblTitle);
        formPanel.add(txtTitle);
        formPanel.add(lblCallNumber);
        formPanel.add(txtCallNumber);
        formPanel.add(lblStock);
        formPanel.add(txtStock);
        formPanel.add(lblAuthor);
        formPanel.add(authorComboBox);
        formPanel.add(lblPublisher);
        formPanel.add(publisherComboBox);
        formPanel.add(btnAdd);
        formPanel.add(btnUpdate);
        formPanel.add(btnDelete);

        add(formPanel, BorderLayout.SOUTH);

        loadBooks();
    }

    private void loadBooks() {
        List<Book> books = bookDAO.getAllBooks();
        tableModel.setRowCount(0);
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                    book.getId(),
                    book.getTitle(),
                    book.getCallNumber(),
                    book.getStock(),
                    book.getAuthorName(),  // Display author name
                    book.getPublisherName(), // Display publisher name
                    book.getCreatedAt(),
                    book.getUpdatedAt()
            });
        }
    }

    private void loadAuthorsIntoComboBox() {
        List<Author> authors = authorDAO.getAllAuthors();
        for (Author author : authors) {
            authorComboBox.addItem(author.getName());
        }
    }

    private void loadPublishersIntoComboBox() {
        List<Publisher> publishers = publisherDAO.getAllPublishers();
        for (Publisher publisher : publishers) {
            publisherComboBox.addItem(publisher.getName());
        }
    }

    private int getAuthorPublisherId(int authorId, int publisherId) {
        int authorPublisherId = bookDAO.getAuthorPublisherId(authorId, publisherId);
        if (authorPublisherId == -1) {
            // If combination doesn't exist, create it
            authorPublisherId = bookDAO.addAuthorPublisher(authorId, publisherId);
        }
        return authorPublisherId;
    }

    private void searchBooks(ActionEvent e) {
        String keyword = txtSearch.getText();
        List<Book> books = bookDAO.searchBooks(keyword);

        tableModel.setRowCount(0);
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                    book.getId(),
                    book.getTitle(),
                    book.getCallNumber(),
                    book.getStock(),
                    book.getAuthorName(), // Display author name
                    book.getPublisherName(), // Display publisher name
                    book.getCreatedAt(),
                    book.getUpdatedAt()
            });
        }
    }
}
