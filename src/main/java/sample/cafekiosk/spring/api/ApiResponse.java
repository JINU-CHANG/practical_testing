package sample.cafekiosk.spring.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;

@Getter
public class ApiResponse<T> {

    private HttpStatus status;

    private int code;

    private String message;

    private T data;

    public ApiResponse(HttpStatus status, String message, T data) {
        this.status = status;
        this.code = status.value();
        this.message = message;
        this.data = data;
    }

    public static<T> ApiResponse<T> of(HttpStatus httpStatus,String message, T data) {
        return new ApiResponse<>(httpStatus, message, data);
    }
    public static<T> ApiResponse<T> of(HttpStatus httpStatus, T data) {
        return of(httpStatus,httpStatus.name(), data);
    }

    public static<T> ApiResponse<T> ok(T data) {
        return of(HttpStatus.OK, HttpStatus.OK.name(), data);
    }
}
