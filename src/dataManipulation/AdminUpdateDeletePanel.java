package dataManipulation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.DBConnection;
import view.MainFrame;

public class AdminUpdateDeletePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private MainFrame frame;
    private JComboBox<String> tableComboBox;

    public AdminUpdateDeletePanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("변경 / 삭제 기능", JLabel.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        try {
            tableComboBox = new JComboBox<>(DBConnection.getAllTables().toArray(new String[0]));
        } catch (SQLException e) {
            e.printStackTrace();
            tableComboBox = new JComboBox<>(new String[]{"에러"});
        }

        JTextField conditionField = new JTextField();
        conditionField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JTextField updateField = new JTextField();
        updateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        centerPanel.add(new JLabel("테이블 선택:"));
        centerPanel.add(tableComboBox);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(new JLabel("WHERE 조건식 입력:"));
        centerPanel.add(conditionField);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(new JLabel("SET 변경 식 (변경 시):"));
        centerPanel.add(updateField);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton updateBtn = new JButton("변경 실행");
        JButton deleteBtn = new JButton("삭제 실행");
        JButton backBtn = new JButton("뒤로가기");

        updateBtn.addActionListener((ActionEvent e) -> {
            int confirm = JOptionPane.showConfirmDialog(this, "정말로 변경하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String table = (String) tableComboBox.getSelectedItem();
                String condition = conditionField.getText().trim();
                String update = updateField.getText().trim();
                try {
                    DBConnection.executeUpdate(table, update, condition);
                    JOptionPane.showMessageDialog(this, "변경 완료");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "변경 실패: " + ex.getMessage());
                }
            }
        });

        deleteBtn.addActionListener((ActionEvent e) -> {
            int confirm = JOptionPane.showConfirmDialog(this, "정말로 삭제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String table = (String) tableComboBox.getSelectedItem();
                String condition = conditionField.getText().trim();
                try {
                    DBConnection.executeDelete(table, condition);
                    JOptionPane.showMessageDialog(this, "삭제 완료");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "삭제 실패: " + ex.getMessage());
                }
            }
        });

        backBtn.addActionListener(e -> frame.setContentPaneAndRefresh(new AdminDataManipulateMenuPanel(frame)));

        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}

