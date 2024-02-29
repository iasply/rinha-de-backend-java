package rinha;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import rinha.model.ClienteModel;
import rinha.model.SaldoModel;
import rinha.model.TransacaoModel;

public class ContaService {

  private ContaRepository cr = new ContaRepository();

  public ContaService() {
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
}
