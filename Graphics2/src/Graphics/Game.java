package Graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{
	
	//Variables
	public static JFrame frame;
	private boolean isRunning = true;
	private Thread thread;
	private final int WIDTH = 640, HEIGHT = 480, SCALE = 1;
	private BufferedImage image;
	

	public Game() {
		this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
	}

	public void initFrame() {
		frame = new JFrame("New Window");
		frame.add(this);
		frame.setResizable(false);//Usuário não irá ajustar janela
		frame.pack();
		frame.setLocationRelativeTo(null);//Janela inicializa no centro
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Fechar o programa por completo
		frame.setVisible(true);//Dizer que estará visível
	}
	

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void tick() {
		
	}
	
	//O que será mostrado em tela
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();//Sequência de buffer para otimizar a renderização, lidando com performace gráfica
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = image.getGraphics();//Renderizar imagens na tela
		g.setColor(new Color(2, 20, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE,null);
		bs.show();
	}
	
	public void run() {
		//Variables
		long lastTime = System.nanoTime();//Usa o tempo atual do computador em nano segundos, bem mais preciso
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;//Calculo exato de Ticks
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		
		//Ruuner Game
		while (isRunning == true) {
			//System.out.println("My game is Running");
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: "+frames);
				frames = 0;
				timer += 1000;
			}
		}
		
	}
	
}
