package com.example.Todolist.service.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.Todolist.domain.RestResponse;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponse<Object>> handleIdException(IdInvalidException idException) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(idException.getMessage());
        res.setMessage("IdInvalidException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<RestResponse<Object>> handleJwtValidationException(JwtValidationException ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        res.setError("Token không hợp lệ hoặc đã hết hạn");
        res.setMessage("JwtValidationException");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }

    @ExceptionHandler(BadJwtException.class)
    public ResponseEntity<RestResponse<Object>> handleBadJwtException(BadJwtException ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        res.setError("Token bị sai định dạng hoặc không hợp lệ");
        res.setMessage("BadJwtException");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<RestResponse<Object>> handleJwtException(JwtException ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        res.setError("Lỗi xử lý JWT: " + ex.getMessage());
        res.setMessage("JwtException");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());

        // Custom error message (optional, hoặc bạn có thể để null)
        List<String> errors = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        if (errors.size() > 1) {
            res.setMessage(errors);
        } else {
            res.setMessage(errors.get(0));
        }

        res.setError("Validation failed");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    // Xử lý UsernameNotFoundException (Sai tên đăng nhập)
    @ExceptionHandler(org.springframework.security.core.userdetails.UsernameNotFoundException.class)
    public ResponseEntity<RestResponse<Object>> handleUsernameNotFoundException(
            org.springframework.security.core.userdetails.UsernameNotFoundException ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        res.setError("Tên đăng nhập không tồn tại");
        res.setMessage("UsernameNotFoundException");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }

    // Xử lý BadCredentialsException (Sai mật khẩu)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RestResponse<Object>> handleBadCredentialsException(BadCredentialsException ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        res.setError("Mật khẩu không đúng");
        res.setMessage("BadCredentialsException");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }
}
