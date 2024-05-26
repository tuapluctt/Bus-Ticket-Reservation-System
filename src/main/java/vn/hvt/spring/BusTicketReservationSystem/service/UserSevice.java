package vn.hvt.spring.BusTicketReservationSystem.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import vn.hvt.spring.BusTicketReservationSystem.entity.User;
import vn.hvt.spring.BusTicketReservationSystem.DTO.RegisterUser;

public interface UserSevice extends UserDetailsService {
    public User findByFullName(String fullName);
    public User findByPhoneNumber(String phoneNumber);

    // đăng ký tài khoản
    public boolean save(RegisterUser registerUser);

    // xác thực tài khoản
    public boolean verifyAccount(String phoneNumber, String otp);


}
