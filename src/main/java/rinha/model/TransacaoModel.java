package rinha.model;

import io.reactivex.rxjava3.annotations.NonNull;

public class TransacaoModel {
  private int id;
  private int valor;
  private String tipo;
  private String descricao;

  public TransacaoModel() {
  }

  public void setValor(@NonNull int valor) {
    this.valor = valor;
  }

  public void setTipo(@NonNull String tipo) {
    this.tipo = tipo;
  }

  public void setDescricao(@NonNull String descricao) {
    this.descricao = descricao;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getValor() {
    return valor;
  }

  public String getTipo() {
    return tipo;
  }

  public String getDescricao() {
    return descricao;
  }

  public int getId() {
    return id;
  }
}
