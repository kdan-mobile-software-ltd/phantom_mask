package KADAN.interview.demo.service;

import KADAN.interview.demo.converter.dto.MaskDto;
import KADAN.interview.demo.converter.dto.PharmacyDto;

import java.math.BigDecimal;
import java.util.List;

public interface PharmacyService {
	List<PharmacyDto> getOpenPharmacies(String weekDay, String time);

	List<MaskDto> getMasksByPharmacy(String pharmacyName, Boolean sortBy, Boolean direction);

	List<PharmacyDto> filterPharmaciesByMask(
			BigDecimal minPrice, BigDecimal maxPrice, int productCount, Boolean type);

}
