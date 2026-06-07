# Trabalho Prático 02 - Árvores Binárias de Busca

## Descrição

Este projeto é referente ao Trabalho Prático 02 da disciplina de Algoritmos e Complexidade Computacional (ACC) da Universidade Federal do Ceará (UFC) - Campus Crateús.

O objetivo central foi implementar, analisar e comparar experimentalmente o desempenho de três estruturas distintas de árvores de busca:

- Árvore Binária de Busca (BST) (Sem balanceamento)

- Árvore AVL

- Árvore Rubro-Negra Caída para a Esquerda (LLRB)

Os testes avaliaram métricas de tempo de execução (inserção, busca e remoção), altura da árvore e custo estrutural (rotações) sob cenários distintos de entradas: Aleatório, Crescente e Decrescente.

## Autores

- Carlos Eduardo de Almeida Coutinho

- Maria Fernanda Vasconcelos Nunes

## Estrutura do Projeto

`src/`: Implementações em Java (Main.java, AVL.java, LLRB.java, ArvoreBinariaBusca.java, OperacoesArvore.java).

`plotar_graficos.py`: Script responsável por gerar as visualizações visuais baseadas nos testes.

`Relatório_ACC_Árvores.pdf`: Relatório completo documentando a análise empírica e assintótica.

`resultados.csv`: (Gerado na execução) Base de dados extraída dos testes da classe Main.

`graficos/`: (Gerado na execução) Diretório com os gráficos exportados pelo script Python.

## Pré-requisitos

Certifique-se de possuir em sua máquina:

- JDK (Java Development Kit) 17 ou superior.

- Python 3.8 ou superior.

Recomendável: python3-venv (Para o isolamento das dependências no Python).

## Como Executar

O projeto possui duas fases de execução: a geração de dados via Java e a plotagem dos gráficos via Python.

### 1. Compilando e Executando o Código Java

Os testes em Java inserem até 100.000 elementos. Como a árvore BST degrada até virar uma lista encadeada no pior caso (entradas ordenadas), a profundidade das chamadas recursivas exige um aumento no tamanho da pilha de memória da JVM.

No terminal, a partir da pasta raiz do projeto:

#### Compilar todos os arquivos Java localizados em src/
`javac src/*.java`

#### Executar a classe Main (Aumentando a stack para 100MB)
`java -Xss100m -cp src Main`


Nota: A execução completa leva tempo (aproximadamente 30-40 minutos dependendo do processador) e gerará automaticamente o arquivo resultados.csv na pasta raiz.

### 2. Gerando os Gráficos com Python

Para preservar seu ambiente global, recomendamos a utilização de um ambiente virtual (venv). Como a pasta do .venv é consideravelmente pesada com a instalação do pandas e matplotlib, ela não é enviada junto aos arquivos para facilitar a submissão.

Siga os passos abaixo para recriar o ambiente e rodar o script (a partir da raiz do projeto):

#### 1. Criar o ambiente virtual (será criada uma pasta chamada .venv)
`python -m venv .venv`

#### 2. Ativar o ambiente virtual
No Linux/macOS:
`source .venv/bin/activate`

No Windows:
`.venv\Scripts\activate`

### 3. Instalar as bibliotecas necessárias
`pip install pandas matplotlib numpy`

### 4. Executar o script
`python plotar_graficos.py`


Após o fim da execução, os gráficos serão gerados no formato .png e salvos dentro da pasta `graficos/`. Quando finalizar a avaliação, você pode simplesmente apagar a pasta .venv para liberar espaço.
