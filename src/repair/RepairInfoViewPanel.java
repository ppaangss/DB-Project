package repair;

import javax.swing.*;
import java.awt.*;

public class RepairInfoViewPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JLabel partNameLabel;
    private JLabel dateLabel;
    private JLabel durationLabel;
    private JLabel employeeLabel;

    public RepairInfoViewPanel() {
        setLayout(new GridLayout(4, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        partNameLabel = new JLabel("<HTML><U>부품명: -</U></HTML>");
        partNameLabel.setForeground(Color.BLUE);
        partNameLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        dateLabel = new JLabel("정비일자: -");
        durationLabel = new JLabel("정비시간(분): -");
        employeeLabel = new JLabel("정비담당자 ID: -");

        Font f = new Font("맑은 고딕", Font.PLAIN, 16);
        partNameLabel.setFont(f);
        dateLabel.setFont(f);
        durationLabel.setFont(f);
        employeeLabel.setFont(f);

        add(partNameLabel);
        add(dateLabel);
        add(durationLabel);
        add(employeeLabel);

//        // 클릭 시 부품 상세보기
//        partNameLabel.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent e) {
//                JOptionPane.showMessageDialog(RepairInfoViewPanel.this, "부품 상세 정보는 추후 구현 예정");
//            }
//        });
    }

    public void setRepairInfo(String partName, String date, String duration, String employeeId) {
        partNameLabel.setText("<HTML><U>부품명: " + partName + "</U></HTML>");
        dateLabel.setText("정비일자: " + date);
        durationLabel.setText("정비시간(분): " + duration);
        employeeLabel.setText("정비담당자 ID: " + employeeId);
    }
}
