package skillclan.taskmanager.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    private final String errorName;
    private final String detailedErrorMessage;
    private final LocalDateTime timestamp;

    public ErrorResponse(String errorName, String detailedErrorMessage) {
        this.errorName = errorName;
        this.detailedErrorMessage = detailedErrorMessage;
        this.timestamp = LocalDateTime.now();
    }

    public String getErrorName() {
        return errorName;
    }

    public String getDetailedErrorMessage() {
        return detailedErrorMessage;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
