
import siperpus.gui.MainFrame;

import javax.swing.*;
import java.io.File;
import siperpus.database.Database;

public class Main {
    public static void main(String[] args) {
        String dbPath = "siperpus.db";
        File dbFile = new File(dbPath);

        Database db = new Database();

        // Initialize database if it does not exist
        if (!dbFile.exists()) {
            db.initializeDatabase();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });

    }
}
