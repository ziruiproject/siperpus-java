package siperpus.gui;

import siperpus.dao.AuthorDAO;
import siperpus.database.Database;
import siperpus.model.Author;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AuthorForm extends JPanel {
    private final AuthorDAO authorDAO;
    private final JTable table;
    private final DefaultTableModel tableModel;

    public AuthorForm() {
        Database database = new Database();
        authorDAO = new AuthorDAO(database);

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600)); // Adjust panel size as needed

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Created At");
        tableModel.addColumn("Updated At");

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.setRowHeight(20);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around form panel

        JLabel lblName = new JLabel("Name:");
        lblName.setFont(new Font("Arial", Font.BOLD, 18)); // Increase font size for labels

        JTextField txtName = new JTextField();
        txtName.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size for text fields

        JButton btnAdd = new JButton("Add");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 16)); // Increase font size for buttons
        btnAdd.addActionListener((ActionEvent e) -> {
            String name = txtName.getText();

            Author author = new Author();
            author.setName(name);

            authorDAO.addAuthor(author);
            loadAuthors();
        });

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setFont(new Font("Arial", Font.BOLD, 16)); // Increase font size for buttons
        btnUpdate.addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String name = txtName.getText();

                Author author = new Author();
                author.setId(id);
                author.setName(name);

                authorDAO.updateAuthor(author);
                loadAuthors();
            }
        });

        JButton btnDelete = new JButton("Delete");
        btnDelete.setFont(new Font("Arial", Font.BOLD, 16)); // Increase font size for buttons
        btnDelete.addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                authorDAO.deleteAuthor(id);
                loadAuthors();
            }
        });

        formPanel.add(lblName);
        formPanel.add(txtName);
        formPanel.add(btnAdd);
        formPanel.add(btnUpdate);
        formPanel.add(btnDelete);

        add(formPanel, BorderLayout.SOUTH);

        loadAuthors();
    }

    private void loadAuthors() {
        List<Author> authors = authorDAO.getAllAuthors();
        tableModel.setRowCount(0);
        for (Author author : authors) {
            tableModel.addRow(new Object[]{
                    author.getId(),
                    author.getName(),
                    author.getCreatedAt(),
                    author.getUpdatedAt()
            });
        }
    }
}
