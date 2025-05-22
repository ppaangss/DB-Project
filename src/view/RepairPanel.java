package view;

import javax.swing.*;
import java.awt.*;

public class RepairPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JList<String> camperList;
    private JTable repairTable;
    private JButton internalBtn;
    private JButton externalBtn;

    public RepairPanel(MainFrame frame) {
        setLayout(new BorderLayout());

        // 🔼 NORTH: 제목 + 오른쪽 버튼 2개
        JPanel headerPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("캠핑카 정비 내역", JLabel.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        internalBtn = new JButton("자체 정비 내역");
        externalBtn = new JButton("외부 정비 내역");
        buttonPanel.add(internalBtn);
        buttonPanel.add(externalBtn);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // ◀ WEST: 캠핑카 이름 목록
        camperList = new JList<>();
        camperList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroll = new JScrollPane(camperList);
        listScroll.setPreferredSize(new Dimension(200, 0));
        listScroll.setBorder(BorderFactory.createTitledBorder("캠핑카 목록"));
        add(listScroll, BorderLayout.WEST);

        // ▶ CENTER: 정비 내역 테이블
        repairTable = new JTable();
        JScrollPane tableScroll = new JScrollPane(repairTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("정비 내역"));
        add(tableScroll, BorderLayout.CENTER);

        // ❗TODO: 이후에 버튼 클릭 시 필터링 로직 / camper 선택 시 데이터 로딩 연결 예정
    }

    // 캠핑카 이름 목록 설정용
    public void setCamperListData(String[] camperNames) {
        camperList.setListData(camperNames);
    }

    // 테이블 모델 갱신용
    public void setRepairTableModel(javax.swing.table.TableModel model) {
        repairTable.setModel(model);
    }

    // 버튼 접근자 제공
    public JButton getInternalButton() {
        return internalBtn;
    }

    public JButton getExternalButton() {
        return externalBtn;
    }

    public JList<String> getCamperList() {
        return camperList;
    }
}
