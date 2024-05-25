import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            int phoneNumber;

            try {
                phoneNumber = Integer.parseInt(phoneNumberField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Invalid phone number", "Login Failed", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            int userId = getUserIdByUserName(userName);
            if (userId == -1) {
                userId = saveToDatabase(userName, phoneNumber);
            }

            if (userId != -1) {
                UserSession session = new UserSession();
                session.setUserName(userName);
                session.setUserId(userId);
                session.setPhoneNumber(phoneNumber);

                JOptionPane.showMessageDialog(LoginFrame.this, "Hello, " + session.getUserName() + " :)", "Login Successful", JOptionPane.PLAIN_MESSAGE);
                dispose();
                MainFrame mainFrame = new MainFrame(session);
                mainFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Error saving to database", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }

        private int getUserIdByUserName(String userName) {
            String query = "SELECT user_id FROM user WHERE user_name = ?";

            try (Connection connection = App.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, userName);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("user_id");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return -1;
        }

        private int saveToDatabase(String userName, int phoneNumber) {
            String query = "INSERT INTO user (user_name, phone_num) VALUES (?, ?)";

            try (Connection connection = App.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, userName);
                stmt.setInt(2, phoneNumber);
                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return -1;
            }
        }
    }
}
