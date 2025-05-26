package repair;

import javax.swing.*;

import view.MainFrame;

import java.awt.*;

public class ShopPanel extends JPanel {
    private MainFrame frame;
    private JPanel previousPanel;

    public ShopPanel(MainFrame frame, JPanel previousPanel, String shopName) {
        this.frame = frame;
        this.previousPanel = previousPanel;

        setLayout(new BorderLayout());

        // 헤더: 뒤로가기 버튼 + 제목
        JPanel header = new JPanel(new BorderLayout());

        JButton backButton = new JButton("뒤로가기");
        backButton.addActionListener(e -> frame.setContentPaneAndRefresh(previousPanel));
        header.add(backButton, BorderLayout.WEST);

        JLabel title = new JLabel("정비소 정보", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        // 내용 영역
        JPanel content = new JPanel(new GridLayout(6, 1, 10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        content.add(new JLabel("정비소명: " + shopName));
        content.add(new JLabel("주소: 서울특별시 강남구 ..."));
        content.add(new JLabel("전화번호: 02-123-4567"));
        content.add(new JLabel("담당자: 홍길동"));
        content.add(new JLabel("이메일: repair@example.com"));
        content.add(new JLabel("전문 분야: 엔진/브레이크 정비"));

        add(content, BorderLayout.CENTER);
    }
}
