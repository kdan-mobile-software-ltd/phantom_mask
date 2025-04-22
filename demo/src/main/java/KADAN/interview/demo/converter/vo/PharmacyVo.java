package KADAN.interview.demo.converter.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class PharmacyVo {
	private String name;
	private BigDecimal cashBalance;
	private List<MaskVo> masks = new ArrayList<>();
	private List<OpeningTimeVo> openingHours = new ArrayList<>();
	private static final List<String> WEEK_ORDER = Arrays.asList(
			"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
	);

	public PharmacyVo(JSONObject obj) {
		this.name = obj.getString("name");
		this.cashBalance = obj.getBigDecimal("cashBalance");

		JSONArray masks = obj.getJSONArray("masks");
		masks.forEach((e) -> {
			JSONObject object = new JSONObject(e.toString());
			MaskVo maskVo = new MaskVo(object);
			this.masks.add(maskVo);
		});

		String openingRaw = obj.getString("openingHours");
		parseOpeningHours(openingRaw);
	}

	/**
	 * 將原始 openingHours 字串轉為 OpeningHourVo 清單
	 * 支援格式：
	 * Mon - Fri 08:00 - 17:00 / Sat, Sun 08:00 - 12:00
	 * Mon, Wed, Fri 10:00 - 15:00
	 */
	private void parseOpeningHours(String raw) {
		String[] segments = raw.split("/");

		for (String segment : segments) {
			segment = segment.trim();

			Pattern timePattern = Pattern.compile("(\\d{2}:\\d{2})\\s*-\\s*(\\d{2}:\\d{2})");
			Matcher timeMatcher = timePattern.matcher(segment);
			String startTime = "", endTime = "";

			if (timeMatcher.find()) {
				startTime = timeMatcher.group(1) + ":00";
				endTime = timeMatcher.group(2) + ":00";
			} else {
				System.err.println("❌ 無法擷取時間: " + segment);
				continue;
			}
			String daysPart = segment.substring(0, timeMatcher.start()).trim();
			List<String> expandedDays = extractDays(daysPart);

			for (String day : expandedDays) {
				openingHours.add(new OpeningTimeVo(day, LocalTime.parse(startTime), LocalTime.parse(endTime)));
			}
		}
	}

	/**
	 * 將日期部分字串轉為星期清單
	 * 例如 "Mon - Wed, Fri" → ["Mon", "Tue", "Wed", "Fri"]
	 */
	private List<String> extractDays(String daysPart) {
		List<String> result = new ArrayList<>();
		String[] tokens = daysPart.split(",");

		for (String token : tokens) {
			token = token.trim();
			if (token.contains("-")) {
				String[] range = token.split("-");
				result.addAll(expandWeekdays(range[0].trim(), range[1].trim()));
			} else {
				result.add(token);
			}
		}
		return result;
	}

	/**
	 * 擴展星期範圍，例如 Mon - Fri → Mon, Tue, Wed, Thu, Fri
	 */
	private List<String> expandWeekdays(String start, String end) {
		int startIndex = WEEK_ORDER.indexOf(normalizeDay(start));
		int endIndex = WEEK_ORDER.indexOf(normalizeDay(end));
		List<String> result = new ArrayList<>();
		if (startIndex != -1 && endIndex != -1 && startIndex <= endIndex) {
			for (int i = startIndex; i <= endIndex; i++) {
				result.add(WEEK_ORDER.get(i));
			}
		}
		return result;
	}

	/**
	 * 對於 "Thur" → "Thu" 做標準化處理
	 */
	private String normalizeDay(String day) {
		return day.equalsIgnoreCase("Thur") ? "Thu" : day;
	}
}
