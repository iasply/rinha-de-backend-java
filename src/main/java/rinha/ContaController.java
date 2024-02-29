package rinha;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class ContaController {

  private static  Vertx vertx;
  private static ContaService cr = new ContaService();
  public ContaController(Vertx vertx) {
  this.vertx = vertx;
  }

  public static Router getRouter() {
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.get("/teste").handler(RoutingContext ->{
      RoutingContext.response().setStatusCode(200).end("as");
    });
    transacao(router);
    return router;
  }
  static void transacao(Router rt){
    rt.post("/clientes/:id/transacoes").handler(cr::retornoSaldo);
  }
}
