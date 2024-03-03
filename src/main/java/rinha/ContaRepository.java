package rinha;

import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlClient;
import rinha.model.RespostaExtrato;
import rinha.model.TransacaoModel;

import static rinha.util.Util.getData;

public class ContaRepository {

    private SqlClient sqlClient;

    public ContaRepository(SqlClient sqlClient) {
        this.sqlClient = sqlClient;
    }


    public Future<RowSet<Row>> getCliente(int id) {
        return sqlClient.query("SELECT * FROM cliente WHERE id =" + id).execute().onComplete(rowSetAsyncResult -> {
            if (rowSetAsyncResult.succeeded()) {
                RowSet<Row> result = rowSetAsyncResult.result();
            } else {
                System.out.println("Failure: " + rowSetAsyncResult.cause().getMessage());
            }
        });
    }

    public void updateCliente(int id, Integer saldo, TransacaoModel transacaoModel) {
        sqlClient.query("UPDATE cliente SET saldo =" + saldo + " WHERE id =" + id).execute(asyncResult -> {
            if (asyncResult.succeeded()) {
                String insertQuery = "INSERT INTO transacao (cliente_id, valor, tipo, descricao, data) VALUES (%s, %s, '%s', '%s', '%s')";
                String query = String.format(insertQuery, transacaoModel.getId(), transacaoModel.getValor(), transacaoModel.getTipo(), transacaoModel.getDescricao(), getData());
                sqlClient.query(query).execute(asyncResult1 -> {

                });
            }
        });
    }


    public RespostaExtrato getExtrato() {
        return null;
    }
}
