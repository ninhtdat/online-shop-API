package com.online.shop.config;

import com.online.shop.security.JwtAuthenticationEntryPoint;
import com.online.shop.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Đánh dấu lớp này là một lớp cấu hình Spring
@EnableMethodSecurity // Cho phép bảo mật ở cấp độ phương thức
public class SpringSecurityConfig {

    // Khai báo các thành phần cần thiết cho bảo mật
    private UserDetailsService userDetailsService;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    // Hàm khởi tạo với các thành phần được inject
    public SpringSecurityConfig(
            UserDetailsService userDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint
    ){
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean // Tạo bean cho SecurityFilterChain
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable) // Vô hiệu hóa CSRF
                .authorizeHttpRequests((authorize) -> { // Cấu hình phân quyền
                    authorize.requestMatchers("/api/auth/**").permitAll(); // Cho phép truy cập không cần xác thực
                    authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll(); // Cho phép tất cả các request OPTIONS
                    authorize.anyRequest().authenticated(); // Yêu cầu xác thực cho tất cả các request khác
                }).httpBasic(Customizer.withDefaults()); // Sử dụng xác thực cơ bản

        httpSecurity.exceptionHandling( exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)); // Xử lý ngoại lệ xác thực

        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Thêm filter xác thực JWT

        return httpSecurity.build(); // Xây dựng cấu hình bảo mật
    }

    @Bean // Tạo bean cho AuthenticationManager
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean // Tạo bean cho PasswordEncoder sử dụng BCrypt
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

