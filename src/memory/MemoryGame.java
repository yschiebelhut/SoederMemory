package memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Matrikel-Nr. 3354235
 */
public class MemoryGame {
	private int rows;
	private int cols;
	private List<Player> players = new ArrayList<>();
	private List<MemoryImages.MemoryImage> images = new ArrayList<>();
	private Player curPlayer;

	public MemoryGame(List<Player> players, List<MemoryImages.MemoryImage> images, int rows, int cols) throws MemoryException {
		this.rows = rows;
		this.cols = cols;
		if (players.size() < 2) {
			throw new MemoryException("At least two players required");
		}
		this.players = players;
		int neededImages = (rows*cols)/2;
		if (images.size() < neededImages) {
			throw new MemoryException("Too few images available");
		}
		Collections.shuffle(images);
		this.images = images.subList(0, neededImages);
	}

	public boolean isBlankRequired() {
		return (this.cols * this.rows) % 2 == 1;
	}

	public void nextPlayer() {
		curPlayer.setStatus(PlayerStatus.WAITING);
		curPlayer = players.get((players.indexOf(curPlayer) + 1) % players.size());
		curPlayer.setStatus(PlayerStatus.ACTIVE);
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public List<MemoryImages.MemoryImage> getImages() {
		return images;
	}

	public void setImages(List<MemoryImages.MemoryImage> images) {
		this.images = images;
	}

	public Player getCurrentPlayer() {
		return curPlayer;
	}

	public void setCurrentPlayer(Player curPlayer) {
		this.curPlayer = curPlayer;
		this.curPlayer.setStatus(PlayerStatus.ACTIVE);
	}
}
