# Pong-java

## Descrição do projeto

Este é um jogo simples, onde chamo de Pong (simimar a um ping-pong) escrito em Java. É um jogo 
para um jogador, no qual você controle uma raquete
.O objetivo é fazer a bola passar pela raquete do computador, quem fizer 10 pontos vence a partida.
O jogo possui 2 dificuldades, a de nível FACIL e a de nível
DIFICIL onde comparado a facil aumenta a velocidade da bola 
e da raquete do computador 

## Linguagem e Bibliotecas

- Java
- Swing (JFrame, JPanel, JComponent,...)
- Graphics

## Estrutura do Projeto

- **Main:** Executa da janela do jogo
- **JanelaJogo:** Compõe a dimenção da janela com seus atributos e adiciona o campo do jogo
- **CampoJogo:** Estrutura toda interface do jogo e suas regras
- **Raquete:** Modela a raquete do jogador e do computador
- **Bola:** Modela a dimenção e velocidade da bola
- **Placar:** Conta pontos, onde 10 acaba o jogo
- **ControleComputador:** Estrutura a raquete do computador para que seja guiada no jogo
- **Dificuldade:** Configurações de dificuldade do jogo (FACIL/DIFICIL)
