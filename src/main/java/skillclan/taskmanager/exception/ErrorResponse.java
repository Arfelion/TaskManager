package skillclan.taskmanager.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    private final String errorName;
    private final String DetailedErrorMessage;
    private final LocalDateTime timestamp;

    public ErrorResponse(String errorName, String DetailedErrorMessage) {
        this.errorName = errorName;
        this.DetailedErrorMessage = DetailedErrorMessage;
        this.timestamp = LocalDateTime.now();
    }

    public String getErrorName() {
        return errorName;
    }

    public String getDetailedErrorMessage() {
        return DetailedErrorMessage;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
