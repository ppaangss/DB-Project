package dataManipulation;

import javax.swing.*;

import view.AdminLoginPanel;
import view.MainFrame;

import java.awt.*;

public class AdminDataManipulateMenuPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private MainFrame frame;

    public AdminDataManipulateMenuPanel(MainFrame frame) {
        this.frame = frame;

        setLayout(new BorderLayout());

        // 제목
        JLabel titleLabel = new JLabel("데이터 조작 메뉴", JLabel.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // 버튼 영역
        JPanel buttonPanel = new JPanel(new GridLayout(1,3, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        JButton insertButton = new JButton("입력");
        JButton updateButton = new JButton("변경");
        JButton deleteButton = new JButton("삭제");

        insertButton.setFont(new Font("맑은 고딕", Font.BOLD, 30));
        updateButton.setFont(new Font("맑은 고딕", Font.BOLD, 30));
        deleteButton.setFont(new Font("맑은 고딕", Font.BOLD, 30));

        // 액션 설정
        insertButton.addActionListener(e -> {
            frame.setContentPaneAndRefresh(new AdminInsertPanel(frame)); // 입력화면으로 이동
        });

        updateButton.addActionListener(e -> {
            frame.setContentPaneAndRefresh(new AdminUpdateDeletePanel(frame)); // 변경/삭제 화면으로 이동
        });
        
        deleteButton.addActionListener(e -> {
            frame.setContentPaneAndRefresh(new AdminUpdateDeletePanel(frame)); // 변경/삭제 화면으로 이동
        });

        buttonPanel.add(insertButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.CENTER);

        // 뒤로가기 버튼
        JButton backButton = new JButton("뒤로가기");
       // backButton.addActionListener(e -> frame.showAdminMenu());
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        backPanel.add(backButton);
        add(backPanel, BorderLayout.SOUTH);
        
        backButton.addActionListener(e -> {
            frame.setContentPaneAndRefresh(new AdminLoginPanel(frame)); // 변경/삭제 화면으로 이동
        });
    }
}

/*
 * 적응력 토론  자신감 기초 면접
 * 
 * */
