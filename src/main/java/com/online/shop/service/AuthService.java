package com.online.shop.service;

import com.online.shop.payload.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto); // Phương thức đăng nhập cần được cài đặt
}
