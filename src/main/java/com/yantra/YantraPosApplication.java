package com.yantra;

import com.yantra.config.AwsSecretsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YantraPosApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(YantraPosApplication.class);
		app.addInitializers(new AwsSecretsConfig());
		app.run(args);
	}
}
