package gt.core.IntroSpringDataJpa;

import gt.core.IntroSpringDataJpa.persistence.entity.Customer;
import gt.core.IntroSpringDataJpa.persistence.repository.CustomerCrudRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class IntroSpringDataJpaApplication {

	@Autowired
	private DataSource ds;

	@Autowired
	private CustomerCrudRepository customerCrudRepository;

	public static void main(String[] args) {
		SpringApplication.run(IntroSpringDataJpaApplication.class, args);
	}

	@Bean
	public CommandLineRunner validatorDSCommand(DataSource ds) {
		return args -> {

			System.out.println("\n Probando conexion y DS");
			Connection conn = ds.getConnection();
			PreparedStatement pstm = conn.prepareStatement("SELECT * from characters");
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String mensaje = rs.getInt("id") + " " + rs.getString("name");
				System.out.println(mensaje);
			}
		};
	}

	@Bean
	public CommandLineRunner validateEntityManagerFactory(EntityManagerFactory emf) {
		return args -> {
			System.out.println("\n Probando entityManagerFactory");

			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			List<Object[]> characters = em.createNativeQuery("SELECT * from characters").getResultList();
			characters.forEach(character -> {
				String mensaje = character[0] + " " + character[1];
				System.out.println(mensaje);
			});
			em.getTransaction().commit();
		};
	}

	@Bean
	public CommandLineRunner validateEntityManager(EntityManager em) {
		return args -> {
			System.out.println("\n Probando entityManager");

			List<Object[]> characters = em.createNativeQuery("SELECT * from characters").getResultList();
			characters.forEach(character -> {
				String mensaje = character[0] + " " + character[1];
				System.out.println(mensaje);
			});
		};

	}

	@Bean
	public CommandLineRunner validateCrudRepository() {
		return args -> {
			Customer marvin = new Customer();
			marvin.setName("Marvin");
			marvin.setPassword("1234");

			Customer ramon = new Customer();
			ramon.setName("Ramon");
			ramon.setPassword("1234");

			Customer luis = new Customer();
			luis.setName("Luis");
			luis.setPassword("1234");

			List<Customer> customers = List.of(marvin, ramon, luis);

			System.out.println("Se guardaron tres entidades");
			customerCrudRepository.saveAll(customers);

			System.out.println("Buscando cliente Marvin");
			customerCrudRepository.findById(1L)
					.ifPresent(each -> {
						each.setName("Marvin Menchu");
						each.setPassword("85008500");
						customerCrudRepository.save(each);
					});

			System.out.println("Buscando cliente Ramon");
			customerCrudRepository.findById(2L).ifPresent(System.out::println);

			System.out.println("Buscando cliente Luis");
			customerCrudRepository.findById(3L).ifPresent(System.out::println);

			System.out.println("Impriendo todos los clientes");
			customerCrudRepository.findAll().forEach(customer -> {
				System.out.println("Cliente: " + customer);
			});

			System.out.println("Eliminando cliente con id 1");
			customerCrudRepository.deleteById(2L);

			System.out.println("Impriendo todos los clientes");
			customerCrudRepository.findAll().forEach(customer -> {
				System.out.println("Cliente: " + customer);
			});
		};
	}
}