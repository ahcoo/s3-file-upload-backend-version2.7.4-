package com.example.fileupload.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.fileupload.auth.PrincipalDetails;
import com.example.fileupload.user.domain.User;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter라는게 있음.
//login 요청해서 username, password를 post로 요청하면 UsernamePasswordAuthenticationFilter가 동작함.

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;

        //login url 지정
        setFilterProcessesUrl("/api/vi/login");
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

        try {
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)  throws IOException, ServletException {
        System.out.println("인증완료됨");
        System.out.println(authResult.getName());

        //토큰만들기
        String jwtToken = JWT.create()
                .withSubject("login token")
                        .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                                .withClaim("username", authResult.getName())
                                        .sign(Algorithm.HMAC256(JwtProperties.SECRET));
        response.addHeader("Authorization", JwtProperties.TOKEN_PREFIX + jwtToken);
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
