package KADAN.interview.demo.repository;

import KADAN.interview.demo.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long>, JpaSpecificationExecutor<Pharmacy> {
	Optional<Pharmacy> findByName(String name);

	@Query(value = "SELECT *, MATCH(name) AGAINST(:keyword IN BOOLEAN MODE) AS relevance " +
			"FROM pharmacy WHERE MATCH(name) AGAINST(:keyword IN BOOLEAN MODE) " +
			"ORDER BY relevance DESC", nativeQuery = true)
	List<Pharmacy> searchByName(@Param("keyword") String keyword);
}
