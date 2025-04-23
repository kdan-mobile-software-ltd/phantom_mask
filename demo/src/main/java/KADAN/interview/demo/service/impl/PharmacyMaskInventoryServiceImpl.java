package KADAN.interview.demo.service.impl;

import KADAN.interview.demo.entity.*;
import KADAN.interview.demo.enumType.SortDirection;
import KADAN.interview.demo.enumType.SortField;
import KADAN.interview.demo.repository.PharmacyMaskInventoryRepository;
import KADAN.interview.demo.service.PharmacyMaskInventoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacyMaskInventoryServiceImpl implements PharmacyMaskInventoryService {
	private final PharmacyMaskInventoryRepository inventoryRepository;

	@Override
	public PharmacyMaskInventory findByPharmacyId(Long pharmacyId) {
		return inventoryRepository
				.findByPharmacyId(pharmacyId)
				.orElseThrow(
						() -> new EntityNotFoundException("The pharmacy doesn't exist on inventory.")
				);
	}

	@Override
	public PharmacyMaskInventory findByMaskId(Long maskId) {
		return inventoryRepository
				.findByMaskId(maskId)
				.orElseThrow(
						() -> new EntityNotFoundException("The mask doesn't exist on inventory.")
				);
	}

	@Override
	public List<PharmacyMaskInventory> findAll(Pharmacy pharmacy, SortField sortBy, SortDirection direction) {
		return inventoryRepository
				.findAll(
						(root, criteriaQuery, criteriaBuilder) ->
						{
							// 避免 N+1
							root.fetch(PharmacyMaskInventory_.mask, JoinType.LEFT);

							// 預設以 name 排序
							SortField sortField = sortBy != null ? sortBy : SortField.NAME;
							// 預設 asc
							SortDirection sortDir = direction != null ? direction : SortDirection.ASC;

							// 決定排序欄位
							Path<?> sortPath =
									sortField.equals(SortField.NAME) ?
											root.get(PharmacyMaskInventory_.mask).get(Mask_.name) :
											root.get(PharmacyMaskInventory_.mask).get(Mask_.price);

							// 決定排序方向
							Order order = sortDir.equals(SortDirection.DESC)
									? criteriaBuilder.desc(sortPath)
									: criteriaBuilder.asc(sortPath);

							// 套用排序條件
							criteriaQuery.orderBy(order);
							return criteriaBuilder.equal(
									root.get(PharmacyMaskInventory_.pharmacy), pharmacy
							);
						}
				);
	}
}
