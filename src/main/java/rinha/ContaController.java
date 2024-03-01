package rinha;


import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class ContaController {

  private static Vertx vertx;
  private static  ContaService cr ;

  public ContaController(Vertx vertx,ContaService cr) {
    ContaController.vertx = vertx;
    ContaController.cr = cr;
  }


  public static Router getRouter() {
    Router router = Router.router(vertx);
    transacao(router);

    return router;
  }

  static void transacao(Router rt) {
    rt.post("/clientes/:id/transacoes").handler(cr::retornoSaldo);
  }
  static void extrato(Router rt){
    rt.get("//clientes/:id/extrato").handler(cr::retornoExtrato);
  }
}
