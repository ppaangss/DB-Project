package view;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AdminLoginPanel extends JPanel{
	public AdminLoginPanel(MainFrame frame) {
        setLayout(new BorderLayout());

        JLabel label = new JLabel("관리자 로그인 화면입니다", JLabel.CENTER);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        add(label, BorderLayout.CENTER);

        JButton backButton = new JButton("뒤로가기");
        backButton.addActionListener(e -> frame.showMainMenu());  // 다시 메인 화면으로

        add(backButton, BorderLayout.SOUTH);
    }
}
