package KADAN.interview.demo.repository;

import KADAN.interview.demo.entity.OpeningTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;

@Repository
public interface OpeningTimeRepository extends JpaRepository<OpeningTime, Long>, JpaSpecificationExecutor<OpeningTime> {
	@Query("""
			   SELECT DISTINCT ot FROM OpeningTime ot
			            JOIN FETCH ot.pharmacy p
			            JOIN FETCH p.openingTimes
			            JOIN FETCH p.inventories i
			            JOIN FETCH i.mask
			            WHERE ot.weekDay = :weekDay
			            AND ot.startTime <= :queryTime
			            AND ot.endTime >= :queryTime
			""")
	List<OpeningTime> findOpenPharmacies(@Param("weekDay") String weekDay, @Param("queryTime") Time time);
}
