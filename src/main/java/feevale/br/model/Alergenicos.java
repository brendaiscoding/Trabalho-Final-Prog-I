/*
 * Modelo que representa um alergênico do produto.
 *
 * Exemplo: glúten, lactose, ovos ou castanhas.
 */
package feevale.br.model;

public class Alergenicos {
    private String nome;

    public Alergenicos(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
