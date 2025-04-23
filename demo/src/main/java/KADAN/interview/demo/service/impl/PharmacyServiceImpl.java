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
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {
	private final PharmacyRepository pharmacyRepository;
	private final OpeningTimeRepository openingTimeRepository;
	private final PharmacyMaskInventoryService inventoryService;

	@Override
	public List<PharmacyDto> getOpenPharmacies(WeekDay weekDay, String time) {
		final Time queryTime = Time.valueOf(time + ":00");
		List<OpeningTime> openTimes = openingTimeRepository.findOpenPharmacies(weekDay.getLabel(), queryTime);

		return openTimes.stream()
				.map(this::toPharmacyDto)
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
							dto.setId(inventory.getMask().getId());
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
			BigDecimal minPrice, BigDecimal maxPrice,
			int productCount, CompareType compareType
	) {
		List<Pharmacy> filteredPharmacies =
				pharmacyRepository.findAll(
						(root, criteriaQuery, criteriaBuilder) -> {
							// 避免N+1
							root
									.fetch(Pharmacy_.inventories, JoinType.LEFT)
									.fetch(PharmacyMaskInventory_.mask, JoinType.LEFT);
							root.fetch(Pharmacy_.openingTimes, JoinType.LEFT);

							// 避免 JOIN 重複資料
							criteriaQuery.distinct(true);

							Join<Pharmacy, PharmacyMaskInventory> inventoryJoin = root.join(Pharmacy_.inventories, JoinType.LEFT);
							Join<PharmacyMaskInventory, Mask> maskJoin = inventoryJoin.join(PharmacyMaskInventory_.mask);

							Predicate quantityPredicate = (compareType == CompareType.GREATER)
									? criteriaBuilder.greaterThanOrEqualTo(inventoryJoin.get(PharmacyMaskInventory_.quantity), productCount)
									: criteriaBuilder.lessThan(inventoryJoin.get(PharmacyMaskInventory_.quantity), productCount);

							Predicate priceMin = criteriaBuilder.greaterThanOrEqualTo(maskJoin.get(Mask_.price), minPrice);
							Predicate priceMax = criteriaBuilder.lessThanOrEqualTo(maskJoin.get(Mask_.price), maxPrice);

							return criteriaBuilder.and(quantityPredicate, priceMin, priceMax);
						});

		return filteredPharmacies
				.stream()
				.map(
						pharmacy -> toPharmacyDtoWithMaskFilter(
								pharmacy, minPrice, maxPrice, productCount, compareType)
				)
				.toList();
	}

	private PharmacyDto toPharmacyDto(OpeningTime openingTime) {
		Pharmacy pharmacy = openingTime.getPharmacy();

		List<MaskVo> maskVos = pharmacy.getInventories().stream()
				.map(inventory -> {
					MaskVo vo = new MaskVo();
					vo.setName(inventory.getMask().getName());
					vo.setPrice(inventory.getMask().getPrice());
					vo.setPackSize(inventory.getQuantity());
					return vo;
				}).toList();

		Set<OpeningTimeVo> openingTimeVos = pharmacy.getOpeningTimes().stream()
				.map(open -> new OpeningTimeVo(
						open.getWeekDay(),
						open.getStartTime().toLocalTime(),
						open.getEndTime().toLocalTime())
				)
				.collect(Collectors.toSet());

		PharmacyDto dto = new PharmacyDto();
		dto.setId(pharmacy.getId());
		dto.setName(pharmacy.getName());
		dto.setCashBalance(pharmacy.getCashBalance());
		dto.setMasks(maskVos);
		dto.setOpeningHours(openingTimeVos);
		return dto;
	}

	private PharmacyDto toPharmacyDtoWithMaskFilter(
			Pharmacy pharmacy,
			BigDecimal minPrice,
			BigDecimal maxPrice,
			int productCount,
			CompareType compareType
	) {
		List<MaskVo> maskVoList = pharmacy.getInventories().stream()
				.filter(inv -> {
					BigDecimal price = inv.getMask().getPrice();
					int quantity = inv.getQuantity();

					boolean priceInRange = price.compareTo(minPrice) >= 0 && price.compareTo(maxPrice) <= 0;
					boolean quantityValid = (compareType == CompareType.GREATER)
							? quantity >= productCount
							: quantity < productCount;

					return priceInRange && quantityValid;
				})
				.map(inv -> {
					MaskVo vo = new MaskVo();
					vo.setName(inv.getMask().getName());
					vo.setPrice(inv.getMask().getPrice());
					vo.setPackSize(inv.getQuantity());
					return vo;
				})
				.toList();

		Set<OpeningTimeVo> openingTimeVos = pharmacy.getOpeningTimes().stream()
				.map(open -> new OpeningTimeVo(
						open.getWeekDay(),
						open.getStartTime().toLocalTime(),
						open.getEndTime().toLocalTime()
				))
				.collect(Collectors.toSet());

		PharmacyDto dto = new PharmacyDto();
		dto.setId(pharmacy.getId());
		dto.setName(pharmacy.getName());
		dto.setCashBalance(pharmacy.getCashBalance());
		dto.setMasks(maskVoList);
		dto.setOpeningHours(openingTimeVos);
		return dto;
	}

	private List<PharmacyMaskInventory> findAll(Pharmacy pharmacy, SortField sortBy, SortDirection direction) {
		return pharmacyRepository.findAll(
						(root, criteriaQuery, criteriaBuilder) -> {
							// 避免 N+1
							root
									.fetch(Pharmacy_.inventories, JoinType.LEFT)
									.fetch(PharmacyMaskInventory_.mask, JoinType.LEFT);

//					root
//							.join(Pharmacy_.inventories, JoinType.LEFT)
//							.join(PharmacyMaskInventory_.mask, JoinType.LEFT);
							Join<Pharmacy, PharmacyMaskInventory> inventoryJoin = root.join(Pharmacy_.inventories, JoinType.LEFT);
							Join<PharmacyMaskInventory, Mask> maskJoin = inventoryJoin.join(PharmacyMaskInventory_.mask, JoinType.LEFT);

							Path<?> sortField;
							switch (sortBy) {
								case NAME -> sortField = maskJoin.get(Mask_.name);
								case PRICE -> sortField = maskJoin.get(Mask_.price);
								default -> throw new IllegalArgumentException("Unsupported sort field");
							}
							criteriaQuery.distinct(true);
							criteriaQuery.orderBy(
									direction == SortDirection.ASC ? criteriaBuilder.asc(sortField) : criteriaBuilder.desc(sortField)
							);
							return criteriaQuery.getRestriction();
						}
				)
				.stream()
				.flatMap(
						ph -> ph.getInventories().stream()
				)
				.toList();
	}
}
