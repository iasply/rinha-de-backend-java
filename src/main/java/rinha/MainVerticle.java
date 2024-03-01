package rinha;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.rxjava3.pgclient.PgBuilder;
import io.vertx.rxjava3.sqlclient.SqlClient;
import io.vertx.sqlclient.PoolOptions;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    ContaRepository contaRepository = new ContaRepository(client((Vertx) vertx));
    ContaService contaService = new ContaService(contaRepository);
    ContaController contaController = new ContaController(vertx,contaService);

    Router route = ContaController.getRouter();

    vertx.createHttpServer().requestHandler(route).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }

  private static SqlClient client(Vertx vertx){
    PgConnectOptions connectOptions = new PgConnectOptions()
      .setPort(5423)
      .setHost("host")
      .setDatabase("rinha")
      .setUser("rinha")
      .setPassword("rinha");
    io.vertx.sqlclient.PoolOptions poolOptions = new PoolOptions().setMaxSize(20);
    SqlClient client = PgBuilder
      .client()
      .with(poolOptions)
      .connectingTo(connectOptions)
      .using(vertx)
      .build();
    return client;

  }

}
