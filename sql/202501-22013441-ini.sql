# 임시 작성 테이블
# 정규화 및 테이블 구조 등 다시 고민해보기.
DROP DATABASE IF EXISTS test;
CREATE DATABASE test;
use test;

-- select * from CampingCarCompany;
-- select * from campingcar;
-- select * from part;
-- select * from Employee;
-- select * from MaintenanceRecord;
-- select * from customer;
-- select * from usagehistory;
-- select * from rental;
-- select * from ExternalRepairShop;
-- select * from ExternalMaintenance;

# 1.캠핑카 대여 회사
CREATE TABLE CampingCarCompany (
    company_id INT AUTO_INCREMENT PRIMARY KEY, 
    company_name VARCHAR(100) NOT NULL UNIQUE, 
    address VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    manager_name VARCHAR(50) NOT NULL,
    manager_email VARCHAR(100) NOT NULL UNIQUE
);
# 캠핑회사에 대한 담당자와 이메일은 이행 종속을 만족하지 않음. 즉, 동명이인이 존재할 수 있으며 해당 이름에 대한 이메일이 서로 다를 수 있음.
# 캠핑 회사명 중복 x, 전화번호 중복 x, 이메일 중복 x

INSERT INTO CampingCarCompany (company_name, address, phone_number, manager_name, manager_email)
VALUES 
('캠핑로드', '서울특별시 강남구 테헤란로 123', '010-1234-5678', '홍길동', 'hong@campingroad.com'),
('오토캠프', '부산광역시 해운대구 센텀로 77', '010-2345-6789', '김영희', 'kim@autocamp.co.kr'),
('드라이브앤고', '대전광역시 유성구 대학로 1', '010-3456-7890', '이철수', 'lee@driveandgo.com'),
('캠프홀릭', '광주광역시 북구 운암로 88', '010-4567-8901', '박민수', 'park@campholic.com'),
('바이크앤캠핑', '대구광역시 수성구 동대구로 101', '010-5678-9012', '최지훈', 'choi@bikecamp.kr');

# 2.캠핑카
CREATE TABLE CampingCar (
    car_id INT AUTO_INCREMENT PRIMARY KEY,
    car_name VARCHAR(100) NOT NULL,
    car_number VARCHAR(20) NOT NULL UNIQUE,
    seat_capacity INT NOT NULL CHECK (seat_capacity > 0),
    image_url VARCHAR(255),
    description TEXT,
    rental_fee DECIMAL(10,2) NOT NULL CHECK (rental_fee >= 0),
    company_id INT NOT NULL,
    registration_date DATE NOT NULL,
    FOREIGN KEY (company_id) REFERENCES CampingCarCompany(company_id)
);

INSERT INTO CampingCar (car_name, car_number, seat_capacity, image_url, description, rental_fee, company_id, registration_date)
VALUES 
('캠핑카 A', '12가3456', 4, 'https://img.example.com/a.jpg', '가족 여행에 적합한 캠핑카', 120000, 1, '2024-04-10'),
('캠핑카 B', '34나5678', 2, 'https://img.example.com/b.jpg', '2인 커플용 캠핑카', 90000, 2, '2024-05-01'),
('캠핑카 C', '56다7890', 6, NULL, '대형 가족 전용 캠핑카', 150000, 1, '2024-03-15'),
('캠핑카 D', '78라0123', 3, 'https://img.example.com/d.jpg', '컴팩트 사이즈에 최적화', 100000, 3, '2024-06-01'),
('캠핑카 E', '90마4567', 5, 'https://img.example.com/e.jpg', '중형 사이즈, 넓은 공간', 130000, 2, '2024-04-20');

# 3.부품 
CREATE TABLE Part (
    part_id INT AUTO_INCREMENT PRIMARY KEY,
    part_name VARCHAR(100) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL CHECK (unit_price >= 0),
    quantity INT NOT NULL CHECK (quantity >= 0),
    arrival_date DATE NOT NULL,
    supplier_name VARCHAR(100) NOT NULL
);

INSERT INTO Part (part_name, unit_price, quantity, arrival_date, supplier_name)
VALUES 
('타이어', 120000, 10, '2024-04-01', '한국타이어'),
('오일필터', 15000, 50, '2024-03-28', '모비스'),
('브레이크패드', 40000, 20, '2024-04-10', 'SBS 브레이크'),
('와이퍼', 8000, 100, '2024-05-01', '불스원'),
('전구세트', 12000, 30, '2024-04-18', '필립스');

# 4.직원
CREATE TABLE Employee (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    address VARCHAR(255) NOT NULL,
    salary DECIMAL(10,2) NOT NULL CHECK (salary >= 0),
    dependents INT NOT NULL CHECK (dependents >= 0),
    department VARCHAR(50) NOT NULL,
    role VARCHAR(10) NOT NULL CHECK (role IN ('관리', '사무', '정비'))
);

INSERT INTO Employee (name, phone_number, address, salary, dependents, department, role)
VALUES 
('김철수', '010-1111-2222', '서울시 강남구 역삼동', 3200000, 2, '정비부', '정비'),
('이영희', '010-3333-4444', '부산시 해운대구 우동', 2800000, 0, '예약부', '사무'),
('박지훈', '010-5555-6666', '대구시 수성구 범어동', 3500000, 1, '관리부', '관리'),
('최수정', '010-7777-8888', '광주시 서구 치평동', 3000000, 3, '정비부', '정비'),
('정다은', '010-9999-0000', '인천시 남동구 구월동', 2900000, 1, '예약부', '사무');

# 5.자체 정비 기록
CREATE TABLE MaintenanceRecord (
    record_id INT AUTO_INCREMENT PRIMARY KEY,
    car_id INT NOT NULL,
    part_id INT NOT NULL,
    maintenance_date DATE NOT NULL,
    maintenance_duration INT NOT NULL CHECK (maintenance_duration > 0),
    employee_id INT NOT NULL,
    FOREIGN KEY (car_id) REFERENCES CampingCar(car_id),
    FOREIGN KEY (part_id) REFERENCES Part(part_id),
    FOREIGN KEY (employee_id) REFERENCES Employee(employee_id)
);

INSERT INTO MaintenanceRecord (car_id, part_id, maintenance_date, maintenance_duration, employee_id)
VALUES 
(1, 1, '2024-04-01', 60, 1),
(2, 2, '2024-04-03', 30, 4),
(1, 3, '2024-04-05', 45, 1),
(3, 1, '2024-04-07', 50, 4),
(4, 4, '2024-04-09', 40, 1);

# 6.고객
# a.고객
CREATE TABLE Customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    license_number VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE
);

# b.사용이력
CREATE TABLE UsageHistory (
    usage_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    usage_date DATE NOT NULL,
    car_id INT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
    FOREIGN KEY (car_id) REFERENCES CampingCar(car_id)
);
# 사용이력과 캠핑카 종류 정보는 이행 종속을 만족하므로, 3정규화 해줌.


INSERT INTO Customer (username, password, license_number, name, address, phone_number, email)
VALUES 
('user01', 'pass01', 'DL123456', '홍길동', '서울시 강남구', '010-1234-5678', 'hong01@example.com'),
('user02', 'pass02', 'DL223456', '김영희', '부산시 해운대구', '010-2345-6789', 'kim02@example.com'),
('user03', 'pass03', 'DL323456', '이철수', '대전시 유성구', '010-3456-7890', 'lee03@example.com'),
('user04', 'pass04', 'DL423456', '박민수', '광주시 북구', '010-4567-8901', 'park04@example.com'),
('user05', 'pass05', 'DL523456', '최지훈', '대구시 수성구', '010-5678-9012', 'choi05@example.com');

INSERT INTO UsageHistory (customer_id, usage_date, car_id)
VALUES
(1, '2024-04-10', 1),
(1, '2024-04-20', 3),
(2, '2024-04-12', 2),
(3, '2024-04-18', 5),
(4, '2024-04-22', 4);

# 7.캠핑카 대여
CREATE TABLE Rental (
    rental_id INT AUTO_INCREMENT PRIMARY KEY,
    car_id INT NOT NULL,
    customer_id INT NOT NULL,
    company_id INT NOT NULL,
    rental_start_date DATE NOT NULL,
    rental_period INT NOT NULL CHECK (rental_period > 0),
    total_fee DECIMAL(10,2) NOT NULL CHECK (total_fee >= 0),
    due_date DATE NOT NULL,
    extra_details VARCHAR(255),
    extra_fee DECIMAL(10,2) DEFAULT 0 CHECK (extra_fee >= 0),
    FOREIGN KEY (car_id) REFERENCES CampingCar(car_id),
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
    FOREIGN KEY (company_id) REFERENCES CampingCarCompany(company_id)
);

INSERT INTO Rental (car_id, customer_id, company_id, rental_start_date, rental_period, total_fee, due_date, extra_details, extra_fee)
VALUES
(1, 1, 1, '2024-05-01', 3, 360000, '2024-04-30', NULL, 0),
(2, 2, 2, '2024-05-05', 2, 180000, '2024-05-04', '침구류 추가', 20000),
(3, 3, 1, '2024-05-10', 1, 150000, '2024-05-09', NULL, 0),
(4, 4, 3, '2024-05-15', 5, 500000, '2024-05-14', '애완동물 동반', 30000),
(5, 5, 2, '2024-05-20', 4, 400000, '2024-05-19', NULL, 0);

# 8. 외부 정비소
CREATE TABLE ExternalRepairShop (
    shop_id INT AUTO_INCREMENT PRIMARY KEY,
    shop_name VARCHAR(100) NOT NULL UNIQUE,
    address VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    manager_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO ExternalRepairShop (shop_name, address, phone_number, manager_name, email)
VALUES
('정비월드', '서울시 성동구 성수동', '02-1234-5678', '김정비', 'fix@repairworld.co.kr'),
('오토닥터', '부산시 동래구 온천동', '051-234-5678', '이정비', 'lee@autodoc.com'),
('스피드정비', '대구시 달서구 월성동', '053-345-6789', '박정비', 'speed@fastfix.kr'),
('캠핑카케어', '광주시 남구 봉선동', '062-456-7890', '최정비', 'care@camperfix.net'),
('수리마스터', '대전시 중구 오류동', '042-567-8901', '정정비', 'master@repairpro.org');

# 9. 외부 정비 기록
CREATE TABLE ExternalMaintenance (
    maintenance_id INT AUTO_INCREMENT PRIMARY KEY,
    car_id INT NOT NULL,
    shop_id INT NOT NULL,
    company_id INT NOT NULL,
    customer_id INT NOT NULL,
    maintenance_details VARCHAR(255) NOT NULL,
    repair_date DATE NOT NULL,
    repair_cost DECIMAL(10,2) NOT NULL CHECK (repair_cost >= 0),
    due_date DATE NOT NULL,
    extra_details VARCHAR(255),
    FOREIGN KEY (car_id) REFERENCES CampingCar(car_id),
    FOREIGN KEY (shop_id) REFERENCES ExternalRepairShop(shop_id),
    FOREIGN KEY (company_id) REFERENCES CampingCarCompany(company_id),
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)
);

INSERT INTO ExternalMaintenance (car_id, shop_id, company_id, customer_id, maintenance_details, repair_date, repair_cost, due_date, extra_details)
VALUES
(1, 1, 1, 1, '엔진 점검 및 오일 교체', '2024-04-01', 250000, '2024-04-10', '엔진청소 포함'),
(2, 2, 2, 2, '타이어 교체', '2024-04-03', 120000, '2024-04-13', NULL),
(3, 3, 1, 3, '브레이크 패드 교체', '2024-04-05', 180000, '2024-04-15', NULL),
(4, 4, 3, 4, '전기 시스템 점검', '2024-04-07', 220000, '2024-04-17', '배터리 상태 점검 포함'),
(5, 5, 2, 5, '에어컨 필터 교체', '2024-04-09', 90000, '2024-04-19', NULL);



