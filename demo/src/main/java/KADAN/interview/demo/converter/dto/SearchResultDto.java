package KADAN.interview.demo.converter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Search result object containing the type and name of the matched entity")
public class SearchResultDto {
	@Schema(description = "Result type, either PHARMACY or MASK", example = "PHARMACY")
	private String type;

	@Schema(description = "Name of the matched pharmacy or mask", example = "Happy Pharmacy")
	private String name;
}
