package rinha;

import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Tuple;
import rinha.model.TransacaoModel;



public class ContaRepository {

    private SqlClient sqlClient;

    public ContaRepository(SqlClient sqlClient) {
        this.sqlClient = sqlClient;
    }


    public Future<RowSet<Row>> getCliente(int id) {
        return sqlClient.preparedQuery("SELECT * FROM clientes WHERE id =" + id).execute().onComplete(rowSetAsyncResult -> {
            if (rowSetAsyncResult.succeeded()) {
                RowSet<Row> result = rowSetAsyncResult.result();
            } else {
                System.out.println("Failure: " + rowSetAsyncResult.cause().getMessage());
            }
        });
    }

    public void updateCliente(int id, Integer saldo, TransacaoModel transacaoModel) {
        sqlClient.preparedQuery("UPDATE cliente SET saldo =" + saldo + " WHERE id =" + id).execute(asyncResult -> {
            if (asyncResult.succeeded()) {
                String insertQuery = "INSERT INTO transacao (cliente_id, valor, tipo, descricao, data) VALUES (%s, %s, '%s', '%s')";
                String query = String.format(insertQuery, transacaoModel.getId(), transacaoModel.getValor(), transacaoModel.getTipo(), transacaoModel.getDescricao());
                sqlClient.query(query).execute(asyncResult1 -> {

                });
            }
        });
    }

public  Future<RowSet<Row>> updateViaFuncao(TransacaoModel transacaoModel){
        return sqlClient.preparedQuery(
               " SELECT new_saldo, limite FROM update_saldo_cliente($1, $2, $3, $4)"
        ).execute(Tuple.of(transacaoModel.getId(),transacaoModel.getValor(),transacaoModel.getTipo(),transacaoModel.getDescricao())).onComplete(rowSetAsyncResult -> {
            if (rowSetAsyncResult.succeeded()) {
                RowSet<Row> result = rowSetAsyncResult.result();
            } else {
                System.out.println("Failure: " + rowSetAsyncResult.cause().getMessage());
            }
        });

}

    public Future<RowSet<Row>> getExtrato(int id) {
       return sqlClient.preparedQuery(
               "    SELECT *\n" +
               "    FROM transacoes \n" +
               "    WHERE cliente_id = \n" +id+
               "    ORDER BY id DESC\n" +
               "    LIMIT 10\n"
             ).execute().onComplete(rowSetAsyncResult -> {
            if (rowSetAsyncResult.succeeded()) {
                RowSet<Row> result = rowSetAsyncResult.result();
            } else {
                System.out.println("Failure: " + rowSetAsyncResult.cause().getMessage());
            }
        });

    }
}
