/*
 * Modelo que representa uma categoria do cardápio.
 *
 * Exemplo: Bolos, Cupcakes, Croissants ou Sazonais.
 */
package feevale.br.model;

public class Categoria {
    private String nome;

    public Categoria(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
