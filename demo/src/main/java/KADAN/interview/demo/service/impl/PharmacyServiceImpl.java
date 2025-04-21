package KADAN.interview.demo.service.impl;

import KADAN.interview.demo.converter.dto.MaskDto;
import KADAN.interview.demo.converter.dto.PharmacyDto;
import KADAN.interview.demo.entity.*;
import KADAN.interview.demo.repository.MaskRepository;
import KADAN.interview.demo.repository.OpeningTimeRepository;
import KADAN.interview.demo.repository.PharmacyRepository;
import KADAN.interview.demo.service.PharmacyService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {
	private final PharmacyRepository pharmacyRepository;
	private final OpeningTimeRepository openingTimeRepository;
	private final MaskRepository maskRepository;

	@Override
	public List<PharmacyDto> getOpenPharmacies(String weekDay, String time) {
		final Time queryTime = Time.valueOf(time + ":00");
		return openingTimeRepository
				.findAll(
						((root, criteriaQuery, criteriaBuilder) ->
								criteriaBuilder.and(
										criteriaBuilder.equal(
												root.get(OpeningTime_.weekDay), weekDay
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
	public List<MaskDto> getMasksByPharmacy(String pharmacyName, Boolean sortBy, Boolean direction) {
		Pharmacy pharmacy = pharmacyRepository.findByName(pharmacyName)
				.orElseThrow(
						() -> new EntityNotFoundException("Pharmacy not found: " + pharmacyName));
		return maskRepository.findAll(
						(root, criteriaQuery, criteriaBuilder) ->
						{
							// 預設值處理
							boolean sortByName = sortBy == null || sortBy;         // 預設以 name 排序
							boolean sortDesc = direction != null && direction;     // 預設 asc

							// 決定排序欄位
							Path<?> sortPath = sortByName ? root.get(Mask_.name) : root.get(Mask_.price);

							// 決定排序方向
							Order order = sortDesc
									? criteriaBuilder.desc(sortPath)
									: criteriaBuilder.asc(sortPath);

							// 套用排序條件
							criteriaQuery.orderBy(order);
							return criteriaBuilder.equal(
									root.get(Mask_.pharmacy), pharmacy
							);
						}
				)
				.stream()
				.map(
						mask -> {
							MaskDto dto = new MaskDto();
							dto.setName(mask.getName());
							dto.setPrice(mask.getPrice());
							dto.setPackSize(mask.getPackSize());
							return dto;
						}
				)
				.toList();

	}

	@Override
	public List<PharmacyDto> filterPharmaciesByMask(
			BigDecimal minPrice, BigDecimal maxPrice, int productCount, Boolean type) {
		return pharmacyRepository.findAll(
						(root, criteriaQuery, criteriaBuilder) ->
						{
							Join<Pharmacy, Mask> maskJoin = root.join(Pharmacy_.masks, JoinType.LEFT);
							List<Predicate> predicates = new ArrayList<>();
							if (type == null || type) {
								predicates.add(
										criteriaBuilder.greaterThanOrEqualTo(
												maskJoin.get(Mask_.packSize), productCount
										)
								);
							} else {
								predicates.add(
										criteriaBuilder.lessThan(
												maskJoin.get(Mask_.packSize), productCount
										)
								);
							}
							criteriaBuilder.greaterThan(
									maskJoin.get(Mask_.price), minPrice
							);
							criteriaBuilder.lessThan(
									maskJoin.get(Mask_.price), maxPrice
							);

							return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
						}
				)
				.stream()
				.map(
						pharmacy -> {
							PharmacyDto dto = new PharmacyDto();
							dto.setName(pharmacy.getName());
							dto.setCashBalance(pharmacy.getCashBalance());
							return dto;
						}
				).toList();
	}

}
