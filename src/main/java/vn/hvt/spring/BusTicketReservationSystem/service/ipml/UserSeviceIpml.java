package vn.hvt.spring.BusTicketReservationSystem.service.ipml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.hvt.spring.BusTicketReservationSystem.entity.Role;
import vn.hvt.spring.BusTicketReservationSystem.entity.User;
import vn.hvt.spring.BusTicketReservationSystem.DTO.RegisterUser;
import vn.hvt.spring.BusTicketReservationSystem.repository.RoleRepository;
import vn.hvt.spring.BusTicketReservationSystem.repository.UserRepository;
import vn.hvt.spring.BusTicketReservationSystem.service.UserSevice;
import vn.hvt.spring.BusTicketReservationSystem.util.RandomCode;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserSeviceIpml implements UserSevice {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private EmailSeviceIpml emailSeviceIpml;

    @Autowired
    public UserSeviceIpml(UserRepository userRepository, RoleRepository roleRepository,EmailSeviceIpml emailSeviceIpml) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailSeviceIpml = emailSeviceIpml;
    }

    public UserSeviceIpml() {
    }

    // cài đặt securyti theo số điẹn thoại password
    @Override
    public User findByFullName(String fullName) {
        return userRepository.findByFullName(fullName);
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
       User user = userRepository.findByPhoneNumber(phone);
       if(user==null){
           throw new UsernameNotFoundException("invalid Username or password");
       }
       if(!user.isEnable()){
           throw new UsernameNotFoundException("invalid Username or password");
       }
       return new org.springframework.security.core.userdetails.User(user.getPhoneNumber(), user.getPassWord(), rolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> rolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
    // ----------------------END



    @Override
    public boolean save(RegisterUser registerUser) {
        User user = new User();
        user .setFullName(registerUser.getFullName());
        user.setEmail(registerUser.getEmail());
        user.setPassWord(registerUser.getPassWord());
        user.setPhoneNumber(registerUser.getPhoneNumber());
        user.setOtpGeneratedTime(LocalDateTime.now());
        user.setActivationCode(RandomCode.getSoNgauNhien());

        // xét dèault role USER cho ngưiof dùng
        Role role = roleRepository.findByName("ROLE_USER");
        Collection<Role> roles = new ArrayList<Role>();
        roles.add(role);
        user.setRoles(roles);

        // luư thanh cồn thì gủi email xác nhận tài khoản
        if(userRepository.save(user) != null){
            emailSeviceIpml.sendHtmlEmail(user.getPhoneNumber(),user.getEmail(),user.getActivationCode());
            return true;
        }

        return false;
    }

    @Override
    public boolean verifyAccount(String phoneNumber, String token) {
        User user = userRepository.findByPhoneNumber(phoneNumber);

        // kiểm tra otp khớp hay không . và xem hiệu lục token đã hết hạn hay chưa
        if (user.getActivationCode().equals(token)
                && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (24 * 60 * 60)
                ) {
            user.setEnable(true);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    public void regenerateOtp(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);

        user.setActivationCode(RandomCode.getSoNgauNhien());
        user.setOtpGeneratedTime(LocalDateTime.now());

        emailSeviceIpml.sendHtmlEmail(user.getPhoneNumber(),user.getEmail(),user.getActivationCode());

        userRepository.save(user);
    }

    public boolean isEnable(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if(user != null){
            return user.isEnable();
        }
        return false;
    }
}
