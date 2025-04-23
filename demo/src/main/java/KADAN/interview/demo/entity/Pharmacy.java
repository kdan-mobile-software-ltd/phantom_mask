package KADAN.interview.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pharmacy", schema = "kadan")
public class Pharmacy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(name = "cash_balance", nullable = false, precision = 10, scale = 2)
	private BigDecimal cashBalance;

	@OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PharmacyMaskInventory> inventories = new ArrayList<>();

	@OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OpeningTime> openingTimes;

	@OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TransactionHistory> transactions;
}
