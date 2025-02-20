package dio.gerenciamento_projetos;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(servers = {
		@Server(url = "/" , description = "Server Padrão")
})
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println("http://localhost:8080/swagger-ui.html");
		System.out.println("http://localhost:8080/h2-console");
	}

}
