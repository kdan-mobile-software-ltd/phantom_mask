package KADAN.interview.demo.service.impl;

import KADAN.interview.demo.converter.PurchaseRequest;
import KADAN.interview.demo.converter.dto.TransactionDto;
import KADAN.interview.demo.converter.dto.TransactionSummaryDto;
import KADAN.interview.demo.entity.*;
import KADAN.interview.demo.repository.PharmacyMaskInventoryRepository;
import KADAN.interview.demo.repository.PharmacyRepository;
import KADAN.interview.demo.repository.TransactionHistoryRepository;
import KADAN.interview.demo.repository.UsersRepository;
import KADAN.interview.demo.service.MaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class MaskServiceImpl implements MaskService {
	private final TransactionHistoryRepository transactionHistoryRepository;
	private final UsersRepository userRepository;
	private final PharmacyRepository pharmacyRepository;
	private final PharmacyMaskInventoryRepository inventoryRepository;

	@Override
	public TransactionSummaryDto getMaskTransactionSummary(Date startDate, Date endDate) {
		return transactionHistoryRepository.getTransactionSummary(startDate, endDate);
	}

	@Override
	@Transactional
	public TransactionDto processPurchase(final PurchaseRequest request) {
		Users users = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new IllegalArgumentException("[USER_NOT_FOUND] User ID " + request.getUserId() + " not found."));

		Pharmacy pharmacy = pharmacyRepository.findById(request.getPharmacyId())
				.orElseThrow(() -> new IllegalArgumentException("[PHARMACY_NOT_FOUND] Pharmacy ID " + request.getPharmacyId() + " not found."));

		PharmacyMaskInventory inventory = inventoryRepository
				.findByPharmacyIdAndMaskName(pharmacy.getId(), request.getMaskName())
				.orElseThrow(() -> new IllegalArgumentException(
						"[MASK_NOT_FOUND] Pharmacy '%s' does not carry a mask named '%s'.".formatted(
								pharmacy.getName(), request.getMaskName())
				));

		Mask mask = inventory.getMask();
		BigDecimal unitPrice = mask.getPrice();
		Integer quantity = request.getQuantity();
		BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(quantity));

		if (users.getCashBalance().compareTo(totalAmount) < 0) {
			throw new IllegalStateException(String.format(
					"[INSUFFICIENT_BALANCE] User '%s' does not have enough balance. Need: %s, Has: %s",
					users.getName(), totalAmount, users.getCashBalance()));
		}

		// 檢查使用者餘額
		BigDecimal afterCashBalance = users.getCashBalance().subtract(totalAmount);
		if (afterCashBalance.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalStateException(String.format(
					"[INSUFFICIENT_BALANCE] User '%s' does not have enough balance. Need: $%.2f, Has: $%.2f",
					users.getName(), totalAmount, users.getCashBalance()));
		}
		// 使用者扣除金額
		users.setCashBalance(afterCashBalance);
		// 藥局增加金額
		pharmacy.setCashBalance(pharmacy.getCashBalance().add(totalAmount));
		// 扣除口罩數量
		Integer inventoryQuantity = inventory.getQuantity();
		int afterQuantity = inventoryQuantity - quantity;
		if (afterQuantity < 0) {
			throw new IllegalStateException(
					String.format("Not enough stock for mask '%s'. Available: %d, requested: %d",
							mask.getName(), inventoryQuantity, quantity)
			);
		}
		// 更新庫存
		inventory.setQuantity(afterQuantity);

		try {
			userRepository.save(users);
			pharmacyRepository.save(pharmacy);
			inventoryRepository.save(inventory);
		} catch (Exception e) {
			throw new IllegalStateException("[PERSIST_ERROR] Failed to save user or pharmacy transaction.", e);
		}

		TransactionHistory transaction = new TransactionHistory();
		transaction.setUsers(users);
		transaction.setPharmacy(pharmacy);
		transaction.setMaskName(mask.getName());
		transaction.setTransactionAmount(totalAmount);
		transaction.setTransactionDate(new Date());

		try {
			transactionHistoryRepository.save(transaction);
		} catch (Exception e) {
			throw new IllegalStateException("[TRANSACTION_SAVE_ERROR] Failed to record transaction.", e);
		}

		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setTransactionId(transaction.getId());
		transactionDto.setName(users.getName());
		transactionDto.setMaskName(mask.getName());
		transactionDto.setPharmacyName(pharmacy.getName());
		transactionDto.setQuantity(quantity);
		transactionDto.setTotalAmount(totalAmount);
		transactionDto.setTransactionDate(transaction.getTransactionDate());
		transactionDto.setUnitPrice(unitPrice);
		transactionDto.setUserBalanceAfter(afterCashBalance);
		return transactionDto;
	}
}
