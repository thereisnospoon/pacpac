package pacpac;

import java.awt.Graphics2D;

/**
  * Basic interface for PaPac beings
  *
  * @author thereisnospoon
  */

interface Being {

	/**
	  * moves object across the field
	  */
	void move(Graphics2D g2d);

	/**
	  * @param   speed   Object`s speed
	  */
	void setSpeed(int speed);
}