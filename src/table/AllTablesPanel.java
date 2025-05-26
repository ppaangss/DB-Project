package table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import view.AdminLoginPanel;
import view.MainFrame;

public class AllTablesPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JList<String> tableList;
    private JTable contentTable;

    public AllTablesPanel(MainFrame frame) {
        setLayout(new BorderLayout());
        
        // 🔼 상단 제목
        JLabel titleLabel = new JLabel("전체 테이블 보기", JLabel.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        // ◀ 왼쪽 테이블 목록 리스트
        tableList = new JList<>();
        tableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(tableList);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("테이블 목록"));
        listScrollPane.setPreferredSize(new Dimension(200, 0));
        add(listScrollPane, BorderLayout.WEST);

        // ▶ 가운데 테이블 내용 출력
        contentTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(contentTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("테이블 내용"));
        add(tableScrollPane, BorderLayout.CENTER);

        // ◀ 리스트에서 항목 선택 시 → 오른쪽 테이블 내용 출력 (내용은 controller/model에서 채울 예정)
        tableList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedTable = tableList.getSelectedValue();
                if (selectedTable != null) {
                    // TODO: DB에서 해당 테이블 내용 불러오기
                    System.out.println("선택된 테이블: " + selectedTable);
                }
            }
        });

        // ⏪ 뒤로가기 버튼
        JButton backButton = new JButton("뒤로가기");
        backButton.addActionListener(e -> frame.setContentPaneAndRefresh(new AdminLoginPanel(frame)));

        JPanel southPanel = new JPanel();
        southPanel.add(backButton);
        add(southPanel, BorderLayout.SOUTH);
        
        // 🔹 테이블 목록 초기화
        try {
            java.util.List<String> tables = data.DBConnection.getAllTables();
            setTableListData(tables.toArray(new String[0]));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "테이블 목록 불러오기 실패\n" + ex.getMessage(), "DB 오류", JOptionPane.ERROR_MESSAGE);
        }

        
        tableList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedTable = tableList.getSelectedValue();
                if (selectedTable != null) {
                    try {
                        DefaultTableModel content = data.DBConnection.getTableContent(selectedTable);
                        setTableContent(content);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "테이블 내용 불러오기 실패\n" + ex.getMessage(), "DB 오류", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        
    }

    // 📌 외부에서 테이블 리스트를 채워줄 수 있게 public 메서드 제공
    public void setTableListData(String[] tableNames) {
        tableList.setListData(tableNames);
    }

    // 📌 외부에서 테이블 내용을 DefaultTableModel로 주입
    public void setTableContent(javax.swing.table.TableModel model) {
        contentTable.setModel(model);
    }
}
