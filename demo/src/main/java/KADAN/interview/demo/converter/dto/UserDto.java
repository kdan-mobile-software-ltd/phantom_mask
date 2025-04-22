package KADAN.interview.demo.converter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for top user summary by total transaction amount")
public class UserDto {
	@Schema(description = "Unique identifier for the user")
	private long id;

	@Schema(description = "User's name", example = "Alice")
	private String name;

	@Schema(description = "Total transaction amount spent by the user", example = "1200.50")
	private BigDecimal totalAmount;
}
