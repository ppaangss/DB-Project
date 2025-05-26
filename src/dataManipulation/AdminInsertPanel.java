package dataManipulation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import data.DBConnection;
import view.MainFrame;

public class AdminInsertPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private MainFrame frame;
    private JComboBox<String> tableComboBox;
    private JPanel formPanel;
    private List<JComponent> fieldInputs = new ArrayList<>();
    private List<String> columnNames = new ArrayList<>();
    private Map<String, Map<String, String>> foreignKeyData = new HashMap<>();

    public AdminInsertPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("데이터 입력", JLabel.CENTER);
        titleLabel.setFont(new Font("\uB9D1\uC740 \uACE0\uB515", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());

        try {
            List<String> tables = DBConnection.getAllTables();
            tableComboBox = new JComboBox<>(tables.toArray(new String[0]));
        } catch (SQLException e) {
            e.printStackTrace();
            tableComboBox = new JComboBox<>(new String[]{"에러"});
        }

        tableComboBox.addActionListener(e -> loadTableFields());
        centerPanel.add(tableComboBox, BorderLayout.NORTH);

        formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton insertBtn = new JButton("입력 완료");
        JButton backBtn = new JButton("뒤로가기");

        insertBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, "정말로 입력하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                executeInsert();
            }
        });

        backBtn.addActionListener(e -> frame.setContentPaneAndRefresh(new AdminDataManipulateMenuPanel(frame)));

        buttonPanel.add(insertBtn);
        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        loadTableFields(); // 초기 테이블 로딩
    }

    private void loadTableFields() {
        formPanel.removeAll();
        fieldInputs.clear();
        columnNames.clear();

        String tableName = (String) tableComboBox.getSelectedItem();
        try {
            ResultSetMetaData meta = DBConnection.getMetaData(tableName);
            int columnCount = meta.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                if (meta.isAutoIncrement(i)) continue;

                String colName = meta.getColumnLabel(i);
                columnNames.add(colName);

                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
                JLabel label = new JLabel(colName + ":");
                label.setFont(new Font("\uB9D1\uC740 \uACE0\uB515", Font.PLAIN, 15));
                label.setPreferredSize(new Dimension(120, 25));

                if (colName.endsWith("_id") && !colName.equals("password")) {
                    JComboBox<String> comboBox = new JComboBox<>();
                    comboBox.setPreferredSize(new Dimension(220, 28));
                    Map<String, String> map = DBConnection.getForeignKeyDisplayOptions(colName);
                    foreignKeyData.put(colName, map);
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        comboBox.addItem(entry.getValue());
                    }
                    fieldInputs.add(comboBox);
                    row.add(label);
                    row.add(comboBox);
                } else {
                    JTextField field = new JTextField(20);
                    field.setFont(new Font("\uB9D1\uC740 \uACE0\uB515", Font.PLAIN, 15));
                    field.setPreferredSize(new Dimension(220, 28));
                    fieldInputs.add(field);
                    row.add(label);
                    row.add(field);
                }

                formPanel.add(row);
                formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            formPanel.add(new JLabel("필드 로딩 실패"));
        }

        formPanel.revalidate();
        formPanel.repaint();
    }

    private void executeInsert() {
        String tableName = (String) tableComboBox.getSelectedItem();
        List<String> values = new ArrayList<>();

        for (int i = 0; i < fieldInputs.size(); i++) {
            JComponent comp = fieldInputs.get(i);
            String colName = columnNames.get(i);

            if (comp instanceof JTextField) {
                values.add(((JTextField) comp).getText().trim());
            } else if (comp instanceof JComboBox) {
                String selected = (String) ((JComboBox<?>) comp).getSelectedItem();
                String id = foreignKeyData.get(colName).entrySet().stream()
                        .filter(e -> e.getValue().equals(selected))
                        .map(Map.Entry::getKey)
                        .findFirst().orElse("");
                values.add(id);
            }
        }

        try {
            DBConnection.insertIntoTable(tableName, columnNames, values);
            JOptionPane.showMessageDialog(this, "입력 완료");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "입력 실패: " + e.getMessage());
        }
    }
}
