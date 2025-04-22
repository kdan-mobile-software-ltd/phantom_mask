package KADAN.interview.demo.controller;

import KADAN.interview.demo.exception.ErrorResponse;
import KADAN.interview.demo.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Search", description = "Full-text search for pharmacies or masks")
@RestController
@RequestMapping("/search")
public class SearchController {

	private final SearchService searchService;

	public SearchController(SearchService searchService) {
		this.searchService = searchService;
	}

	@Operation(
			summary = "Full-text search for pharmacy or mask",
			description = "Searches for pharmacies or masks by keyword using MySQL full-text search. Set `type=true` to search pharmacy, or `type=false` for masks.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Search result returned successfully",
							content = @Content(mediaType = "application/json")),
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
	@PostMapping
	public ResponseEntity<List<?>> search(
			@Parameter(description = "Keyword to search")
			@RequestParam String keyword,

			@Parameter(description = "true = search pharmacy, false = search mask")
			@RequestParam(required = false) Boolean type
	) {
		return ResponseEntity.ok(searchService.searchAll(keyword, type));
	}
}

