package KADAN.interview.demo.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Schema(description = "Standard error response")
public class ErrorResponse {
	@Schema(description = "Timestamp of the error", example = "yyyy-MM-ddTHH:mm:ss")
	private LocalDateTime timestamp;

	@Schema(description = "HTTP status code", example = "XXX")
	private int status;

	@Schema(description = "HTTP status reason", example = "Some status")
	private String error;

	@Schema(description = "Error message detail", example = "Some error message")
	private String message;

	public ErrorResponse(HttpStatus status, String message) {
		this.timestamp = LocalDateTime.now();
		this.status = status.value();
		this.error = status.getReasonPhrase();
		this.message = message;
	}
}

