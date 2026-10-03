import javax.swing.JFrame;

public class PongGame {

	public static void main(String[] args) {
		
		GameFrame frame = new GameFrame();
		frame.setSize(800,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}