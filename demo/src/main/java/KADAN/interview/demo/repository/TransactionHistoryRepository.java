package KADAN.interview.demo.repository;

import KADAN.interview.demo.converter.dto.TransactionSummaryDto;
import KADAN.interview.demo.converter.dto.UserDto;
import KADAN.interview.demo.entity.TransactionHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long>, JpaSpecificationExecutor<TransactionHistory> {
	@Query("""
				SELECT new KADAN.interview.demo.converter.dto.TransactionSummaryDto(
					SUM(i.quantity),
					SUM(t.transactionAmount)
				)
				FROM TransactionHistory t
				JOIN PharmacyMaskInventory i
				ON t.pharmacy = i.pharmacy AND t.maskName = i.mask.name
				WHERE t.transactionDate BETWEEN :startDate AND :endDate
			""")
	TransactionSummaryDto getTransactionSummary(@Param("startDate") Date startDate,
	                                            @Param("endDate") Date endDate);

	@Query("""
				SELECT new KADAN.interview.demo.converter.dto.UserDto(th.users.id,th.users.name, SUM(th.transactionAmount))
				FROM TransactionHistory th
				WHERE th.transactionDate BETWEEN :startDate AND :endDate
				GROUP BY th.users.id, th.users.name
				ORDER BY SUM(th.transactionAmount) DESC
			""")
	List<UserDto> findTopUsersByTransactionBetween(
			@Param("startDate") Date startDate,
			@Param("endDate") Date endDate,
			Pageable pageable
	);
}
