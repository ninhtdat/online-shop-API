package com.online.shop.controller;

import com.online.shop.payload.JwtAuthResponse;
import com.online.shop.payload.LoginDto;
import com.online.shop.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Đánh dấu lớp này là một Rest Controller trong Spring, xử lý các yêu cầu RESTful
@RequestMapping("/api/auth") // Định nghĩa path cơ bản cho tất cả các yêu cầu được xử lý bởi controller này
public class AuthController {
    private AuthService authService; // Khai báo dịch vụ xác thực

    // Hàm khởi tạo với dịch vụ xác thực được inject
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login") // Xử lý yêu cầu POST tới '/login'
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
        // Gọi phương thức đăng nhập từ dịch vụ xác thực và lấy token
        String token = authService.login(loginDto);

        // Tạo đối tượng phản hồi chứa token JWT
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse(token);

        // Trả về phản hồi với trạng thái HTTP OK và đối tượng jwtAuthResponse
        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }

    @GetMapping("/abc") // Xử lý yêu cầu GET tới '/abc'
    public String abc() {
        // Trả về chuỗi "abc" như một phản hồi đơn giản
        return "abc";
    }
}
