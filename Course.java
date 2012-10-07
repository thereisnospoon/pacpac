package pacpac;

enum Course {
	UP(0), 
	DOWN(1), 
	LEFT(2), 
	RIGHT(3);

	private final int index;

	Course(int index) {
		this.index = index;
	}

	int getIndex() {
		return index;
	}
}