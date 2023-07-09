package ru.endienasg.foxworld.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.endienasg.foxworld.models.User;
import ru.endienasg.foxworld.repositories.IUserRepository;
import ru.endienasg.foxworld.security.jwt.JwtTokenService;

import java.io.IOException;
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static JwtTokenService jwtTokenService = new JwtTokenService();
    @Autowired
    private IUserRepository repository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")){
            String token = header.substring(7);
            if(jwtTokenService.validateToken(token)){
                String username = jwtTokenService.extractClaim(token, claims -> claims.get("sub",String.class));
                log.error(username);
                User user = repository.findByUsername(username);
                if(user != null){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            user,
                            user.getRole(),
                            user.getAuthorities()
                    );
                    SecurityContext ctx = SecurityContextHolder.createEmptyContext();
                    SecurityContextHolder.setContext(ctx);
                    ctx.setAuthentication(authenticationToken);
                    if(SecurityContextHolder.getContext().getAuthentication() == null){
                        log.error("Произошла ошибка в модуле авторизации");
                    }
                }
            }else{
                log.error("Токен истек" + token);
            }
        }
        filterChain.doFilter(request, response);
    }
}
