package KADAN.interview.demo.converter.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Mask details")
public class MaskVo {
	@Schema(description = "Mask name", example = "Second Smile (black)")
	private String name;

	@Schema(description = "Price of the mask", example = "15.99")
	private BigDecimal price;

	@Schema(description = "Pack size or quantity available", example = "10")
	private int packSize;

	public MaskVo(JSONObject obj) {
		this.name = obj.getString("name");
		this.price = obj.getBigDecimal("price");
	}
}
