package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SQLQueryPanel extends JPanel {
    public SQLQueryPanel(MainFrame frame) {
        setLayout(new BorderLayout());

        
        //화면
        //header(상단) : 뒤로가기 버튼, 제목
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        //뒤로가기 버튼
        JPanel backPanel = new JPanel(new BorderLayout());
        backPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); 
        
        JButton backButton = new JButton("뒤로가기");
 
        backPanel.add(backButton,BorderLayout.CENTER);
        
        headerPanel.add(backPanel,BorderLayout.WEST);
        
        //제목
        JLabel title = new JLabel("SQL 질의 검색", JLabel.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        headerPanel.add(title, BorderLayout.CENTER);

        //오른쪽 빈 여백, 좌우 균형 맞추기
        JPanel eastSpacer = new JPanel();
        eastSpacer.setPreferredSize(new Dimension(100, 10)); //버튼 크기
        headerPanel.add(eastSpacer, BorderLayout.EAST);
        
        //header(상단)
        add(headerPanel, BorderLayout.NORTH);
        
        //main(중간) : 검색 패널
        
        JTextArea queryArea = new JTextArea(7, 30);
        queryArea.setLineWrap(true);
        queryArea.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        JScrollPane scrollPane = new JScrollPane(queryArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("SQL 쿼리를 입력하세요"));
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel searchPanel = new JPanel();
        JButton searchButton = new JButton("검색");
        
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.SOUTH);
        
        //select만 가능하게, 예외처리 해줘야함.

        
        
        //버튼 액션
        backButton.addActionListener(e -> frame.setContentPaneAndRefresh(new AdminLoginPanel(frame)));  // 다시 메인 화면으로
        
        searchButton.addActionListener(e -> {
            String query = queryArea.getText().trim();
            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(this, "SQL 쿼리를 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                return;
            }
            frame.setContentPaneAndRefresh(new SQLResultPanel(frame, query));
        });
        
        
        
//        // 테이블 (중앙)
//        String[] columns = {"ID", "이름", "이메일"};
//        Object[][] data = {
//                {"1", "홍길동", "hong@test.com"},
//                {"2", "김철수", "kim@test.com"},
//                {"3", "이영희", "lee@test.com"}
//        };
//        DefaultTableModel model = new DefaultTableModel(data, columns);
//        JTable table = new JTable(model);
//        JScrollPane scrollPane = new JScrollPane(table);
//        add(scrollPane, BorderLayout.CENTER);
//
//        // 검색 버튼 클릭 이벤트 (샘플: 텍스트 포함한 행만 표시)
//        searchButton.addActionListener(e -> {
//            String keyword = searchField.getText().trim();
//            DefaultTableModel filteredModel = new DefaultTableModel(columns, 0);
//
//            for (int i = 0; i < data.length; i++) {
//                boolean match = false;
//                for (int j = 0; j < data[i].length; j++) {
//                    if (data[i][j].toString().contains(keyword)) {
//                        match = true;
//                        break;
//                    }
//                }
//                if (match) {
//                    filteredModel.addRow(data[i]);
//                }
//            }
//
//            table.setModel(filteredModel);
//        });

        // 뒤로가기 버튼
//        JButton backButton = new JButton("뒤로가기");
//        backButton.addActionListener(e -> frame.setContentPaneAndRefresh(new AdminLoginPanel(frame)));
//        JPanel southPanel = new JPanel();
//        southPanel.add(backButton);
//        add(southPanel, BorderLayout.SOUTH);
    }
}

