# <font color="orange">Purrfect Bakery - JavaFX/Maven</font>

 🐱 Projeto de quiosque de autoatendimento para uma padaria temática de gatinhos.

![alt text](<img/purrfect_bakery.png>)

## 🛠️ Tecnologias e Pré-requisitos

Para executar e testar este projeto, você precisará ter instalado em sua máquina:  
- **Java Development Kit (JDK):** Versão 17 ou superior.
- **JavaFX SDK:** Compatível com a versão do JDK utilizada (caso não esteja usando um gerenciador de dependências como Maven/Gradle).
- **IDE recomendada:** Visual Studio Code (com a extensão Extension Pack for Java).

## 🚀 Instruções de Execução
Como o projeto utiliza JavaFX, a execução necessita da configuração dos módulos gráficos. Siga os passos abaixo de acordo com o seu ambiente:

#### Executando via IDE (IntelliJ / Eclipse / VS Code)
**1.**  Abra a pasta raiz do projeto na sua IDE.  

**2.** Certifique-se de que o JDK está configurado corretamente nas propriedades do projeto.

**3.** Caso os caminhos do JavaFX não sejam reconhecidos automaticamente, adicione as seguintes VM Options (Argumentos de Inicialização) na configuração de execução da classe Main ou App:
```
--module-path /caminho/para/o/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
```
*(Substitua /caminho/para/o/javafx-sdk/lib pelo local real onde você descompactou o SDK do JavaFX).*

**4.** Execute o arquivo Main.java ou App.java.

## 🥯 O que o projeto possui

- Tela esquerda: visão do cliente no quiosque.
- Tela direita: sistema interno da padaria/cozinha.
- Comunicação entre as telas: quando o cliente confirma o pagamento, o pedido aparece automaticamente no sistema interno.
- Atualização de status: Em preparo → Pronto → Entregue.
- Carrinho com quantidade, remoção, cupom e resumo do pedido.

### 🧪 Fluxo completo de teste do quiosque:
#### 1. Tela inicial
![alt text](<img/tela_inicial.png>)
#### 2. Escolha entre Comer aqui / Levar
![alt text](<img/comer_aqui_levar.png>)
#### 3. Cardápio e seleção do pedido
![alt text](<img/item_cardapio.png>)
![alt text](<img/cardapio.png>)
#### 4. Visão geral do pedido
![alt text](<img/resumo_pedido.png>)
#### 5. Identificação do cliente
![alt text](<img/identificacao.png>)
#### 6. Método de pagamento
![alt text](<img/metodo_pagamento.png>)
#### 7. Confirmação do pedido
![alt text](<img/conclusao.png>)
#### 8. Cozinha recebe o pedido
![alt text](<img/cozinha_recebe_pedido.png>)
#### 9. Cozinha começa o preparo
![alt text](<img/cozinha_prepara_pedido.png>)
#### 10. Cozinha avisa que está pronto
![alt text](<img/cozinha_termina_pedido.png>)
#### 11. Cozinha entrega o pedido
![alt text](<img/cozinha_entrega_pedido.png>)

## 🎟️ Cupom de teste

Use o cupom `MEOW10` para aplicar 10% de desconto.