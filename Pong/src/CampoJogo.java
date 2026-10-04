import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.Random;

public class CampoJogo extends JPanel {

    //Dimenção constantes da Janela
    public static final int LARGURA = 800;
    public static final int ALTURA = 600;

    //Dimenções constantes da Raquete
    private static final int LARGURA_RAQUETE = 15;
    private static final int ALTURA_RAQUETE = 100;
    private static final int MARGEM_RAQUETE = 30;
    private static final int VELOCIDADE_RAQUETE = 6;

    //Dimenções constantes da Bola
    private static final int DIAMETRO_BOLA = 20;
    private static final int VELOCIDADE_BOLA_X = 4;
    private static final int VELOCIDADE_BOLA_Y = 3;

    //Intervalo do ciclo do jogo, em milissegundos (-60 atualizações por segundo)
    private static final int INTERVALO_MS = 16;

    //Controla as raquetes e a bola
    private final Raquete raqueteJogador;
    private final Raquete raqueteComputador;
    private final Bola bola;
    private final Placar placar = new Placar();

    //Usado para sortear a direção vertical do saque
    private final Random sorteio = new Random();

    private static final Font FONTE_PLACAR = new Font("Monospaced", Font.BOLD,48);

    private boolean subindo = false;
    private boolean descendo = false;

    private final Timer timer;

    public CampoJogo(){
        setPreferredSize(new Dimension(LARGURA,ALTURA)); //configura dimenção da janela
        setBackground(Color.BLACK); //muda a cor do background

        int yCentralizado = (ALTURA - ALTURA_RAQUETE) / 2;

        //Esquerda
        raqueteJogador = new Raquete(
                MARGEM_RAQUETE,yCentralizado,
                LARGURA_RAQUETE, ALTURA_RAQUETE,VELOCIDADE_RAQUETE,ALTURA
        );

        //Direita
        raqueteComputador = new Raquete(
                LARGURA - MARGEM_RAQUETE - LARGURA_RAQUETE, yCentralizado,
                LARGURA_RAQUETE,ALTURA_RAQUETE, VELOCIDADE_RAQUETE, ALTURA
        );

        bola = new Bola(
                (LARGURA - DIAMETRO_BOLA) / 2, (ALTURA - DIAMETRO_BOLA) / 2,
                DIAMETRO_BOLA, VELOCIDADE_BOLA_X, VELOCIDADE_BOLA_Y
        );

        configurarTeclado();
        configurarPerdaDeFoco();

        timer = new Timer(INTERVALO_MS, e -> atualizarJogo());
        timer.start();
    }

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

    private void atualizarJogo() {
        // 1) Comandos do jogador -> raquete
        if (subindo && !descendo) {
            raqueteJogador.moverParaCima();
        } else if (descendo && !subindo) {
            raqueteJogador.moverParaBaixo();
        }

        // 2) Bola
        bola.mover();

        // 3) Colisões (sempre DEPOIS de mover)
        tratarLimitesVerticais();
        tratarColisoesComRaquetes();
        tratarPontuacao();

        // 4) Pede o redesenho (o desenho em si acontece em paintComponent)
        repaint();
    }

    //corrige a posição e inverte a direção vertical.
    private void tratarLimitesVerticais() {
        if (bola.getY() <= 0) {
            bola.rebaterNoTeto();
        } else if (bola.getY() + bola.getDiametro() >= ALTURA) {
            bola.rebaterNoChao(ALTURA);
        }
    }

    //só rebate se houver interseção E a bola estiver se APROXIMANDO (velocidadeX negativa na esquerda, positiva na direita).
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


    // Ponto: a bola saiu COMPLETAMENTE por uma lateral.Como a bola volta ao centro na hora, o mesmo evento não pontua duas vezes.
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

    // Novo saque: horizontal em direção a quem perdeu o ponto; vertical sorteada.
    private void reiniciarBola(boolean paraDireita) {
        bola.reiniciar(paraDireita, sorteio.nextBoolean());
    }

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
    }

    // Escreve cada pontuação centralizada na sua metade, no alto do campo.
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
