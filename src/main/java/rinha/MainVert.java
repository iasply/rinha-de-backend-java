package rinha;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.sqlclient.SqlClient;

public class MainVert extends AbstractVerticle {

  private final SqlClient sqlClient;
  private final Logger logger = LoggerFactory.getLogger(MainVert.class);


  public MainVert(SqlClient sqlClient) {
    super();
    this.sqlClient = sqlClient;
  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    ContaRepository contaRepository = new ContaRepository(sqlClient);
    ContaService contaService = new ContaService(contaRepository);
    ContaController contaController = new ContaController(contaService);

    contaController.transacao(router);
    contaController.extrato(router);

    server.requestHandler(router).listen(8888)
      .onComplete(ar -> {
        if (ar.succeeded()) {
          System.out.println("Application started on port: " + 8888);
        } else {
          System.out.println("Error starting HTTP server");
        }
      });
  }
}
