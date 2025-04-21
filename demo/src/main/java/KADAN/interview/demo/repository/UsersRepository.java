package KADAN.interview.demo.repository;

import KADAN.interview.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users> {
}
