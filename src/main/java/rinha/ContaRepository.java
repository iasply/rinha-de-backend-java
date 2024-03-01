package rinha;

import io.vertx.pgclient.PgConnectOptions;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.pgclient.PgBuilder;
import io.vertx.rxjava3.sqlclient.SqlClient;
import io.vertx.sqlclient.PoolOptions;
import rinha.model.ClienteModel;

import java.util.ArrayList;
import java.util.List;

public class ContaRepository {
  private final SqlClient client;


  public ContaRepository(SqlClient client) {
    this.client = client;
  }


  public ClienteModel getCliente(int id) {
    return null;
  }

  public RespostaExtrato getExtrato() {
    return new RespostaExtrato(null,null);
  }





}
