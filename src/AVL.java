public class AVL  implements OperacoesArvore{

    private long rotacaoCount = 0;

    protected class NoAVL {
        int chave;
        int fb; // Fator de Balanceamento, altura da direita menos a da esquerda
        NoAVL esquerda, direita;

        public NoAVL(int valor) {
            this.chave = valor;
            this.fb = 0;
            this.esquerda = null;
            this.direita = null;
        }
    }

    private NoAVL raiz;

    @Override
    public void inserir(int valor) {
        // h -> retorna se houve ou não alteração na altura da subárvore do nó em questão
        boolean[] h = {false}; // O boolean é para passar por referência
        raiz = inserir(valor, raiz, h);
    }
    private NoAVL inserir(int valor, NoAVL no, boolean[] h) {
        if (no == null) {
            h[0] = true;
            return new NoAVL(valor);
        }
        if (valor < no.chave) {
            no.esquerda = inserir(valor, no.esquerda, h);
            if (h[0]) {
                no.fb--;
                if (no.fb == 0) h[0] = false;
                if (no.fb == -2) {
                    no = rotacao_direita(no);
                    h[0] = false; // Na inserção, qualquer rotação restaura a altura original
                }
            }
        } else if (valor > no.chave) {
            no.direita = inserir(valor, no.direita, h);
            if (h[0]) {
                no.fb++;
                if (no.fb == 0) h[0] = false;
                if (no.fb == 2) {
                    no = rotacao_esquerda(no);
                    h[0] = false; // Na inserção, qualquer rotação restaura a altura original
                }
            }
        } else {
            h[0] = false;
        }
        return no;
    }

    private NoAVL rotacao_direita(NoAVL no) {
        rotacaoCount++;

        NoAVL u = no.esquerda;
        if (u.fb <= 0) { // Cobre o -1 (inserção/remoção) e o 0 (exclusivo da remoção)
            no.esquerda = u.direita;
            u.direita = no;
            if (u.fb == -1) {
                no.fb = 0;
                u.fb = 0;
            } else { // u.fb == 0
                no.fb = -1;
                u.fb = 1;
            }
            return u;
        } else { // Rotação dupla
            rotacaoCount++; // +1 porque a rotação dupla equivale a duas simples
            NoAVL v = u.direita;
            u.direita = v.esquerda;
            v.esquerda = u;
            no.esquerda = v.direita;
            v.direita = no;
            if (v.fb == -1) { no.fb = 1; u.fb = 0; }
            else if (v.fb == 1) { no.fb = 0; u.fb = -1; }
            else { no.fb = 0; u.fb = 0; }
            v.fb = 0;
            return v;
        }
    }

    private NoAVL rotacao_esquerda(NoAVL no) {
        rotacaoCount++;

        NoAVL u = no.direita;
        if (u.fb >= 0) { // Cobre o 1 (inserção/remoção) e o 0 (exclusivo da remoção)
            no.direita = u.esquerda;
            u.esquerda = no;
            if (u.fb == 1) {
                no.fb = 0;
                u.fb = 0;
            } else { // u.fb == 0
                no.fb = 1;
                u.fb = -1;
            }
            return u;
        } else { // Rotação dupla
            rotacaoCount++;
            NoAVL v = u.esquerda;
            u.esquerda = v.direita;
            v.direita = u;
            no.direita = v.esquerda;
            v.esquerda = no;
            if (v.fb == 1) { no.fb = -1; u.fb = 0; }
            else if (v.fb == -1) { no.fb = 0; u.fb = 1; }
            else { no.fb = 0; u.fb = 0; }
            v.fb = 0;
            return v;
        }
    }
    @Override
    public boolean buscar(int valor) {
        return buscar(valor, raiz);
    }
    private boolean buscar(int valor, NoAVL no) {
        if (no == null) return false;
        if (valor == no.chave) return true;

        if  (valor < no.chave) {
            return buscar(valor, no.esquerda);
        } else {
            return buscar(valor, no.direita);
        }

    }

    @Override
    public void remover(int valor) {
        boolean[] h = {false};
        raiz = remover(valor, raiz, h);
    }

    private NoAVL remover(int valor, NoAVL no, boolean[] h) {
        if (no == null) {
            h[0] = false;
            return null;
        }

        if (valor < no.chave) {
            no.esquerda = remover(valor, no.esquerda, h);
            if (h[0]) {
                no.fb++;
                if (no.fb == 1) h[0] = false; // Altura estabilizou
                else if (no.fb == 2) {
                    no = rotacao_esquerda(no);
                    if (no.fb != 0) h[0] = false; // Altura estabilizou após rotação
                }
            }
        } else if (valor > no.chave) {
            no.direita = remover(valor, no.direita, h);
            if (h[0]) {
                no.fb--;
                if (no.fb == -1) h[0] = false; // Altura estabilizou
                else if (no.fb == -2) {
                    no = rotacao_direita(no);
                    if (no.fb != 0) h[0] = false; // Altura estabilizou após rotação
                }
            }
        } else {
            // Nó encontrado!
            if (no.esquerda == null || no.direita == null) {
                // Caso 1 e 2: Zero ou um filho
                NoAVL substituto = (no.esquerda != null) ? no.esquerda : no.direita;
                h[0] = true; // Subárvore encolheu
                return substituto;
            } else {
                // Caso 3: Dois filhos (Busca o sucessor)
                NoAVL sucessor = no.direita;
                while (sucessor.esquerda != null) {
                    sucessor = sucessor.esquerda;
                }
                no.chave = sucessor.chave;
                no.direita = remover(sucessor.chave, no.direita, h);
                if (h[0]) {
                    no.fb--;
                    if (no.fb == -1) h[0] = false;
                    else if (no.fb == -2) {
                        no = rotacao_direita(no);
                        if (no.fb != 0) h[0] = false;
                    }
                }
            }
        }
        return no;
    }

    @Override
    public int min() {
        if (raiz == null) throw new java.util.NoSuchElementException("A árvore está vazia.");
        NoAVL atual = raiz;
        while (atual.esquerda != null) atual = atual.esquerda;
        return atual.chave;
    }

    @Override
    public int max() {
        if (raiz == null) throw new java.util.NoSuchElementException("A árvore está vazia.");
        NoAVL atual = raiz;
        while (atual.direita != null) atual = atual.direita;
        return atual.chave;
    }

    @Override
    public void imprimirEmOrdem() {
        imprimirEmOrdem(raiz);
        System.out.println();
    }

    private void imprimirEmOrdem(NoAVL no) {
        if (no != null) {
            imprimirEmOrdem(no.esquerda);
            System.out.print(no.chave + " - ");
            imprimirEmOrdem(no.direita);
        }
    }

    public int altura() {
        return altura(raiz);
    }

    @Override
    public long getRotacoes() {
        return this.rotacaoCount;
    }

    @Override
    public long getTrocaCor() {
        return 0;
    }

    private int altura(NoAVL no) {
        if (no == null) return -1;
        return 1 + Math.max(altura(no.esquerda), altura(no.direita));
    }

    public void zerarContadores() {
        this.rotacaoCount = 0;
    }
}
