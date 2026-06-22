/*
 * Classe principal do sistema da Purrfect Bakery.
 *
 * Monta a interface JavaFX do quiosque e do painel interno da cozinha.
 * Centraliza o fluxo visual do cliente: boas-vindas, escolha de consumo, cardápio, carrinho, identificação, pagamento e confirmação do pedido.
 */
package feevale.br;

import feevale.br.model.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.*;

public class App extends Application {
    private final ObservableList<Item> cardapio = FXCollections.observableArrayList();
    private final LinkedHashMap<Item, CartItem> carrinho = new LinkedHashMap<>();
    private final ObservableList<Pedido> pedidosCozinha = FXCollections.observableArrayList();
    private final GerenciadorAtendimentos gerenciador = GerenciadorAtendimentos.getInstancia();

    private StackPane telaCliente;
    private Label totalBarLabel, toastLabel, codigoPedidoLabel;
    private String tipoConsumo = "";
    private String clienteIdentificacao = "";
    private String pagamento = "";
    private double desconto = 0.0;
    private int contadorPedidos = 1;

    private ListView<Pedido> pedidosListView;
    private VBox detalhePedidoBox;

    // Método chamado automaticamente pelo JavaFX ao iniciar o programa
    // Aqui o cardápio é carregado e a janela principal é montada com cliente + cozinha
    @Override
    public void start(Stage stage) {
        carregarCardapio();
        SplitPane root = new SplitPane(criarTelaCliente(), criarTelaCozinha());
        root.setDividerPositions(0.45);
        Scene scene = new Scene(root, 1360, 820);
        stage.setTitle("Purrfect Bakery - Quiosque e Sistema Interno");
        stage.setMinWidth(1180);
        stage.setMinHeight(760);
        stage.setScene(scene);
        stage.show();
    }

    // Cria a área visual do quiosque usada pelo cliente para fazer o pedido
    private StackPane criarTelaCliente() {
        StackPane fundo = new StackPane();
        fundo.setPadding(new Insets(24));
        fundo.setStyle("-fx-background-color: #4A2A1A;");

        VBox corpoQuiosque = new VBox(12);
        corpoQuiosque.setAlignment(Pos.CENTER);
        corpoQuiosque.setMaxWidth(455);
        corpoQuiosque.setPadding(new Insets(18));
        corpoQuiosque.setStyle("-fx-background-color: #FFF8EF; -fx-background-radius: 34; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.36), 22, 0, 0, 7);");

        telaCliente = new StackPane();
        telaCliente.setPrefSize(392, 620);
        telaCliente.setMaxSize(392, 620);
        telaCliente.setStyle("-fx-background-color: linear-gradient(to bottom right, #FFE3C4, #FFF4DF); -fx-background-radius: 25; -fx-border-color: #111111; -fx-border-width: 8; -fx-border-radius: 28;");

        HBox base = new HBox(42);
        base.setAlignment(Pos.CENTER);
        Rectangle leitor = new Rectangle(104, 72, Color.WHITE);
        leitor.setArcWidth(12); leitor.setArcHeight(12); leitor.setStroke(Color.web("#F2C9A8"));
        Label maquininha = new Label("💳");
        maquininha.setFont(Font.font(55));
        base.getChildren().addAll(leitor, maquininha);

        corpoQuiosque.getChildren().addAll(telaCliente, base);
        fundo.getChildren().add(corpoQuiosque);
        mostrarWelcome();
        return fundo;
    }

    // Cria o painel interno da cozinha, onde os pedidos enviados aparecem e podem ser gerenciados
    private BorderPane criarTelaCozinha() {
        BorderPane painel = new BorderPane();
        painel.setPadding(new Insets(24));
        painel.setStyle("-fx-background-color: #FFF7EA;");

        Label titulo = new Label("Sistema interno da Purrfect Bakery");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 28));
        Label desc = new Label("Pedidos enviados pelo quiosque aparecem aqui e podem ter o status atualizado.");
        VBox topo = new VBox(6, titulo, desc, new Separator());
        painel.setTop(topo);

        pedidosListView = new ListView<>(pedidosCozinha);
        pedidosListView.setPrefWidth(360);
        pedidosListView.setCellFactory(list -> new ListCell<>() {
            @Override protected void updateItem(Pedido p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null : p.getNumeroAtendimento() + "  |  " + p.getStatus() + "  |  R$ " + money(p.calcularValorTotal()));
            }
        });
        pedidosListView.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> mostrarDetalhesCozinha(val));

        detalhePedidoBox = new VBox(10);
        detalhePedidoBox.setPadding(new Insets(18));
        detalhePedidoBox.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-border-color: #F2C9A8; -fx-border-radius: 18;");
        mostrarDetalhesCozinha(null);

        HBox centro = new HBox(18, pedidosListView, detalhePedidoBox);
        HBox.setHgrow(detalhePedidoBox, Priority.ALWAYS);
        painel.setCenter(centro);

        Button status = botao("Avançar status", "#E85B1F", "white");
        status.setOnAction(e -> avancarStatus());
        Button historico = botao("Ver entregues", "#FFE3C4", "#7A3A20");
        historico.setOnAction(e -> mostrarHistorico());
        painel.setBottom(new HBox(10, status, historico));
        BorderPane.setMargin(painel.getBottom(), new Insets(16,0,0,0));
        return painel;
    }

    // Mostra a primeira tela do quiosque: logo, mensagem de boas-vindas e botão inicial
    private void mostrarWelcome() {
        VBox box = baseTela();
        box.setAlignment(Pos.CENTER);
        VBox logo = logoGrande();
        Label title = tituloCentral("Bem-vindo à\nPurrfect Bakery", 28);
        Label sub = subtitulo("Delícias fresquinhas feitas com amor.");
        Button start = botaoGrande("Toque para Começar");
        start.setOnAction(e -> mostrarTipoConsumo());
        box.getChildren().addAll(logo, title, sub, start);
        setTela(box);
    }

    // Mostra a tela onde o cliente escolhe entre as opções: Comer Aqui ou Levar para Casa
    private void mostrarTipoConsumo() {
        VBox box = baseTela();
        box.getChildren().addAll(headerPequeno(), tituloCentral("Onde você gostaria de\naproveitar seu pedido?", 24));
        HBox cards = new HBox(14);
        cards.setAlignment(Pos.CENTER);
        VBox dine = selectionCard("/images/dine-in.png", "Comer Aqui", "Aproveite suas delícias em nossa padaria aconchegante.");
        VBox take = selectionCard("/images/take-out.png", "Levar para Casa", "Leve suas delícias para casa.");
        dine.setOnMouseClicked(e -> { tipoConsumo = "Comer Aqui"; dine.setStyle(cardSelected()); take.setStyle(cardStyle()); });
        take.setOnMouseClicked(e -> { tipoConsumo = "Levar para Casa"; take.setStyle(cardSelected()); dine.setStyle(cardStyle()); });
        cards.getChildren().addAll(dine, take);
        Button next = botaoGrande("Continue");
        next.setOnAction(e -> { if (tipoConsumo.isBlank()) tipoConsumo = "Comer Aqui"; mostrarMenu(); });
        box.getChildren().addAll(cards, next);
        setTela(box);
    }

    // Monta a tela do cardápio com categorias, produtos, carrinho e botão de checkout
    private void mostrarMenu() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(12));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #FFF4DF, #FFE3C4); -fx-background-radius: 20;");

        VBox side = new VBox(7);
        side.setPrefWidth(104);
        side.setPadding(new Insets(6));
        String[] cats = {"Popular", "Bolos", "Cupcakes", "Croissants", "Tematizados", "Sazonais"};
        for (String c : cats) side.getChildren().add(new Label("• " + c));
        side.setStyle("-fx-background-color: rgba(255,255,255,0.65); -fx-background-radius: 14; -fx-font-weight: bold; -fx-text-fill: #7A3A20;");
        root.setLeft(side);

        VBox content = new VBox(12);
        content.setPadding(new Insets(0, 0, 72, 10));
        content.getChildren().addAll(headerPequeno(), tituloEsq("Items Populares"), popularCarousel());
        for (String cat : cats) {
            content.getChildren().add(tituloEsq(cat));
            FlowPane flow = new FlowPane(10, 10);
            flow.setPrefWrapLength(235);
            for (Item item : cardapio) if (cat.equals("Popular") || item.getCategoria().getNome().equalsIgnoreCase(cat)) flow.getChildren().add(produtoCard(item));
            content.getChildren().add(flow);
        }
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        root.setCenter(scroll);

        HBox bottom = new HBox(8);
        bottom.setAlignment(Pos.CENTER_LEFT);
        bottom.setPadding(new Insets(9));
        bottom.setStyle("-fx-background-color: rgba(255,255,255,0.92); -fx-background-radius: 16;");
        totalBarLabel = new Label("Total: $" + money(totalCarrinho()));
        totalBarLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        Button checkout = botao("Finalizar Pedido", "#E85B1F", "white"); checkout.setOnAction(e -> mostrarPedido());
        Button reset = botao("Limpar", "#FFE3C4", "#7A3A20"); reset.setOnAction(e -> { carrinho.clear(); atualizarTotalBar(); });
        bottom.getChildren().addAll(totalBarLabel, spacer, checkout, reset);
        root.setBottom(bottom);

        toastLabel = new Label();
        toastLabel.setStyle("-fx-background-color: #A94418; -fx-text-fill: white; -fx-padding: 8 14; -fx-background-radius: 18; -fx-font-weight: bold;");
        toastLabel.setVisible(false);
        StackPane stack = new StackPane(root, toastLabel);
        StackPane.setAlignment(toastLabel, Pos.TOP_CENTER);
        StackPane.setMargin(toastLabel, new Insets(12));
        setTela(stack);
    }

    // Mostra o resumo do pedido atual, com itens, quantidades, cupom e total
    private void mostrarPedido() {
        VBox box = baseTela();
        box.setSpacing(10);
        HBox header = new HBox(8, new Label("🐾"), tituloEsq("Meu Pedido")); header.setAlignment(Pos.CENTER_LEFT);
        VBox lista = new VBox(8);
        for (CartItem ci : carrinho.values()) lista.getChildren().add(linhaCarrinho(ci, lista));
        TextField cupom = new TextField(); cupom.setPromptText("Cupom de Desconto:"); cupom.setPrefWidth(160);
        Button aplicar = botao("Aplicar", "#F7B267", "#5C2B17");
        aplicar.setOnAction(e -> { if (cupom.getText().equalsIgnoreCase("MEOW10")) desconto = totalCarrinho() * 0.10; mostrarPedido(); });
        VBox resumo = resumoPedido();
        Button proceed = botaoGrande("Finalizar Pedido"); proceed.setOnAction(e -> mostrarIdentificacao());
        Button voltar = botao("Voltar", "#FFE3C4", "#7A3A20"); voltar.setOnAction(e -> mostrarMenu());
        box.getChildren().addAll(header, new ScrollPane(lista), tituloEsq("Cupom de Desconto"), new HBox(8, cupom, aplicar), resumo, proceed, voltar);
        setTela(box);
    }

    // Coleta o nome do cliente ou gera um código para identificar o pedido
    private void mostrarIdentificacao() {
        VBox box = baseTela(); box.setAlignment(Pos.CENTER);
        TextField nome = new TextField(); nome.setPromptText("Nome do Cliente"); nome.setMaxWidth(250);
        codigoPedidoLabel = new Label(""); codigoPedidoLabel.setFont(Font.font("System", FontWeight.BOLD, 22));
        Button gerar = botao("Gerar Código", "#F7B267", "#5C2B17");
        gerar.setOnAction(e -> codigoPedidoLabel.setText(gerarCodigo()));
        Button clear = botao("Limpar", "#FFE3C4", "#7A3A20"); clear.setOnAction(e -> { nome.clear(); codigoPedidoLabel.setText(""); });
        Button confirm = botaoGrande("Confirmar");
        confirm.setOnAction(e -> { clienteIdentificacao = !nome.getText().isBlank() ? nome.getText() : codigoPedidoLabel.getText(); if (clienteIdentificacao.isBlank()) clienteIdentificacao = gerarCodigo(); mostrarPagamento(); });
        box.getChildren().addAll(headerPequeno(), tituloCentral("Qual o seu nome?", 26), nome, new Label("OU"), gerar, codigoPedidoLabel, subtitulo("Este nome/código será usado ao chamar seu pedido."), new HBox(10, clear, confirm));
        setTela(box);
    }

    // Mostra as opções de pagamento: PIX ou cartão
    private void mostrarPagamento() {
        VBox box = baseTela(); box.setAlignment(Pos.CENTER);
        ImageView cat = imagem("/images/cartao-pix.png", 230, 130);
        ToggleGroup group = new ToggleGroup();
        RadioButton pix = new RadioButton("PIX - Pagamento instantâneo"); pix.setToggleGroup(group);
        RadioButton card = new RadioButton("Cartão - Crédito ou Débito"); card.setToggleGroup(group);
        ComboBox<String> tipoCartao = new ComboBox<>(FXCollections.observableArrayList("Cartão de Crédito", "Cartão de Débito"));
        tipoCartao.setPromptText("Selecione o tipo de cartão"); tipoCartao.setVisible(false);
        card.selectedProperty().addListener((o, old, val) -> tipoCartao.setVisible(val));
        Button pay = botaoGrande("Confirmar Pagamento");
        pay.setOnAction(e -> { pagamento = pix.isSelected() ? "PIX" : (tipoCartao.getValue() == null ? "Card" : tipoCartao.getValue()); confirmarPedidoFinal(); });
        box.getChildren().addAll(headerPequeno(), tituloCentral("Escolha o Método de Pagamento", 25), pix, card, tipoCartao, cat, pay);
        setTela(box);
    }

    // Tela final exibida depois do pagamento, com mensagem deagradecimento e dados do pedido
    private void mostrarConfirmacao(String numero) {
        VBox box = baseTela(); box.setAlignment(Pos.CENTER); box.setSpacing(10);
        ImageView happy = imagem("/images/logo-croissant.jpg", 220, 220);
        Label qr = new Label("▣ ▢ ▣\n▢ ▣ ▢\n▣ ▢ ▣"); qr.setAlignment(Pos.CENTER); qr.setFont(Font.font("Monospaced", 28));
        Button novo = botaoGrande("Novo Pedido"); novo.setOnAction(e -> { carrinho.clear(); desconto = 0; tipoConsumo = ""; clienteIdentificacao = ""; mostrarWelcome(); });
        box.getChildren().addAll(headerPequeno(), happy, tituloCentral("Obrigado pelo seu pedido!", 25), subtitulo("Tempo de espera estimado: 15 minutos"), textoWrap("Por favor, espere até que seu nome ou código do pedido seja \nexibido na tela ou anunciado no balcão."), new Label("Seu pedido está no nome/código de:"), tituloCentral(clienteIdentificacao, 24), tituloEsq("Acompanhe seu pedido:"), subtitulo("Leia o QR Code para acompanhar o status do seu pedido \nem tempo real."), qr, novo);
        setTela(box);
    }

    // Transforma o carrinho atual em um Pedido e envia para a lista da cozinha
    private void confirmarPedidoFinal() {
        if (carrinho.isEmpty()) { mostrarMenu(); return; }
        Pedido pedido = new Pedido("PB-" + String.format("%03d", contadorPedidos++), null, new Pix("purrfect-bakery-pix"));
        carrinho.values().forEach(ci -> pedido.adicionarItem(ci.item, ci.quantidade));
        pedidosCozinha.add(pedido);
        gerenciador.registrarNovoAtendimento(pedido);
        pedidosListView.refresh();
        mostrarConfirmacao(pedido.getNumeroAtendimento());
    }

    // Carrega uma imagem dos resources e ajusta seu tamanho para exibição na interface
    private ImageView imagem(String caminho, double largura, double altura) {
        Image image = new Image(getClass().getResourceAsStream(caminho));

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(largura);
        imageView.setFitHeight(altura);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        return imageView;
    }

    // Cria o card visual de um produto no cardápio
    private Region produtoCard(Item item) {
        VBox card = new VBox(4); card.setPadding(new Insets(8)); card.setPrefSize(112, 142); card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: rgba(255,255,255,0.86); -fx-background-radius: 16; -fx-border-color: white; -fx-border-radius: 16;");
        ImageView img = imagem(item.getIcone(), 86, 58);
        Label nome = new Label(item.getNome()); nome.setFont(Font.font("System", FontWeight.BOLD, 11)); nome.setWrapText(true); nome.setAlignment(Pos.CENTER);
        Label desc = new Label(item.getDescricao()); desc.setWrapText(true); desc.setStyle("-fx-font-size: 9px;");
        Label preco = new Label("$" + money(item.getPreco())); preco.setTextFill(Color.web("#E85B1F"));
        Button add = botao("Add", "#F7B267", "#5C2B17"); add.setOnAction(e -> abrirModalProduto(item));
        card.getChildren().addAll(img, nome, preco, desc, add);
        return card;
    }

    // Abre uma janela modal com detalhes do produto e opções de customização
    private void abrirModalProduto(Item item) {
        Stage modal = new Stage(); modal.initModality(Modality.APPLICATION_MODAL); modal.setTitle(item.getNome());
        VBox box = new VBox(10); box.setPadding(new Insets(20)); box.setAlignment(Pos.CENTER); box.setStyle("-fx-background-color: #FFF7EA;");
        ImageView img = imagem(item.getIcone(), 230, 170);
        Label desc = textoWrap(item.getDescricao() + "\nIngredientes: farinha, açúcar, manteiga, amor e magia de gato.\nAlergênicos: " + item.getEspecificacoes());
        String[] opts = {"Sem Glúten", "Sem Lactose", "Vegano", "Leite Integral", "Leite de Aveia", "Leite de Amêndoa", "Leite de Soja", "Chocolate Extra", "Creme"};
        FlowPane checks = new FlowPane(8, 8); List<CheckBox> checkBoxes = new ArrayList<>();
        for (String o : opts) { CheckBox cb = new CheckBox(o); checkBoxes.add(cb); checks.getChildren().add(cb); }
        Button add = botaoGrande("Adicionar ao Carrinho");
        add.setOnAction(e -> { String custom = String.join(", ", checkBoxes.stream().filter(CheckBox::isSelected).map(CheckBox::getText).toList()); adicionarAoCarrinho(item, custom); modal.close(); });
        box.getChildren().addAll(tituloCentral(item.getNome(), 23), img, desc, checks, add);
        modal.setScene(new Scene(box, 420, 540)); modal.showAndWait();
    }

    // Adiciona o produto selecionado ao carrinho ou aumenta sua quantidade
    private void adicionarAoCarrinho(Item item, String custom) {
        CartItem ci = carrinho.computeIfAbsent(item, k -> new CartItem(item));
        ci.quantidade++; if (!custom.isBlank()) ci.customizacoes = custom;
        atualizarTotalBar();
        if (toastLabel != null) { toastLabel.setText("✓ Produto adicionado com sucesso!"); toastLabel.setVisible(true); }
    }

    // Cria uma linha visual do carrinho com imagem, nome, customizações e botões +/-
    private HBox linhaCarrinho(CartItem ci, VBox lista) {
        ImageView icon = imagem(ci.item.getIcone(), 46, 38);
        VBox txt = new VBox(2, new Label(ci.item.getNome()), new Label(ci.customizacoes.isBlank() ? "Sem customizações" : ci.customizacoes), new Label("$" + money(ci.item.getPreco())));
        Button menos = botao(ci.quantidade == 1 ? "X" : "-", "#FFE3C4", "#7A3A20");
        Label qtd = new Label(String.valueOf(ci.quantidade));
        Button mais = botao("+", "#F7B267", "#5C2B17");
        menos.setOnAction(e -> { if (ci.quantidade <= 1) carrinho.remove(ci.item); else ci.quantidade--; mostrarPedido(); });
        mais.setOnAction(e -> { ci.quantidade++; mostrarPedido(); });
        HBox h = new HBox(8, icon, txt, menos, qtd, mais); h.setAlignment(Pos.CENTER_LEFT); h.setPadding(new Insets(6)); h.setStyle("-fx-background-color: rgba(255,255,255,0.75); -fx-background-radius: 12;");
        return h;
    }

    // Calcula e mostra subtotal, desconto e total do pedido
    private VBox resumoPedido() {
        double subtotal = totalCarrinho();
        double total = Math.max(0, subtotal - desconto);
        return new VBox(4, new Label("Subtotal: $" + money(subtotal)), new Label("Descontos: -$" + money(desconto)), new Label("Total: $" + money(total)));
    }

    // Cria uma faixa com alguns produtos populares no topo do cardápio
    private HBox popularCarousel() {
        HBox h = new HBox(8); h.setAlignment(Pos.CENTER_LEFT);
        cardapio.stream().limit(5).forEach(i -> {
            HBox item = new HBox(6, imagem(i.getIcone(), 38, 30), new Label(i.getNome()));
            item.setAlignment(Pos.CENTER_LEFT);
            item.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-padding: 8; -fx-font-weight: bold;");
            h.getChildren().add(item);
        });
        return h;
    }

    // Cria o card de seleção usado para Comer Aqui e Levar para Casa
    private VBox selectionCard(String imagePath, String title, String desc) {
        VBox c = new VBox(8);
        c.setAlignment(Pos.CENTER);
        c.setPrefSize(158, 175);
        c.setPadding(new Insets(10));
        c.setStyle(cardStyle());

        ImageView icon = imagem(imagePath, 92, 92);
        Label t = new Label(title);
        t.setFont(Font.font("System", FontWeight.BOLD, 18));

        Label d = textoWrap(desc);
        d.setAlignment(Pos.CENTER);

        c.getChildren().addAll(icon, t, d);
        return c;
    }

    // Avança o status do pedido selecionado na cozinha
    private void avancarStatus() {
        Pedido p = pedidosListView.getSelectionModel().getSelectedItem();
        if (p == null) return;
        gerenciador.avancarStatus(p);
        if (p.getStatus().equalsIgnoreCase("Entregue")) pedidosCozinha.remove(p);
        pedidosListView.refresh(); mostrarDetalhesCozinha(p);
    }

    // Mostra os detalhes do pedido selecionado no painel da cozinha
    private void mostrarDetalhesCozinha(Pedido p) {
        detalhePedidoBox.getChildren().clear();
        if (p == null) { detalhePedidoBox.getChildren().add(new Label("Selecione um pedido para ver os detalhes.")); return; }
        Label num = tituloEsq(p.getNumeroAtendimento());
        Label status = new Label("Status: " + p.getStatus()); status.setStyle("-fx-background-color: #FFE3C4; -fx-background-radius: 12; -fx-padding: 8 12; -fx-font-weight: bold;");
        detalhePedidoBox.getChildren().addAll(num, status, new Separator());
        for (Map.Entry<Item, Integer> e : p.getItensQuantidade().entrySet()) detalhePedidoBox.getChildren().add(new Label(e.getValue() + "x " + e.getKey().getNome() + " - " + e.getKey().getDescricao()));
        detalhePedidoBox.getChildren().add(new Separator());
        Label total = new Label("Total: R$ " + money(p.calcularValorTotal())); total.setFont(Font.font("System", FontWeight.BOLD, 18)); detalhePedidoBox.getChildren().add(total);
    }

    // Exibe em um alerta os pedidos que já foram entregues
    private void mostrarHistorico() {
        StringBuilder sb = new StringBuilder();
        gerenciador.getHistoricoAtendimentosRealizados().forEach(a -> sb.append(a.getNumeroAtendimento()).append(" - R$ ").append(money(a.calcularValorTotal())).append("\n"));
        new Alert(Alert.AlertType.INFORMATION, sb.length() == 0 ? "Nenhum pedido entregue ainda." : sb.toString()).showAndWait();
    }

    // Métodos utilitários abaixo criam componentes reutilizáveis da interface, formatam valores e geram códigos aleatórios
    private VBox baseTela() { VBox v = new VBox(14); v.setPadding(new Insets(20)); v.setAlignment(Pos.TOP_CENTER); v.setStyle("-fx-background-color: linear-gradient(to bottom right, #FFF4DF, #FFE3C4); -fx-background-radius: 20;"); return v; }
    private void setTela(javafx.scene.Node node) { telaCliente.getChildren().setAll(node); }
    private VBox logoGrande() {
        ImageView logo = imagem("/images/logo.jpg", 170, 170);

        Label nome = new Label("Purrfect Bakery");
        nome.setAlignment(Pos.CENTER);
        nome.setFont(Font.font("System", FontWeight.BOLD, 30));
        nome.setTextFill(Color.web("#A94418"));

        VBox box = new VBox(8, logo, nome);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private HBox headerPequeno() {
        ImageView logo = imagem("/images/logo.jpg", 34, 34);
        Label l = new Label("Purrfect Bakery");
        l.setFont(Font.font("System", FontWeight.BOLD, 18));
        l.setTextFill(Color.web("#A94418"));

        HBox header = new HBox(8, logo, l);
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }
    private Label tituloCentral(String s, int size) { Label l = new Label(s); l.setAlignment(Pos.CENTER); l.setFont(Font.font("System", FontWeight.BOLD, size)); l.setTextFill(Color.web("#A94418")); return l; }
    private Label tituloEsq(String s) { Label l = new Label(s); l.setFont(Font.font("System", FontWeight.BOLD, 18)); l.setTextFill(Color.web("#A94418")); return l; }
    private Label subtitulo(String s) { Label l = textoWrap(s); l.setTextFill(Color.web("#8A5A3C")); l.setAlignment(Pos.CENTER); return l; }
    private Label textoWrap(String s) { Label l = new Label(s); l.setWrapText(true); return l; }
    private Button botaoGrande(String t) { Button b = botao(t, "#E85B1F", "white"); b.setMinHeight(45); b.setMaxWidth(Double.MAX_VALUE); return b; }
    private Button botao(String texto, String fundo, String cor) { Button b = new Button(texto); b.setStyle("-fx-background-color: " + fundo + "; -fx-text-fill: " + cor + "; -fx-background-radius: 16; -fx-padding: 8 12; -fx-font-weight: bold;"); return b; }
    private String cardStyle() { return "-fx-background-color: rgba(255,255,255,0.78); -fx-background-radius: 20; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 20;"; }
    private String cardSelected() { return "-fx-background-color: #FFE3C4; -fx-background-radius: 20; -fx-border-color: #E85B1F; -fx-border-width: 3; -fx-border-radius: 20;"; }
    private double totalCarrinho() { return carrinho.values().stream().mapToDouble(ci -> ci.item.getPreco() * ci.quantidade).sum(); }
    private void atualizarTotalBar() { if (totalBarLabel != null) totalBarLabel.setText("Total: $" + money(totalCarrinho())); }
    private String money(double v) { return String.format(Locale.US, "%.2f", v); }
    private String gerarCodigo() { return "ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(new Random().nextInt(26)) + String.format("%04d", new Random().nextInt(10000)); }

    // Cadastra os produtos exibidos no cardápio do quiosque
    private void carregarCardapio() {
        Categoria bolos = new Categoria("Bolos");
        Categoria cupcakes = new Categoria("Cupcakes");
        Categoria croissants = new Categoria("Croissants");
        Categoria gatinhos = new Categoria("Tematizados");
        Categoria sazonais = new Categoria("Sazonais");

        Alergenicos gluten = new Alergenicos("gluten");
        Alergenicos lactose = new Alergenicos("lactose");

        // Bolos
        cardapio.add(new Item("Torta de Limão", 12.90, "Fatia", bolos, new Alergenicos[]{gluten, lactose}, "/images/products/tortadelimao.jpg", "Torta cítrica com creme de limão e cobertura delicada."));
        cardapio.add(new Item("Torta Red Velvet", 14.90, "Fatia", bolos, new Alergenicos[]{gluten, lactose}, "/images/products/tortaredvelvet.jpg", "Massa red velvet com recheio cremoso."));
        cardapio.add(new Item("Cheesecake", 13.90, "Fatia", bolos, new Alergenicos[]{gluten, lactose}, "/images/products/cheesecake.jpg", "Cheesecake suave com base crocante."));
        cardapio.add(new Item("Bolo Floresta Negra", 15.90, "Fatia", bolos, new Alergenicos[]{gluten, lactose}, "/images/products/tortaflorestanegra.jpg", "Bolo de chocolate com cerejas e chantilly."));
        cardapio.add(new Item("Tiramisu", 16.90, "Fatia", bolos, new Alergenicos[]{gluten, lactose}, "/images/products/tiramisu.jpg", "Sobremesa italiana com café, creme e cacau."));

        // Cupcakes
        cardapio.add(new Item("Cupcake de Baunilha", 7.90, "Unidade", cupcakes, new Alergenicos[]{gluten, lactose}, "/images/products/vanillacupcake.jpg", "Cupcake de baunilha com cobertura cremosa."));
        cardapio.add(new Item("Choco-chip Cupcake", 8.90, "Unidade", cupcakes, new Alergenicos[]{gluten, lactose}, "/images/products/chocochipcupcake.jpg", "Cupcake com gotas de chocolate."));
        cardapio.add(new Item("Oreo Cupcake", 9.90, "Unidade", cupcakes, new Alergenicos[]{gluten, lactose}, "/images/products/oreocupcake.jpg", "Cupcake com pedaços de Oreo e creme."));
        cardapio.add(new Item("Red Velvet Cupcake", 9.90, "Unidade", cupcakes, new Alergenicos[]{gluten, lactose}, "/images/products/redvelvetcupcake.jpg", "Mini versão do clássico red velvet."));
        cardapio.add(new Item("Berry Cupcake", 9.50, "Unidade", cupcakes, new Alergenicos[]{gluten, lactose}, "/images/products/berrycupcake.jpg", "Cupcake de morango com cobertura frutada."));
        cardapio.add(new Item("Blueberry Cupcake", 9.50, "Unidade", cupcakes, new Alergenicos[]{gluten, lactose}, "/images/products/blueberrycupcake.jpg", "Cupcake com blueberry e creme delicado."));

        // Croissants
        cardapio.add(new Item("Croissant de Pêssego", 10.90, "Unidade", croissants, new Alergenicos[]{gluten, lactose}, "/images/products/croissantpessego.jpg", "Croissant folhado com recheio de pêssego."));
        cardapio.add(new Item("Croissant de Morango", 10.90, "Unidade", croissants, new Alergenicos[]{gluten, lactose}, "/images/products/croissantmorango.jpg", "Croissant com recheio doce de morango."));
        cardapio.add(new Item("Croissant de Chocolate", 11.90, "Unidade", croissants, new Alergenicos[]{gluten, lactose}, "/images/products/croissantchocolatepreto.jpg", "Croissant amanteigado com chocolate."));

        // Gatinhos
        cardapio.add(new Item("Pão de Gatinho", 8.90, "Unidade", gatinhos, new Alergenicos[]{gluten}, "/images/products/catbread.jpg", "Pão artesanal em formato de gatinho."));
        cardapio.add(new Item("Pudim de Gatinho", 9.90, "Unidade", gatinhos, new Alergenicos[]{lactose}, "/images/products/catpudim.jpg", "Pudim fofinho decorado com carinha de gato."));
        cardapio.add(new Item("Cookies de Gatinho", 7.90, "Unidade", gatinhos, new Alergenicos[]{gluten}, "/images/products/catcookies.jpg", "Cookies decorados com tema de gatinho."));
        cardapio.add(new Item("Torta de Maçã de Gatinho", 12.90, "Fatia", gatinhos, new Alergenicos[]{gluten, lactose}, "/images/products/catapplepie.jpg", "Torta de maçã decorada no estilo Purrfect Bakery."));

        // Sazonais - LGBT (mês do orgulho) e Festa Junina
        cardapio.add(new Item("Pride Cupcake", 9.90, "Unidade", sazonais, new Alergenicos[]{gluten, lactose}, "/images/products/pridecupcakes.jpg", "Cupcake colorido inspirado no orgulho LGBT."));
        cardapio.add(new Item("Donut Arco-Íris", 8.90, "Unidade", sazonais, new Alergenicos[]{gluten, lactose}, "/images/products/rainbowdonuts.jpg", "Donut arco-íris com cobertura colorida."));
        cardapio.add(new Item("Bolo Pride", 16.90, "Fatia", sazonais, new Alergenicos[]{gluten, lactose}, "/images/products/pridecake.jpg", "Bolo colorido especial da temporada Pride."));
        cardapio.add(new Item("Quentão", 6.90, "Copo", sazonais, new Alergenicos[0], "/images/products/quentao.jpg", "Bebida quente típica de Festa Junina."));
        cardapio.add(new Item("Maçã do Amor", 7.90, "Unidade", sazonais, new Alergenicos[0], "/images/products/macadoamor.jpg", "Maçã caramelizada tradicional de Festa Junina."));
        cardapio.add(new Item("Pamonha", 8.90, "Unidade", sazonais, new Alergenicos[]{lactose}, "/images/products/pamonha.jpg", "Pamonha cremosa típica das festas juninas."));
        cardapio.add(new Item("Bolo de Milho", 9.90, "Fatia", sazonais, new Alergenicos[]{gluten, lactose}, "/images/products/bolodemilho.jpg", "Bolo de milho fofinho com sabor caseiro."));
    }

// Classe auxiliar usada apenas no carrinho visual para guardar quantidade e customizações.
private static class CartItem { Item item; int quantidade = 0; String customizacoes = ""; CartItem(Item item) { this.item = item; } }
    public static void main(String[] args) { launch(); }
}
