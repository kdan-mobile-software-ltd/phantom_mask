package KADAN.interview.demo.service;

import KADAN.interview.demo.converter.dto.MaskDto;
import KADAN.interview.demo.converter.dto.PharmacyDto;
import KADAN.interview.demo.enumType.CompareType;
import KADAN.interview.demo.enumType.SortDirection;
import KADAN.interview.demo.enumType.SortField;
import KADAN.interview.demo.enumType.WeekDay;

import java.math.BigDecimal;
import java.util.List;

public interface PharmacyService {
	List<PharmacyDto> getOpenPharmacies(WeekDay weekDay, String time);

	List<MaskDto> getMasksByPharmacy(long id, SortField sortBy, SortDirection direction);

	List<PharmacyDto> filterPharmaciesByMask(
			BigDecimal minPrice, BigDecimal maxPrice, int productCount, CompareType compareType);
}
