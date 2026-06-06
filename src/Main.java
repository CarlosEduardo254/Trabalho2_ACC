import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main {
    private static final int[] TAMANHOS = {1000, 5000, 10000, 50000, 100000};
    private static final String[] PADROES = {"Aleatorio", "Crescente", "Decrescente"};
    private static final String[] ARVORES = {"BST", "AVL", "LLRB"};
    private static final int REPETICOES = 10;

    public static void main(String[] args) {
        System.out.println("Iniciando experimentos");

        try (PrintWriter writer = new PrintWriter(new FileWriter("resultados.csv"))) {
            // Cabeçalho do arquivo CSV
            writer.println("Estrutura,TamanhoN,Padrao,Operacao,TempoMedio_ms,DesvioPadrao_ms,Rotacoes,Altura");

            for (int n : TAMANHOS) {
                for (String padrao : PADROES) {
                    for (String nomeArvore : ARVORES) {
                        System.out.println("Testando: " + nomeArvore + " | Quantidade = " + n + " | Padrão = " + padrao);
                        executarExperimento(writer, nomeArvore, n, padrao);
                    }
                }
            }
            System.out.println("\nExperimentos finalizados. Resultados salvos em 'resultados.csv'");
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo CSV: " + e.getMessage());
        }
    }

    private static void executarExperimento(PrintWriter writer, String nomeArvore, int n, String padrao) {
        long[] temposInsercao = new long[REPETICOES];
        long[] temposBusca = new long[REPETICOES];
        long[] temposRemocao = new long[REPETICOES];

        long rotacoesFinais = 0;
        int alturaFinal = 0;
        boolean falhouPorStackOverflow = false;

        for (int i = 0; i < REPETICOES; i++) {
            OperacoesArvore arvore = instanciarArvore(nomeArvore);

            int[] dadosInsercao = gerarVetorInsercao(n, padrao);
            int[] dadosBusca = gerarVetorBusca(n);
            int[] dadosRemocao = gerarVetorRemocao(n);

            try {
                // Inserção
                long inicio = System.nanoTime();
                for (int valor : dadosInsercao) {
                    arvore.inserir(valor);
                }
                temposInsercao[i] = System.nanoTime() - inicio;

                // Captura de métricas estruturais apenas na primeira rodada (para não pesar)
                if (i == 0) {
                    rotacoesFinais = arvore.getRotacoes();
                    alturaFinal = arvore.altura();
                }

                // Busca
                inicio = System.nanoTime();
                for (int valor : dadosBusca) {
                    arvore.buscar(valor);
                }
                temposBusca[i] = System.nanoTime() - inicio;

                // Remoção
                inicio = System.nanoTime();
                for (int valor : dadosRemocao) {
                    arvore.remover(valor);
                }
                temposRemocao[i] = System.nanoTime() - inicio;

            } catch (StackOverflowError e) {
                falhouPorStackOverflow = true;
                break; // Caso estoure a pilha, aborta as repetições deste cenário
            }
        }

        if (falhouPorStackOverflow) {
            System.out.println("No teste para uma entrada com " + n + " valores a altura da árvore excedeu a pilha do Java rodando a " + nomeArvore);
            // Escreve no CSV avisando que falhou (útil para o relatório)
            writer.printf("%s,%d,%s,%s,ERRO,ERRO,ERRO,ERRO\n", nomeArvore, n, padrao, "Insercao");
            writer.printf("%s,%d,%s,%s,ERRO,ERRO,ERRO,ERRO\n", nomeArvore, n, padrao, "Busca");
            writer.printf("%s,%d,%s,%s,ERRO,ERRO,ERRO,ERRO\n", nomeArvore, n, padrao, "Remocao");
        } else {
            // Calcula e salva estatísticas de Inserção
            Estatistica estIns = calcularEstatisticas(temposInsercao);
            writer.printf(java.util.Locale.US, "%s,%d,%s,%s,%.4f,%.4f,%d,%d\n", nomeArvore, n, padrao, "Insercao", estIns.mediaMs, estIns.desvioMs, rotacoesFinais, alturaFinal);

            // Calcula e salva estatísticas de Busca
            Estatistica estBusca = calcularEstatisticas(temposBusca);
            writer.printf(java.util.Locale.US, "%s,%d,%s,%s,%.4f,%.4f,-,-\n", nomeArvore, n, padrao, "Busca", estBusca.mediaMs, estBusca.desvioMs);

            // Calcula e salva estatísticas de Remoção
            Estatistica estRem = calcularEstatisticas(temposRemocao);
            writer.printf(java.util.Locale.US, "%s,%d,%s,%s,%.4f,%.4f,-,-\n", nomeArvore, n, padrao, "Remocao", estRem.mediaMs, estRem.desvioMs);
        }
    }


    private static OperacoesArvore instanciarArvore(String nome) {
        switch (nome) {
            case "BST": return new ArvoreBinariaBusca();
            case "AVL": return new AVL();
            case "LLRB": return new LLRB();
            default: throw new IllegalArgumentException("Árvore desconhecida");
        }
    }

    private static int[] gerarVetorInsercao(int n, String padrao) {
        int[] vetor = new int[n];
        List<Integer> lista = new ArrayList<>(n);
        for (int i = 1; i <= n; i++) lista.add(i);

        if (padrao.equals("Aleatorio")) {
            Collections.shuffle(lista, new Random(42));
        } else if (padrao.equals("Decrescente")) {
            Collections.reverse(lista);
        } // Crescente já está na ordem certa

        for (int i = 0; i < n; i++) vetor[i] = lista.get(i);
        return vetor;
    }

    private static int[] gerarVetorBusca(int n) {
        // Como o documento pede busca com elementos presentes e ausentes.
        // Iremos buscar 'n' elementos com metade entre 1 e n (presentes), metade entre n+1 e 2n (ausentes)
        List<Integer> lista = new ArrayList<>(n);
        for (int i = 1; i <= n / 2; i++) lista.add(i); // Presentes
        for (int i = n + 1; i <= n + (n / 2); i++) lista.add(i); // Ausentes
        Collections.shuffle(lista, new Random(84));

        int[] vetor = new int[n];
        for (int i = 0; i < n; i++) vetor[i] = lista.get(i);
        return vetor;
    }

    private static int[] gerarVetorRemocao(int n) {
        // Remove todos os elementos inseridos de forma aleatória
        List<Integer> lista = new ArrayList<>(n);
        for (int i = 1; i <= n; i++) lista.add(i);
        Collections.shuffle(lista, new Random(128));

        int[] vetor = new int[n];
        for (int i = 0; i < n; i++) vetor[i] = lista.get(i);
        return vetor;
    }

    private static class Estatistica {
        double mediaMs;
        double desvioMs;
    }

    private static Estatistica calcularEstatisticas(long[] temposNanos) {
        Estatistica est = new Estatistica();
        double soma = 0;
        for (long t : temposNanos) {
            soma += (t / 1_000_000.0); // Converte Nano para Milisegundos
        }
        est.mediaMs = soma / temposNanos.length;

        double somaVariancia = 0;
        for (long t : temposNanos) {
            double tempoMs = t / 1_000_000.0;
            somaVariancia += Math.pow(tempoMs - est.mediaMs, 2);
        }
        est.desvioMs = Math.sqrt(somaVariancia / temposNanos.length);

        return est;
    }
}