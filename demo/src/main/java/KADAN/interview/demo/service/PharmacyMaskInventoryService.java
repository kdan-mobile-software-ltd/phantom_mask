package KADAN.interview.demo.service;

import KADAN.interview.demo.entity.Pharmacy;
import KADAN.interview.demo.entity.PharmacyMaskInventory;
import KADAN.interview.demo.enumType.SortDirection;
import KADAN.interview.demo.enumType.SortField;

import java.util.List;

public interface PharmacyMaskInventoryService {
	PharmacyMaskInventory findByPharmacyId(Long pharmacyId);

	PharmacyMaskInventory findByMaskId(Long pharmacyId);

	List<PharmacyMaskInventory> findAll(Pharmacy pharmacy, SortField sortBy, SortDirection direction);
}
