package com.blixthalka.tracing;

import brave.propagation.B3Propagation;
import brave.propagation.ExtraFieldPropagation;
import brave.propagation.Propagation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class DemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  @Bean
  WebClient webClient() {
    return WebClient.create();
  }

  @Bean
  Propagation.Factory getFactory() {
    return ExtraFieldPropagation.newFactory(B3Propagation.FACTORY, "cool-baggage-key");
  }
}
