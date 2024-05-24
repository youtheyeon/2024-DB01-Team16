import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField userNameField;
    private JTextField phoneNumberField;

    public LoginFrame() {
        setTitle("Library Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel label = new JLabel("Enter your name");
        label.setHorizontalAlignment(JLabel.CENTER);
        add(label, gbc);

        gbc.gridy = 1;
        userNameField = new JTextField(20);
        add(userNameField, gbc);

        gbc.gridy = 2;
        JLabel phoneLabel = new JLabel("Enter your phone number");
        phoneLabel.setHorizontalAlignment(JLabel.CENTER);
        add(phoneLabel, gbc);

        gbc.gridy = 3;
        phoneNumberField = new JTextField(20);
        add(phoneNumberField, gbc);

        gbc.gridy = 4;
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginAction());
        add(loginButton, gbc);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userName = userNameField.getText();
            String phoneNumber = phoneNumberField.getText();

            if (saveToDatabase(userName, phoneNumber)) {
                UserSession session = new UserSession();
                session.setUserName(userName);
                session.setUserId(1);
                session.setPhoneNumber(phoneNumber);

                JOptionPane.showMessageDialog(LoginFrame.this, "Hello, " + session.getUserName() + " :)", "Login Successful", JOptionPane.PLAIN_MESSAGE);
                dispose();
                MainFrame mainFrame = new MainFrame(session);
                mainFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Error saving to database", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }

        private boolean saveToDatabase(String userName, String phoneNumber) {
            String url = "jdbc:mysql://localhost:3306/librarymanagement";
            String user = "root";  // 데이터베이스 사용자 이름
            String password = "";  // 데이터베이스 비밀번호

            String query = "INSERT INTO user (user_name, phone_num) VALUES (?, ?)";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, userName);
                stmt.setString(2, phoneNumber);
                stmt.executeUpdate();
                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}
