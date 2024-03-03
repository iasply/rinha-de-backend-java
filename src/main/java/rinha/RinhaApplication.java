package rinha;

import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.pgclient.PgBuilder;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlClient;

public class RinhaApplication {

  private static final Logger logger = LoggerFactory.getLogger(RinhaApplication.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    PgConnectOptions connectOptions = new PgConnectOptions()
            .setPort(5432)
            .setHost("0.0.0.0")
            .setDatabase("rinha")
            .setUser("rinha")
            .setPassword("rinha")
            .setSsl(false)
            ;


    PoolOptions poolOptions = new PoolOptions()
      .setMaxSize(20);

    Pool client = PgBuilder
            .pool()
      .with(poolOptions)
      .connectingTo(connectOptions)
      .using(vertx)
      .build();

    client.query("SELECT NOW()").execute(ar -> {
      if (ar.succeeded()) {
        System.out.println("Connection successful!");
        // You can do further operations with the database here
      } else {
        System.out.println("Connection failed: " + ar.cause().getCause());
      }


    });

    vertx.deployVerticle(new MainVert(client))
      .onSuccess(handler -> System.out.println("HTTP Verticle deployed"));
  }
}
