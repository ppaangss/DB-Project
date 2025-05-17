package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainFrame extends JFrame{

	public MainFrame() {
        setTitle("캠핑카 예약 시스템");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);//중앙 정렬

        showMainMenu();  // 메인 메뉴 패널로 시작
        setVisible(true);
    }

    // 메인 메뉴 화면
	
    public void showMainMenu() {
    	JPanel panel = new JPanel(new BorderLayout());
    	//BorderLayout 으로 구현

        // 제목 Label
        JLabel titleLabel = new JLabel("캠핑카 예약 시스템", JLabel.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));  // 상하 여백
        panel.add(titleLabel, BorderLayout.NORTH);

        // 버튼 영역
        JButton userLoginButton = new JButton("회원 로그인");
        JButton adminLoginButton = new JButton("관리자 로그인");

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 30));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100)); // 버튼 패널 여백
        buttonPanel.add(userLoginButton);
        buttonPanel.add(adminLoginButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        //버튼 이벤트
        
        //회원 로그인 
        //userLoginButton.addActionListener(e -> setContentPaneAndRefresh(new UserLoginPanel(this)));
        
        //관리자 로그인
        adminLoginButton.addActionListener(e -> setContentPaneAndRefresh(new AdminLoginPanel(this)));

        setContentPaneAndRefresh(panel);
    }

    // 공통 패널 교체 함수
    public void setContentPaneAndRefresh(JPanel panel) {
        setContentPane(panel);
        revalidate();  // 레이아웃 다시 계산
        repaint();     // 화면 다시 그리기
    }
	
}
