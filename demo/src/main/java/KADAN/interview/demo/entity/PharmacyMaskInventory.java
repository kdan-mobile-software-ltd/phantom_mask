package KADAN.interview.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pharmacy_mask_inventory",
		uniqueConstraints = @UniqueConstraint(columnNames = {"pharmacy_id", "mask_id"}))
public class PharmacyMaskInventory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pharmacy_id", nullable = false)
	private Pharmacy pharmacy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mask_id", nullable = false)
	private Mask mask;

	@Column(nullable = false)
	private Integer quantity;
}
