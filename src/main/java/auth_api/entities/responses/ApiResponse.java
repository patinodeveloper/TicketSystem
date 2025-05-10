package auth_api.entities.responses;

import auth_api.enums.ApiStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private ApiStatus status;
    private String message;
    private T data;

    public ApiResponse(ApiStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(ApiStatus.SUCCESS, message, data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ApiStatus.SUCCESS, "Operación exitosa", data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(ApiStatus.ERROR, message, null);
    }
}