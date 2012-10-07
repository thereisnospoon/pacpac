package pacpac;

import javax.swing.JFrame;

class PacPac extends JFrame {
	
	public static void main(String[] args) {
		PacPac t = new PacPac();
		t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		t.setSize(435, 600);
		Field f = new Field();
		f.setFocusable(true);
		t.add(f);
		t.setResizable(false);
		t.setVisible(true);
	}
}