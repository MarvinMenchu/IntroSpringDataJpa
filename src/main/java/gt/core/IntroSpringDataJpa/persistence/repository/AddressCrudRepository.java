package gt.core.IntroSpringDataJpa.persistence.repository;

import gt.core.IntroSpringDataJpa.persistence.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressCrudRepository extends JpaRepository<Address, Long> {
    List<Address> findByCustomerNameEndsWith(String customerName);

    @Query("select addr from Address addr join customer c where c.name like %?1")
    List<Address> findByCustomerEndsWith(String customerName);
}