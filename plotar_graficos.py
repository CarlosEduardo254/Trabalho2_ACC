import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import os

os.makedirs('graficos', exist_ok=True)

print("A processar e corrigir a formatação do CSV (vírgulas vs pontos)...")

dados_limpos = []

# Lemos o ficheiro linha a linha para não deixar o Pandas confundir as vírgulas
with open('resultados.csv', 'r', encoding='utf-8') as f:
    linhas = f.readlines()

header = linhas[0].strip().split(',')

for linha in linhas[1:]:
    linha = linha.strip()
    if not linha: continue

    partes = linha.split(',')

    # Se a linha tem 8 partes, significa que deu "ERRO" ou usou ponto. Formato normal.
    if len(partes) == 8:
        dados_limpos.append(partes)

    # Se tem 10 partes, o Java usou vírgulas nos dois números decimais (Tempo e Desvio).
    elif len(partes) == 10:
        estrutura = partes[0]
        n = partes[1]
        padrao = partes[2]
        operacao = partes[3]

        # Juntamos as partes separadas pela vírgula indesejada e substituímos por um ponto
        tempo_medio = f"{partes[4]}.{partes[5]}"
        desvio = f"{partes[6]}.{partes[7]}"

        rotacoes = partes[8]
        altura = partes[9]

        dados_limpos.append([estrutura, n, padrao, operacao, tempo_medio, desvio, rotacoes, altura])
    else:
        print(f"Aviso: Linha ignorada devido a formato inesperado: {linha}")

df = pd.DataFrame(dados_limpos, columns=header)

# Convertendo para formato numérico oficial
df['TamanhoN'] = pd.to_numeric(df['TamanhoN'], errors='coerce')
df['TempoMedio_ms'] = pd.to_numeric(df['TempoMedio_ms'], errors='coerce')
df['DesvioPadrao_ms'] = pd.to_numeric(df['DesvioPadrao_ms'], errors='coerce')
df['Rotacoes'] = pd.to_numeric(df['Rotacoes'], errors='coerce')
df['Altura'] = pd.to_numeric(df['Altura'], errors='coerce')

print("Dados carregados com sucesso! A gerar os gráficos...")

estruturas = ['BST', 'AVL', 'LLRB']
padroes = ['Aleatorio', 'Crescente', 'Decrescente']
cores = {'BST': '#e63946', 'AVL': '#1d3557', 'LLRB': '#2a9d8f'}
marcadores = {'BST': 'o', 'AVL': 's', 'LLRB': '^'}


# TEMPO DE EXECUÇÃO
for operacao in ['Insercao', 'Busca', 'Remocao']:
    fig, axes = plt.subplots(1, 3, figsize=(18, 5))
    fig.suptitle(f'Tempo de Execução - {operacao}', fontsize=16, fontweight='bold')

    for i, padrao in enumerate(padroes):
        ax = axes[i]
        dados_padrao = df[(df['Operacao'] == operacao) & (df['Padrao'] == padrao)]

        for est in estruturas:
            dados_est = dados_padrao[dados_padrao['Estrutura'] == est].dropna(subset=['TempoMedio_ms'])
            if not dados_est.empty:
                ax.errorbar(dados_est['TamanhoN'], dados_est['TempoMedio_ms'],
                            yerr=dados_est['DesvioPadrao_ms'], label=est,
                            color=cores[est], marker=marcadores[est], capsize=4, linewidth=2)

        ax.set_title(f'Entrada: {padrao}', fontsize=12)
        ax.set_xlabel('Tamanho da Instância (N)')
        ax.set_ylabel('Tempo Médio (ms)')
        ax.legend()
        ax.grid(True, linestyle='--', alpha=0.6)

        if padrao in ['Crescente', 'Decrescente']:
            ax.set_yscale('log')
            ax.set_ylabel('Tempo Médio (ms) - Escala LOG')

    plt.tight_layout()
    plt.savefig(f'graficos/tempo_{operacao.lower()}.png', dpi=300)
    plt.close()

# GRÁFICO DE ROTAÇÕES
fig, axes = plt.subplots(1, 3, figsize=(18, 5))
fig.suptitle('Custo Estrutural: Quantidade de Rotações (Inserção)', fontsize=16, fontweight='bold')

for i, padrao in enumerate(padroes):
    ax = axes[i]
    dados_padrao = df[(df['Operacao'] == 'Insercao') & (df['Padrao'] == padrao)]

    for est in ['AVL', 'LLRB']:
        dados_est = dados_padrao[dados_padrao['Estrutura'] == est].dropna(subset=['Rotacoes'])
        if not dados_est.empty:
            ax.plot(dados_est['TamanhoN'], dados_est['Rotacoes'], label=est,
                    color=cores[est], marker=marcadores[est], linewidth=2)

    ax.set_title(f'Entrada: {padrao}')
    ax.set_xlabel('Tamanho da Instância (N)')
    ax.set_ylabel('Total de Rotações')
    ax.legend()
    ax.grid(True, linestyle='--', alpha=0.6)

plt.tight_layout()
plt.savefig('graficos/rotacoes.png', dpi=300)
plt.close()

# GRÁFICO DE ALTURA
fig, axes = plt.subplots(1, 3, figsize=(18, 5))
fig.suptitle('Altura da Árvore: Observada vs Teórica', fontsize=16, fontweight='bold')

for i, padrao in enumerate(padroes):
    ax = axes[i]
    dados_padrao = df[(df['Operacao'] == 'Insercao') & (df['Padrao'] == padrao)]

    for est in estruturas:
        dados_est = dados_padrao[dados_padrao['Estrutura'] == est].dropna(subset=['Altura'])
        if not dados_est.empty:
            ax.plot(dados_est['TamanhoN'], dados_est['Altura'], label=f'Obs: {est}',
                    color=cores[est], marker=marcadores[est], linewidth=2)

    ns = np.array([1000, 5000, 10000, 50000, 100000])
    altura_ideal = np.log2(ns)
    ax.plot(ns, altura_ideal, 'k--', label='Teórica O(log N)', linewidth=2.5)

    ax.set_title(f'Entrada: {padrao}')
    ax.set_xlabel('Tamanho da Instância (N)')
    ax.set_ylabel('Altura')
    ax.legend()
    ax.grid(True, linestyle='--', alpha=0.6)

    if padrao in ['Crescente', 'Decrescente']:
        ax.set_yscale('log')
        ax.set_ylabel('Altura - Escala LOG')

plt.tight_layout()
plt.savefig('graficos/altura.png', dpi=300)
plt.close()

print("Todos os gráficos foram gerados na pasta 'graficos'!")