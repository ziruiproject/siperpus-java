package siperpus.gui;

import siperpus.dao.PublisherDAO;
import siperpus.database.Database;
import siperpus.model.Publisher;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PublisherForm extends JPanel {
    private final PublisherDAO publisherDAO;
    private final JTable table;
    private final DefaultTableModel tableModel;

    public PublisherForm() {
        Database database = new Database();
        publisherDAO = new PublisherDAO(database);

        setLayout(new BorderLayout());

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
        lblName.setFont(new Font("Arial", Font.BOLD, 16));
        JTextField txtName = new JTextField();
        txtName.setFont(new Font("Arial", Font.PLAIN, 16));

        // Add ListSelectionListener to table
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        String publisher = (String) tableModel.getValueAt(selectedRow, 1);

                        txtName.setText(publisher);
                    }
                }
            }
        });

        JButton btnAdd = new JButton("Add");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 16));
        btnAdd.addActionListener((ActionEvent e) -> {
            String name = txtName.getText();

            Publisher publisher = new Publisher();
            publisher.setName(name);

            publisherDAO.addPublisher(publisher);
            loadPublishers();
        });

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setFont(new Font("Arial", Font.BOLD, 16));
        btnUpdate.addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String name = txtName.getText();

                Publisher publisher = new Publisher();
                publisher.setId(id);
                publisher.setName(name);

                publisherDAO.updatePublisher(publisher);
                loadPublishers();
            }
        });

        JButton btnDelete = new JButton("Delete");
        btnDelete.setFont(new Font("Arial", Font.BOLD, 16));
        btnDelete.addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                publisherDAO.deletePublisher(id);
                loadPublishers();
            }
        });

        formPanel.add(lblName);
        formPanel.add(txtName);
        formPanel.add(btnAdd);
        formPanel.add(btnUpdate);
        formPanel.add(btnDelete);

        add(formPanel, BorderLayout.SOUTH);

        loadPublishers();
    }

    private void loadPublishers() {
        List<Publisher> publishers = publisherDAO.getAllPublishers();
        tableModel.setRowCount(0);
        for (Publisher publisher : publishers) {
            tableModel.addRow(new Object[]{
                    publisher.getId(),
                    publisher.getName(),
                    publisher.getCreatedAt(),
                    publisher.getUpdatedAt()
            });
        }
    }
}
