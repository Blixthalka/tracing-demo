package com.blixthalka.tracing;

import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class DemoController {

  private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

  @Autowired
  WebClient webClient;

  @GetMapping("/demo")
  public Mono<String> demo(@RequestParam() String id) {
    logger.info("server1: got id={}", id);
    return webClient
        .get()
        .uri("localhost:8080/user?id=" + id)
        .retrieve()
        .bodyToMono(String.class)
        .doOnNext(user -> logger.info("server1: Got user: {}", user))
        .map(user -> "Hello " + user + "!\n");
  }

  @GetMapping("/user")
  public Mono<String> getUser(@RequestParam()String id) {
    logger.info("server2: got id={}", id);
    return Mono.just(id)
        .flatMap(this::getUserFromDatabase)
        .switchIfEmpty(Mono.just("Well better luck next time!"))
        .doOnNext(s -> logger.info("server2: Returning string: {}", s));
  }

  private Mono<String> getUserFromDatabase(String id) {
    logger.info("server2: looking up id={} in database", id);
    Map<String, String> db = Map.of(
        "1", "John Doe",
        "2", "Jane Doe"
    );

    return Mono.justOrEmpty(Optional.ofNullable(db.get(id)));
  }
}
