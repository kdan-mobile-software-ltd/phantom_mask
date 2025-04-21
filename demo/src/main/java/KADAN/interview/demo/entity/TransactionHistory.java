package KADAN.interview.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction_history", schema = "kadan")
public class TransactionHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "pharmacy_id")
	private Pharmacy pharmacy;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "user_id")
	private Users users;

	@Column(name = "mask_name",nullable = false)
	private String maskName;

	@Column(name = "transaction_amount", nullable = false, precision = 10, scale = 2)
	private BigDecimal transactionAmount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "transaction_date", nullable = false)
	private Date transactionDate;

}
