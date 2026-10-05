import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import java.util.Random;

/**
 * Campo de jogo. Herda de JPanel ("é um" painel) e mantém a bola e as raquetes
 * ("tem" uma bola e duas raquetes): herança + composição.
 */
public class CampoJogo extends JPanel {

    public static final int LARGURA = 800;
    public static final int ALTURA = 600;

    private static final int LARGURA_RAQUETE = 15;
    private static final int ALTURA_RAQUETE = 100;
    private static final int MARGEM_RAQUETE = 30;
    private static final int VELOCIDADE_RAQUETE = 6;

    // Constantes da bola (velocidades baixas, para evitar atravessar raquetes depois)
    private static final int DIAMETRO_BOLA = 20;
    private static final int VELOCIDADE_BOLA_X = 4;
    private static final int VELOCIDADE_BOLA_Y = 3;

    private static final int INTERVALO_MS = 16;

    private static final int PAUSA_SAQUE_MS = 800;
    private static final int TICKS_PAUSA_SAQUE = PAUSA_SAQUE_MS / INTERVALO_MS;

    // Pontos necessários para vencer a partida
    private static final int META_PONTOS = 10;

    private final Raquete raqueteJogador;
    private final Raquete raqueteComputador;
    private final Bola bola;
    private final Placar placar = new Placar();
    private final ControleComputador controleComputador;

    // Usado só para sortear a direção vertical do saque
    private final Random sorteio = new Random();

    private static final Font FONTE_PLACAR = new Font("Monospaced", Font.BOLD, 48);

    // Estado da partida: false depois que alguém vence, até começar outra
    private boolean partidaEmAndamento = true;

    private int ticksAteSaque = 0;

    private boolean subindo = false;
    private boolean descendo = false;

    private final Timer timer;

    public CampoJogo(Dificuldade dificuldade) {
        setPreferredSize(new Dimension(LARGURA, ALTURA));
        setBackground(Color.BLACK);

        int yCentralizado = (ALTURA - ALTURA_RAQUETE) / 2;

        raqueteJogador = new Raquete(
                MARGEM_RAQUETE, yCentralizado,
                LARGURA_RAQUETE, ALTURA_RAQUETE, VELOCIDADE_RAQUETE, ALTURA);

        // A dificuldade define a velocidade da raquete do computador
        raqueteComputador = new Raquete(
                LARGURA - MARGEM_RAQUETE - LARGURA_RAQUETE, yCentralizado,
                LARGURA_RAQUETE, ALTURA_RAQUETE,
                dificuldade.getVelocidadeComputador(), ALTURA);

        // ...e a tolerância de alinhamento
        controleComputador = new ControleComputador(dificuldade.getTolerancia());

        // x e y são o canto superior esquerdo, então o centro exige descontar o diâmetro
        bola = new Bola(
                (LARGURA - DIAMETRO_BOLA) / 2, (ALTURA - DIAMETRO_BOLA) / 2,
                DIAMETRO_BOLA, dificuldade.getVelocidadeBolaX(), dificuldade.getVelocidadeBolaY());

        configurarTeclado();
        configurarPerdaDeFoco();

        timer = new Timer(INTERVALO_MS, e -> atualizarJogo());
        timer.start();
    }

    // ---------- Teclado ----------

    private void configurarTeclado() {
        associarTecla(KeyEvent.VK_W, "subir", () -> subindo = true, () -> subindo = false);
        associarTecla(KeyEvent.VK_UP, "subirSeta", () -> subindo = true, () -> subindo = false);

        associarTecla(KeyEvent.VK_S, "descer", () -> descendo = true, () -> descendo = false);
        associarTecla(KeyEvent.VK_DOWN, "descerSeta", () -> descendo = true, () -> descendo = false);
    }

    private void associarTecla(int tecla, String nome, Runnable aoPressionar, Runnable aoSoltar) {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(tecla, 0, false), nome + ".pressionada");
        inputMap.put(KeyStroke.getKeyStroke(tecla, 0, true), nome + ".solta");

        actionMap.put(nome + ".pressionada", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aoPressionar.run();
            }
        });
        actionMap.put(nome + ".solta", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aoSoltar.run();
            }
        });
    }

    private void configurarPerdaDeFoco() {
        setFocusable(true);
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                limparComandos();
            }
        });
    }

    private void limparComandos() {
        subindo = false;
        descendo = false;
    }

    // ---------- Ciclo do jogo ----------

    private void atualizarJogo() {
        if (!partidaEmAndamento) {
            return; // partida encerrada: nada avança
        }

        // 1) Comandos do jogador -> raquete
        if (subindo && !descendo) {
            raqueteJogador.moverParaCima();
        } else if (descendo && !subindo) {
            raqueteJogador.moverParaBaixo();
        }

        // 2) Computador
        controleComputador.atualizar(raqueteComputador, bola);

        // 3) Bola: durante a pausa de saque ela fica parada no centro,
        //    enquanto as raquetes continuam respondendo
        if (ticksAteSaque > 0) {
            ticksAteSaque--;
        } else {
            bola.mover();

            // 4) Colisões e pontos (sempre DEPOIS de mover)
            tratarLimitesVerticais();
            tratarColisoesComRaquetes();
            tratarPontuacao();
        }

        // 5) Pede o redesenho (o desenho em si acontece em paintComponent)
        repaint();

        // 6) Por último: alguém atingiu a meta?
        verificarVitoria();
    }

    /** Encerra a partida se algum lado atingiu a meta. */
    private void verificarVitoria() {
        if (placar.jogadorVenceu(META_PONTOS)) {
            encerrarPartida("Você venceu!");
        } else if (placar.computadorVenceu(META_PONTOS)) {
            encerrarPartida("O computador venceu!");
        }
    }

    /**
     * Para a partida UMA vez, mostra o vencedor e pergunta o que fazer.
     * O timer é parado ANTES do diálogo modal.
     */
    private void encerrarPartida(String mensagemVencedor) {
        partidaEmAndamento = false;
        timer.stop();
        paintImmediately(0, 0, LARGURA, ALTURA); // mostra o placar final atrás do diálogo

        String[] opcoes = {"Nova partida", "Sair"};
        int escolha = JOptionPane.showOptionDialog(
                this,
                mensagemVencedor + "\nPlacar final: "
                        + placar.getPontosJogador() + " x " + placar.getPontosComputador(),
                "Fim de partida",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opcoes,
                opcoes[0]);

        if (escolha == 0) {
            iniciarNovaPartida();
        } else {
            System.exit(0);
        }
    }

    /** Restaura tudo e REINICIA o mesmo timer (nunca cria outro). */
    private void iniciarNovaPartida() {
        placar.zerar();
        raqueteJogador.reiniciar();
        raqueteComputador.reiniciar();
        reiniciarBola(sorteio.nextBoolean());
        limparComandos();

        partidaEmAndamento = true;
        requestFocusInWindow();
        timer.start();
    }

    /** Teto e chão: corrige a posição e inverte a direção vertical. */
    private void tratarLimitesVerticais() {
        if (bola.getY() <= 0) {
            bola.rebaterNoTeto();
        } else if (bola.getY() + bola.getDiametro() >= ALTURA) {
            bola.rebaterNoChao(ALTURA);
        }
    }

    /**
     * Raquetes: só rebate se houver interseção E a bola estiver se APROXIMANDO
     * (velocidadeX negativa na esquerda, positiva na direita).
     */
    private void tratarColisoesComRaquetes() {
        Rectangle areaBola = bola.getRetangulo();

        if (bola.getVelocidadeX() < 0 && areaBola.intersects(raqueteJogador.getRetangulo())) {
            // Reposiciona a bola logo à direita da raquete
            bola.rebaterParaDireita(raqueteJogador.getX() + raqueteJogador.getLargura());
        } else if (bola.getVelocidadeX() > 0 && areaBola.intersects(raqueteComputador.getRetangulo())) {
            // Reposiciona a bola logo à esquerda da raquete
            bola.rebaterParaEsquerda(raqueteComputador.getX() - bola.getDiametro());
        }
    }

    /**
     * Ponto: a bola saiu COMPLETAMENTE por uma lateral.
     * Como a bola volta ao centro na hora, o mesmo evento não pontua duas vezes.
     */
    private void tratarPontuacao() {
        if (bola.getX() + bola.getDiametro() <= 0) {
            // Saiu pela esquerda: ponto do computador
            placar.marcarPontoComputador();
            reiniciarBola(false);
        } else if (bola.getX() >= LARGURA) {
            // Saiu pela direita: ponto do jogador
            placar.marcarPontoJogador();
            reiniciarBola(true);
        }
    }

    /** Novo saque: horizontal em direção a quem perdeu o ponto; vertical sorteada. */
    private void reiniciarBola(boolean paraDireita) {
        bola.reiniciar(paraDireita, sorteio.nextBoolean());
        ticksAteSaque = TICKS_PAUSA_SAQUE;
    }

    // ---------- Desenho ----------

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        int x = LARGURA / 2;
        for (int y = 0; y < ALTURA; y += 30) {
            g.drawLine(x, y, x, y + 15);
        }

        raqueteJogador.desenhar(g);
        raqueteComputador.desenhar(g);
        bola.desenhar(g);

        desenharPlacar(g);
    }

    /** Escreve cada pontuação centralizada na sua metade, no alto do campo. */
    private void desenharPlacar(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(FONTE_PLACAR);
        FontMetrics medidas = g.getFontMetrics();

        String textoJogador = String.valueOf(placar.getPontosJogador());
        String textoComputador = String.valueOf(placar.getPontosComputador());

        int xJogador = LARGURA / 4 - medidas.stringWidth(textoJogador) / 2;
        int xComputador = 3 * LARGURA / 4 - medidas.stringWidth(textoComputador) / 2;

        g.drawString(textoJogador, xJogador, 60);
        g.drawString(textoComputador, xComputador, 60);
    }
}