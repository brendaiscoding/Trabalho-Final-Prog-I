package feevale.br;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import feevale.br.model.Atendimento;
import feevale.br.model.GerenciadorAtendimentos;

public class CozinhaController {

    @FXML
    private ListView<Atendimento> listViewCozinha;
    @FXML
    private Button btnMudarStatus;

    @FXML
    public void initialize() {
        // Alimenta a lista da tela com os atendimentos ativos
        listViewCozinha.setItems(GerenciadorAtendimentos.getInstancia().getAtendimentosAtivos());

        btnMudarStatus.setOnAction(event -> {
            Atendimento selecionado = listViewCozinha.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                // Avança de "Em preparo" -> "Pronto" -> "Entregue"
                GerenciadorAtendimentos.getInstancia().avancarStatus(selecionado);
                listViewCozinha.refresh(); // Atualiza o texto na tela
            }
        });
    }
}