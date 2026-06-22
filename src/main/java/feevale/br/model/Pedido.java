/*
 * Modelo que representa o pedido do cliente.
 *
 * Herda de Atendimento, guarda os itens com suas quantidades, aplica cupom
 * quando existir e calcula o valor total do pedido.
 */
package feevale.br.model;

import java.util.HashMap;
import java.util.Map;

public class Pedido extends Atendimento {
    private HashMap<Item, Integer> itensQuantidade;
    private Cupom desconto;
    private MetodoPagamento metodoPagamento;

    public Pedido(String numeroAtendimento, Cupom desconto, MetodoPagamento metodoPagamento) {
        super(numeroAtendimento, "Em preparo");
        this.itensQuantidade = new HashMap<>();
        this.desconto = desconto;
        this.metodoPagamento = metodoPagamento;
    }

    public void adicionarItem(Item item, int quantidade) {
        itensQuantidade.put(item, itensQuantidade.getOrDefault(item, 0) + quantidade);
    }

    public void removerItem(Item item) {
        itensQuantidade.remove(item);
    }

    public void removerUmaUnidade(Item item) {
        int q = itensQuantidade.getOrDefault(item, 0);
        if (q <= 1)
            itensQuantidade.remove(item);
        else
            itensQuantidade.put(item, q - 1);
    }

    public void limparItens() {
        itensQuantidade.clear();
    }

    @Override
    public double calcularValorTotal() {
        double total = 0.0;
        for (Map.Entry<Item, Integer> e : itensQuantidade.entrySet())
            total += e.getKey().getPreco() * e.getValue();
        if (desconto != null)
            total -= total * (desconto.getPercentual() / 100.0);
        return total;
    }

    public HashMap<Item, Integer> getItensQuantidade() {
        return itensQuantidade;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }
}
