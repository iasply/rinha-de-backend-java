package rinha.model;


public class TransacaoModel {
  private int id;
  private int valor;
  private String tipo;
  private String descricao;

  public TransacaoModel() {
  }

  public void setValor( int valor) {
    this.valor = valor;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public void setDescricao( String descricao) {
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
