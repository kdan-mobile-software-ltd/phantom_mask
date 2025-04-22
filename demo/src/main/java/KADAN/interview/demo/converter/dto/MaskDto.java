package KADAN.interview.demo.converter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Mask's basic information")
public class MaskDto {
	@Schema(description = "Unique identifier for the mask")
	private long id;

	@Schema(description = "Name of the mask", example = "True Barrier (black) (10 per pack)")
	private String name;

	@Schema(description = "Price of the mask", example = "29.99")
	private BigDecimal price;

	@Schema(description = "Number of masks in one pack", example = "10")
	private int packSize;
}
