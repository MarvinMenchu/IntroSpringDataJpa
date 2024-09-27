package gt.core.IntroSpringDataJpa.persistence.repository;

import gt.core.IntroSpringDataJpa.persistence.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressCrudRepository extends JpaRepository<Address, Long> {
}
