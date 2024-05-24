import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField userNameField;
    private JTextField phoneNumberField;  // 전화번호 입력 필드

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

        // 전화번호 입력 필드 추가
        gbc.gridy = 2;
        JLabel phoneLabel = new JLabel("Enter your phonenumber");
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
            String phoneNumber = phoneNumberField.getText();  // 전화번호 입력값 가져오기

            // 임시 세션 생성
            UserSession session = new UserSession();
            session.setUserName(userName);
            session.setUserId(1);  // 임시 ID
            session.setPhoneNumber(phoneNumber);  // 전화번호 세션에 저장

            JOptionPane.showMessageDialog(LoginFrame.this, "Hello, " + session.getUserName() + " :) " , "Login Successful", JOptionPane.PLAIN_MESSAGE);
            dispose();
            MainFrame mainFrame = new MainFrame(session);
            mainFrame.setVisible(true);
        }
    }
}