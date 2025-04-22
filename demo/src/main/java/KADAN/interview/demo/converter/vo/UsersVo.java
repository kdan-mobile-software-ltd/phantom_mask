package KADAN.interview.demo.converter.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UsersVo {
	private String name;
	private BigDecimal cashBalance;
	private List<TransactionHistoryVo> histories = new ArrayList<>();

	public UsersVo(JSONObject obj) {
		this.name = obj.getString("name");
		this.cashBalance = obj.getBigDecimal("cashBalance");

		JSONArray historyArray = obj.getJSONArray("purchaseHistories");
		for (int i = 0; i < historyArray.length(); i++) {
			histories.add(new TransactionHistoryVo(historyArray.getJSONObject(i)));
		}
	}
}
