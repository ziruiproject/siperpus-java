package siperpus.gui;

import siperpus.dao.MemberDAO;
import siperpus.database.Database;
import siperpus.model.Member;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MemberForm extends JPanel {
    private final MemberDAO memberDAO;
    private final JTable table;
    private final DefaultTableModel tableModel;

    public MemberForm() {
        Database database = new Database();
        memberDAO = new MemberDAO(database);

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600)); // Adjust panel size as needed

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Email");
        tableModel.addColumn("Phone");
        tableModel.addColumn("Created At");
        tableModel.addColumn("Updated At");

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.setRowHeight(20);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around form panel

        JLabel lblName = new JLabel("Name:");
        lblName.setFont(new Font("Arial", Font.BOLD, 18)); // Increase font size for labels

        JTextField txtName = new JTextField();
        txtName.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size for text fields

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 18)); // Increase font size for labels

        JTextField txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size for text fields

        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setFont(new Font("Arial", Font.BOLD, 18)); // Increase font size for labels

        JTextField txtPhone = new JTextField();
        txtPhone.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size for text fields

        // Add ListSelectionListener to table
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        String name = (String) tableModel.getValueAt(selectedRow, 1);
                        String email = (String) tableModel.getValueAt(selectedRow, 2);
                        String phone = tableModel.getValueAt(selectedRow, 3).toString();

                        txtName.setText(name);
                        txtEmail.setText(email);
                        txtPhone.setText(phone);
                    }
                }
            }
        });

        JButton btnAdd = new JButton("Add");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 16)); // Increase font size for buttons
        btnAdd.addActionListener((ActionEvent e) -> {
            String name = txtName.getText();
            String email = txtEmail.getText();
            String phone = txtPhone.getText();

            Member member = new Member();
            member.setName(name);
            member.setEmail(email);
            System.out.println(phone);
            member.setPhoneNumber(phone);

            memberDAO.addMember(member);
            loadMembers();
        });

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setFont(new Font("Arial", Font.BOLD, 16)); // Increase font size for buttons
        btnUpdate.addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String name = txtName.getText();
                String email = txtEmail.getText();
                String phone = txtPhone.getText();

                Member member = new Member();
                member.setId(id);
                member.setName(name);
                member.setEmail(email);
                member.setPhoneNumber(phone);

                memberDAO.updateMember(member);
                loadMembers();
            }
        });

        JButton btnDelete = new JButton("Delete");
        btnDelete.setFont(new Font("Arial", Font.BOLD, 16)); // Increase font size for buttons
        btnDelete.addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                memberDAO.deleteMember(id);
                loadMembers();
            }
        });

        formPanel.add(lblName);
        formPanel.add(txtName);
        formPanel.add(lblEmail);
        formPanel.add(txtEmail);
        formPanel.add(lblPhone);
        formPanel.add(txtPhone);
        formPanel.add(btnAdd);
        formPanel.add(btnUpdate);
        formPanel.add(btnDelete);

        add(formPanel, BorderLayout.SOUTH);

        loadMembers();
    }

    private void loadMembers() {
        List<Member> members = memberDAO.getAllMembers();
        tableModel.setRowCount(0);
        for (Member member : members) {
            tableModel.addRow(new Object[]{
                    member.getId(),
                    member.getName(),
                    member.getEmail(),
                    member.getPhoneNumber(),
                    member.getCreatedAt(),
                    member.getUpdatedAt()
            });
        }
    }
}
