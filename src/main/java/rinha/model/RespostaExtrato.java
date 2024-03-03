package rinha.model;

import rinha.model.Saldo;
import rinha.model.Transacao;

import java.util.List;

public class RespostaExtrato {
  private Saldo saldo;
  private List<Transacao> ultimas_transacoes;

  public RespostaExtrato(Saldo saldo, List<Transacao> ultimas_transacoes) {
    this.saldo = saldo;
    this.ultimas_transacoes = ultimas_transacoes;
  }

  public Saldo getSaldo() {
    return saldo;
  }

  public void setSaldo(Saldo saldo) {
    this.saldo = saldo;
  }

  public List<Transacao> getUltimas_transacoes() {
    return ultimas_transacoes;
  }

  public void setUltimas_transacoes(List<Transacao> ultimas_transacoes) {
    this.ultimas_transacoes = ultimas_transacoes;
  }
}
