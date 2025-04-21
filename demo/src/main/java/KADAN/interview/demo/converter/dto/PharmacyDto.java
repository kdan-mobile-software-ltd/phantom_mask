package KADAN.interview.demo.converter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Pharmacy's basic information")
public class PharmacyDto {

	@Schema(description = "Pharmacy name", example = "Carepoint")
	private String name;

	@Schema(description = "Current cash balance of the pharmacy", example = "593.35")
	private BigDecimal cashBalance;
}
