package rinha.model;

public class SaldoModel {
  private Integer limite;
  private Integer saldo;

  public SaldoModel() {
  }

  public void setLimite(Integer limite) {
    this.limite = limite;
  }

  public void setSaldo(Integer saldo) {
    this.saldo = saldo;
  }

  public Integer getLimite() {
    return limite;
  }

  public Integer getSaldo() {
    return saldo;
  }
}
