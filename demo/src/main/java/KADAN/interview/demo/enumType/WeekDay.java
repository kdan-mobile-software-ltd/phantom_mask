package KADAN.interview.demo.enumType;

import lombok.Getter;

@Getter
public enum WeekDay {
	MON("Mon"),
	TUE("Tue"),
	WED("Wed"),
	THU("Thu"),
	FRI("Fri"),
	SAT("Sat"),
	SUN("Sun");

	private final String label;

	WeekDay(String label) {
		this.label = label;
	}

}