public class ArvoreBinariaBusca implements OperacoesArvore{
    protected class NoBST {
        int chave;
        NoBST esquerda, direita;

        public NoBST(int chave) {
            this.chave = chave;
            this.esquerda = null;
            this.direita = null;
        }
    }

    private NoBST raiz;

    @Override
    public void inserir(int valor) {
        raiz = inserir(valor, raiz);
    }
    private NoBST inserir(int valor, NoBST no) {
        if (no == null) {
            return new NoBST(valor);
        }
        if (valor < no.chave) {
            no.esquerda = inserir(valor, no.esquerda);
        } else if (valor > no.chave) {
            no.direita = inserir(valor, no.direita);
        }
        //Ignoramos duplicatas(verificar no documento se tem algo falando sobre)
        return no;
    }

    @Override
    public boolean buscar(int valor) {
        return buscar(valor, raiz);
    }
    private boolean buscar(int valor, NoBST no) {
        if (no == null) {
            return false;
        }
        if (valor == no.chave) {
            return true;
        }

        if (valor < no.chave) {
            return buscar(valor, no.esquerda);
        } else {
            return buscar(valor, no.direita);
        }
    }

    @Override
    public void remover(int valor) {
        raiz = remover(valor, raiz);
    }
    private NoBST remover(int valor, NoBST no) {
        if (no == null) {
            return null;
        }
        if (valor < no.chave) {
            no.esquerda = remover(valor, no.esquerda);
        } else if (valor > no.chave) {
            no.direita = remover(valor, no.direita);
        } else {
            // Casos 1 e 2: Nó sem filhos ou com apenas um filho
            if (no.esquerda == null) {
                return no.direita;
            } else if (no.direita == null) {
                return no.esquerda;
            }

            // Caso 3: Nó com dois filhos
            // Busca o sucessor (menor valor da subárvore direita)
            NoBST sucessor = no.direita;
            while (sucessor.esquerda != null) {
                sucessor = sucessor.esquerda;
            }

            // Copia o valor do sucessor para o nó atual
            no.chave = sucessor.chave;

            // Remove o sucessor da subárvore direita
            no.direita = remover(sucessor.chave, no.direita);
        }

        return no;
    }
}
