/*
 * Controller de uma tela FXML simples para o lado do cliente.
 */
package feevale.br;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import feevale.br.model.*;

import java.util.HashMap;

public class ClienteController {

    // Componentes visuais do FXML
    @FXML
    private ListView<Item> listViewCardapio;
    @FXML
    private ListView<Item> listViewCarrinho;
    @FXML
    private Label lblValorTotal;
    @FXML
    private Button btnAdicionar;
    @FXML
    private Button btnRemover;
    @FXML
    private Button btnConfirmarPedido;

    // Listas internas para controle da tela
    private ObservableList<Item> itensCardapio;
    private ObservableList<Item> itensCarrinho;
    private Pedido pedidoAtual;

    @FXML
    public void initialize() {
        // 1. Inicializa as listas do JavaFX
        itensCardapio = FXCollections.observableArrayList();
        itensCarrinho = FXCollections.observableArrayList();
        
        // 2. Cria o pedido do cliente com um número único gerado pelo Estabelecimento
        String numeroUnico = GerenciadorAtendimentos.getInstancia().gerarNumeroUnico();
        // Criando o pedido sem cupom inicial e com método fictício (pode ser expandido com combobox)
        pedidoAtual = new Pedido(numeroUnico, null, new Pix("chave-exemplo"));

        // 3. Alimenta o cardápio com alguns itens de exemplo (na prática, viria de um banco)
        carregarItensExemplo();

        // 4. Vincula as listas internas com os componentes visuais da tela
        listViewCardapio.setItems(itensCardapio);
        listViewCarrinho.setItems(itensCarrinho);

        // 5. Ação do Botão ADICIONAR ITEM
        btnAdicionar.setOnAction(event -> {
            Item selecionado = listViewCardapio.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                itensCarrinho.add(selecionado);
                pedidoAtual.adicionarItem(selecionado, 1); // Atualiza o modelo (Requisito Funcional)
                atualizarTotalTela();
            }
        });

        // 6. Ação do Botão REMOVER ITEM
        btnRemover.setOnAction(event -> {
            Item selecionado = listViewCarrinho.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                itensCarrinho.remove(selecionado);
                pedidoAtual.removerItem(selecionado); // Atualiza o modelo (Requisito Funcional)
                atualizarTotalTela();
            }
        });

        // 7. Ação do Botão CONFIRMAR E CONCLUIR PROCESSOS
        btnConfirmarPedido.setOnAction(event -> {
            if (!itensCarrinho.isEmpty()) {
                // Envia o pedido pronto para a cozinha através do Gerenciador Central
                GerenciadorAtendimentos.getInstancia().registrarNovoAtendimento(pedidoAtual);
                
                // Exibe uma mensagem de sucesso (Pode trocar por um Alerta visual do JavaFX)
                System.out.println("Pedido " + pedidoAtual.getNumeroAtendimento() + " enviado para a cozinha!");
                
                // Limpa a tela para o próximo cliente
                limparTelaParaNovoCliente();
            }
        });
    }

    private void atualizarTotalTela() {
        // Requisito Funcional: "visualizar o resumo com o valor total"
        double total = pedidoAtual.calcularValorTotal();
        lblValorTotal.setText(String.format("Total: R$ %.2f", total));
    }

    private void limparTelaParaNovoCliente() {
        itensCarrinho.clear();
        String novoNumero = GerenciadorAtendimentos.getInstancia().gerarNumeroUnico();
        pedidoAtual = new Pedido(novoNumero, null, new Pix("chave-exemplo"));
        lblValorTotal.setText("Total: R$ 0,00");
    }

    private void carregarItensExemplo() {
        Categoria lanches = new Categoria("Lanches");
        Alergenicos[] semAlergenicos = new Alergenicos[0];

        itensCardapio.add(new Item("X-Burger", 15.90, "Padrão", lanches, semAlergenicos));
        itensCardapio.add(new Item("Batata Frita", 9.50, "Média", lanches, semAlergenicos));
        itensCardapio.add(new Item("Refrigerante", 6.00, "350ml", lanches, semAlergenicos));
    }
}
