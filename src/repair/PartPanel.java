package repair;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import view.MainFrame;

public class PartPanel extends JPanel {
    private MainFrame frame;
    private JPanel previousPanel;  // 이전 패널 저장용

    public PartPanel(MainFrame frame, JPanel previousPanel, String partName) {
        this.frame = frame;
        this.previousPanel = previousPanel;

        setLayout(new BorderLayout());

        // 상단 타이틀 + 뒤로가기 버튼
        JPanel header = new JPanel(new BorderLayout());
        JButton backButton = new JButton("뒤로가기");
        backButton.addActionListener(e -> frame.setContentPaneAndRefresh(previousPanel));
        header.add(backButton, BorderLayout.WEST);

        JLabel title = new JLabel("부품 정보", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        // 부품 정보 표시 (임시 텍스트)
        JPanel content = new JPanel(new GridLayout(5, 1, 10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        content.add(new JLabel("부품명: " + partName));
        content.add(new JLabel("부품 설명: 이 부품은 ..."));
        content.add(new JLabel("가격: ￦10000"));
        content.add(new JLabel("재고 수량: 25개"));
        content.add(new JLabel("입고일자: 2024-01-01"));

        add(content, BorderLayout.CENTER);
    }
}
