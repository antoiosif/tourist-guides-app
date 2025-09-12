package gr.tourist_guides.ds.touristguidesapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TouristGuidesApplication {

	public static void main(String[] args) {
		SpringApplication.run(TouristGuidesApplication.class, args);
	}

}
