package memory;

import java.awt.*;

/**
 * @author Matrikel-Nr. 3354235
 */
public enum PlayerStatus {
	ACTIVE(Color.ORANGE),
	WAITING(Color.BLACK),
	FINISHED(Color.GRAY);

	public Color color;

	PlayerStatus(Color color) {
		this.color = color;
	}
}
