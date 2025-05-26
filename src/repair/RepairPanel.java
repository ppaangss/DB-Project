package repair;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;

import repair.dto.RepairRecord;
import repair.dto.RepairRecordExternal;
import view.MainFrame;

public class RepairPanel extends JPanel {
	
    private static final long serialVersionUID = 1L;

    private MainFrame frame;
    RepairPanel self = this;
    //
    private JList<String> camperList;
    
    //
    private JButton prevButton;
    private JButton nextButton;
    //탭
    private JTabbedPane tabbedPane;
    
    
    //??
    private RepairInfoViewPanel internalView;
    private RepairInfoViewPanel externalView;
    
    //캠핑카 조회
    private Map<String, Integer> camperMap = new LinkedHashMap<>();
    
    private int tmp = 0;
    
    //자체 정비 내역 조회
    private List<RepairRecord> internalRecords = new ArrayList<>();
    private int internalIndex = 0;
    
    //외부정비소 정비 내역 조회
    private List<RepairRecordExternal> externalRecords = new ArrayList<>();
    private int externalIndex = 0;

    // 페이지 수 표시용 라벨
    private JLabel internalPageLabel;
    private JLabel externalPageLabel;

    public RepairPanel(MainFrame frame) {
        this.frame = frame;
    	
    	setLayout(new BorderLayout());
        // NORTH: 뒤로가기 버튼, 제목, 정비내역버튼 2개
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        // - 뒤로가기 버튼
        JPanel backPanel = new JPanel(new BorderLayout());
        backPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 25, 5));         
        JButton backButton = new JButton("뒤로가기");
        backButton.addActionListener(e -> frame.showMainMenu());  // 다시 메인 화면으로
        backPanel.add(backButton,BorderLayout.CENTER);         
        headerPanel.add(backPanel,BorderLayout.WEST);
        
     	// - 제목
        JLabel title = new JLabel("캠핑카 정비 내역", JLabel.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        headerPanel.add(title, BorderLayout.CENTER);

        // - 오른쪽 빈 여백, 좌우 균형 맞추기
        JPanel eastSpacer = new JPanel();
        eastSpacer.setPreferredSize(new Dimension(100, 10)); //버튼 크기
        headerPanel.add(eastSpacer, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);

        // WEST: 캠핑카 이름 목록
        camperList = new JList<>();
        camperList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroll = new JScrollPane(camperList);
        listScroll.setPreferredSize(new Dimension(200, 0));
        listScroll.setBorder(BorderFactory.createTitledBorder("캠핑카 목록"));
        add(listScroll, BorderLayout.WEST);
        
        try {
            camperMap = data.DBConnection.getCamperMap();
            setCamperListData(camperMap.keySet().toArray(new String[0]));
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "캠핑카 목록 로딩 실패: " + ex.getMessage());
        }

        // CENTER: 탭으로 구분된 정비 내역
        internalView = new RepairInfoViewPanel();
        externalView = new RepairInfoViewPanel();

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("자체 정비", internalView);
        tabbedPane.addTab("외부 정비", externalView);
        add(tabbedPane, BorderLayout.CENTER);
        
        // SOUTH: 앞뒤 버튼 + 페이지 라벨 (탭에 따라 동적으로 변경)
        JPanel fbButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        prevButton = new JButton("이전");
        nextButton = new JButton("다음");

        internalPageLabel = new JLabel(" ( 0 / 0 ) ");
        externalPageLabel = new JLabel(" ( 0 / 0 ) ");

        fbButtonPanel.add(internalPageLabel);  // 기본은 내부 정비 탭
        fbButtonPanel.add(prevButton);
        fbButtonPanel.add(nextButton);
        add(fbButtonPanel, BorderLayout.SOUTH);

        updateInternalRepairView();
        //이벤트
        //캠핑카 누를 시 이벤트
        camperList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = camperList.getSelectedValue();
                if (selected != null && camperMap.containsKey(selected)) {
                    int carId = camperMap.get(selected);
                    try {
                        internalRecords = data.DBConnection.getInternalRepairs(carId);
                        externalRecords = data.DBConnection.getExternalRepairs(carId);
                        internalIndex = 0;
                        externalIndex = 0;

                        if (tmp == 0) {
                            updateInternalRepairView();
                        } else {
                            updateExternalRepairView();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        //탭키 눌렀을 시 이벤트
        tabbedPane.addChangeListener(e -> {
            tmp = tabbedPane.getSelectedIndex();
            if (fbButtonPanel.getComponentCount() > 2) {
                fbButtonPanel.remove(2);
            }
            
            fbButtonPanel.add(tmp == 0 ? internalPageLabel : externalPageLabel);
            fbButtonPanel.revalidate();
            fbButtonPanel.repaint();

            if (tmp == 0) updateInternalRepairView();
            else updateExternalRepairView();
        });

        //이전 버튼 눌렀을 시 이벤트
        prevButton.addActionListener(e -> {
            if (tmp == 0 && internalIndex > 0) {
                internalIndex--;
                updateInternalRepairView();
            } else if (tmp == 1 && externalIndex > 0) {
                externalIndex--;
                updateExternalRepairView();
            }
        });

        //다음 버튼 눌렀을 시 이벤트
        nextButton.addActionListener(e -> {
            if (tmp == 0 && internalIndex < internalRecords.size() - 1) {
                internalIndex++;
                updateInternalRepairView();
            } else if (tmp == 1 && externalIndex < externalRecords.size() - 1) {
                externalIndex++;
                updateExternalRepairView();
            }
        });
    }

    // 캠핑카 이름 목록 설정용
    public void setCamperListData(String[] camperNames) {
        camperList.setListData(camperNames);
    }

    public JList<String> getCamperList() {
        return camperList;
    }
    
    private void updateInternalRepairView() {
        if (internalRecords.isEmpty()) {
            setInternalRepairInfo("-", "-", "-", "-");
            internalPageLabel.setText(" ( 0 / 0 ) ");
            return;
        }
        RepairRecord r = internalRecords.get(internalIndex);
        setInternalRepairInfo(r.getPartName(), r.getMaintenanceDate(), r.getMaintenanceDuration(), r.getEmployeeId());
        internalPageLabel.setText(" ( " + (internalIndex + 1) + " / " + internalRecords.size() + " ) ");
    }

    private void updateExternalRepairView() {
        if (externalRecords.isEmpty()) {
            setExternalRepairInfo("-", "-", "-", "-", "-", "-", "-", "-");
            externalPageLabel.setText(" ( 0 / 0 ) ");
            return;
        }
        RepairRecordExternal r = externalRecords.get(externalIndex);
        setExternalRepairInfo(
            r.getShopName(), r.getCompanyName(), r.getCustomerName(), r.getDetails(),
            r.getRepairDate(), r.getRepairCost(), r.getDueDate(), r.getExtraDetails()
        );
        externalPageLabel.setText(" ( " + (externalIndex + 1) + " / " + externalRecords.size() + " ) ");
    }

    public void setInternalRepairInfo(String part, String date, String dur, String empId) {
        internalView.removeAll();
        internalView.setLayout(new GridLayout(4, 1, 10, 10));
        internalView.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel partLabel = new JLabel("<HTML><U>부품명: " + part + "</U></HTML>");
        
        if (part.equals("-")) {
            partLabel.setCursor(Cursor.getDefaultCursor());
        }
        else {
        	partLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	frame.setContentPaneAndRefresh(new PartPanel(frame, self, part));
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
                	partLabel.setForeground(Color.BLUE); // hover 시 색 변경
                	partLabel.setText("<html><u>" + partLabel.getText() + "</u></html>"); // 밑줄 추가
                }

                @Override
                public void mouseExited(MouseEvent e) {
                	partLabel.setForeground(Color.BLACK); // 원래대로
                	partLabel.setText(partLabel.getText());
                }
            });
        }
        
        
        internalView.add(partLabel);
        internalView.add(new JLabel("정비일자: " + date));
        internalView.add(new JLabel("정비시간(분): " + dur));
        internalView.add(new JLabel("정비담당자 ID: " + empId));
        internalView.revalidate();
        internalView.repaint();
    }

    public void setExternalRepairInfo(String shopName, String company, String customer, String details, String date, String cost, String due, String extra) {
        externalView.removeAll();
        externalView.setLayout(new GridLayout(4, 2, 10, 10));
        externalView.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel shopLabel = new JLabel("<HTML><U>정비소명: " + shopName + "</U></HTML>");
        
        if (shopName.equals("-")) {
        	shopLabel.setCursor(Cursor.getDefaultCursor());
        }
        else {
        	shopLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	frame.setContentPaneAndRefresh(new ShopPanel(frame, self, shopName));
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
                	shopLabel.setForeground(Color.BLUE); // hover 시 색 변경
                	shopLabel.setText("<html><u>" + shopLabel.getText() + "</u></html>"); // 밑줄 추가
                }

                @Override
                public void mouseExited(MouseEvent e) {
                	shopLabel.setForeground(Color.BLACK); // 원래대로
                	shopLabel.setText(shopLabel.getText());
                }
            });
        }
        
        
        externalView.add(shopLabel);
        externalView.add(new JLabel("캠핑카 회사: " + company));
        externalView.add(new JLabel("고객명: " + customer));
        externalView.add(new JLabel("정비내역: " + details));
        externalView.add(new JLabel("수리날짜: " + date));
        externalView.add(new JLabel("수리비용: " + cost));
        externalView.add(new JLabel("납입기한: " + due));
        externalView.add(new JLabel("기타 정비내역: " + extra));
        externalView.revalidate();
        externalView.repaint();
        
        
    }
}
