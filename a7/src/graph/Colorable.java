package graph;

import java.awt.Color;

/** A colorable object is an object that has a color field.<br>
 * Getting the color should be open, but setting it is protected, so <br>
 * setting the color can be done only by the game files.
 *
 * Color may or may not have a significance (if not, is just for show).<br>
 * As such, significant color should not be changed while a run is proceeding.<br>
 * 
 * @author MPatashnik */
public interface Colorable {

	/** Return the color of this object. */
	Color color();

	/** Return true iff the color of this object has significance */
	boolean isColorSignificant();
}
