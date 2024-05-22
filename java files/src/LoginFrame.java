import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField userNameField;

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

        JLabel label = new JLabel("닉네임을 입력하세요.");
        label.setHorizontalAlignment(JLabel.CENTER);
        add(label, gbc);

        gbc.gridy = 1;
        userNameField = new JTextField(20);
        add(userNameField, gbc);

        gbc.gridy = 2;
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginAction());
        add(loginButton, gbc);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userName = userNameField.getText();
            // 임시 세션 생성
            UserSession session = new UserSession();
            session.setUserName(userName);
            session.setUserId(1);  // 임시 ID

            JOptionPane.showMessageDialog(LoginFrame.this, session.getUserName() + "님 안녕하세요:)", "Login Successful", JOptionPane.PLAIN_MESSAGE);
            dispose();
            MainFrame mainFrame = new MainFrame(session);
            mainFrame.setVisible(true);
        }
    }
}
