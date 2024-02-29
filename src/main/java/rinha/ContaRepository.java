package rinha;

import rinha.model.ClienteModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ContaRepository {

  List<ClienteModel> clienteModels = new ArrayList<>();

  public ContaRepository() {
    ClienteModel cliente1 = new ClienteModel();
    cliente1.setId(1);
    cliente1.setLimite(100000);
    cliente1.setSaldo(0);

    ClienteModel cliente2 = new ClienteModel();
    cliente2.setId(2);
    cliente2.setLimite(80000);
    cliente2.setSaldo(0);

    ClienteModel cliente3 = new ClienteModel();
    cliente3.setId(3);
    cliente3.setLimite(1000000);
    cliente3.setSaldo(0);

    ClienteModel cliente4 = new ClienteModel();
    cliente4.setId(4);
    cliente4.setLimite(10000000);
    cliente4.setSaldo(0);

    ClienteModel cliente5 = new ClienteModel();
    cliente5.setId(5);
    cliente5.setLimite(500000);
    cliente5.setSaldo(0);

    clienteModels.add(cliente1);
    clienteModels.add(cliente2);
    clienteModels.add(cliente3);
    clienteModels.add(cliente4);
    clienteModels.add(cliente5);
  }

  public ClienteModel getCliente(int id) {
    return clienteModels.get(id-1);
  }
}
