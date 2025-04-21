package KADAN.interview.demo.converter.vo;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class TransactionHistoryVo {
	private String pharmacyName;
	private String maskName;
	private BigDecimal transactionAmount;
	private Date transactionDate;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public TransactionHistoryVo(JSONObject obj) {
		this.pharmacyName = obj.getString("pharmacyName");
		this.maskName = obj.getString("maskName");
		this.transactionAmount = obj.getBigDecimal("transactionAmount");
		try {
			this.transactionDate = sdf.parse(obj.getString("transactionDate"));
		} catch (ParseException e) {
			throw new RuntimeException("Invalid date format: " + obj.getString("transactionDate"), e);
		}
	}
}
