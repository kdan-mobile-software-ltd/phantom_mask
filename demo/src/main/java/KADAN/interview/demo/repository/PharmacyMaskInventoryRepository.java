package KADAN.interview.demo.repository;

import KADAN.interview.demo.entity.PharmacyMaskInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PharmacyMaskInventoryRepository extends JpaRepository<PharmacyMaskInventory, Long>, JpaSpecificationExecutor<PharmacyMaskInventory> {
	Optional<PharmacyMaskInventory> findByPharmacyId(Long pharmacyId);

	Optional<PharmacyMaskInventory> findByMaskId(Long maskId);

	Optional<PharmacyMaskInventory> findByPharmacyIdAndMaskName(Long pharmacyId, String name);
}
