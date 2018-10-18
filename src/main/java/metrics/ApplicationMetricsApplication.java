package metrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"metrics", "metrics.service,metrics.controller"})
public class ApplicationMetricsApplication extends SpringBootServletInitializer  {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationMetricsApplication.class, args);
	}
}
