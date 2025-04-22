package KADAN.interview.demo.repository;

import KADAN.interview.demo.entity.Mask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaskRepository extends JpaRepository<Mask, Long>, JpaSpecificationExecutor<Mask> {
	@Query(value = "SELECT *, MATCH(name) AGAINST(:keyword IN BOOLEAN MODE) AS relevance " +
			"FROM mask WHERE MATCH(name) AGAINST(:keyword IN BOOLEAN MODE) " +
			"ORDER BY relevance DESC", nativeQuery = true)
	List<Mask> searchByName(@Param("keyword") String keyword);
}
