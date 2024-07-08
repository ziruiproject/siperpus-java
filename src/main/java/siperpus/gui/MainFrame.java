package siperpus.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainFrame extends JFrame {
    private JPanel sidebar;
    private JPanel contentPanel;

    public MainFrame() {
        setTitle("Library Management System");
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200, 600));
        sidebar.setBackground(new Color(54, 63, 77));
        sidebar.setBorder(new EmptyBorder(10, 5, 10, 5));

        JButton btnAuthors = createSidebarButton("Authors", "icons/author.png");
        JButton btnBooks = createSidebarButton("Books", "icons/book.png");
        JButton btnMembers = createSidebarButton("Members", "icons/member.png");
        JButton btnBorrowTransactions = createSidebarButton("Borrow Transactions", "icons/transaction.png");
        JButton btnPublishers = createSidebarButton("Publishers", "icons/publisher.png");

        btnAuthors.addActionListener((ActionEvent e) -> showAuthorForm());
        btnBooks.addActionListener((ActionEvent e) -> showBookForm());
        btnMembers.addActionListener((ActionEvent e) -> showMemberForm());
        btnPublishers.addActionListener((ActionEvent e) -> showPublisherForm());
        btnBorrowTransactions.addActionListener((ActionEvent e) -> showBorrowTransaction());
        // Add action listeners for other buttons as needed

        sidebar.add(btnAuthors);
        sidebar.add(btnBooks);
        sidebar.add(btnMembers);
        sidebar.add(btnBorrowTransactions);
        sidebar.add(btnPublishers);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setPreferredSize(new Dimension(600, 600));
        contentPanel.setBackground(Color.WHITE);

        // Initially show AuthorForm
        showAuthorForm();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidebar, BorderLayout.WEST);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
    }

    private JButton createSidebarButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(180, 40));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(54, 63, 77));
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        try {
            Path path = Paths.get(iconPath);
            byte[] iconBytes = Files.readAllBytes(path);
            ImageIcon icon = new ImageIcon(iconBytes);
            button.setIcon(icon);
            button.setHorizontalTextPosition(SwingConstants.RIGHT);
            button.setIconTextGap(10);
        } catch (IOException e) {
            System.out.println(e);
        }

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void showAuthorForm() {
        contentPanel.removeAll();
        AuthorForm authorFormPanel = new AuthorForm();
        contentPanel.add(authorFormPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showBookForm() {
        contentPanel.removeAll();
        BookForm bookFormPanel = new BookForm();
        contentPanel.add(bookFormPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showPublisherForm() {
        contentPanel.removeAll();
        PublisherForm publisherFormPanel = new PublisherForm();
        contentPanel.add(publisherFormPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showMemberForm() { // New method to show MemberForm
        contentPanel.removeAll();
        MemberForm memberFormPanel = new MemberForm();
        contentPanel.add(memberFormPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showBorrowTransaction() { // New method to show MemberForm
        contentPanel.removeAll();
        BorrowTransactionForm borrowTransactionFormPanel = new BorrowTransactionForm();
        contentPanel.add(borrowTransactionFormPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

}
