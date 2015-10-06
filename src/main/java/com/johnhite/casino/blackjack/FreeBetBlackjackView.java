package com.johnhite.casino.blackjack;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FreeBetBlackjackView extends JFrame {

	public FreeBetBlackjackView() {
		super("Free Bet Blackjack");
		this.setSize(805, 625);
		this.setLocationByPlatform(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(new TablePanel());
	}
	
	
	public static class TablePanel extends JPanel {
		private static final long serialVersionUID = -610094304101667708L;
		private Point pc1 = new Point(353, 351);
		private Point pc2 = new Point(383, 351);
		private Point dc1 = new Point(353, 51);
		private Point dc2 = new Point(383, 51);
		private SpriteSheet deck;
		private BufferedImage background;
		
		public TablePanel() {
			super();
			try {
				background = ImageIO.read(ClassLoader.getSystemResourceAsStream("assets/FreeHandTable.png"));
				deck = new SpriteSheet("assets/sprite_deck.png", 140, 190);
			} catch (IOException e) {
				System.out.println(e);
			}
		}

		@Override
		public void paint(Graphics g) {
			try {
				g.drawImage(background, 0, 0, this);
				
				//BufferedImage deck = ImageIO.read(ClassLoader.getSystemResourceAsStream("assets/decksprite.gif"));
				//g.drawImage(deck, (int)pc1.getX(), (int)pc1.getY(), (int)pc1.getX() + (int)CARD_SIZE.getWidth(), (int)pc1.getY() + (int)CARD_SIZE.getHeight(), 0, 0, (int)CARD_SIZE.getWidth(), (int)CARD_SIZE.getHeight(), this);
				deck.getSprite(50).drawAt(g, (int)pc1.getX(), (int)pc1.getY(), 90, 122, this);
				deck.getSprite(23).drawAt(g, (int)pc2.getX(), (int)pc2.getY(), 90, 122, this);
				deck.getSprite(12).drawAt(g, (int)dc1.getX(), (int)dc1.getY(), 90, 122, this);
				deck.getSprite(53).drawAt(g, (int)dc2.getX(), (int)dc2.getY(), 90, 122, this);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
	private static class Sprite {
		//public static final int WIDTH = 81;
		//public static final int HEIGHT = 117;
		public static final int WIDTH = 140;
		public static final int HEIGHT = 190;
		public final int width;
		public final int height;
		public final int x1;
		public final int y1;
		public final int x2;
		public final int y2;
		public BufferedImage image;
		public Sprite(int x1, int y1, int width, int height, BufferedImage image) {
			super();
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x1 + width;
			this.y2 = y1 + height;
			this.width = width;
			this.height = height;
			this.image = image;
		}
		
		public void drawAt(Graphics g, int dx1, int dy1, int destWidth, int destHeight, ImageObserver observer) {
			int rx = 84;
			int ry = 114;
			//g.drawImage(image, dx1, dy1, dx1+WIDTH, dy1+HEIGHT, x1, y1, x2, y2, observer);
			/*BufferedImage resized = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = resized.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g2.drawImage(image, 0, 0, destWidth, destWidth, null);
			g2.dispose();*/
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g.drawImage(image, dx1, dy1, dx1+destWidth, dy1+destHeight, x1, y1, x2, y2, observer);
		}
		
		public void drawAt(Graphics g, int dx1, int dy1, ImageObserver observer) {
			g.drawImage(image, dx1, dy1, dx1+width, dy1+height, x1, y1, x2, y2, observer);
		}
	}
	
	private static class SpriteSheet {
		private int width;
		private int height;
		private BufferedImage sheet;
		
		public SpriteSheet(String image, int spriteWidth, int spriteHeight) throws IOException {
			sheet = ImageIO.read(ClassLoader.getSystemResourceAsStream(image));
			this.height = spriteHeight;
			this.width = spriteWidth;
		}
		
		public Sprite getSprite(int ordinal) {
			/*int row = ordinal / 13;
			int index = ordinal % 13;
			int startX = index * CardSprite.WIDTH;
			int startY = row * CardSprite.HEIGHT;
			return new CardSprite(startX, startY, deck);*/
			return new Sprite(width * ordinal, 0, width, height, sheet);
		}
	}
	
	public static void main(String... args) {
		FreeBetBlackjackView view = new FreeBetBlackjackView();
		
		view.setVisible(true);
	}

}
