package com.online.shop.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Đánh dấu lớp này là một thành phần của Spring
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    // Hàm khởi tạo với các thành phần được inject
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Lấy token từ yêu cầu
        String token = getTokenFromRequest(request);

        // Kiểm tra token có nội dung và hợp lệ không
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)){
            // Lấy username từ token
            String username = jwtTokenProvider.getUsername(token);

            // Lấy thông tin chi tiết người dùng từ username
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Tạo token xác thực mới với thông tin người dùng và quyền hạn
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            // Thiết lập chi tiết xác thực từ yêu cầu
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Thiết lập thông tin xác thực vào SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // Tiếp tục thực hiện các bộ lọc tiếp theo
        filterChain.doFilter(request, response);
    }

    // Phương thức lấy token từ header 'Authorization' của yêu cầu
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Kiểm tra header có chứa token theo định dạng 'Bearer ' không
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            // Trả về phần token sau 'Bearer '
            return bearerToken.substring(7);
        }
        return null;
    }
}

