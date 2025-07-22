package com.example.blogMs.payload;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String role;
    
    public JwtResponse(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRoles() { return role; }
    public void setRoles(String role) { this.role = role; }
    
}
