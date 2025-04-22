package KADAN.interview.demo.enumType;

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

	public String getLabel() {
		return label;
	}
}