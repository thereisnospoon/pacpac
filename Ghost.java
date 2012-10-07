package pacpac;

import java.awt.Image;
import java.awt.Graphics2D;

import java.util.Collections;
import java.util.ArrayList;

import javax.swing.ImageIcon;


class Ghost implements Being {

	private int 
				speed = 5,
				blockSize,
				iterator = 0,
				lastSeenX,	
				lastSeenY;

	private Image currentImage;
	private Image[] images = new Image[4];

	
	private Field field;

	public Position position;

	private GhostColor color;

	private Pacman pacman;
	
	private Course ghostCourse = Course.RIGHT;
	
	private State state = State.SEARCHING;

	private Image runImage = new ImageIcon(getClass().getResource("sprites/mortalghost.png")).getImage();

	Ghost(int blockSize, Field field, GhostColor color, Pacman pacman) {
		this.field = field;
		this.blockSize = blockSize;
		this.pacman = pacman;
		this.color = color;
		switch (color) {
			
			case PINK:
				images = new Image[]{
					new ImageIcon(getClass().getResource("sprites/pinkghostup.png")).getImage(), 
					new ImageIcon(getClass().getResource("sprites/pinkghostdown.png")).getImage(),
					new ImageIcon(getClass().getResource("sprites/pinkghostleft.png")).getImage(),
					new ImageIcon(getClass().getResource("sprites/pinkghostright.png")).getImage()
				};
				position = new Position(9, 10, 1, 1);
				break;

			case RED:
				images = new Image[]{
					new ImageIcon(getClass().getResource("sprites/redghostup.png")).getImage(), 
					new ImageIcon(getClass().getResource("sprites/redghostdown.png")).getImage(),
					new ImageIcon(getClass().getResource("sprites/redghostleft.png")).getImage(),
					new ImageIcon(getClass().getResource("sprites/redghostright.png")).getImage()
				};
				position = new Position(8, 10, 1, 1);
				break;

			case YELLOW:
				images = new Image[]{
					new ImageIcon(getClass().getResource("sprites/yellowghostup.png")).getImage(), 
					new ImageIcon(getClass().getResource("sprites/yellowghostdown.png")).getImage(),
					new ImageIcon(getClass().getResource("sprites/yellowghostleft.png")).getImage(),
					new ImageIcon(getClass().getResource("sprites/yellowghostright.png")).getImage()
				};
				position = new Position(10, 10, 1, 1);
				break;

			case BLUE:
				images = new Image[]{
					new ImageIcon(getClass().getResource("sprites/blueghostup.png")).getImage(), 
					new ImageIcon(getClass().getResource("sprites/blueghostdown.png")).getImage(),
					new ImageIcon(getClass().getResource("sprites/blueghostleft.png")).getImage(),
					new ImageIcon(getClass().getResource("sprites/blueghostright.png")).getImage()
				};
				position = new Position(9, 9, 1, 1);
				break;
		}

		currentImage = images[ghostCourse.getIndex()];
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void toStartPosition() {
		state = State.SEARCHING;
		switch (color) {
			case PINK:
				position.setInX(1);
				position.setInY(1);
				position.setX(9);
				position.setY(10);
				break;

			case RED:
				position.setInX(1);
				position.setInY(1);
				position.setX(8);
				position.setY(10);
				break;

			case YELLOW:
				position.setInX(1);
				position.setInY(1);
				position.setX(10);
				position.setY(10);
				break;

			case BLUE:
				position.setInX(1);
				position.setInY(1);
				position.setX(9);
				position.setY(9);
				break;
		}
	}

	public void move(Graphics2D g2d) {
		iterator++;
		if (iterator == speed) {
			iterator = 0;
		} else {
			draw(g2d);
			return;
		}

		switch (state) {
			case SEARCHING:
				search();
				break;

			case HUNTING:
				hunt();
				break;

			case RUNNING:
				run();
				break;
		}
		draw(g2d);
	}

	public void draw(Graphics2D g2d) {
		g2d.drawImage(currentImage, position.getX()*blockSize + blockSize/3*position.getInX() - 2, 
			position.getY()*blockSize + blockSize/3*position.getInY() - 2, null);
		// System.out.println("x = " + position.getX());
		// System.out.println("y = " + position.getY());
		// System.out.println("inX = " + position.getInX());
		// System.out.println("inY = " + position.getInY());
	}

	/* checks if pacman on the same coordinate line
	 * if not - returns -1
	 * if on Y line and pacman lefter from ghost - returns 2
	 * if on Y line and pacman righter - returns 3
	 * if on X line and pacman upper - returns 0
	 * if on X line and pacman downer - returns 1
	 */
	private int isOnLine() {

		if (position.getX() == pacman.position.getX() && position.getInX() == 1 && position.getInY() == 1) {
			if (position.getY() - pacman.position.getY() > 0) {
				return 0;
			}
			else {
				return 1;
			}
		}

		if (position.getY() == pacman.position.getY() && position.getInX() == 1 && position.getInY() == 1) {
			if (position.getX() - pacman.position.getX() > 0) {
				return 2;
			} else {
				return 3;
			}
		} 

		return -1;
	}

	// Checks if pacman visible for ghost. If true changes state to HUNTING
	private boolean isVisible(int k) {
		switch(k) {
		case 1:
			for (int i = position.getY(); i < pacman.position.getY(); i++) {
				if (field.arrayField[i][position.getX()] == 0) {
					return false;
				}
			}
			state = State.HUNTING; 
			ghostCourse = Course.DOWN;
			currentImage = images[ghostCourse.getIndex()];
			lastSeenX = pacman.position.getX();
			lastSeenY = pacman.position.getY();
			return true;

		case 0:
			for (int i = pacman.position.getY(); i < position.getY(); i++) {
				if (field.arrayField[i][position.getX()] == 0) {
					return false;
				}				
			}
			state = State.HUNTING;
			ghostCourse = Course.UP;
			currentImage = images[ghostCourse.getIndex()];
			lastSeenX = pacman.position.getX();
			lastSeenY = pacman.position.getY(); 
			return true;

		case 2:
			for (int i = pacman.position.getX(); i < position.getX(); i++) {
				if (field.arrayField[position.getY()][i] == 0) {
					return false;
				}
			}				
			state = State.HUNTING;
			ghostCourse = Course.LEFT;
			currentImage = images[ghostCourse.getIndex()];
			lastSeenX = pacman.position.getX();
			lastSeenY = pacman.position.getY();
			return true;

		case 3:
			for (int i = position.getX(); i < pacman.position.getX(); i++) {
				if (field.arrayField[position.getY()][i] == 0) {
					return false;
				}
			}
			state = State.HUNTING;
			ghostCourse = Course.RIGHT;
			currentImage = images[ghostCourse.getIndex()];
			lastSeenX = pacman.position.getX();
			lastSeenY = pacman.position.getY();
			return true;

		default:
			return false;
		}
	}

	private void moveOnCourse() {	

		switch (ghostCourse) {
			case UP:
				position.up();
				return;
			case DOWN:
				position.down();
				return;
			case LEFT:
				position.left();
				return;
			case RIGHT:
				position.right();
				return;
		}
	}

	private Course ifCrossroad() {
		ArrayList<Course> courses = new ArrayList<Course>();

		if (position.getInX() == 1 && position.getInY() == 1) {
			if (field.arrayField[position.getY() - 1][position.getX()] > 0 && Course.DOWN != ghostCourse) {
				courses.add(Course.UP);
			}
			if (field.arrayField[position.getY() + 1][position.getX()] > 0 && Course.UP != ghostCourse) {
				courses.add(Course.DOWN);
			}
			if (position.getX() < 18 && field.arrayField[position.getY()][position.getX() + 1] > 0 && Course.LEFT != ghostCourse) {
				courses.add(Course.RIGHT);
			}
			if (position.getX() == 18 && position.getY() == 10 && Course.LEFT != ghostCourse) {
				courses.add(Course.RIGHT);
			}
			if (position.getX() > 0 && field.arrayField[position.getY()][position.getX() - 1] > 0 && Course.RIGHT != ghostCourse) {
				courses.add(Course.LEFT);
			}
			if (position.getX() == 0 && position.getY() == 10 && Course.RIGHT != ghostCourse) {
				courses.add(Course.LEFT);
			}

			if (courses.isEmpty()) {
				switch (ghostCourse) {
					case UP:
						if (field.arrayField[position.getY() - 1][position.getX()] == 0) {
							return Course.DOWN;
						}
						break;

					case DOWN:
						if (field.arrayField[position.getY() + 1][position.getX()] == 0) {
							return Course.UP;
						}
						break;

					case LEFT:
						if (field.arrayField[position.getY()][position.getX() - 1] == 0) {
							return Course.RIGHT;
						}
						break;

					case RIGHT:
						if (field.arrayField[position.getY()][position.getX() + 1] == 0) {
							return Course.LEFT;
						}
						break;

					default:
						break;				 
				}
			}

			if (!courses.isEmpty()) {
				Collections.shuffle(courses);
				Course course = courses.get(0);
				currentImage = images[course.getIndex()];
				return course;
			}
		}

		return ghostCourse;
	}

	private void search() {
		
		if (isVisible(isOnLine())) {
			moveOnCourse();
			return;
		}

		ghostCourse = ifCrossroad();
		currentImage = images[ghostCourse.getIndex()];
		moveOnCourse();
	}

	private void hunt() {
		if (isVisible(isOnLine())) {
			moveOnCourse();
			return;
		} else {
			if (position.getX() == lastSeenX && position.getY() == lastSeenY
				&& position.getInX() == 1 && position.getInY() == 1) {

				state = State.SEARCHING;
				search();
			}
			moveOnCourse();
		}
	}

	private void run() {
		if (isVisible(isOnLine())) {
			boolean flag = false;
			switch (ghostCourse) {
				case UP:
					if (field.arrayField[position.getY() + 1][position.getX()] > 0) {
						ghostCourse = Course.DOWN;
						flag = true;
					}
					break;

				case DOWN:
					if (field.arrayField[position.getY() - 1][position.getX()] > 0) {
						ghostCourse = Course.UP;
						flag = true;
					}
					break;

				case LEFT:
					if (field.arrayField[position.getY()][position.getX() + 1] > 0) {
						ghostCourse = Course.RIGHT;
						flag = true;
					}
					break;

				case RIGHT:
					if (field.arrayField[position.getY()][position.getX() - 1] > 0) {
						ghostCourse = Course.LEFT;
						flag = true;
					}			
					break;
			}
			state = State.RUNNING;
			currentImage = runImage;
			if (flag) {
				moveOnCourse();
				return;
			}
		}

		ghostCourse = ifCrossroad();
		currentImage = runImage;
		moveOnCourse();
	}

	public void setRunning() {
		state = State.RUNNING;
	}

	public void setSearching() {
		state = State.SEARCHING;
	}

	public State getState() {
		return state;
	}
} 