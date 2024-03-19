package com.online.shop.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component // Đánh dấu lớp này là một thành phần của Spring
public class JwtTokenProvider {

    @Value("${app.jwt-secret}") // Lấy giá trị bí mật JWT từ cấu hình
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}") // Lấy thời gian hết hạn JWT từ cấu hình
    private long jwtExpirationDate;

    // Phương thức tạo token JWT
    public String generateToken(Authentication authentication) {
        String username = authentication.getName(); // Lấy tên người dùng từ đối tượng xác thực

        Date currentDate = new Date(); // Lấy thời gian hiện tại

        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate); // Tính thời gian hết hạn

        // Tạo token JWT
        return Jwts.builder()
                .subject(username) // Đặt chủ đề token là tên người dùng
                .issuedAt(new Date()) // Đặt thời gian phát hành
                .expiration(expireDate) // Đặt thời gian hết hạn
                .signWith(key()) // Ký token với khóa bí mật
                .compact(); // Nén và trả về chuỗi token
    }

    // Phương thức tạo khóa từ chuỗi bí mật JWT
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Phương thức lấy tên người dùng từ token JWT
    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key()) // Xác thực token với khóa bí mật
                .build()
                .parseSignedClaims(token) // Phân tích token
                .getPayload()
                .getSubject(); // Lấy chủ đề token (tên người dùng)
    }

    // Phương thức xác thực token JWT
    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .verifyWith((SecretKey) key()) // Xác thực token với khóa bí mật
                    .build()
                    .parse(token); // Phân tích token
            return true; // Token hợp lệ
        }catch(JwtException e) {
            return false; // Token không hợp lệ
        }
    }
}

