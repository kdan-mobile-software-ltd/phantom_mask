package KADAN.interview.demo.controller;

import KADAN.interview.demo.converter.dto.UserDto;
import KADAN.interview.demo.exception.ErrorResponse;
import KADAN.interview.demo.service.DataLoaderService;
import KADAN.interview.demo.service.UserService;
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
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Tag(name = "User", description = "Operations related to users and their transactions")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
	private final DataLoaderService dataLoaderService;
	private final UserService userService;

	@Operation(
			summary = "Initialize and load pharmacy and user data",
			description = "Clears all existing records and loads new data into the database from the JSON files.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Data initialized successfully",
							content = @Content(schema = @Schema(example = "InitData Success!"))
					),
					@ApiResponse(
							responseCode = "500",
							description = "Failed to initialize data",
							content = @Content(mediaType = "application/json",
									schema = @Schema(implementation = ErrorResponse.class))
					)
			}
	)
	@PostMapping("/loadData")
	public ResponseEntity<Object> loadData() {
		return dataLoaderService.loadData();
	}

	@Operation(
			summary = "Get top users by total transaction amount",
			description = "Returns the top X users who have the highest total transaction amounts within the specified date range.",
			responses = {
					@ApiResponse(responseCode = "200",
							description = "Top users returned successfully",
							content = @Content(mediaType = "application/json",
									array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))),
					@ApiResponse(responseCode = "400",
							description = "Invalid date format or parameters",
							content = @Content(mediaType = "application/json",
									schema = @Schema(implementation = ErrorResponse.class))),
					@ApiResponse(responseCode = "500",
							description = "Internal server error",
							content = @Content(mediaType = "application/json",
									schema = @Schema(implementation = ErrorResponse.class)))
			}
	)
	@GetMapping("/getTopUsers")
	public ResponseEntity<List<UserDto>> getTopUsers(
			@Parameter(description = "Start date (yyyy-MM-dd)")
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,

			@Parameter(description = "End date (yyyy-MM-dd)")
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,

			@Parameter(description = "Number of top users to return")
			@RequestParam(defaultValue = "5") int top
	) {
		if (top <= 0) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(userService.getTopUsers(startDate, endDate, top));
	}
}
