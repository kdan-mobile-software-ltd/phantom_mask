package KADAN.interview.demo.converter.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "DTO representing a mask purchase transaction record")
public class TransactionDto {

	@Schema(description = "Transaction ID", example = "1001")
	private Long transactionId;

	@Schema(description = "User's name", example = "Alice")
	private String name;

	@Schema(description = "Pharmacy name", example = "Carepoint")
	private String pharmacyName;

	@Schema(description = "Purchased mask name", example = "True Barrier (black) (10 per pack)")
	private String maskName;

	@Schema(description = "Quantity purchased", example = "2")
	private Integer quantity;

	@Schema(description = "Unit price of the mask", example = "29.99")
	private BigDecimal unitPrice;

	@Schema(description = "Total amount of this transaction", example = "59.98")
	private BigDecimal totalAmount;

	@Schema(
			description = "Transaction date",
			example = "2025-04-21 12:00:00",
			type = "string"
	)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date transactionDate;

	@Schema(description = "User's balance after transaction", example = "500.75")
	private BigDecimal userBalanceAfter;
}
