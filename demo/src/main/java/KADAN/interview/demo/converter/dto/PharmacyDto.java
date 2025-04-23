package KADAN.interview.demo.converter.dto;

import KADAN.interview.demo.converter.vo.MaskVo;
import KADAN.interview.demo.converter.vo.OpeningTimeVo;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Pharmacy's basic information")
public class PharmacyDto {
	@Schema(description = "Unique identifier for the pharmacy")
	private long id;

	@Schema(description = "Pharmacy name", example = "Carepoint")
	private String name;

	@Schema(description = "Current cash balance of the pharmacy", example = "593.35")
	private BigDecimal cashBalance;

	@ArraySchema(
			schema = @Schema(implementation = MaskVo.class),
			arraySchema = @Schema(description = "List of masks sold by the pharmacy")
	)
	private List<MaskVo> masks = new ArrayList<>();

	@ArraySchema(
			schema = @Schema(implementation = OpeningTimeVo.class),
			arraySchema = @Schema(description = "Pharmacy's opening hours by weekday")
	)
	private Set<OpeningTimeVo> openingHours;
}
