package KADAN.interview.demo.controller;

import KADAN.interview.demo.converter.PurchaseRequest;
import KADAN.interview.demo.converter.dto.TransactionDto;
import KADAN.interview.demo.converter.dto.TransactionSummaryDto;
import KADAN.interview.demo.exception.ErrorResponse;
import KADAN.interview.demo.service.MaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/mask")
@RequiredArgsConstructor
@Tag(name = "Mask", description = "Mask purchase and summary operations")
public class MaskController {

	private final MaskService maskService;

	@Operation(
			summary = "Get mask transaction summary",
			description = "Return total transaction amount and quantity for all masks within the specified date range.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Transaction summary returned successfully",
							content = @Content(
									mediaType = "application/json",
									array = @ArraySchema(
											schema = @Schema(
													implementation = TransactionSummaryDto.class
											))
							)),
					@ApiResponse(
							responseCode = "400",
							description = "Invalid date format",
							content = @Content(mediaType = "application/json",
									schema = @Schema(implementation = ErrorResponse.class))),
					@ApiResponse(
							responseCode = "500",
							description = "Internal server error",
							content = @Content(mediaType = "application/json",
									schema = @Schema(implementation = ErrorResponse.class)))
			}
	)
	@GetMapping("/getMaskTransactionSummary")
	public ResponseEntity<TransactionSummaryDto> getMaskTransactionSummary(
			@Parameter(description = "Start date in yyyy-MM-dd format", example = "2021-01-01")
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,

			@Parameter(description = "End date in yyyy-MM-dd format", example = "2021-01-31")
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
	) {
		return ResponseEntity.ok(maskService.getMaskTransactionSummary(startDate, endDate));
	}

	@Operation(
			summary = "Purchase mask",
			description = "User purchases a mask from a specific pharmacy. " +
					"This operation updates balance and records transaction.",
			responses = {
					@ApiResponse(
							responseCode = "200", description = "Mask purchase successful",
							content = @Content(mediaType = "application/json",
									schema = @Schema(implementation = TransactionDto.class))),
					@ApiResponse(
							responseCode = "404",
							description = "User, Pharmacy, or Mask not found",
							content = @Content(mediaType = "application/json",
									schema = @Schema(implementation = ErrorResponse.class))),
					@ApiResponse(
							responseCode = "409",
							description = "Insufficient balance or other conflict",
							content = @Content(mediaType = "application/json",
									schema = @Schema(implementation = ErrorResponse.class))),
					@ApiResponse(
							responseCode = "500",
							description = "Internal server error",
							content = @Content(mediaType = "application/json",
									schema = @Schema(implementation = ErrorResponse.class)))
			}
	)
	@PostMapping("/purchase")
	public ResponseEntity<TransactionDto> purchaseMask(
			@RequestBody @Validated final PurchaseRequest request
	) {
		if (request.getQuantity() < 0) {
			throw new IllegalArgumentException("The purchase quantity must be greater than zero");
		}
		return ResponseEntity.ok(maskService.processPurchase(request));
	}
}

