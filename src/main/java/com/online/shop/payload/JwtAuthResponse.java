package com.online.shop.payload;

public class JwtAuthResponse {

    private String accessToken; // Token truy cập JWT
    private String tokenType; // Loại token, thường là "Bearer"

    // Hàm khởi tạo với cả accessToken và tokenType
    public JwtAuthResponse(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    // Hàm khởi tạo chỉ với accessToken, mặc định tokenType là "Bearer"
    public JwtAuthResponse(String accessToken) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
    }

    // Getter và Setter cho accessToken và tokenType
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
