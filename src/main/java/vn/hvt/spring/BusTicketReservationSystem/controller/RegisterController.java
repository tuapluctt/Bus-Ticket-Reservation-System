package vn.hvt.spring.BusTicketReservationSystem.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import vn.hvt.spring.BusTicketReservationSystem.entity.User;
import vn.hvt.spring.BusTicketReservationSystem.DTO.RegisterUser;
import vn.hvt.spring.BusTicketReservationSystem.service.ipml.UserSeviceIpml;

@Controller
@RequestMapping("/register")
public class RegisterController {
    UserSeviceIpml userSeviceIpml;

    @Autowired
    public RegisterController(UserSeviceIpml userSeviceIpml) {
        this.userSeviceIpml = userSeviceIpml;
    }

    @GetMapping(value = "/showRegisterForm")
    public String showRegisterForm(@RequestParam(value = "error", required = false) String error,Model model){
        model.addAttribute("registerUser",new RegisterUser());
        return "public/register";
    }


    @InitBinder
    public void initBinder(WebDataBinder data){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        data.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @PostMapping(value = "/process")
    public String process(@Valid @ModelAttribute("registerUser") RegisterUser registerUser,
                          BindingResult result,
                          Model model
                          ){
        // form validation
        if(result.hasErrors()){
            return "public/register";
        }

        // kiểm tra xem số điện thoại đã tồn tại hay chưa
        User userExisting = userSeviceIpml.findByEmail(registerUser.getEmail());

        if(userExisting!=null){
            model.addAttribute("registerUser",new RegisterUser());
            model.addAttribute("my_error","Email đã được đăng ký");

            return "public/register";
        }

        // nếu số điện thoại chưa được đăng ký thì tạo tài khoản mới và kiểm tra đã đăng ký thành công hay chưa

        if(!userSeviceIpml.save(registerUser)){
            model.addAttribute("registerUser",new RegisterUser());
            model.addAttribute("my_error","đăng ký tài khoản không thành công");
            return "public/register";
        }

        return "public/confirmation";
    }


    @GetMapping("/verify-account")
    public String verifyAccount(@RequestParam String email,
                                @RequestParam String token,
                                Model model  ) {

        String notification = "";
        if(userSeviceIpml.verifyAccount(email, token) ){
            notification="XIN CHÚC MỪNG , TÀI KHOẢN CỦA BẠN ĐÃ ĐƯỢC XÁC MINH.";
        }else{
            notification="RẤT TIẾC, CHÚNG TÔI KHÔNG THỂ XÁC MINH TÀI KHOẢN. VUI LÒNG TẠO LẠI MÃ XÁC THỰC VÀ THỬ LẠI";
            model.addAttribute("email",email);
        }

        model.addAttribute("notification",notification);

        return "public/verifyaccount";
    }

    @GetMapping("/regenerate-otp")
    public String regenerateOtp(@RequestParam String email){
        userSeviceIpml.regenerateOtp(email);
        return "public/confirmation";
    }

}
