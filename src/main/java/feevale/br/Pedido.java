package feevale.br;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.HashMap;
import java.util.Map;

public class Pedido {
    private String codigo;
    // MUDANÇA: Agora mapeia o objeto Item diretamente para a quantidade
    private HashMap<Item, Integer> itensQuantidade;
    private StringProperty status = new SimpleStringProperty();
    private Cupom desconto;
    private MetodoPagamento metodoPagamento;

    public Pedido(String codigo, HashMap<Item, Integer> itensQuantidade, String status, Cupom desconto,
            MetodoPagamento metodoPagamento) {
        this.codigo = codigo;
        this.itensQuantidade = itensQuantidade;
        setStatus(status);
        this.desconto = desconto;
        this.metodoPagamento = metodoPagamento;
    }

    public double valorTotal(boolean aplicarDesconto) {
        double total = 0.0;

        for (Map.Entry<Item, Integer> entrada : itensQuantidade.entrySet()) {
            Item item = entrada.getKey();
            int quantidade = entrada.getValue();
            total += item.getPreco() * quantidade;
        }

        if (aplicarDesconto && desconto != null) {
            total -= total * (desconto.getPercentual() / 100.0);
        }

        return total;
    }

    public String getCodigo() {
        return codigo;
    }

    public HashMap<Item, Integer> getItensQuantidade() {
        return itensQuantidade;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String novoStatus) {
        this.status.set(novoStatus);
    }

    public StringProperty statusProperty() {
        return status;
    }

    public Cupom getDesconto() {
        return desconto;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }
}
