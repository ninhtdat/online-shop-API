package com.online.shop.security;

import com.online.shop.entity.User;
import com.online.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service // Đánh dấu lớp này là một dịch vụ trong Spring
public class CustomUserDetailService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired // Tự động inject `UserRepository`
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Tìm kiếm người dùng dựa trên username hoặc email
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not exists by Username or Email!"));
        // Chuyển đổi các vai trò của người dùng thành `GrantedAuthority`
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        // Trả về một đối tượng `UserDetails` chứa thông tin xác thực
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), // Sử dụng username cho đối tượng `UserDetails`
                user.getPassword(), // Mật khẩu của người dùng
                authorities // Các quyền hạn của người dùng
        );
    }
}

