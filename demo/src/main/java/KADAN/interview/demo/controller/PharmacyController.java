package KADAN.interview.demo.controller;

import KADAN.interview.demo.converter.dto.MaskDto;
import KADAN.interview.demo.converter.dto.PharmacyDto;
import KADAN.interview.demo.exception.ErrorResponse;
import KADAN.interview.demo.service.PharmacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Pharmacy", description = "Operations for querying pharmacy business hours and mask sales")
@RestController
@RequestMapping("/pharmacy")
@RequiredArgsConstructor
public class PharmacyController {
	private final PharmacyService pharmacyService;

	@Operation(
			summary = "Get pharmacies open at a specific time",
			description = "Returns a list of pharmacies that are open at a specified time and day of the week.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "List returned successfully",
							content = @Content(mediaType = "application/json",
									array = @ArraySchema(schema = @Schema(implementation = PharmacyDto.class)))),
					@ApiResponse(
							responseCode = "400",
							description = "Invalid input",
							content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class))),
					@ApiResponse(
							responseCode = "500",
							description = "Internal server error",
							content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class)))
			}
	)
	@PostMapping("/queryOpeningHour")
	public ResponseEntity<List<PharmacyDto>> getOpenPharmacies(
			@Parameter(description = "Day of the week, e.g., Mon, Tue, Wed...")
			@RequestParam String weekDay,

			@Parameter(description = "Time in HH:mm format, e.g., 08:00 or 20:00")
			@RequestParam String time
	) {
		return ResponseEntity.ok(pharmacyService.getOpenPharmacies(weekDay, time));
	}

	@Operation(
			summary = "List masks sold by a pharmacy with optional sorting",
			description = "Returns a list of masks from a specific pharmacy, optionally sorted by name or price.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "List returned successfully",
							content = @Content(mediaType = "application/json",
									array = @ArraySchema(schema = @Schema(implementation = MaskDto.class)))),
					@ApiResponse(
							responseCode = "400",
							description = "Invalid input",
							content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class))),
					@ApiResponse(
							responseCode = "500",
							description = "Internal server error",
							content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class)))
			}
	)
	@GetMapping("/queryMasks")
	public ResponseEntity<List<MaskDto>> getMasksByPharmacy(
			@Parameter(description = "Pharmacy name")
			@RequestParam String pharmacyName,

			@Parameter(description = "Sort by: true=name, false=price", required = false)
			@RequestParam(required = false) Boolean sortBy,

			@Parameter(description = "Sort direction: true=desc, false=asc", required = false)
			@RequestParam(required = false) Boolean direction
	) {
		return ResponseEntity.ok(pharmacyService.getMasksByPharmacy(pharmacyName, sortBy, direction));
	}

	@Operation(
			summary = "Filter pharmacies based on mask price range and product count",
			description = "Returns a list of pharmacies that offer a certain number of mask products within the specified price range.",
			responses = {
					@ApiResponse(responseCode = "200", description = "List returned successfully",
							content = @Content(mediaType = "application/json",
									array = @ArraySchema(schema = @Schema(implementation = PharmacyDto.class)))),
					@ApiResponse(
							responseCode = "400",
							description = "Invalid input",
							content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class))),
					@ApiResponse(
							responseCode = "500",
							description = "Internal server error",
							content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class)))
			}
	)
	@GetMapping("/filter")
	public ResponseEntity<List<PharmacyDto>> filterPharmacies(
			@Parameter(description = "Minimum mask price")
			@RequestParam BigDecimal minPrice,

			@Parameter(description = "Maximum mask price")
			@RequestParam BigDecimal maxPrice,

			@Parameter(description = "Threshold of number of mask products and must be a positive integer")
			@RequestParam int productCount,

			@Parameter(description = "Type of filter: true=greater than or equal, false=less than")
			@RequestParam Boolean type
	) {
		if (productCount < 0 ){
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(pharmacyService.filterPharmaciesByMask(minPrice, maxPrice, productCount, type));
	}

}
