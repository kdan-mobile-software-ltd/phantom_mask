package KADAN.interview.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", schema = "kadan")
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(name = "cash_balance", nullable = false, precision = 10, scale = 2)
	private BigDecimal cashBalance;

	@OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TransactionHistory> transactions;
}
