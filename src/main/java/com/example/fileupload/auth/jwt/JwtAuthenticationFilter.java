package com.example.fileupload.auth.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter라는게 있음.
//login 요청해서 username, password를 post로 요청하면 UsernamePasswordAuthenticationFilter가 동작함.

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {

    }

    //login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 로그인 시도 중");

        // Todo : Jwt 반환
        // 1. UsernamePasswordAuthenticationFilter가 username, password를 받음
        // 2. 로그인 시도함
        // 3. authenticationManager가 로그인 시도를 하는데, 이 때 PrincipalDetailsService가 호출됨.
        // 4. loaduserByusername이 자동으로 실행됨

        return super.attemptAuthentication(request, response);
    }
}
