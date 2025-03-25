package greenjangtanji.yeosuro.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Integer exception = (Integer) request.getAttribute("exception");

        response.setContentType("application/json;charset=UTF-8");

        if (authException instanceof AuthenticationServiceException) {
            // 잘못된 타입의 데이터 처리
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject responseJson = new JSONObject();
            responseJson.put("code", 1001);
            responseJson.put("message", "Invalid data type for credentials.");
            response.getWriter().print(responseJson);
        } else if (exception == null) {
            setResponse(response, ExceptionCode.UNKNOWN_ERROR);
        } else if (exception == 1002) {
            setResponse(response, ExceptionCode.WRONG_TYPE_TOKEN);
        }
        // 토큰 만료된 경우
        else if (exception == 1003) {
            setResponse(response, ExceptionCode.EXPIRED_TOKEN);
        }
        // 지원되지 않는 토큰인 경우
        else if (exception == 1004) {
            setResponse(response, ExceptionCode.UNSUPPORTED_TOKEN);
        } else {
            setResponse(response, ExceptionCode.ACCESS_DENIED);
        }
    }

    // 한글 출력을 위해 getWriter() 사용
    private void setResponse(HttpServletResponse response, ExceptionCode exceptionCode) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("message", exceptionCode.getMessage());
        responseJson.put("code", exceptionCode.getStatus());

        response.getWriter().print(responseJson);
    }
}