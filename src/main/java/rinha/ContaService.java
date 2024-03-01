package rinha;


import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import rinha.model.ClienteModel;
import rinha.model.SaldoModel;
import rinha.model.TransacaoModel;

public class ContaService {

  private ContaRepository cr;

  public ContaService(ContaRepository cr) {
    this.cr = cr;
  }

  public void retornoSaldo(RoutingContext rc) {
    TransacaoModel transacaoModel = ValidarJson.validarTransacao(rc);
    if (transacaoModel == null) {
      rc.fail(400);
      return;
    }
    ClienteModel clienteModel = cr.getCliente(transacaoModel.getId());
    boolean b = validacaoSaldo(clienteModel, transacaoModel);
    if (!b) {
      rc.fail(422);
      return;
    }

    SaldoModel saldo = new SaldoModel();
    saldo.setSaldo(
      transacaoModel.getTipo().equals("c") ?
        clienteModel.getSaldo() + transacaoModel.getValor() :
        clienteModel.getSaldo() - transacaoModel.getValor()
    );
    saldo.setLimite(clienteModel.getLimite());
    JsonObject jsonObject = JsonObject.mapFrom(saldo);


    // TODO: 29/02/2024 importar o jackson
    rc.end(jsonObject.encode());
  }

  private boolean validacaoSaldo(ClienteModel clienteModel, TransacaoModel transacaoModel) {
    if (transacaoModel.getTipo() == "c") {
      return true;
    }
    return (clienteModel.getSaldo() - transacaoModel.getValor()) > (-clienteModel.getLimite());
  }

  public void retornoExtrato(RoutingContext rc) {
    RespostaExtrato respostaExtrato = null;
    try {
      String s = rc.pathParam("id");
      int id = Integer.parseInt(s);
      if (!(id < 6 && id > 0)) {
        throw new RuntimeException();
      }
      respostaExtrato = cr.getExtrato();

    } catch (RuntimeException e) {
      rc.fail(400);
    }
    if (respostaExtrato != null) {
      rc.end(JsonObject.mapFrom(respostaExtrato).encodePrettily());
      return;
    }
    rc.fail(400);
  }
}
