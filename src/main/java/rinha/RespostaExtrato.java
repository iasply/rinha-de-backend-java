package rinha;

import java.util.List;

public class RespostaExtrato {
  private Saldo saldo;
  private List<Transacao> ultimas_transacoes;

  public RespostaExtrato(Saldo saldo, List<Transacao> ultimas_transacoes) {
    this.saldo = saldo;
    this.ultimas_transacoes = ultimas_transacoes;
  }
}
