package rinha;

import io.vertx.ext.web.RoutingContext;
import rinha.model.ClienteModel;
import rinha.model.TransacaoModel;

public class ContaService {

  private ContaRepository cr  = new ContaRepository();
  public ContaService() {
  }

  public void retornoSaldo(RoutingContext rc) {
    TransacaoModel transacaoModel = ValidarJson.validarTransacao(rc);
    ClienteModel clienteModel = cr.getCliente(transacaoModel.getId());
    boolean b = validacaoSaldo(clienteModel,transacaoModel);
  }

  private boolean validacaoSaldo(ClienteModel clienteModel, TransacaoModel transacaoModel) {
    if (transacaoModel.getTipo().equals("c")){
      return true;
    }
    boolean i= (clienteModel.getSaldo() - transacaoModel.getValor())< (- clienteModel.getLimite());
    if (clienteModel.getSaldo())


    return false;
  }
}
