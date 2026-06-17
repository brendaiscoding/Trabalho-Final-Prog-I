package feevale.br;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GerenciadorPedidos {
    private static GerenciadorPedidos instancia;
    private ObservableList<Pedido> listaPedidosCozinha;

    private GerenciadorPedidos() {
        // Inicializa a lista observável do JavaFX
        this.listaPedidosCozinha = FXCollections.observableArrayList();
    }

    public static synchronized GerenciadorPedidos getInstancia() {
        if (instancia == null) {
            instancia = new GerenciadorPedidos();
        }
        return instancia;
    }

    public ObservableList<Pedido> getListaPedidosCozinha() {
        return listaPedidosCozinha;
    }

    /**
     * Simula a chegada de um novo pedido vindo do site
     */
    public void receberNovoPedido(Pedido pedido) {
        pedido.setStatus("Pendente na Cozinha");
        listaPedidosCozinha.add(pedido); 
        // No momento que adiciona aqui, o JavaFX atualiza a tela da cozinha na hora!
    }

    /**
     * Chamado quando o cozinheiro clica em "Pronto"
     */
    public void concluirPedido(Pedido pedido) {
        pedido.setStatus("Pronto para Entrega");
        listaPedidosCozinha.remove(pedido); 
        // Remove da tela da cozinha automaticamente
    }
}
