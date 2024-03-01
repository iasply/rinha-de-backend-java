package rinha;

import io.vertx.rxjava3.core.Vertx;

public class Main {
  public static void main(String args[]) {
    final Vertx vertx = Vertx.vertx();
    vertx.rxDeployVerticle(MainVerticle.class.getName()).subscribe(
      verticle -> System.out.println("Nova app vertx"),
      throwable -> {
        System.out.println("deu merda: " + throwable.getMessage());
        System.exit(1);
      });

  }
}
