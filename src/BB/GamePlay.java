package BB;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;


public class GamePlay extends JPanel implements KeyListener, ActionListener  {

	private boolean play = false;
	private int score = 0;
	
	private int totalBricks = 21;
	
	private Timer timer;
	private int delay = 8;
	
	private int playerX = 310;
	
	private int ballPosX = 120;
	private int ballPosY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
	
	private MapGenerator map;
	
	public GamePlay() {
		map = new MapGenerator(3, 7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		//Painting background
		g.setColor(Color.white);
		g.fillRect(1, 1, 692, 592);
		
		map.draw((Graphics2D) g);
		
		//Painting borders
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 562);
		
		//Painting the pedal
		g.setColor(Color.blue);
		g.fillRect(playerX, 550, 100, 8);
		
		//Painting the ball
		g.setColor(Color.green);
		g.fillOval(ballPosX, ballPosY, 20, 20 );
		
		//Painting the bricks
		g.setColor(Color.black);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString(""+score, 590, 30);
		
		//Printing the win message and restart
		if(totalBricks == 0) {
			play = false;
			ballXdir = 0;
			ballXdir = 0;
			
			g.setColor(Color.green);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("¡You Won! Score: "+score, 190, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 230, 350);
		}
		
		//Printing loose message and restart
		if(ballPosY > 570) {
			play = false;
			ballXdir = 0;
			ballXdir = 0;
			
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over, score: "+score, 190, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 230, 350);
		}
		
		g.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		timer.start();
		
		if(play) {
			// Ball pedal interaction
			if(new Rectangle(ballPosX, ballPosY, 20, 30).intersects(new Rectangle(playerX, 550, 100, 8)))
				ballYdir = -ballYdir;
			
			// Ball brick interaction
			for(int i=0; i<map.map.length; i++) 
				for(int j=0; j<map.map[0].length; j++) {
					if(map.map[i][j] > 0) {
						int brickX = j*map.brickWidth + 80;
						int brickY = i*map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect)) {
							map.setBrickVaule(0, i, j);
							totalBricks--;
							score+=5;
							
							if(ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width)
								ballXdir = -ballXdir;
							else
								ballYdir = -ballYdir;
						}
					}
				} 
			
			ballPosX += ballXdir;
			ballPosY += ballYdir;
			if(ballPosX < 0)
				ballXdir = -ballXdir;
			if(ballPosY < 0)
				ballYdir = -ballYdir;
			if(ballPosX > 670)
				ballXdir = -ballXdir;
			if(ballPosY > 670)
				ballXdir = -ballXdir;
		}
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent keyPressed) {
		
		//Pressing right key
		if (keyPressed.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(playerX >= 600)
				playerX = 600;
			else
				moveRight();
		}
		
		//Pressing left key
		if (keyPressed.getKeyCode() == KeyEvent.VK_LEFT) {
			if(playerX < 10)
				playerX = 10;
			else
				moveLeft();
		}
		
		// Pressing ENTER
		if(keyPressed.getKeyCode() == KeyEvent.VK_ENTER && !play) {
			play = true;
			ballPosX = 120;
			ballPosY = 350;
			ballXdir = -1;
			ballPosY = -2;
			score = 0;
			totalBricks = 21;
			map = new MapGenerator(3, 7);
			
			repaint();
		}
		
		//increment speed
		if(keyPressed.getKeyCode() == KeyEvent.VK_UP) {
			ballXdir*=2;
			ballYdir*=2;
		}	
		if(keyPressed.getKeyCode() == KeyEvent.VK_DOWN && ballXdir < -1) {
			ballXdir/=2;
			ballYdir/=2;
		}
		
		
	}

	private void moveLeft() {
		play = true;
		playerX -= 20;
	}

	private void moveRight() {
		play = true;
		playerX += 20;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
