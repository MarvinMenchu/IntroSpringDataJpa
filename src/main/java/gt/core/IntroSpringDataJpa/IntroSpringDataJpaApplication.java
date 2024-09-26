package gt.core.IntroSpringDataJpa;

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
}