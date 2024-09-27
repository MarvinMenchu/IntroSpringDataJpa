package gt.core.IntroSpringDataJpa.persistence.repository;

import gt.core.IntroSpringDataJpa.persistence.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerCrudRepository extends JpaRepository<Customer, Long> {
    Customer searchByUsername(String username);
    Optional<Customer> findByUsername(String username);

    // select c.* from clientes c where c.nombre like %name%
    List<Customer> findByNameContaining(String name);

    // select c.* from clientes c where c.nombre like name%
    List<Customer> queryByNameStartingWith(String name);

    // select c.* from clientes c where c.nombre like %name
    List<Customer> readByNameIsEndingWith(String name);

    // select c.* from clientes c where c.nombre like %name% and c.id > id order by c.id desc
    List<Customer> findByNameContainingAndIdGreaterThanOrderByIdDesc(String name, Long id);

    @Query("select c from Customer c where c.name like %?1% and c.id >= ?2 order by c.id desc") // JPQL (Java Persistence Query Language)
    List<Customer> findAllByNameAndIdGreaterThan(String name, Long id);

    @Query(value = "select c.* from clientes c where c.nombre like %?1% and c.id >= ?2 order by c.username desc", nativeQuery = true)
    List<Customer> findAllByNameAndIdGreaterThanUsingNativeSQL(String name, Long id);
}