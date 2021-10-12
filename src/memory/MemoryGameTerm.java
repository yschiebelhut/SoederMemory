package memory;

import misc.InputHelper;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Matrikel-Nr. 3354235
 */
public class MemoryGameTerm extends JFrame {

	private ArrayList<JToggleButton> buttons = new ArrayList<>();
	private ArrayList<String> ids = new ArrayList<>();
	private ArrayList<JToggleButton> selected = new ArrayList<>();
	private JPanel panelPlayer = new JPanel();

	public MemoryGameTerm(MemoryGame game) {
		Thread t = new Thread(() -> {
			int time = 0;
			while(!this.finished()) {
				this.setTitle("Soeder Memory (" + time + ")");
				time++;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();

//		this.setTitle("Soeder Memory");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		game.setCurrentPlayer(game.getPlayers().get(0));
		genPlayerLabels(game);
		this.add(panelPlayer, BorderLayout.NORTH);

		JPanel panelCards = new JPanel(new GridLayout(game.getRows(), game.getCols(), 5, 5));
		ArrayList<MemoryImages.MemoryImage> cards = new ArrayList<>();
		for (MemoryImages.MemoryImage image :
				game.getImages()) {
			cards.add(image);
			cards.add(image);
		}
		Collections.shuffle(cards);
		int blankPosition = -1;
		if (game.isBlankRequired()) {
			blankPosition = InputHelper.getRandomInt(0, cards.size());
		}
		int listPosition = 0;
		for (int i = 0; i < game.getRows() * game.getCols(); i++) {
			JToggleButton button = new JToggleButton();
			button.setIcon(MemoryImages.getBackside());
			if (i == blankPosition) {
				button.setSelectedIcon(MemoryImages.getBlank());
				button.setActionCommand("blank");
			} else {
				button.setSelectedIcon(cards.get(listPosition).getImage());
				button.setActionCommand(cards.get(listPosition).getId());
				listPosition++;
			}
			button.addActionListener(e -> {
				this.ids.add(button.getActionCommand());
				this.selected.add(button);
			});
			buttons.add(button);
			panelCards.add(button);
		}
		this.add(panelCards, BorderLayout.CENTER);

		this.pack();
		this.setVisible(true);


		int rounds = 0;
		while (!finished()) {
			rounds++;
			while (ids.size() < 2) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (ids.get(0).equals(ids.get(1))) {
				game.getCurrentPlayer().addPoint();
				for (JToggleButton b : selected) {
					b.setEnabled(false);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Sorry, those don't match", "Wrong", JOptionPane.ERROR_MESSAGE);
				for (JToggleButton b : selected) {
					b.setSelected(false);
				}
				game.nextPlayer();
			}
			ids = new ArrayList<>();
			selected = new ArrayList<>();
			genPlayerLabels(game);
		}
		for (JToggleButton b :
				buttons) {
			b.setSelected(true);
		}
		for (Player p :
				game.getPlayers()) {
			p.setStatus(PlayerStatus.FINISHED);
		}
		genPlayerLabels(game);
		this.pack();

		StringBuilder savedGames = new StringBuilder();
		try (BufferedReader r = new BufferedReader(new FileReader("memory.txt"))) {
			while (r.ready()) {
				savedGames.append(r.readLine());
				savedGames.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		game.getPlayers().sort((a, b) -> {
			return b.getPoints() - a.getPoints();
		});
		String message = "Game ends after " + rounds + " rounds: ";
		for (int i = 0; i < game.getPlayers().size() - 1; i++) {
			Player p = game.getPlayers().get(i);
			message += p.getName() + " (" + p.getPoints() + "), ";
		}
		Player lastPlayer = game.getPlayers().get(game.getPlayers().size() - 1);
		message += lastPlayer.getName() + " (" + lastPlayer.getPoints() + ")";

		try (PrintWriter w = new PrintWriter(new FileWriter("memory.txt", true))) {
			w.println(message);
		} catch (IOException e) {
			e.printStackTrace();
		}

		message += "\n\n";
		message += "Last games:\n";
		message += savedGames;
		JOptionPane.showMessageDialog(this, message, "Score", JOptionPane.PLAIN_MESSAGE);
	}

	private void genPlayerLabels(MemoryGame game) {
		panelPlayer.removeAll();
		panelPlayer.setLayout(new GridLayout(game.getPlayers().size(), 1, 5, 5));
		for (Player p : game.getPlayers()) {
			JLabel label = new JLabel(p.getName() + " (" + p.getPoints() + ")");
			label.setForeground(p.getStatus().color);
			panelPlayer.add(label);
		}
		this.pack();
	}

	private boolean finished() {
		if (this.buttons.size()==0) {
			return false;
		}
		int numEnabled = 0;
		for (JToggleButton b :
				this.buttons) {
			if (b.isEnabled()) {
				numEnabled++;
			}
		}
		return numEnabled <= 1;
	}
}
