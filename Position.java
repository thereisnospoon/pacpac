package pacpac;

class Position {
	
	private int
				arrayX,
				arrayY,
				inX,
				inY;

	Position(int arrayX, int arrayY, int inX, int inY) {
		this.arrayX = arrayX;
		this.arrayY = arrayY;
		this.inX = inX;
		this.inY = inY;
	}

	public void up() {
		inY--;
		if (inY == -1) {
			inY = 2;
			arrayY--;
			if (arrayY < 0) {
				arrayY = 21;				
			}
		}
	}

	public void down() {
		inY++;
		if (inY == 3) {
			inY = 0;
			arrayY++;
			if (arrayY > 21) {
				arrayY = 0;
			}
		}
	}

	public void left() {
		inX--;
		if (inX == -1) {
			inX = 2;
			arrayX--;
			if (arrayX < 0) {
				arrayX = 18;
			}
		}
	}

	public void right() {
		inX++;
		if (inX == 3) {
			inX = 0;
			arrayX++;
			if (arrayX == 19) {
				arrayX = 0;
			}
		}
	}
	public int getX() {
		return arrayX;
	} 

	public int getY() {
		return arrayY;
	}

	public int getInX() {
		return inX;
	}

	public int getInY() {
		return inY;
	}

	public void setInY(int y) {
		inY = y;
	}

	public void setInX(int x) {
		inX = x;
	}

	public void setX(int x) {
		arrayX = x;
	}

	public void setY(int y) {
		arrayY = y;
	}
}