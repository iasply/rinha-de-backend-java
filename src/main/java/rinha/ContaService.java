package rinha;


import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import rinha.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
        Future<RowSet<Row>> rowSetFuture = cr.updateViaFuncao(transacaoModel);

        rowSetFuture.onFailure(throwable -> {
            rc.fail(422);

        });

        rowSetFuture.onComplete(asyncResult -> {
            if (asyncResult.succeeded()) {
                asyncResult.result().forEach(row -> {
                    SaldoModel saldoModel = new SaldoModel();
                    saldoModel.setSaldo(row.getInteger("new_saldo"));
                    saldoModel.setLimite(row.getInteger("limite"));
                    JsonObject jsonObject = JsonObject.mapFrom(saldoModel);
                    rc.end(jsonObject.encode());
                });
            }
        });
    }

    public void retornoExtrato(RoutingContext rc) {


        String s = rc.pathParam("id");
        int id = Integer.parseInt(s);
        if (!(id < 6 && id > 0)) {
            System.out.println("erro do id");
            rc.fail(404);
            return;

        }

        Future<RowSet<Row>> rowSetFuture = cr.getExtrato(id);
        rowSetFuture.onComplete(asyncResult -> {


            List<Transacao> list = new ArrayList<>();

            asyncResult.result().forEach(
                    row -> {


                        Transacao transacao = new Transacao();
                        transacao.setDescricao(row.getString("descricao"));
                        transacao.setTipo(row.getString("tipo"));
                        transacao.setRealizada_em(String.valueOf(row.getLocalDateTime("realizada_em")));
                        transacao.setValor(row.getInteger("valor"));
                        list.add(transacao);

                    }
            );

            Future<RowSet<Row>> rowSetFuture2 = cr.getCliente(id);
            rowSetFuture2.onComplete(asyncResult1 -> {
                Saldo saldo = new Saldo();

                asyncResult1.result().forEach(
                        row -> {
                            saldo.setLimite(row.getInteger("limite"));
                            saldo.setTotal(row.getInteger("saldo"));

                        }
                );
                LocalDateTime agora = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
                saldo.setData_extrato(agora.format(formatter));
                RespostaExtrato respostaExtrato = new RespostaExtrato(saldo, list);
                if (respostaExtrato != null) {
                    rc.end(JsonObject.mapFrom(respostaExtrato).encodePrettily());
                    return;
                }
                rc.fail(400);

            });
        }).onFailure(throwable -> {
            throwable.printStackTrace();
        });


    }



}
