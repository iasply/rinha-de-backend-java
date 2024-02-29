package rinha;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import rinha.model.TransacaoModel;

import java.util.Objects;

public class ValidarJson {


  static public TransacaoModel validarTransacao(RoutingContext rc) {
    String idString = rc.pathParam("id");

    TransacaoModel transacaoModel = null;
    try {
      int id = Integer.parseInt(idString);
      if (!(id < 6 && id > 0)) {
        erro(rc, 404);
        return null;
      }
      transacaoModel.setId(id);
    } catch (RuntimeException e) {
      erro(rc, 400);
      return null;
    }
    JsonObject js = rc.getBodyAsJson();
    try {

      transacaoModel = new TransacaoModel();

      transacaoModel.setValor(js.getInteger("valor"));

      String tipo = js.getString("tipo");
      if (!(Objects.equals(tipo, "c") || Objects.equals(tipo, "d"))) {
        throw new RuntimeException();
      }
      transacaoModel.setDescricao(tipo);

      String desc = js.getString("descricao");
      if (!(desc.length() > 0 && desc.length() < 11)) {
        throw new RuntimeException();
      }
      transacaoModel.setDescricao(desc);

    } catch (RuntimeException e) {
      erro(rc, 400);
      return null;
    }
    rc.response().setStatusCode(200).end("valido");
    return transacaoModel;
  }

  static private void erro(RoutingContext rc, int status) {
    rc.fail(status);
  }

}
