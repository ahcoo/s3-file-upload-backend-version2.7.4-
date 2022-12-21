package com.example.fileupload.auth.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.fileupload.auth.PrincipalDetails;
import com.example.fileupload.user.dao.UserRepository;
import com.example.fileupload.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


import java.io.IOException;

//Spring security가 가지고 있는 filter 중, BasicAuthenticationFilter라는 것이 있음.
//권한, 인증이 필요한 특정 주소를 요청했을 때 이 필터를 무조건 타게 되어 있음.


public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
////권한 체크하는 필터

    private UserRepository userRepository;


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("권한 필터");

        String jwtHeader = request.getHeader("Authorization");
        System.out.println("JwtHeader : " + jwtHeader);

        if(jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            System.out.println("토큰 없음");
            chain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization").replace(JwtProperties.TOKEN_PREFIX, "");
//        //여기서의 JwtProperties.TOKEN_PREFIX == "Bearer "

        String username = null;

        try {
            username = JWT.require(Algorithm.HMAC256(JwtProperties.SECRET)).build().verify(token).getClaim("username").asString();
            System.out.println(username);
        } catch (TokenExpiredException e) {
            System.out.println("토큰 만료됨");
            ResponseDto responseDto = ResponseDto.builder()
                    .code("TOKEN-0001")
                    .message("token has expired")
                    .build();
            response.setHeader("Content-type", "application/json;charset=UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getOutputStream(), responseDto);
        }

        if (username != null) {
            User user = userRepository.findByUsername(username);
            PrincipalDetails principalDetails = new PrincipalDetails(user);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("principalDetails : " + principalDetails);
            chain.doFilter(request, response);
        }

        super.doFilterInternal(request, response, chain);
    }


}
