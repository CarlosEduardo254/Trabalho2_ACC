public interface OperacoesArvore {
    void inserir(int valor);
    boolean buscar(int valor);
    void remover(int valor);
    int min();
    int max();
    void imprimirEmOrdem();
    int altura();
    long getRotacoes();
    long getTrocaCor();
    void zerarContadores();
}
