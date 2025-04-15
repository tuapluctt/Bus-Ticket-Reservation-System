# 🚍 Hệ Thống Đặt Vé Xe Liên Tỉnh

Website hỗ trợ người dùng tra cứu, đặt vé xe khách và quản lý thông tin chuyến đi một cách dễ dàng và tiện lợi.

---

## ✨ Chức năng chính

### 🧑‍💼 Khách vãng lai

- Đăng ký tài khoản, xác thực email
- Tìm kiếm chuyến xe
- Xem thông tin chi tiết chuyến xe
- Xem các trang giới thiệu tuyến xe
- Đặt vé và thanh toán online
- Nhận thông tin vé qua email

### 👤 Thành viên

- Đăng nhập
- Quản lý thông tin cá nhân
- Xem lịch sử đặt vé
- Đánh giá chuyến xe đã đi

### 🛠️ Admin

- Quản lý lịch trình chuyến xe
- Quản lý thông tin vé

### 🧑‍✈️ Nhân viên tài xế

- Xem lịch làm việc
- Check-in vé của khách thông qua QR code khi lên xe

---

## 💻 Công nghệ sử dụng

- Java Spring Boot
- MySQL / MariaDB
- Thymeleaf (Giao diện)
- Docker
- AWS EC2
- Bootstrap, HTML, CSS, JS

---

## 🐳 Triển khai hệ thống

Hệ thống được đóng gói và triển khai bằng Docker:

- Build ứng dụng từ `Dockerfile`
- Deploy trên EC2 (AWS) thông qua Docker container
- MySQL cũng được chạy bằng Docker để hỗ trợ việc kết nối nội bộ
- Ứng dụng chạy trên port `8080`, MySQL trên `3306`

---

## 📷 Một số hình ảnh (gợi ý - thêm sau nếu có)

> Bạn có thể chèn thêm ảnh minh họa như:
> - Giao diện tìm kiếm chuyến xe
> - Trang thanh toán
> - Mã QR vé
> - Giao diện quản trị của Admin

---

## 📦 Cấu trúc thư mục (gợi ý)
