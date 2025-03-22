package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes.JwtServiceImpl;

@Component
@RequiredArgsConstructor
public class JwtBlackListInterceptor implements HandlerInterceptor {

    private final JwtServiceImpl jwtServiceImpl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jwtToken = extractJwtTokenFromRequest(request);
        if (jwtToken != null && jwtServiceImpl.isBlackListed(jwtToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    private String extractJwtTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}

