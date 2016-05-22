package gent.timmos.mvntry;

import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Main entrypoint.
 */
public class App implements Runnable {
	
	private App (){
		
	}
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new App());
    }

	public void run() {
		JFrame frame = new JFrame ("3D engine");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		Engine engine = new PongEngine();
		
		frame.createBufferStrategy(2);
		BufferStrategy strategy = frame.getBufferStrategy();
		InnerEngine inner = new InnerEngine(frame, strategy, engine);
		inner.start();
	}
}
