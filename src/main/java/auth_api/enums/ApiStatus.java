package auth_api.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ApiStatus {
    SUCCESS,
    ERROR;

    @JsonValue
    public String toLowerCase() {
        return name().toLowerCase();
    }
}