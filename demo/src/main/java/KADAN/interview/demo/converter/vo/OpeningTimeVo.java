package KADAN.interview.demo.converter.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
public class OpeningTimeVo {
	private String weekDay;
	private Time startTime;
	private Time endTime;

	public OpeningTimeVo(String weekDay, Time startTime, Time endTime) {
		this.weekDay = weekDay;
		this.startTime = startTime;
		this.endTime = endTime;
	}
}
