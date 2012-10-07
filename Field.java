package pacpac;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Image;
import java.awt.geom.GeneralPath;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
  * Field represents game board where all actions are taking place
  *
  * @author thereisnospoon
  */

class Field extends JPanel implements ActionListener {

	//array representation of game field 19x22
	public int[][] arrayField = {

	//   0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18	

		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	//0 
		{0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0},	//1
		{0, 3, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 3, 0},	//2
		{0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0},	//3
		{0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},	//4
		{0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0},	//5
		{0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0},	//6
		{0, 0, 0, 0, 1, 0, 0, 0, 2, 0, 2, 0, 0, 0, 1, 0, 0, 0, 0},	//7
		{0, 0, 0, 0, 1, 0, 2, 2, 2, 2, 2, 2, 2, 0, 1, 0, 0, 0, 0},	//8
		{0, 0, 0, 0, 1, 0, 2, 0, 0, 2, 0, 0, 2, 0, 1, 0, 0, 0, 0},	//9
		{1, 1, 1, 1, 1, 2, 2, 0, 2, 2, 2, 0, 2, 2, 1, 1, 1, 1, 1},	//10
		{0, 0, 0, 0, 1, 0, 2, 0, 0, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0},	//11
		{0, 0, 0, 0, 1, 0, 2, 2, 2, 2, 2, 2, 2, 0, 1, 0, 0, 0, 0},	//12
		{0, 0, 0, 0, 1, 0, 2, 0, 0, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0},	//13
		{0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0},	//14
		{0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0},	//15
		{0, 3, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 3, 0},	//16
		{0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0},	//17
		{0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0},	//18
		{0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0},	//19
		{0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},	//20
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}	//21
	};

	private int blockSize = 23,					//graphical block size in pixels
				lives = 3,
				arrayWidth = 19,
				arrayHeight = 22;

	public int dots = -1;

	private boolean initFlag = true;

	private Font font = new Font("Ubuntu Mono", Font.PLAIN, 40);
	private Font infoFont = new Font("Ubuntu Mono", Font.PLAIN, 20);

	private Image imgField= new ImageIcon(getClass().getResource("sprites/field1.png")).getImage();
	private Image live = new ImageIcon(getClass().getResource("sprites/pacright.png")).getImage();

	private Pacman pacman;

	public Ghost[] ghosts;

	public int runTime = 0;

	public static Timer timer;

	Field() {
		timer = new Timer(18, this);
		timer.start();
	}

	public void death() {
		lives--;
		pacman.toStartPosition();
		for (Ghost ghost: ghosts) {
			ghost.toStartPosition();
		}
	}

	//prepare for drawing
	private void graphicalFieldInit(Graphics2D g2d) {
		setBackground(Color.BLACK);

		//Rendering options
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHints(rh);

		//Stokes options
		BasicStroke bs = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2d.setStroke(bs);
	}			

	private void drawGrid(Graphics2D g2d) {
		g2d.setColor(Color.GREEN);
		for (int i = 0; i < arrayWidth; i++) {
			g2d.drawLine(blockSize*(i + 1), 0, blockSize*(i + 1), arrayHeight*blockSize);
		}
		for (int i = 0; i < arrayHeight; i++) {
			g2d.drawLine(0, blockSize*(i + 1), blockSize*arrayWidth, blockSize*(i + 1));
		}
	}

	//tedious drawing of field
	private void drawField(Graphics2D g2d) {
		g2d.drawImage(imgField, 0, 0, this);
	}

	private void drawDot(Graphics2D g2d,int x, int y) {
		g2d.setColor(Color.CYAN);
		g2d.fillOval(x*blockSize + blockSize/3 + 3, y*blockSize + blockSize/3 + 3, blockSize/3 - 3, blockSize/3 - 3);
	}

	private void drawBigDot(Graphics2D g2d, int x, int y) {
		g2d.setColor(Color.ORANGE);
		g2d.fillOval(x*blockSize + blockSize/3, y*blockSize + blockSize/3 , blockSize/3, blockSize/3);		
	}

	private void drawDots(Graphics2D g2d) {
		dots = 0;
		for (int i = 0; i < arrayHeight; i++) {
			for (int k = 0; k < arrayWidth; k++) {
				if (arrayField[i][k] == 1) {
					drawDot(g2d, k, i);
					dots++;
				}
				if (arrayField[i][k] == 3) {
					drawBigDot(g2d, k, i);
					dots++;
				}
			}
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		

		if (dots != 0 && lives != 0) {
		
			if (initFlag) {
				graphicalFieldInit(g2d);
				drawField(g2d);
				pacman = new Pacman(blockSize, this);
				addKeyListener(pacman);
				ghosts = new Ghost[]{ 
					new Ghost(blockSize, this, GhostColor.PINK, pacman),
					new Ghost(blockSize, this, GhostColor.RED, pacman),
					new Ghost(blockSize, this, GhostColor.BLUE, pacman),
					new Ghost(blockSize, this, GhostColor.YELLOW, pacman)
					};
				initFlag = false;
			} else {
				drawField(g2d);
				drawDots(g2d);
				drawInfo(g2d);
				if (runTime > 0) {
					runTime--;
				}
				if (runTime == 1) {
					for (Ghost ghost: ghosts) {
						ghost.setSearching();
					}
					pacman.setSpeed(4);
				}
				if (!isColisions()) {
					for (Ghost ghost: ghosts) {
						ghost.move(g2d);
					}
					pacman.move(g2d);
				} else {
					death();
					return;
				}
				// drawGrid(g2d);
			}
		}

		if (lives == 0) {
			timer.stop();
			drawField(g2d);
			drawDots(g2d);
			g2d.setColor(Color.RED);
			g2d.setFont(font);
			g2d.drawString("DEFEAT", blockSize*7, blockSize*11);
		}

		if (dots == 0) {
			timer.stop();
			drawField(g2d);
			drawDots(g2d);
			g2d.setColor(Color.CYAN);
			g2d.setFont(font);
			g2d.drawString("VICTORY", blockSize*7, blockSize*11);
		}
	}

	private boolean isColisions() {
		for (Ghost g: ghosts) {
			if (g.position.getX() == pacman.position.getX()
				&& g.position.getY() == pacman.position.getY()) {

				if (g.position.getInX() - pacman.position.getInX() < 2
					|| g.position.getInY() - pacman.position.getInY() < 2) {

					if (g.getState() == State.RUNNING) {
						g.toStartPosition();
						return false;
					}
					return true;
				}
			}
		}
		return false;
	}

	private void drawInfo(Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		g2d.setFont(infoFont);
		g2d.drawString("Space for pause", blockSize*12, blockSize*23);
		for(int i = 1; i <= lives; i++) {
			g2d.drawImage(live, blockSize*(i + 1), blockSize*23 - 12, null);
		}
	}

	public void actionPerformed(ActionEvent e) {
		repaint();
	}	
}