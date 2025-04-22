package KADAN.interview.demo.controller;

import KADAN.interview.demo.converter.dto.SearchResultDto;
import KADAN.interview.demo.enumType.SearchTarget;
import KADAN.interview.demo.exception.ErrorResponse;
import KADAN.interview.demo.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Search", description = "Full-text search for pharmacies or masks")
@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

	private final SearchService searchService;

	public SearchController(SearchService searchService) {
		this.searchService = searchService;
	}

	@Operation(
			summary = "Full-text search for pharmacy or mask",
			description = "Searches for pharmacies or masks by keyword using MySQL full-text search. Set `type=true` to search pharmacy, or `type=false` for masks.",
			responses = {
					@ApiResponse(responseCode = "200", description = "搜尋結果",
							content = @Content(array = @ArraySchema(
									schema = @Schema(implementation = SearchResultDto.class))))
					,
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
	@GetMapping
	public ResponseEntity<List<Map<String, String>>> search(
			@Parameter(description = "Keyword to search")
			@RequestParam String keyword,

			@Parameter(	description = "Search target: PHARMACY = search pharmacies, MASK = search masks")
			@RequestParam(required = false) SearchTarget type
	) {
		return ResponseEntity.ok(searchService.searchAll(keyword, type));
	}
}

