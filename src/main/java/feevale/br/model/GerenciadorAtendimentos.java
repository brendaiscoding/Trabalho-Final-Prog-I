/*
 * Classe responsável por controlar os atendimentos do sistema.
 *
 * Usa o padrão Singleton para existir apenas uma instância central.
 * Assim, cliente e cozinha acessam a mesma lista de pedidos ativos.
 */
package feevale.br.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorAtendimentos {
    private static GerenciadorAtendimentos instancia;
    
    // Lista dinâmica para a interface gráfica (JavaFX) monitorar a cozinha
    private ObservableList<Atendimento> atendimentosAtivos;
    
    // Requisito: "armazenar os atendimentos em uma lista de atendimentos realizados"
    private List<Atendimento> historicoAtendimentosRealizados;
    
    private int proximoNumero = 1; // Gerador de número único

    private GerenciadorAtendimentos() {
        this.atendimentosAtivos = FXCollections.observableArrayList();
        this.historicoAtendimentosRealizados = new ArrayList<>();
    }

    public static synchronized GerenciadorAtendimentos getInstancia() {
        if (instancia == null) {
            instancia = new GerenciadorAtendimentos();
        }
        return instancia;
    }

    // Gera o número único automaticamente (Requisito do Estabelecimento)
    public synchronized String gerarNumeroUnico() {
        return "ATEND-" + (proximoNumero++);
    }

    public void registrarNovoAtendimento(Atendimento atendimento) {
        atendimentosAtivos.add(atendimento);
    }

    // Requisito: "atualizar o status do atendimento conforme o andamento"
    public void avancarStatus(Atendimento atendimento) {
        String statusAtual = atendimento.getStatus();
        
        if (statusAtual.equals("Em preparo")) {
            atendimento.setStatus("Pronto");
        } else if (statusAtual.equals("Pronto")) {
            atendimento.setStatus("Entregue");
            
            // Se foi entregue, sai da tela da cozinha e vai para o histórico
            atendimentosAtivos.remove(atendimento);
            historicoAtendimentosRealizados.add(atendimento);
        }
    }

    public ObservableList<Atendimento> getAtendimentosAtivos() {
        return atendimentosAtivos;
    }

    public List<Atendimento> getHistoricoAtendimentosRealizados() {
        return historicoAtendimentosRealizados;
    }
}