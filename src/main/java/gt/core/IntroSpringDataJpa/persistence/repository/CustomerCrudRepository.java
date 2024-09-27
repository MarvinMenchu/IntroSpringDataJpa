package gt.core.IntroSpringDataJpa.persistence.repository;

import gt.core.IntroSpringDataJpa.persistence.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CustomerCrudRepository extends JpaRepository<Customer, Long> {
}