# Chuỗi Hiệu Thuốc - Quản Trị Cấu Hình Tập Trung (Spring Cloud Config & Git)

Dự án triển khai mô hình kiến trúc **Microservice** với **Spring Cloud Config Server** và **Spring Cloud Config Client** (`pharmacy-service`) theo yêu cầu:
- Tách cấu hình Database và các thông số vận hành của hiệu thuốc ra khỏi mã nguồn ứng dụng.
- Quản lý cấu hình tập trung từ Git Repository (`Microservice-Ss09-Ex01`).
- Dịch vụ `pharmacy-service` tự động kéo cấu hình khi khởi động và nạp các thông tin chi nhánh qua `@Value`.

---

## 1. Cấu trúc thư mục

```text
Ss09-Ex01/
├── config-repo/                               # Thư mục lưu trữ cấu hình tập trung
│   └── pharmacy-service.properties            # Cấu hình DB và thông số chi nhánh hiệu thuốc
├── config-server/                             # Spring Cloud Config Server (Port 8888)
│   ├── src/main/java/.../ConfigServerApplication.java
│   ├── src/main/resources/application.properties
│   └── pom.xml
├── pharmacy-service/                          # Dịch vụ Pharmacy Client (Port 8081)
│   ├── src/main/java/.../PharmacyServiceApplication.java
│   ├── src/main/java/.../controller/PharmacyController.java
│   ├── src/main/java/.../entity/Medicine.java
│   ├── src/main/java/.../repository/MedicineRepository.java
│   ├── src/main/resources/application.properties
│   └── pom.xml
├── pom.xml                                    # Parent POM quản lý multi-module
└── README.md
```

---

## 2. Thông số cấu hình trên Git (`config-repo/pharmacy-service.properties`)

```properties
# Database Configuration for pharmacy-service
spring.datasource.url=jdbc:mysql://localhost:3306/pharmacy_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Pharmacy Branch Operational Parameters
app.branch-name=Nha Thuoc So 1
app.hotline=1900xxxx
app.address=123 Duong Nguyen Hue, Quan 1, TP.HCM
```

---

## 3. Cấu hình Config Server (`config-server/src/main/resources/application.properties`)

```properties
server.port=8888
spring.application.name=config-server

# Cloud Config Git backend configuration
spring.cloud.config.server.git.uri=https://github.com/harry-leon/Microservice-Ss09-Ex01.git
spring.cloud.config.server.git.search-paths=config-repo
spring.cloud.config.server.git.default-label=main
spring.cloud.config.server.git.clone-on-start=true

management.endpoints.web.exposure.include=health,info
```

---

## 4. Hướng dẫn chạy ứng dụng

### Bước 1: Build toàn bộ project
```bash
mvn clean package -DskipTests
```

### Bước 2: Khởi động Config Server (Port 8888)
```bash
cd config-server
mvn spring-boot:run
```
Kiểm tra cấu hình được trả về tại endpoint:
[http://localhost:8888/pharmacy-service/default](http://localhost:8888/pharmacy-service/default)

### Bước 3: Khởi động Pharmacy Service (Port 8081)
```bash
cd pharmacy-service
mvn spring-boot:run
```
Khi khởi động, `pharmacy-service` sẽ in log thông tin chi nhánh lấy từ Config Server:
```text
=========================================================
PHARMACY SERVICE HAS STARTED SUCCESSFULLY
Loaded Branch Name   : Nha Thuoc So 1
Loaded Hotline       : 1900xxxx
Loaded Address       : 123 Duong Nguyen Hue, Quan 1, TP.HCM
Loaded Datasource Url: jdbc:mysql://localhost:3306/pharmacy_db?...
=========================================================
```

### Bước 4: Kiểm tra API
- Xem thông tin chi nhánh:
  ```bash
  curl http://localhost:8081/api/pharmacy/info
  ```
- Lấy danh sách thuốc:
  ```bash
  curl http://localhost:8081/api/pharmacy/medicines
  ```
