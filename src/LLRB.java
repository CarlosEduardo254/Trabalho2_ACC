import java.util.NoSuchElementException;

public class LLRB implements OperacoesArvore {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private NoRB raiz;

    private long rotacaoCount = 0;
    private long trocaCorCount = 0;

    protected class NoRB {
        int chave;
        NoRB esquerda, direita;
        boolean cor;

        public NoRB(int valor, boolean cor) {
            this.chave = valor;
            this.cor = cor;
        }
    }

    private boolean cor(NoRB x) {
        if(x==null) return false;
        return x.cor == RED;
    }

    private NoRB rotacao_esquerda(NoRB h) {
        rotacaoCount++;
        NoRB x = h.direita;
        h.direita = x.esquerda;
        x.esquerda = h;
        x.cor = h.cor;
        h.cor = RED;
        return x;
    }

    private NoRB rotacao_direita(NoRB h) {
        rotacaoCount++;
        NoRB x = h.esquerda;
        h.esquerda = x.direita;
        x.direita = h;
        x.cor = h.cor;
        h.cor = RED;
        return x;
    }

    private void trocaCor(NoRB h) {
        trocaCorCount++;
        h.cor = !h.cor;
        if (h.esquerda != null) h.esquerda.cor = !h.esquerda.cor;
        if (h.direita != null) h.direita.cor = !h.direita.cor;
    }

    @Override
    public void inserir(int valor) {
        raiz = inserir(raiz, valor);
        raiz.cor = BLACK;
    }

    private NoRB inserir(NoRB h, int valor) {
        if (h == null) return new NoRB(valor, RED);

        if (valor < h.chave) h.esquerda = inserir(h.esquerda, valor);
        else if (valor > h.chave) h.direita = inserir(h.direita, valor);
        else return h;

        if (cor(h.direita) && !cor(h.esquerda)) h = rotacao_esquerda(h);
        if (cor(h.esquerda) && cor(h.esquerda.esquerda)) h = rotacao_direita(h);
        if (cor(h.esquerda) && cor(h.direita)) trocaCor(h);

        return h;
    }

    @Override
    public boolean buscar(int valor) {
        NoRB x = raiz;
        while (x != null) {
            if (valor < x.chave) x = x.esquerda;
            else if (valor > x.chave) x = x.direita;
            else return true;
        }
        return false;
    }

    private NoRB moveRedEsquerda(NoRB h) {
        trocaCor(h);
        if (h.direita != null && cor(h.direita.esquerda)) {
            h.direita = rotacao_direita(h.direita);
            h = rotacao_esquerda(h);
            trocaCor(h);
        }
        return h;
    }

    private NoRB moveRedDireita(NoRB h) {
        trocaCor(h);
        if (h.esquerda != null && cor(h.esquerda.esquerda)) {
            h = rotacao_direita(h);
            trocaCor(h);
        }
        return h;
    }

    private NoRB balance(NoRB h) {
        if (cor(h.direita) && !cor(h.esquerda)) h = rotacao_esquerda(h);
        if (cor(h.esquerda) && cor(h.esquerda.esquerda)) h = rotacao_direita(h);
        if (cor(h.esquerda) && cor(h.direita)) trocaCor(h);
        return h;
    }

    private NoRB removeMin(NoRB h) {
        if (h.esquerda == null) return null;
        if (!cor(h.esquerda) && !cor(h.esquerda.esquerda)) h = moveRedEsquerda(h);
        h.esquerda = removeMin(h.esquerda);
        return balance(h);
    }

    @Override
    public void remover(int valor) {
        if (!buscar(valor)) return;

        if (!cor(raiz.esquerda) && !cor(raiz.direita)) raiz.cor = RED;

        raiz = remover(raiz, valor);
        if (raiz != null) raiz.cor = BLACK;
    }

    private NoRB remover(NoRB h, int valor) {
        if (valor < h.chave) {
            if (!cor(h.esquerda) && !cor(h.esquerda.esquerda)) h = moveRedEsquerda(h);
            h.esquerda = remover(h.esquerda, valor);
        } else {
            if (cor(h.esquerda)) h = rotacao_direita(h);
            if (valor == h.chave && (h.direita == null)) return null;
            if (!cor(h.direita) && !cor(h.direita.esquerda)) h = moveRedDireita(h);
            if (valor == h.chave) {
                NoRB x = min(h.direita);
                h.chave = x.chave;
                h.direita = removeMin(h.direita);
            } else h.direita = remover(h.direita, valor);
        }
        return balance(h);
    }

    @Override
    public int min() {
        if (raiz == null) throw new NoSuchElementException("A árvore está vazia.");
        return min(raiz).chave;
    }

    private NoRB min(NoRB x) {
        if (x.esquerda == null) return x;
        return min(x.esquerda);
    }

    @Override
    public int max() {
        if (raiz == null) throw new NoSuchElementException("A árvore está vazia.");
        return max(raiz).chave;
    }

    private NoRB max(NoRB x) {
        if (x.direita == null) return x;
        return max(x.direita);
    }

    public int altura() {
        return altura(raiz);
    }

    @Override
    public void imprimirEmOrdem() {
        imprimirEmOrdem(raiz);
        System.out.println();
    }

    private void imprimirEmOrdem(NoRB no) {
        if(no != null) {
            imprimirEmOrdem(no.esquerda);
            System.out.print(no.chave + " - ");
            imprimirEmOrdem(no.direita);
        }
    }

    @Override
    public long getRotacoes() {
        return this.rotacaoCount;
    }

    @Override
    public long getTrocaCor() {
        return this.trocaCorCount;
    }

    private int altura(NoRB x) {
        if (x == null) return -1;
        return 1 + Math.max(altura(x.esquerda), altura(x.direita));
    }

    public void zerarContadores() {
        this.rotacaoCount = 0;
        this.trocaCorCount = 0;
    }
}
