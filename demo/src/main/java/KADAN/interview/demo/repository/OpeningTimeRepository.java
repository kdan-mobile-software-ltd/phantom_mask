package KADAN.interview.demo.repository;

import KADAN.interview.demo.entity.OpeningTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OpeningTimeRepository extends JpaRepository<OpeningTime, Long>, JpaSpecificationExecutor<OpeningTime> {
}
