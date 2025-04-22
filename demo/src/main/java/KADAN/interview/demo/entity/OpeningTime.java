package KADAN.interview.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "opening_time", schema = "kadan")
public class OpeningTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "pharmacy_id")
	private Pharmacy pharmacy;

	@Column(name = "week_day", nullable = false)
	private String weekDay;

	@Temporal(TemporalType.TIME)
	@Column(name = "start_time", nullable = false)
	private Time startTime;

	@Temporal(TemporalType.TIME)
	@Column(name = "end_time", nullable = false)
	private Time endTime;
}
