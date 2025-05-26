package data;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import repair.dto.RepairRecord;
import repair.dto.RepairRecordExternal;

public class DBConnection {
	
	public static Connection getConnection() {
		 Connection conn = null;
	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver"); // 드라이버 로드
	            conn = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/test", "root", "1234"); // DB 연결
	            System.out.println("DB 연결 완료");
	         
	        } catch (ClassNotFoundException e) {
	            System.out.println("JDBC 드라이버 로드 오류");
	        } catch (SQLException e) {
	            System.out.println("SQL 실행 오류");
	        }
	        
	        return conn;
    }
	
	// 기본 제공 코드
//	public static Connection getConnection(){
//		Connection conn;
//		Statement stmt = null;
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver"); 
//			// MySQL 드라이버 로드
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/madangdb", "root","1234"); 
//			// JDBC 연결, URL, 사용자명, 비밀번호를 기반으로 MySQL 서버에 연결
//			// MySQL의 경우 연결 성공시 com.mysql.cj.jdbc.ConnectionImp 반환
//			// DB서버와의 연결 세션을 의미하고, 이후 SQL 실행 시 이 연결을 기반으로 작동함.
//
//			stmt = conn.createStatement(); 
//			// SQL문 처리용 Statement 객체 생성
//			// SQL 쿼리를 실행할 수 있는 Statement 객체를 생성함.
//			
//			ResultSet srs = stmt.executeQuery("select * from customer;");// 테이블의 모든 데이터 검색
//			//() 안에 있는 쿼리를 DB에 보냄.
//
//
//			
//		} catch (ClassNotFoundException e) {
//			System.out.println("JDBC 드라이버 로드 오류");
//		} catch (SQLException e) {
//			System.out.println("SQL 실행오류");
//		} 
//    }
	
	
	// sql query를 이용한 테이블 조회
	public static DefaultTableModel executeQuery(String query) throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            String[] columnNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = meta.getColumnLabel(i + 1);
            }

            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = rs.getObject(i + 1);
                }
                model.addRow(rowData);
            }

            return model;
        }
    }
	
	// 전체 테이블 이름 조회
    public static List<String> getAllTables() throws SQLException {
        List<String> tables = new ArrayList<>();
        try (Connection conn = getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getTables("test", null, "%", new String[]{"TABLE"})) {
                while (rs.next()) {
                    tables.add(rs.getString("TABLE_NAME"));
                }
            }
        }
        return tables;
    }

    // 특정 테이블 내용 조회
    public static DefaultTableModel getTableContent(String tableName) throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            String[] columnNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = meta.getColumnLabel(i + 1);
            }

            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = rs.getObject(i + 1);
                }
                model.addRow(rowData);
            }

            return model;
        }
    }
    
    //캠핑카 목록 
    public static Map<String, Integer> getCamperMap() throws SQLException {
        Map<String, Integer> camperMap = new LinkedHashMap<>();

        String sql = "SELECT car_id, car_name, car_number FROM campingcar";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("car_id");
                String display = rs.getString("car_name") + " (" + rs.getString("car_number") + ")";
                camperMap.put(display, id);
            }
        }

        return camperMap;
    }


    // 🔧 내부 정비 내역 조회
    public static List<RepairRecord> getInternalRepairs(int carId) throws SQLException {
        List<RepairRecord> records = new ArrayList<>();

        String sql = """
            SELECT p.part_name, m.maintenance_date, m.maintenance_duration, m.employee_id
            FROM maintenancerecord m, part p
            WHERE m.part_id = p.part_id 
            AND m.car_id = ?
            ORDER BY m.maintenance_date DESC
        """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, carId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    records.add(new RepairRecord(
                        rs.getString("part_name"),
                        rs.getString("maintenance_date"),
                        rs.getString("maintenance_duration"),
                        rs.getString("employee_id")
                    ));
                }
            }
        }

        return records;
    }
    //외부정비소 정비 내역 정보
    public static List<RepairRecordExternal> getExternalRepairs(int carId) throws SQLException {
        List<RepairRecordExternal> records = new ArrayList<>();

        String sql = """
            SELECT s.shop_name, c.company_name, cu.name AS customer_name,
                   e.maintenance_details, e.repair_date, e.repair_cost,
                   e.due_date, e.extra_details
            FROM ExternalMaintenance e
            JOIN ExternalRepairShop s ON e.shop_id = s.shop_id
            JOIN CampingCarCompany c ON e.company_id = c.company_id
            JOIN Customer cu ON e.customer_id = cu.customer_id
            WHERE e.car_id = ?
            ORDER BY e.repair_date DESC
        """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, carId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    records.add(new RepairRecordExternal(
                        rs.getString("shop_name"),
                        rs.getString("company_name"),
                        rs.getString("customer_name"),
                        rs.getString("maintenance_details"),
                        rs.getString("repair_date"),
                        rs.getString("repair_cost"),
                        rs.getString("due_date"),
                        rs.getString("extra_details")
                    ));
                }
            }
        }

        return records;
    }
    public static ResultSetMetaData getMetaData(String tableName) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM " + tableName + " LIMIT 1");
        ResultSet rs = pstmt.executeQuery();
        return rs.getMetaData();
    }

    //
    public static void insertIntoTable(String tableName, List<String> columns, List<String> values) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
        sql.append(String.join(", ", columns));
        sql.append(") VALUES (");
        sql.append("?,".repeat(values.size()));
        sql.setLength(sql.length() - 1); // 마지막 쉼표 제거
        sql.append(")");

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < values.size(); i++) {
                pstmt.setString(i + 1, values.get(i)); // 문자열 기준으로 처리
            }
            pstmt.executeUpdate(); // 쿼리 실행
        }
    }

    public static Map<String, String> getForeignKeyDisplayOptions(String fkColumn) throws SQLException {
        Map<String, String> map = new LinkedHashMap<>();
        String table = ""; String idCol = ""; String labelCol = "";

        switch (fkColumn) {
            case "company_id" -> { table = "CampingCarCompany"; idCol = "company_id"; labelCol = "company_name"; }
            case "car_id" -> { table = "CampingCar"; idCol = "car_id"; labelCol = "car_name"; }
            case "customer_id" -> { table = "Customer"; idCol = "customer_id"; labelCol = "name"; }
            case "part_id" -> { table = "Part"; idCol = "part_id"; labelCol = "part_name"; }
            case "employee_id" -> { table = "Employee"; idCol = "employee_id"; labelCol = "name"; }
            case "shop_id" -> { table = "ExternalRepairShop"; idCol = "shop_id"; labelCol = "shop_name"; }
        }

        if (table.isEmpty()) return map;

        String sql = "SELECT " + idCol + ", " + labelCol + " FROM " + table;
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                map.put(String.valueOf(rs.getInt(idCol)), rs.getString(labelCol));
            }
        }

        return map;
    }
    
    public static void executeUpdate(String table, String updateSet, String condition) throws SQLException {
        String sql = "UPDATE " + table + " SET " + updateSet + " WHERE " + condition;
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    public static void executeDelete(String table, String condition) throws SQLException {
        String sql = "DELETE FROM " + table + " WHERE " + condition;
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
    
    public static void runInitScript(String filePath) throws Exception {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // 파일 내용 전체를 문자열로 읽기
            String sql = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filePath)));

            // 세미콜론(;) 기준으로 명령문 분리
            for (String s : sql.split(";")) {
                if (!s.trim().isEmpty()) {
                    stmt.execute(s.trim());
                }
            }
        }
    }




	
}
