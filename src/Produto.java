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