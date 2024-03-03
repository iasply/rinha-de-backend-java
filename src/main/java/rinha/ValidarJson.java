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
      transacaoModel = new TransacaoModel();
      int id = Integer.parseInt(idString);
      if (!(id < 6 && id > 0)) {
        return null;
      }
      transacaoModel.setId(id);

      JsonObject js = rc.getBodyAsJson();

      transacaoModel.setValor(js.getInteger("valor"));

      String tipo = js.getString("tipo");
      if (!((Objects.equals(tipo, "c")) || (Objects.equals(tipo, "d")))) {
        return null;
      }
      transacaoModel.setTipo(tipo);

      String desc = js.getString("descricao");
      if (!(desc.length() > 0 && desc.length() < 11)) {
        return null;
      }
      transacaoModel.setDescricao(desc);

    } catch (RuntimeException e) {

      return null;
    }
    return transacaoModel;
  }


}
