package com.team6.backend.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team6.backend.common.exception.EncoreHubException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

import static com.team6.backend.common.exception.ErrorCode.*;

/**
 * 인증된 사용자가 접근 권한이 없는 리소스에 접근하려고 할 때 호출됩니다.
 * 사용자가 로그인은 했으나, 특정 리소스에 접근할 권한이 없는 경우에 403 Forbidden를 반환합니다.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(new EncoreHubException(FORBIDDEN_TO_ACCESS));
        PrintWriter out = response.getWriter();
        out.print(jsonString);
        out.flush();
    }
}
