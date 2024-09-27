package gt.core.IntroSpringDataJpa;

import gt.core.IntroSpringDataJpa.persistence.entity.Address;
import gt.core.IntroSpringDataJpa.persistence.entity.Customer;
import gt.core.IntroSpringDataJpa.persistence.repository.AddressCrudRepository;
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

@SpringBootApplication
public class IntroSpringDataJpaApplication {

	@Autowired
	private DataSource ds;

	@Autowired
	private CustomerCrudRepository customerCrudRepository;

	public static void main(String[] args) {
		SpringApplication.run(IntroSpringDataJpaApplication.class, args);
	}

	//@Bean
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

	//@Bean
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

	//@Bean
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

	//@Bean
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

	//@Bean
	public CommandLineRunner testQueryMethodCommand() {
		return args -> {
			Customer marvin = new Customer();
			marvin.setName("Marvin Menchu");
			marvin.setPassword("1234");
			marvin.setUsername("marvin");

			Customer ramon = new Customer();
			ramon.setName("Ramon Hernandez");
			ramon.setPassword("1234");
			ramon.setUsername("ramon");

			Customer luis = new Customer();
			luis.setName("Luis Marquez");
			luis.setPassword("1234");
			luis.setUsername("luis");

			Customer luis2 = new Customer();
			luis2.setName("Luis Cañas");
			luis2.setPassword("1234");
			luis2.setUsername("luisc");

			List<Customer> customers = List.of(marvin, ramon, luis, luis2);

			System.out.println("Se guardaron tres entidades");
			customerCrudRepository.saveAll(customers);

			// pruebas video 1
//			System.out.println("\n probando query method: searchByUsername");
//			System.out.println(customerCrudRepository.searchByUsername("luis"));;
//
//			System.out.println("\n probando query method: findByUsername");
//			System.out.println(customerCrudRepository.findByUsername("luisc"));;

			// pruebas video 2
			System.out.println("\n Nombres que contienen la letra o");
			customerCrudRepository.findByNameContaining("o")
					.forEach(System.out::println);

			System.out.println("\n Nombres que empiece la letra M");
			customerCrudRepository.queryByNameStartingWith("m")
					.forEach(System.out::println);

			System.out.println("\n Nombres que terminen con la letra s");
			customerCrudRepository.readByNameIsEndingWith("s")
					.forEach(System.out::println);

			System.out.println("\n Nombres que contienen as y id mayor a 3");
			customerCrudRepository.findByNameContainingAndIdGreaterThanOrderByIdDesc("a", 2L)
					.forEach(System.out::println);

			System.out.println("\n Nombres que contienen hu y id mayor a 3");
			customerCrudRepository.findAllByNameAndIdGreaterThan("hu", 0L)
					.forEach(System.out::println);

			System.out.println("\n Nombres que contienen hu y id mayor a 3 usando SQL Nativo");
			customerCrudRepository.findAllByNameAndIdGreaterThanUsingNativeSQL("a", 0L)
					.forEach(System.out::println);
		};
	}

	//@Bean
	public CommandLineRunner testRelationOneToOne(){
		return args -> {
			Customer marvin = new Customer();
			marvin.setName("Marvin Menchu");
			marvin.setPassword("1234");
			marvin.setUsername("marvin");

			Address marvinAddress = new Address();
			marvinAddress.setCountry("Guatemala");
			marvinAddress.setAddress("Calle principal, Aldea Argueta, Solola");
			//marvin.setAddress(marvinAddress);

			Customer ramon = new Customer();
			ramon.setName("Ramon Hernandez");
			ramon.setPassword("1234");
			ramon.setUsername("ramon");

			Address ramonAddress = new Address();
			ramonAddress.setCountry("Guatemala");
			ramonAddress.setAddress("Zona 1, 20-40, Solola");
			//ramon.setAddress(ramonAddress);

			Customer luis = new Customer();
			luis.setName("Luis Marquez");
			luis.setPassword("1234");
			luis.setUsername("luis");

			Address luisAddress = new Address();
			luisAddress.setCountry("Mexico");
			luisAddress.setAddress("Casa 456, colonia 123, Ciudad de Mexico");
			//luis.setAddress(luisAddress);
			List<Customer> customers = List.of(marvin, ramon, luis);
			customerCrudRepository.saveAll(customers);

		};
	}

//	@Bean
//	public CommandLineRunner testCrudAddress(AddressCrudRepository addressCrudRepository){
//		return args -> {
//			addressCrudRepository.findAll()
//					.forEach(each -> {
//						System.out.println(each.getAddress() + " - " + each.getCustomer().getUsername());
//					});
//		};
//	}

	@Bean
	public CommandLineRunner testRelationOneToMany(){
		return args -> {
			Customer marvin = new Customer();
			marvin.setName("Marvin Menchu");
			marvin.setPassword("1234");
			marvin.setUsername("marvin");

			Address marvinAddress = new Address();
			marvinAddress.setCountry("Guatemala");
			marvinAddress.setAddress("Calle principal, Aldea Argueta, Solola");

			Address marvinAddress2 = new Address();
			marvinAddress2.setCountry("EEUU");
			marvinAddress2.setAddress("Manhattan, New York");
			marvin.setAddresses(List.of(marvinAddress, marvinAddress2));

			List<Customer> customers = List.of(marvin);
			customerCrudRepository.saveAll(customers);

		};
	}
}