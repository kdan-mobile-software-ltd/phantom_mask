package KADAN.interview.demo.converter.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Opening hour for a specific day")
public class OpeningTimeVo {

	@Schema(description = "Weekday", example = "Mon")
	private String weekDay;

	@Schema(description = "Opening time", example = "08:00:00")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private LocalTime startTime;

	@Schema(description = "Closing time", example = "12:00:00")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private LocalTime endTime;
}
