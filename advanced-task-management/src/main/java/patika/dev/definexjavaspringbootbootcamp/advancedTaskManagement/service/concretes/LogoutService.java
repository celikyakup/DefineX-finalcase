package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final JwtServiceImpl jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token=extractJwtTokenFromRequest(request);
        jwtService.addToBlackList(token);
    }
    private String extractJwtTokenFromRequest(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
