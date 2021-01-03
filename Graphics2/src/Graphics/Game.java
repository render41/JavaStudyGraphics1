package Graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{
	
	//Variables
	public static JFrame frame;
	private boolean isRunning = true;
	private Thread thread;
	private final int WIDTH = 480, HEIGHT = 360, SCALE = 2;
	private BufferedImage image;
	
	private Spritesheet sheet;
	private BufferedImage[] player;
	private int frames = 0;//Retorno de Frames
	private int maxFrames = 10;//Velocidade em relação aos frames
	private int currentAnimation = 0/*Quantidade mínima de sprites para animar*/;
	private int maxAnimation = 2;//Quantidade total de sprites para animar 
	
	private Graphics g;
	private Graphics2D g2;
	/***/
	
	//Construtor
	public Game() {
		/*Player*/
		sheet = new Spritesheet("/spritesheet.png");
		player = new BufferedImage[2];
		player[0] = sheet.getSprite(0, 0, 16, 16);
		player[1] = sheet.getSprite(16, 0, 16, 16);
		/***/
		
		this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
	}
	
	//Criação da Janela
	public void initFrame() {
		frame = new JFrame("New Window");
		frame.add(this);
		frame.setResizable(false);//Usuário não irá ajustar janela
		frame.pack();
		frame.setLocationRelativeTo(null);//Janela inicializa no centro
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Fechar o programa por completo
		frame.setVisible(true);//Dizer que estará visível
	}
	
	//Threads
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void tick() {
		frames++;
		if(frames > maxFrames) {
			frames = 0;
			currentAnimation++;
			if(currentAnimation >= maxAnimation) {
				currentAnimation = 0;
			}
		}
	}
	
	//O que será mostrado em tela
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();//Sequência de buffer para otimizar a renderização, lidando com performace gráfica
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		g = image.getGraphics();//Renderizar imagens na tela
		g.setColor(new Color(2, 20, 0));
		g.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
		
		/*Set Font*/
		g.setFont(new Font("Helvetica", Font.BOLD, 30));
		g.setColor(Color.white);
		g.drawString("Hello World", 30, 40);
		/***/
		
		/*Render do jogo*/
		g2 = (Graphics2D) g; //Transformei em um tipo g e foi feito um cast com o Graphics2D
		//g2.rotate(Math.toRadians(45), 90+8, 90+8);
		g2.drawImage(player[currentAnimation], 90, 90, null);
		/***/
		
		g.dispose();//Limpar dados de imagem não usados
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE,null);
		bs.show();
	}
	
	//Controle de FPS
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
				tick(); render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: "+frames);
				frames = 0;
				timer += 1000;
			}
		}
		
		stop(); //Garante que todas as Threads relacionadas ao computador foram terminadas, para garantir performance.
		
	}
	
}
