package KADAN.interview.demo.converter.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class MaskVo {
	private String name;
	private BigDecimal price;
	private int packSize;

	public MaskVo(JSONObject obj) {
		this.name = obj.getString("name");
		this.price = obj.getBigDecimal("price");
	}
}
