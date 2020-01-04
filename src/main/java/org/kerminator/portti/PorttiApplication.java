package org.kerminator.portti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PorttiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PorttiApplication.class, args);
	}

}
