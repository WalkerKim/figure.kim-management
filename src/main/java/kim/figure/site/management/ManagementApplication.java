package kim.figure.site.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ManagementApplication {

	public static void main(String[] args) {
		//TODO remove after lean dev phase
//		BlockHound.install();
		SpringApplication.run(ManagementApplication.class, args);
	}

}
