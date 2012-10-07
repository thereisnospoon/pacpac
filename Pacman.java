package pacpac;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

/**
  * Implements Pacman object
  *
  * @author thereisnospoon
  */

public class Pacman extends KeyAdapter implements Being {
	
	private int
			speed = 4,			//number of moves skips via repaints
			iterator = 0,		//current move in period of skiped moves for pacman
			blockSize,			//size of field block in pixels
			animCheker = 0,		//number of anim change skips via repeaints
			animIterator = 2;	//iterator for Image arrays

	public Position position;

	private Field field;

	private Course pacCourse = Course.LEFT;

	private Image[] animMoveDown = {
									new ImageIcon(getClass().getResource("sprites/pacclose.png")).getImage(),
									new ImageIcon(getClass().getResource("sprites/pacdown.png")).getImage(),
									new ImageIcon(getClass().getResource("sprites/paclargedown.png")).getImage(),
									new ImageIcon(getClass().getResource("sprites/pacdown.png")).getImage()
								};

	private Image[] animMoveUp = {
									new ImageIcon(getClass().getResource("sprites/pacclose.png")).getImage(),
									new ImageIcon(getClass().getResource("sprites/pacup.png")).getImage(),
									new ImageIcon(getClass().getResource("sprites/paclargeup.png")).getImage(),
									new ImageIcon(getClass().getResource("sprites/pacup.png")).getImage()
								};

	private Image[] animMoveLeft = {
									new ImageIcon(getClass().getResource("sprites/pacclose.png")).getImage(),
									new ImageIcon(getClass().getResource("sprites/pacleft.png")).getImage(),
									new ImageIcon(getClass().getResource("sprites/paclargeleft.png")).getImage(),
									new ImageIcon(getClass().getResource("sprites/pacleft.png")).getImage()
								};

	private Image[] animMoveRight = {
									new ImageIcon(getClass().getResource("sprites/pacclose.png")).getImage(),
									new ImageIcon(getClass().getResource("sprites/pacright.png")).getImage(),
									new ImageIcon(getClass().getResource("sprites/paclargeright.png")).getImage(),
									new ImageIcon(getClass().getResource("sprites/pacright.png")).getImage()
								};

	private Image currentImg = animMoveLeft[1];
	private Image[] currentAnim = animMoveLeft;

	Pacman(int blockSize, Field field) {
		this.blockSize = blockSize;
		this.field = field;
		position = new Position(9, 12, 1, 1);
	}

	public void toStartPosition() {
		position.setInX(1);
		position.setInY(1);
		position.setX(9);
		position.setY(12);
	}

	public void move(Graphics2D g2d) {

		if (field.arrayField[position.getY()][position.getX()] == 1 && position.getInX() == 1 && position.getInY() == 1) {
			field.arrayField[position.getY()][position.getX()] = 2;
			field.dots--;
		}

		if (field.arrayField[position.getY()][position.getX()] == 3 && position.getInX() == 1 && position.getInY() == 1) {
			field.arrayField[position.getY()][position.getX()] = 2;
			field.dots--;
			field.runTime = 250;
			for (Ghost g: field.ghosts) {
				g.setRunning();
			}
			setSpeed(3);
		}

		animCheker++;
		if (animCheker == 3) {
			currentImg = currentAnim[++animIterator % currentAnim.length];
			animCheker = 0;
		} 

		iterator++;
		if (iterator != speed) {
			draw(g2d);
			return;
		} else {
			iterator = 0;
		}

		switch (pacCourse) {
			
			case UP:
				if (field.arrayField[position.getY() - 1][position.getX()] > 0 || position.getInY() == 2) {
					position.up();
				}
				draw(g2d);
				break;

			case DOWN:
				if ( field.arrayField[position.getY() + 1][position.getX()] > 0|| position.getInY() == 0) {
					position.down();
				}
				draw(g2d);
				break;

			case LEFT:
				if (position.getX() == 0) {
					if ( field.arrayField[position.getY()][18] > 0 || position.getInX() == 2) {
						position.left();
						draw(g2d);
						break;
					}
					
				}
				if ( field.arrayField[position.getY()][position.getX() - 1] > 0 || position.getInX() == 2) {
					position.left();		
				}
				draw(g2d);
				break;

			case RIGHT:
				if (position.getX() == 18) { 
					if ( field.arrayField[position.getY()][0] > 0 || position.getInX() == 0) {
						position.right();
						draw(g2d);
						break;
					}
				}
				if ( field.arrayField[position.getY()][position.getX() + 1] > 0 || position.getInX() == 0) {
					position.right();	
				}
				draw(g2d);
				break;
		}
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}	

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		switch (key) {
			case KeyEvent.VK_LEFT:
				if (tryChangeCourse(Course.LEFT)) {
					currentAnim = animMoveLeft;
				}
				break;

			case KeyEvent.VK_RIGHT:
				if (tryChangeCourse(Course.RIGHT)) {
					currentAnim = animMoveRight;
				}
				break;

			case KeyEvent.VK_UP:
				if (tryChangeCourse(Course.UP)) {
					currentAnim = animMoveUp;
				}
				break;

			case KeyEvent.VK_DOWN:
				if (tryChangeCourse(Course.DOWN)) {
					currentAnim = animMoveDown;
				}
				break;

			case KeyEvent.VK_SPACE:
				if (Field.timer.isRunning()) {
					Field.timer.stop();
				} else {
					Field.timer.start();
				}
				break;

			default:
				break;
		}
	}

	public void draw(Graphics2D g2d) {
		g2d.drawImage(currentImg, position.getX()*blockSize + blockSize/3*position.getInX() - 2, 
			position.getY()*blockSize + blockSize/3*position.getInY() - 2, null);
		// System.out.println("x = " + position.getX());
		// System.out.println("y = " + position.getY());
		// System.out.println("inX = " + position.getInX());
		// System.out.println("inY = " + position.getInY());
	}

	private boolean tryChangeCourse(Course course) {
		switch (course) {

			case UP:
				if (field.arrayField[position.getY() - 1][position.getX()] > 0) {
					position.setInX(1);
					pacCourse = Course.UP;
					return true;
				}

				return false;

			case DOWN:
				if (field.arrayField[position.getY() + 1][position.getX()] > 0) {
					position.setInX(1);
					pacCourse = Course.DOWN;
					return true;
				}
				return false;

			case LEFT:
				if (field.arrayField[position.getY()][position.getX() - 1] > 0) {
					position.setInY(1);
					pacCourse = Course.LEFT;
					return true;
				}
				return false;

			case RIGHT:
				if (field.arrayField[position.getY()][position.getX() + 1] > 0) {
					position.setInY(1);
					pacCourse = Course.RIGHT;
					return true;
				}
				return false;

			default:
				return false;
		}
	}
}