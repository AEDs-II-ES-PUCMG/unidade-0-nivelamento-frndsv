import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Produto {

    protected static final double MARGEM_PADRAO = 0.2;

    protected String descricao;
    protected double precoCusto;
    protected double margemLucro;
    // Teste 
    // Construtor com margem de lucro informada
    public Produto(String desc, double precoCusto, double margemLucro) {
        if (precoCusto < 0) {
            throw new IllegalArgumentException("Preço de custo não pode ser negativo.");
        }

        if (margemLucro < 0) {
            throw new IllegalArgumentException("Margem de lucro não pode ser negativa.");
        }
        init(desc, precoCusto, margemLucro);
    }

    // Construtor utilizando margem padrão
    public Produto(String desc, double precoCusto) {
        if (precoCusto < 0) {
            throw new IllegalArgumentException("Preço de custo não pode ser negativo.");
        }
        init(desc, precoCusto, MARGEM_PADRAO);
    }

    // Método auxiliar de inicialização
    private void init(String desc, double precoCusto, double margemLucro) {
        this.descricao = desc;
        this.precoCusto = precoCusto;
        this.margemLucro = margemLucro;
    }

    // Calcula o valor de venda com base no custo e na margem
    public abstract double valorDeVenda();

    /**
    * Gera uma linha de texto a partir dos dados do produto
    * @return Uma string no formato "tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]"
    */
    public abstract String gerarDadosTexto();

    /**
    * Cria um produto a partir de uma linha de dados em formato texto. A linha de dados deve estar de acordo com a
    formatação
    * "tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]"
    * ou o funcionamento não será garantido. Os tipos são 1 para produto não perecível e 2 para perecível.
    * @param linha Linha com os dados do produto a ser criado.
    * @return Um produto com os dados recebidos
    */
    static Produto criarDoTexto(String linha){
        Produto novoProduto = null;
        String[] atributos = linha.split(";");
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // "1; descrição;preçoDeCusto;margemDeLucro"
        // "2; descrição;preçoDeCusto;margemDeLucro;dataDeValidade"
        int tipoProduto = Integer.parseInt(atributos[0]);
        String descricao = atributos[1];
        Double precoDeCusto = Double.parseDouble(atributos[2]);
        Double margemDeLucro  = Double.parseDouble(atributos[3]);
        if(tipoProduto == 1) {
            novoProduto = new ProdutoNaoPerecivel(descricao, precoDeCusto, margemDeLucro);
        } else if(tipoProduto == 2) {
            LocalDate dataValidade = LocalDate.parse(atributos[4], formato);
            novoProduto = new ProdutoPerecivel(descricao, precoDeCusto, margemDeLucro, dataValidade);

        }
        
        return novoProduto;
    }

    /**
    * Igualdade de produtos: caso possuam o mesmo nome/descrição.
    * @param obj Outro produto a ser comparado
    * @return booleano true/false conforme o parâmetro possua a descrição igual ou não a este produto.
    */
    @Override
    public boolean equals(Object obj){
    Produto outro = (Produto)obj;
    return this.descricao.toLowerCase().equals(outro.descricao.toLowerCase());
    }

    @Override
    public String toString() {
        return "Produto{" +
                "descricao='" + descricao + '\'' +
                ", precoCusto=" + precoCusto +
                ", margemLucro=" + margemLucro +
                ", valorVenda=" + valorDeVenda() +
                '}';
    }
}