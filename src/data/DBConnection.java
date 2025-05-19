package data;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

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
	
}
