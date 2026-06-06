import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import os

os.makedirs('graficos', exist_ok=True)

print("Lendo resultados.csv")
df = pd.read_csv('resultados.csv')

df['TempoMedio_ms'] = pd.to_numeric(df['TempoMedio_ms'], errors='coerce')
df['DesvioPadrao_ms'] = pd.to_numeric(df['DesvioPadrao_ms'], errors='coerce')
df['Rotacoes'] = pd.to_numeric(df['Rotacoes'], errors='coerce')
df['Altura'] = pd.to_numeric(df['Altura'], errors='coerce')

estruturas = ['BST', 'AVL', 'LLRB']
padroes = ['Aleatorio', 'Crescente', 'Decrescente']
cores = {'BST': '#e63946', 'AVL': '#1d3557', 'LLRB': '#2a9d8f'}
marcadores = {'BST': 'o', 'AVL': 's', 'LLRB': '^'}

# Gráfico de tempo de execução
for operacao in ['Insercao', 'Busca', 'Remocao']:
    fig, axes = plt.subplots(1, 3, figsize=(18, 5))
    fig.suptitle(f'Tempo de Execução - {operacao}', fontsize=16, fontweight='bold')

    for i, padrao in enumerate(padroes):
        ax = axes[i]
        dados_padrao = df[(df['Operacao'] == operacao) & (df['Padrao'] == padrao)]

        for est in estruturas:
            dados_est = dados_padrao[dados_padrao['Estrutura'] == est]
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

# Gráfico de rotações
fig, axes = plt.subplots(1, 3, figsize=(18, 5))
fig.suptitle('Custo Estrutural: Quantidade de Rotações (Inserção)', fontsize=16, fontweight='bold')

for i, padrao in enumerate(padroes):
    ax = axes[i]
    dados_padrao = df[(df['Operacao'] == 'Insercao') & (df['Padrao'] == padrao)]

    for est in ['AVL', 'LLRB']:
        dados_est = dados_padrao[dados_padrao['Estrutura'] == est]
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

# Gráfico de altura
fig, axes = plt.subplots(1, 3, figsize=(18, 5))
fig.suptitle('Altura da Árvore: Observada vs Teórica', fontsize=16, fontweight='bold')

for i, padrao in enumerate(padroes):
    ax = axes[i]
    dados_padrao = df[(df['Operacao'] == 'Insercao') & (df['Padrao'] == padrao)]

    for est in estruturas:
        dados_est = dados_padrao[dados_padrao['Estrutura'] == est]
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