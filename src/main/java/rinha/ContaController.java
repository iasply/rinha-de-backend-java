package rinha;


import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class ContaController {


  private static  ContaService cr ;

  public ContaController(ContaService cr) {
    ContaController.cr = cr;
  }

  public void transacao(Router rt) {
    rt.post("/clientes/:id/transacoes").handler(cr::retornoSaldo);
  }
  public void extrato(Router rt){
    rt.get("/clientes/:id/extrato").handler(cr::retornoExtrato);
  }
}
