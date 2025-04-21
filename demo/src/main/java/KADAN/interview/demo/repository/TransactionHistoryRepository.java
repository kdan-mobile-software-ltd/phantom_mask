package KADAN.interview.demo.repository;

import KADAN.interview.demo.converter.dto.UserDto;
import KADAN.interview.demo.entity.TransactionHistory;
import KADAN.interview.demo.converter.dto.TransactionSummaryDto;
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
			SELECT new KADAN.interview.demo.converter.dto.TransactionSummaryDto(SUM(m.packSize), SUM(t.transactionAmount))
			FROM TransactionHistory t JOIN Mask m
			ON t.pharmacy.id = m.pharmacy.id AND t.maskName = m.name
			WHERE t.transactionDate BETWEEN :startDate AND :endDate
	""")
	TransactionSummaryDto getTransactionSummary(@Param("startDate") Date startDate,
	                                            @Param("endDate") Date endDate);
	@Query("""
		SELECT new KADAN.interview.demo.converter.dto.UserDto(th.users.name, SUM(th.transactionAmount))
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

//	@Query(value = """
//    SELECT u.id AS id, u.name AS name, SUM(t.transaction_amount) AS totalAmount
//    FROM kadan.transaction_history t
//    JOIN kadan.users u ON t.user_id = u.id
//    WHERE t.transaction_date BETWEEN :startDate AND :endDate
//    GROUP BY u.id, u.name
//    ORDER BY totalAmount DESC
//    LIMIT :limit
//    """, nativeQuery = true)
//	List<Object[]> findTopUsersByTransactionBetween(
//			@Param("startDate") Date startDate,
//			@Param("endDate") Date endDate,
//			@Param("limit") int limit
//	);

}
