package KADAN.interview.demo.converter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@Schema(description = "DTO representing summary of all mask transactions")
public class TransactionSummaryDto {

	@Schema(description = "Total number of transactions", example = "152")
	private Long totalTransactions;

	@Schema(description = "Total amount of all transactions", example = "3048.75")
	private BigDecimal totalAmount;
}
