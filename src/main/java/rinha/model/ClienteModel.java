package rinha.model;

public class ClienteModel {
  private int id;
  private Integer saldo;
  private Integer limite;

  public ClienteModel() {
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setSaldo(Integer saldo) {
    this.saldo = saldo;
  }

  public void setLimite(Integer limite) {
    this.limite = limite;
  }

  public int getId() {
    return id;
  }

  public Integer getSaldo() {
    return saldo;
  }

  public Integer getLimite() {
    return limite;
  }
}
