import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**teste */

public class Comercio {
    /** Para inclusão de novos produtos no vetor */
    static final int MAX_NOVOS_PRODUTOS = 10;

    /** Para inclusão de novos produtos no vetor */
    static final int MAX_NOVOS_PEDIDOS = 10;

    /** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoDados;
    
    /** Scanner para leitura do teclado */
    static Scanner teclado;

    /** Vetor de produtos cadastrados. Sempre terá espaço para 10 novos produtos a cada execução */
    static Produto[] produtosCadastrados;

    /** Vetor de pedidos cadastrados */
    static Pedido[] pedidoCadastrados;

    /** Quantidade produtos cadastrados atualmente no vetor */
    static int quantosProdutos;

    /** Quantidade pedidos cadastrados atualmente no vetor */
    static int quantosPedidos;

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa(){
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho(){
        System.out.println("AEDII COMÉRCIO DE COISINHAS");
        System.out.println("===========================");
    }

    /** Imprime o menu principal, lê a opção do usuário e a retorna (int).
     * Perceba que poderia haver uma melhor modularização com a criação de uma classe Menu.
     * @return Um inteiro com a opção do usuário.
    */
    static int menu(){
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e listar um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("4 - Fazer um pedido ");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }

    /**
     * Lê os dados de um arquivo texto e retorna um vetor de produtos. Arquivo no formato
     * N  (quantiade de produtos) <br/>
     * tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
     * Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em caso de problemas com o arquivo.
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Um vetor com os produtos carregados, ou vazio em caso de problemas de leitura.
     */
    static Produto[] lerProdutos(String nomeArquivoDados) {

        try (Scanner arqDados = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"))) {

            quantosProdutos = Integer.parseInt(arqDados.nextLine());
            Produto[] vetorProdutos = new Produto[quantosProdutos + MAX_NOVOS_PRODUTOS];

            for(int i = 0; i < quantosProdutos; i++) {
                String linha = arqDados.nextLine();
                vetorProdutos[i] = Produto.criarDoTexto(linha);
            }

            return vetorProdutos;

        } catch (IOException e) {
            System.out.println("Arquivo não encontrado. Criando novo cadastro.");
            quantosProdutos = 0;
            return new Produto[MAX_NOVOS_PRODUTOS];
        }
    }

    /** Lista todos os produtos cadastrados, numerados, um por linha */
    static void listarTodosOsProdutos(){
        cabecalho();
        System.out.println("\nPRODUTOS CADASTRADOS:");
        for (int i = 0; i < produtosCadastrados.length; i++) {
            if(produtosCadastrados[i]!=null)
                System.out.println(String.format("%02d - %s", (i+1),produtosCadastrados[i].toString()));
        }
    }

    /** Localiza um produto no vetor de cadastrados, a partir do nome, e imprime seus dados. 
     *  A busca não é sensível ao caso.  Em caso de não encontrar o produto, imprime mensagem padrão */
    static void localizarProdutos(){
        cabecalho();
        System.out.print("Digite o nome do produto que deseja buscar: ");
        String busca = teclado.nextLine().toLowerCase();

        int produtosFiltrados = 0;

        for(int i = 0; i < quantosProdutos; i++){
            if(produtosCadastrados[i] != null &&
                produtosCadastrados[i].toString().toLowerCase().contains(busca)) {

                System.out.println(String.format("%02d - %s", 
                        (i+1), produtosCadastrados[i].toString()));
                produtosFiltrados++;
            }
        }

        if(produtosFiltrados == 0){
            System.out.println("Este produto não esta cadastrado em nosso sistema.");
        }
    }

    /**
     * Rotina de cadastro de um novo produto: pergunta ao usuário o tipo do produto, lê os dados correspondentes,
     * cria o objeto adequado de acordo com o tipo, inclui no vetor. Este método pode ser feito com um nível muito 
     * melhor de modularização. As diversas fases da lógica poderiam ser encapsuladas em outros métodos. 
     * Uma sugestão de melhoria mais significativa poderia ser o uso de padrão Factory Method para criação dos objetos.
     */
    static void cadastrarProduto(){
        cabecalho();

        if(quantosProdutos >= produtosCadastrados.length){
            System.out.println("O l  imite de prdotuos cadastrados foi atingido.");
            return;
        }

        System.out.println("Tipo do produto:");
        System.out.println("1 - Produto não perecível");
        System.out.println("2 - Produto perecível");
        System.out.print("Digite qual tipo de produto você deseja cadastrar: ");
        int tipo = Integer.parseInt(teclado.nextLine());

        System.out.print("Descrição: ");
        String descricao = teclado.nextLine();

        System.out.print("Preço de custo: ");
        double preco = Double.parseDouble(teclado.nextLine());

        System.out.print("Margem de lucro: ");
        double margem = Double.parseDouble(teclado.nextLine());

        Produto novoProduto;

        switch(tipo){
            case 1:
                novoProduto = new ProdutoNaoPerecivel(descricao, preco, margem);
                break;

            case 2:
                System.out.print("Data de validade (dd/MM/yyyy): ");
                String dataStr = teclado.nextLine();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate validade = LocalDate.parse(dataStr, formatter);

                novoProduto = new ProdutoPerecivel(descricao, preco, margem, validade);
                break;

            default:
                System.out.println("Tipo inválido de produto inválido. Por favor, digite 1 ou 2");
                return; 
        }

        produtosCadastrados[quantosProdutos] = novoProduto;
        quantosProdutos++;

        System.out.println("O produto cadastrado com sucesso!");
    }
    /**
     * Salva os dados dos produtos cadastrados no arquivo csv informado. Sobrescreve todo o conteúdo do arquivo.
     * @param nomeArquivo Nome do arquivo a ser gravado.
     */
    public static void salvarProdutos(String nomeArquivo){

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {

            writer.write(String.valueOf(quantosProdutos));
            writer.newLine();
            
            for(int i = 0; i < quantosProdutos; i++){
                if(produtosCadastrados[i] != null){
                    writer.write(produtosCadastrados[i].gerarDadosTexto());
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.out.println("Erro ao salvar produtos");
        }
    }

    
    public static void criarNovoPedido(){
        cabecalho();
        System.out.println("Produtos cadastrados");
        listarTodosOsProdutos();
        System.out.print("Digite qual produto você deseja adicionar ao seu pedido: ");
        String busca = teclado.nextLine().toLowerCase();
        System.out.print("Digite a quantidade do produto que você deseja adicionar ao seu pedido: ");
        int quantidade = Integer.parseInt(teclado.nextLine());



        Produto produtoParaInserir;
        for(int i = 0; i < quantosProdutos; i++){
            if(produtosCadastrados[i] != null &&
                produtosCadastrados[i].toString().toLowerCase().contains(busca)) {
                produtoParaInserir = produtosCadastrados[i];
            }
        }

        if(produtoParaInserir.quantidadeEmEstoque < quantidade) {
            System.out.print("Infelizmente não temos quantidade suficiente para esse produto em nosso estoque :(");
        } else {
            incluirProdutoAoPedido(produtoParaInserir, quantidade);
            System.out.print("O produto foi inserido no seu pedido com sucesso :D");
        }
        
    }

    public void incluirProdutoAoPedido(Produto novoProduto, int quantidade) {
        Pedido pedido = null;
        LocalDate dataNovoPedido = LocalDate.now();

        if(quantosPedidos >= pedidosCadastrados.length){
            System.out.println("O limite de pedidos cadastrados foi atingido.");
            return;
        } else if(produtoJaNoPedido(novoProduto, dataNovoPedido)) {
            System.out.println("Produto já inserido.");
            System.out.println("Você deseja alterar quantos produtos tem no pedido? (1 - SIM / 2 - NAO)");
            int escolhaUsuario = Integer.parseInt(teclado.nextLine());
            switch() {
                case 1:
                   System.out.println("Digite a quantidade:"); 
                   int novaQuantidade = Integer.parseInt(teclado.nextLine());
                   if(produtoParaInserir.quantidadeEmEstoque < quantidade) {
                    System.out.print("Infelizmente não temos quantidade suficiente para esse produto em nosso estoque :(");
                   } else {
                        pedido.setQuantidade(novaQuantidade);
                   }
                   break;
                case 2:
                    System.out.println("Operação cancelada"); 
                default:
                    return;
            }
            return;
        } else {
            Double valor = novoProduto.valorDeVenda;
            ItemDePedido novoItem = new ItemDePedido(produto, quantidade, valor);
            System.out.print("Qual a forma de pagamento");
            String formaDePagamento = teclado.nextLine();
            pedido = new Pedido(dataPedido, formaDePagamento);
            pedido.incluirProduto(novoItem);
            produto.reduzirEstoque(quantidade);
            System.out.println("Produto inserido no pedido com sucesso");
        }


    }

    public boolean produtoJaNoPedido(Produto produto, LocalDate dataPedido) {
        boolean produtoEncontrado = false;
        for(int i = 0; i < quantosPedidos; i++){
            if(pedidosCadastrados[i] != null && pedidosCadastrados[i].dataPedido().equals(dataPedido)) {
                if(pedidoCadastrados[i].toString().contains(produto.descricao)) {
                    produtoEncontrado = true;
                }
            }
        }
        return produtoEncontrado;
    }

    public static void main(String[] args) throws Exception {
        teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
        nomeArquivoDados = "dadosProdutos.csv";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        int opcao = -1;
        do{
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> localizarProdutos();
                case 3 -> cadastrarProduto();
                case 4 -> criarNovoPedido();
            }
            pausa();
        }while(opcao !=0);       

        salvarProdutos(nomeArquivoDados);
        teclado.close();    
    }
}
