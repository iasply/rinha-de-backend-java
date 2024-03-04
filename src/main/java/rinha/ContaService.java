package rinha;


import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import rinha.model.*;
import rinha.util.Util;

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

        Future<RowSet<Row>> rowSetFuture = cr.getCliente(transacaoModel.getId());
        rowSetFuture.onComplete(asyncResult -> {
            asyncResult.result().forEach(row -> {

                ClienteModel clienteModel = new ClienteModel();
                clienteModel.setId(row.getInteger("id"));
                clienteModel.setLimite(row.getInteger("limite"));
                clienteModel.setSaldo(row.getInteger("saldo"));

                boolean b = validacaoSaldo(clienteModel, transacaoModel);
                if (b) {
                    rc.fail(422);
                    System.out.println("validacao saldo rc 422");
                    return;
                }


                cr.updateCliente(
                        clienteModel.getId(),
                        novoSaldo(
                                clienteModel.getSaldo(),
                                transacaoModel.getValor(),
                                transacaoModel.getTipo()
                        ),
                        transacaoModel);


                SaldoModel saldo = new SaldoModel();
                saldo.setSaldo(
                        transacaoModel.getTipo().equals("c") ?
                                clienteModel.getSaldo() + transacaoModel.getValor() :
                                clienteModel.getSaldo() - transacaoModel.getValor()
                );

                saldo.setLimite(clienteModel.getLimite());
                JsonObject jsonObject = JsonObject.mapFrom(saldo);

                rc.end(jsonObject.encode());


            });
        }).onFailure(throwable -> {
            throwable.printStackTrace();
        });

    }

    public void retornoExtrato(RoutingContext rc) {


        String s = rc.pathParam("id");
        int id = Integer.parseInt(s);
        if (!(id < 6 && id > 0)) {
            System.out.println("erro do id");
            throw new RuntimeException();
        }

        Future<RowSet<Row>> rowSetFuture = cr.getExtrato(id);
        rowSetFuture.onComplete(asyncResult -> {


            List<Transacao> list = new ArrayList<>();

            asyncResult.result().forEach(
                    row -> {


                        Transacao transacao = new Transacao();
                        transacao.setDescricao(row.getString("descricao"));
                        transacao.setTipo(row.getString("tipo"));
                        transacao.setRealizada_em(row.getString("data"));
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
                saldo.setData_extrato(Util.getData());

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

    private boolean validacaoSaldo(ClienteModel clienteModel, TransacaoModel transacaoModel) {
        if (transacaoModel.getValor() < 0) {
            return false;
        }
        if (transacaoModel.getTipo().equals("c")) {
            return (clienteModel.getSaldo() + transacaoModel.getValor()) > (clienteModel.getLimite());
        }
        return (clienteModel.getSaldo() - transacaoModel.getValor()) < (-clienteModel.getLimite());
    }

    private Integer novoSaldo(Integer saldoAtual, Integer valorTransacao, String tipo) {
        Integer integer = tipo.equals("c") ? saldoAtual + valorTransacao : saldoAtual - valorTransacao;
        return integer;
    }

}
