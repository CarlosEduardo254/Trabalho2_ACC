public class Main {
    public static void main(String[] args) {
        // Instanciando as duas árvores
        OperacoesArvore bst = new ArvoreBinariaBusca();
        OperacoesArvore avl = new AVL();

        System.out.println("==================================================");
        System.out.println("1. TESTE DE LÓGICA (INSERÇÃO, BUSCA E REMOÇÃO)");
        System.out.println("==================================================");

        System.out.println("\n-> Testando Árvore Binária de Busca Padrão (BST):");
        testarOperacoesBasicas(bst);

        System.out.println("\n-> Testando Árvore AVL:");
        testarOperacoesBasicas(avl);


        System.out.println("\n\n==================================================");
        System.out.println("2. TESTE DE DESEMPENHO (O PIOR CASO)");
        System.out.println("==================================================");
        /*
         * Vamos inserir números em ordem crescente (1, 2, 3, 4...).
         * Na BST padrão, isso cria uma árvore totalmente desbalanceada (uma lista).
         * Na AVL, as rotações mantêm a árvore baixinha (logarítmica).
         */
        int qtdElementos = 5000; // Cuidado ao aumentar muito: a BST pode dar StackOverflow na recursão!

        System.out.println("\nInserindo " + qtdElementos + " elementos sequenciais...");

        OperacoesArvore bstDesempenho = new ArvoreBinariaBusca();
        OperacoesArvore avlDesempenho = new AVL();

        // Medindo tempo de inserção da BST
        long inicioBST = System.nanoTime();
        for (int i = 1; i <= qtdElementos; i++) {
            bstDesempenho.inserir(i);
        }
        long tempoBST = System.nanoTime() - inicioBST;

        // Medindo tempo de inserção da AVL
        long inicioAVL = System.nanoTime();
        for (int i = 1; i <= qtdElementos; i++) {
            avlDesempenho.inserir(i);
        }
        long tempoAVL = System.nanoTime() - inicioAVL;

        System.out.println("Tempo de Inserção BST: " + (tempoBST / 1_000_000.0) + " ms");
        System.out.println("Tempo de Inserção AVL: " + (tempoAVL / 1_000_000.0) + " ms");

        // Medindo o tempo para buscar o ÚLTIMO elemento (o mais profundo)
        System.out.println("\nBuscando o último elemento (" + qtdElementos + "):");

        inicioBST = System.nanoTime();
        bstDesempenho.buscar(qtdElementos);
        tempoBST = System.nanoTime() - inicioBST;

        inicioAVL = System.nanoTime();
        avlDesempenho.buscar(qtdElementos);
        tempoAVL = System.nanoTime() - inicioAVL;

        System.out.println("Tempo de Busca BST: " + tempoBST + " nanosegundos");
        System.out.println("Tempo de Busca AVL: " + tempoAVL + " nanosegundos");
    }

    // Método auxiliar para não repetir código de teste
    private static void testarOperacoesBasicas(OperacoesArvore arvore) {
        int[] valoresParaInserir = {50, 30, 70, 20, 40, 60, 80};

        System.out.print("Inserindo valores: ");
        for (int v : valoresParaInserir) {
            System.out.print(v + " ");
            arvore.inserir(v);
        }
        System.out.println();

        System.out.println("Buscando 40 (deve ser true): " + arvore.buscar(40));
        System.out.println("Buscando 90 (deve ser false): " + arvore.buscar(90));

        System.out.println("Removendo o 30...");
        arvore.remover(30);

        System.out.println("Buscando 30 após remoção (deve ser false): " + arvore.buscar(30));
        System.out.println("Buscando 40 após remoção do pai (deve ser true): " + arvore.buscar(40));
    }
}