import data.DBConnection;
import view.MainFrame;

public class Main {

	public static void main(String[] args) {
		//DBConnection.runSampleQuery();
		new MainFrame();
	}
}

/* 

프로젝트 구조

- view : 사용자 인터페이스

JFrame은 **보통 1개 (MainFrame)**으로 두고,
안에서 JPanel을 갈아끼우는 방식이 일반적.

기능별로 Panel을 나누기.

- controller : UI 이벤트 처리 및 흐름 제어

ex)
사용자가 잘못된 입력을 넣음	유효성 검사 (예: 이름이 공백이면 에러 띄우기)
사용자 입력값 조정	문자열 다듬기, 날짜 형식 바꾸기 등
여러 DAO 호출	고객 추가 + 포인트 테이블 갱신 같이 연쇄 작업
결과 가공	DAO에서 받아온 데이터를 다시 정리해서 전달
예외 처리	오류 발생 시 사용자에게 메시지 전달

- model : 애플리케이션의 "데이터와 그 처리 로직"을 담당

구성요소
DTO (Data Transfer Object) : 데이터를 객체 형태로 담는 그릇	
DAO (Data Access Object) : DB에 SQL 날리는 역할	
DBConnection : DB 연결 전담 유틸	(DBConnection.java)


*/


/*

단축키

ctrl + shift + /
블럭 주석
ctrl + shift + \
블럭 주석 해제

ctrl + / 
한줄 주석 및 해제

ctrl + shift + o
자동 import 설정

ctrl + 1
목록 띄우기

*/