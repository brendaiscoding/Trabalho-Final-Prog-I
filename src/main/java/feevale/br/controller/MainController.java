package feevale.br.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import feevale.br.model.*;

public class MainController {

    // Componentes do Cliente
    @FXML private ListView<Item> listViewCardapio;
    @FXML private ListView<Item> listViewCarrinho;
    @FXML private Label lblValorTotal;
    @FXML private Button btnAdicionar;
    @FXML private Button btnRemover;
    @FXML private Button btnConfirmarPedido;

    // Componentes da Cozinha
    @FXML private ListView<Atendimento> listViewCozinha;
    @FXML private Button btnMudarStatus;

    // Estado interno da tela
    private ObservableList<Item> itensCardapio;
    private ObservableList<Item> itensCarrinho;
    private Pedido pedidoAtual;

    @FXML
    public void initialize() {
        // --- INICIALIZAÇÃO GERAL ---
        itensCardapio = FXCollections.observableArrayList();
        itensCarrinho = FXCollections.observableArrayList();
        
        // Conecta a lista da cozinha diretamente ao gerenciador global singleton
        listViewCozinha.setItems(GerenciadorAtendimentos.getInstancia().getAtendimentosAtivos());

        carregarCardapioExemplo();
        prepararNovoPedidoCliente();

        listViewCardapio.setItems(itensCardapio);
        listViewCarrinho.setItems(itensCarrinho);

        // --- LÓGICA DO CLIENTE ---
        btnAdicionar.setOnAction(e -> {
            Item selecionado = listViewCardapio.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                itensCarrinho.add(selecionado);
                pedidoAtual.adicionarItem(selecionado, 1);
                atualizarTotalTela();
            }
        });

        btnRemover.setOnAction(e -> {
            Item selecionado = listViewCarrinho.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                itensCarrinho.remove(selecionado);
                pedidoAtual.removerItem(selecionado);
                atualizarTotalTela();
            }
        });

        btnConfirmarPedido.setOnAction(e -> {
            if (!itensCarrinho.isEmpty()) {
                // Ao registrar, o Gerenciador joga na lista que o painel da cozinha já está observando
                GerenciadorAtendimentos.getInstancia().registrarNovoAtendimento(pedidoAtual);
                prepararNovoPedidoCliente(); // Reseta o lado do cliente
            }
        });

        // --- LÓGICA DA COZINHA ---
        btnMudarStatus.setOnAction(e -> {
            Atendimento selecionado = listViewCozinha.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                GerenciadorAtendimentos.getInstancia().avancarStatus(selecionado);
                listViewCozinha.refresh(); // Força a atualização do texto do status na tela
            }
        });
    }

    private void atualizarTotalTela() {
        lblValorTotal.setText(String.format("Total: R$ %.2f", pedidoAtual.calcularValorTotal()));
    }

    private void prepararNovoPedidoCliente() {
        itensCarrinho.clear();
        String numeroUnico = GerenciadorAtendimentos.getInstancia().gerarNumeroUnico();
        pedidoAtual = new Pedido(numeroUnico, null, new Pix("chave-pix"));
        lblValorTotal.setText("Total: R$ 0,00");
    }

    private void carregarCardapioExemplo() {
        Categoria lanches = new Categoria("Lanches");
        Alergenicos[] nenhum = new Alergenicos[0];
        itensCardapio.add(new Item("X-Burguer", 18.00, "Tradicional", lanches, nenhum));
        itensCardapio.add(new Item("Batata Frita", 12.00, "Média", lanches, nenhum));
        itensCardapio.add(new Item("Refrigerante", 6.50, "Lata", lanches, nenhum));
    }
}
