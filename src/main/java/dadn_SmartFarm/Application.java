package dadn_SmartFarm;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		File envFile = new File(".env");

		if (envFile.exists() && envFile.isFile()) {
			Dotenv dotenv = Dotenv.configure().load();

			System.setProperty("SPRING_APPLICATION_NAME", dotenv.get("SPRING_APPLICATION_NAME", ""));
			System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL", ""));
			System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME", ""));
			System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD", ""));
			System.setProperty("SPRING_JPA_HIBERNATE_DDL_AUTO", dotenv.get("SPRING_JPA_HIBERNATE_DDL_AUTO", ""));
			System.setProperty("SPRING_JPA_SHOW_SQL", dotenv.get("SPRING_JPA_SHOW_SQL", ""));
			System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT", ""));
			System.setProperty("SERVER_CONTEXT_PATH", dotenv.get("SERVER_CONTEXT_PATH", ""));
			System.setProperty("JWT_SIGNER_KEY", dotenv.get("JWT_SIGNER_KEY", ""));
			System.setProperty("ADAFRUIT_USERNAME", dotenv.get("ADAFRUIT_USERNAME", ""));
			System.setProperty("ADAFRUIT_X_AIO_KEY", dotenv.get("ADAFRUIT_X_AIO_KEY", ""));
		}

		SpringApplication.run(Application.class, args);
	}
}
