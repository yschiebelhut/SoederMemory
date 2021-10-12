package memory;

/**
 * @author Matrikel-Nr. 3354235
 */
public class Player {
	private String name;
	private int points = 0;
	private PlayerStatus status = PlayerStatus.WAITING;

	public Player(String name) {
		this.name = name;
	}

	public void addPoint() {
		this.points++;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public PlayerStatus getStatus() {
		return status;
	}

	public void setStatus(PlayerStatus status) {
		this.status = status;
	}
}
