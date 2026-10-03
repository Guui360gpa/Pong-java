import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

public class CampoJogo extends JPanel {

    //Dimenção constantes da Janela
    public static final int LARGURA = 800;
    public static final int ALTURA = 600;

    //Dimenções constantes da Raquete
    private static final int LARGURA_RAQUETE = 15;
    private static final int ALTURA_RAQUETE = 100;
    private static final int MARGEM_RAQUETE = 30;
    private static final int VELOCIDADE_RAQUETE = 6;

    //Intervalo do ciclo do jogo, em milissegundos (-60 atualizações por segundo)
    private static final int INTERVALO_MS = 16;

    //Controla as raquetes
    private final Raquete raqueteJogador;
    private final Raquete raqueteComputador;

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

        configurarTeclado();
        configurarPerdaDeFoco();

        timer = new Timer(INTERVALO_MS, e -> atualizarJogo());
        timer.start();
    }

    private void configurarTeclado() {
        // Subir: W ou seta para cima
        associarTecla(KeyEvent.VK_W, "subir", () -> subindo = true, () -> subindo = false);
        associarTecla(KeyEvent.VK_UP, "subirSeta", () -> subindo = true, () -> subindo = false);

        // Descer: S ou seta para baixo
        associarTecla(KeyEvent.VK_S, "descer", () -> descendo = true, () -> descendo = false);
        associarTecla(KeyEvent.VK_DOWN, "descerSeta", () -> descendo = true, () -> descendo = false);
    }

    //Liga uma tecla a duas ações: uma ao pressionar e outra ao soltar.
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

    //Se a janela perder o foco, solta todos os comandos (evita raquete "presa").
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
        // Se as duas teclas estiverem pressionadas, os comandos se anulam.
        if (subindo && !descendo) {
            raqueteJogador.moverParaCima();
        } else if (descendo && !subindo) {
            raqueteJogador.moverParaBaixo();
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        //Desenha o tracejado branco do meio (LARGURA / 2)
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        int x = LARGURA / 2;

        for (int y = 0; y < ALTURA; y+=30) {
            g.drawLine(x,y,x,y+15);
        }

        raqueteJogador.desenhar(g);
        raqueteComputador.desenhar(g);
    }


}
