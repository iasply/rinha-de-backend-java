package rinha;


import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import rinha.model.ClienteModel;
import rinha.model.RespostaExtrato;
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

        Future<RowSet<Row>> rowSetFuture = cr.getCliente(transacaoModel.getId());
        rowSetFuture.onComplete(asyncResult -> {
            asyncResult.result().forEach(row -> {

                ClienteModel clienteModel = new ClienteModel();
                clienteModel.setId(row.getInteger("id"));
                clienteModel.setLimite(row.getInteger("limite"));
                clienteModel.setSaldo(row.getInteger("saldo"));

                boolean b = validacaoSaldo(clienteModel, transacaoModel);
                if (!b) {
                    rc.fail(422);
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
        });

    }

    private boolean validacaoSaldo(ClienteModel clienteModel, TransacaoModel transacaoModel) {
        if (transacaoModel.getTipo() == "c") {
            return true;
        }
        return (clienteModel.getSaldo() - transacaoModel.getValor()) > (-clienteModel.getLimite());
    }

    private Integer novoSaldo(Integer saldoAtual, Integer valorTransacao, String tipo) {
        Integer integer = tipo.equals("c") ? saldoAtual + valorTransacao : saldoAtual - valorTransacao;
        return integer;
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
