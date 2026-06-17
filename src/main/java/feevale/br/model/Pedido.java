package feevale.br.model;

import java.util.HashMap;
import java.util.Map;

public class Pedido extends Atendimento {
    private HashMap<Item, Integer> itensQuantidade; 
    private Cupom desconto;
    private MetodoPagamento metodoPagamento;

    public Pedido(String numeroAtendimento, Cupom desconto, MetodoPagamento metodoPagamento) {
        super(numeroAtendimento, "Em preparo"); // Status inicial exigido pelo requisito
        this.itensQuantidade = new HashMap<>();
        this.desconto = desconto;
        this.metodoPagamento = metodoPagamento;
    }

    // Funcionalidades do Cliente: Adicionar e Remover itens
    public void adicionarItem(Item item, int quantidade) {
        this.itensQuantidade.put(item, this.itensQuantidade.getOrDefault(item, 0) + quantidade);
    }

    public void removerItem(Item item) {
        this.itensQuantidade.remove(item);
    }

    @Override
    public double calcularValorTotal() {
        double total = 0.0;
        for (Map.Entry<Item, Integer> entrada : itensQuantidade.entrySet()) {
            total += entrada.getKey().getPreco() * entrada.getValue();
        }
        if (desconto != null) {
            total -= total * (desconto.getPercentual() / 100.0);
        }
        return total;
    }

    public HashMap<Item, Integer> getItensQuantidade() { return itensQuantidade; }
}