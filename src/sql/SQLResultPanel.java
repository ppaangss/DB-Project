package sql;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.jdbc.result.ResultSetMetaData;
import com.mysql.cj.xdevapi.Statement;

import data.DBConnection;
import view.MainFrame;

public class SQLResultPanel extends JPanel{
	private static final long serialVersionUID = 1L;

    public SQLResultPanel(MainFrame frame, String query) {
        setLayout(new BorderLayout());

      //화면
        //header(상단) : 뒤로가기 버튼, 제목
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        //뒤로가기 버튼
        JPanel backPanel = new JPanel(new BorderLayout());
        backPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); 
        
        JButton backButton = new JButton("뒤로가기");
        backButton.addActionListener(e -> frame.setContentPaneAndRefresh(new SQLQueryPanel(frame)));
        
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

        
        // 결과 테이블 패널
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        try {
            DefaultTableModel resultModel = DBConnection.executeQuery(query); // Model 호출
            table.setModel(resultModel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "SQL 오류: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
        // 쿼리 실행
    }

}
