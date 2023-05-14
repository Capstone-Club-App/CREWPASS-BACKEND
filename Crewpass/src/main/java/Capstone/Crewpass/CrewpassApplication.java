package Capstone.Crewpass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //Auditing이 가능하도록 어노테이션 설정
@SpringBootApplication
public class CrewpassApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrewpassApplication.class, args);
	}

}
