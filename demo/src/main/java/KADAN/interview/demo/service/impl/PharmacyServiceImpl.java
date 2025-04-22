package KADAN.interview.demo.service.impl;

import KADAN.interview.demo.converter.dto.MaskDto;
import KADAN.interview.demo.converter.dto.PharmacyDto;
import KADAN.interview.demo.converter.vo.MaskVo;
import KADAN.interview.demo.converter.vo.OpeningTimeVo;
import KADAN.interview.demo.entity.*;
import KADAN.interview.demo.enumType.CompareType;
import KADAN.interview.demo.enumType.SortDirection;
import KADAN.interview.demo.enumType.SortField;
import KADAN.interview.demo.enumType.WeekDay;
import KADAN.interview.demo.repository.OpeningTimeRepository;
import KADAN.interview.demo.repository.PharmacyRepository;
import KADAN.interview.demo.service.PharmacyMaskInventoryService;
import KADAN.interview.demo.service.PharmacyService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {
	private final PharmacyRepository pharmacyRepository;
	private final OpeningTimeRepository openingTimeRepository;
	private final PharmacyMaskInventoryService inventoryService;

	@Override
	public List<PharmacyDto> getOpenPharmacies(WeekDay weekDay, String time) {
		final Time queryTime = Time.valueOf(time + ":00");
		return openingTimeRepository
				.findAll(
						((root, criteriaQuery, criteriaBuilder) ->
								criteriaBuilder.and(
										criteriaBuilder.equal(
												root.get(OpeningTime_.weekDay), weekDay.getLabel()
										),
										criteriaBuilder.lessThanOrEqualTo(
												root.get(OpeningTime_.startTime), queryTime
										),
										criteriaBuilder.greaterThanOrEqualTo(
												root.get(OpeningTime_.endTime), queryTime
										)

								)
						)
				)
				.stream()
				.map(openingTime -> {
					PharmacyDto dto = new PharmacyDto();
					dto.setName(openingTime.getPharmacy().getName());
					dto.setCashBalance(openingTime.getPharmacy().getCashBalance());
					return dto;
				})
				.distinct()
				.toList();
	}

	@Override
	public List<MaskDto> getMasksByPharmacy(long id, SortField sortBy, SortDirection direction) {
		Pharmacy pharmacy = pharmacyRepository.findById(id)
				.orElseThrow(
						() -> new EntityNotFoundException("Pharmacy not found with id: " + id));
		return inventoryService.findAll(pharmacy, sortBy, direction)
				.stream()
				.map(
						inventory -> {
							MaskDto dto = new MaskDto();
							dto.setName(inventory.getMask().getName());
							dto.setPrice(inventory.getMask().getPrice());
							dto.setPackSize(inventory.getQuantity());
							return dto;
						}
				)
				.toList();

	}

	@Override
	public List<PharmacyDto> filterPharmaciesByMask(
			BigDecimal minPrice
			, BigDecimal maxPrice
			, int productCount
			, CompareType compareType
	) {
		List<Pharmacy> filteredPharmacies = pharmacyRepository.findAll((root, query, cb) -> {
			query.distinct(true);
			Join<Pharmacy, PharmacyMaskInventory> inventoryJoin = root.join(Pharmacy_.inventories, JoinType.LEFT);

			Predicate quantityPredicate = (compareType == CompareType.GREATER)
					? cb.greaterThanOrEqualTo(inventoryJoin.get(PharmacyMaskInventory_.quantity), productCount)
					: cb.lessThan(inventoryJoin.get(PharmacyMaskInventory_.quantity), productCount);

			Predicate priceMin = cb.greaterThanOrEqualTo(
					inventoryJoin.get(PharmacyMaskInventory_.mask).get(Mask_.price), minPrice
			);
			Predicate priceMax = cb.lessThanOrEqualTo(
					inventoryJoin.get(PharmacyMaskInventory_.mask).get(Mask_.price), maxPrice
			);

			return cb.and(quantityPredicate, priceMin, priceMax);
		});

		return filteredPharmacies
				.stream()
				.map(
						pharmacy -> {
							PharmacyDto dto = new PharmacyDto();
							dto.setName(pharmacy.getName());
							dto.setCashBalance(pharmacy.getCashBalance());

							// 取得符合條件的口罩清單
							List<MaskVo> maskVoList = pharmacy.getInventories()
									.stream()
									.filter(
											inv -> {
												BigDecimal price = inv.getMask().getPrice();
												int quantity = inv.getQuantity();

												boolean priceInRange = price.compareTo(minPrice) >= 0 && price.compareTo(maxPrice) <= 0;
												boolean quantityValid = (compareType == CompareType.GREATER)
														? quantity >= productCount
														: quantity < productCount;

												return priceInRange && quantityValid;
											}
									)
									.map(
											masks -> {
												MaskVo vo = new MaskVo();
												vo.setName(masks.getMask().getName());
												vo.setPrice(masks.getMask().getPrice());
												vo.setPackSize(masks.getQuantity());
												return vo;
											}
									).toList();
							dto.setMasks(maskVoList);

							// 取得營業時間
							List<OpeningTimeVo> openingTimeVos =
									pharmacy.getOpeningTimes()
											.stream()
											.map(
													open -> new OpeningTimeVo(
															open.getWeekDay(),
															open.getStartTime().toLocalTime(),
															open.getEndTime().toLocalTime())
											)
											.toList();
							dto.setOpeningHours(openingTimeVos);

							return dto;
						}
				).toList();
	}

}
