package vn.hvt.spring.BusTicketReservationSystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import vn.hvt.spring.BusTicketReservationSystem.service.UserSevice;

@Configuration
public class SecurityConfiguration {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserSevice userSevice){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userSevice);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public CustomAuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                configurer->configurer
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/sandbox.vnpayment.vn/**").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/icons/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/js/**").permitAll()
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/register/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated()
        ).formLogin(
                form->form.loginPage("/login")
                        .loginProcessingUrl("/authenticateTheUser")
//                        .failureForwardUrl("nn") login failed thì đi tới url này
//                        .successForwardUrl("jj")
                        .failureHandler(authenticationFailureHandler())
                        .successHandler(authenticationSuccessHandler())
                        .permitAll()
        ).logout(
                logout->logout.permitAll()
        ).exceptionHandling(
                configurer->configurer.accessDeniedPage("/showPage403")
        );

        return http.build();
    }


}
