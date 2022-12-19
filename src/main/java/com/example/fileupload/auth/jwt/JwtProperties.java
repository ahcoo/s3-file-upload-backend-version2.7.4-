package com.example.fileupload.auth.jwt;

//절대 노출되면 안되는 파일
public interface JwtProperties {

    String SECRET = "MySecretKey"; //원래는 노출되면 안됨.
    int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 14;
    String TOKEN_PREFIX = "Bearer ";

}
