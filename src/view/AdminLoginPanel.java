package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import data.DBConnection;
import dataManipulation.AdminDataManipulateMenuPanel;
import repair.RepairPanel;
import sql.SQLQueryPanel;
import table.AllTablesPanel;

public class AdminLoginPanel extends JPanel{
	public AdminLoginPanel(MainFrame frame) {
        setLayout(new BorderLayout());
        
        //header : 뒤로가기 버튼, 제목
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        //뒤로가기 버튼
        JPanel backPanel = new JPanel(new BorderLayout());
        backPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 25, 5));         
        JButton backButton = new JButton("뒤로가기");
        backButton.addActionListener(e -> frame.showMainMenu());  // 다시 메인 화면으로
        backPanel.add(backButton,BorderLayout.CENTER); 
        headerPanel.add(backPanel,BorderLayout.WEST);
        
        //제목
        JLabel title = new JLabel("관리자 모드", JLabel.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
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

        Button1.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "정말로 데이터베이스를 초기화하시겠습니까?", "DB 초기화", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    DBConnection.runInitScript("sql/202501-22013441-ini.sql");
                    JOptionPane.showMessageDialog(this, "초기화 완료");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "초기화 실패: " + ex.getMessage());
                }
            }
        });

        Button2.addActionListener(e -> frame.setContentPaneAndRefresh(new AdminDataManipulateMenuPanel(frame)));
        Button3.addActionListener(e -> frame.setContentPaneAndRefresh(new AllTablesPanel(frame)));
        Button4.addActionListener(e -> frame.setContentPaneAndRefresh(new RepairPanel(frame)));
        Button5.addActionListener(e -> frame.setContentPaneAndRefresh(new SQLQueryPanel(frame)));
    }
}


/*

초기화 기능



*/