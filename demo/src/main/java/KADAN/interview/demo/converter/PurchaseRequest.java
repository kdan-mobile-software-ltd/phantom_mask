package KADAN.interview.demo.converter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Request body for purchasing a mask")
public class PurchaseRequest {
	@NotNull
	@Schema(description = "User ID", example = "1", required = true)
	private Long userId;

	@NotNull
	@Schema(description = "Pharmacy ID", example = "101", required = true)
	private Long pharmacyId;

	@NotNull
	@Schema(description = "Name of the mask to purchase", example = "True Barrier (black) (10 per pack)", required = true)
	private String maskName;

	@NotNull
	@Positive(message = "The purchase quantity must be greater than zero")
	@Schema(description = "Quantity of the mask to purchase", example = "2", required = true)
	private Integer quantity;
}
