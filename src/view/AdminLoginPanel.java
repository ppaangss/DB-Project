package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AdminLoginPanel extends JPanel{
	public AdminLoginPanel(MainFrame frame) {
        setLayout(new BorderLayout());
        
        //header : 뒤로가기 버튼, 제목
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        //뒤로가기 버튼
        JPanel backPanel = new JPanel(new BorderLayout());
        backPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); 
        
        JButton backButton = new JButton("뒤로가기");
        backButton.addActionListener(e -> frame.showMainMenu());  // 다시 메인 화면으로
 
        backPanel.add(backButton,BorderLayout.CENTER);
        
        headerPanel.add(backPanel,BorderLayout.WEST);
        
        //제목
        JLabel title = new JLabel("관리자 모드", JLabel.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        
        headerPanel.add(title, BorderLayout.CENTER);
 
        //오른쪽 빈 여백, 좌우 균형 맞추기
        JPanel eastSpacer = new JPanel();
        eastSpacer.setPreferredSize(new Dimension(100, 10)); //버튼 크기
        headerPanel.add(eastSpacer, BorderLayout.EAST);
		
        //header(상단)
        add(headerPanel, BorderLayout.NORTH);
       
        // main : 버튼
        JButton Button1 = new JButton("데이터베이스 초기화");
        JButton Button2 = new JButton("데이터베이스 조작");
        JButton Button3 = new JButton("전체 테이블 확인");
        JButton Button4 = new JButton("캠핑카 정비 내역 확인");
        JButton Button5 = new JButton("SQL 질의 검색");

        
        JPanel buttonPanel = new JPanel(new GridLayout(3,2,10,10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 30, 100)); // 버튼 패널 여백
        buttonPanel.add(Button1);
        buttonPanel.add(Button2);
        buttonPanel.add(Button3);
        buttonPanel.add(Button4);
        buttonPanel.add(Button5);
        
        add(buttonPanel, BorderLayout.CENTER);
        
        
        Button3.addActionListener(e -> frame.setContentPaneAndRefresh(new AllTablesPanel(frame)));
        Button4.addActionListener(e -> frame.setContentPaneAndRefresh(new RepairPanel(frame)));
        Button5.addActionListener(e -> frame.setContentPaneAndRefresh(new SQLQueryPanel(frame)));
        
        //		panel.add(buttonPanel, BorderLayout.CENTER);
//      JLabel label1 = new JLabel("", JLabel.CENTER);
//      JPanel footerPanel = new JPanel(new GridLayout(1, 4, 30, 10));
        

//      add(footerPanel, BorderLayout.SOUTH);
    }
}

/*

초기화 기능



*/