package rustGuardian.main;

import java.awt.Point;
import java.util.ArrayList;

import javafx.geometry.Point3D;

public class MapChunk extends AbstractGrid3D<Tile> {
	private static final long serialVersionUID = 1060623638149583738L;
	Tile fillStruct = Tile.FLOOR;
	Tile border = Tile.WALL;

	public Tile border() {
		return border;
	}

	public MapChunk(double length, double width, double height) {
		super((int) length, (int) width, (int) height);
		fill();
	}

	public MapChunk(int length, int width, int height) {
		super(length, width, height);
		fill();
		// drawLine(new Point(5,5), new Point(80, 24));
	}

	public MapChunk(Point3D fullPoint) {
		super(fullPoint.getX(), fullPoint.getY(), fullPoint.getZ());
		fill();
	}

	MapChunk(MapChunk copyChunk) {
		super(copyChunk.length(), copyChunk.width(), copyChunk.height());
		fill();
	}

	/**
	 * Fills the entire map with specified tile
	 * 
	 * @param c
	 *            tile which fills the entire map
	 */
	@Override
	public void fill() {
		for (int a = 0; a <= super.height(); a++) {
			add(new ArrayList<ArrayList<Tile>>());
			for (int b = 0; b <= super.width(); b++) {
				get(a).add(new ArrayList<Tile>());
				for (int c = 0; c <= super.length(); c++) {
					get(a).get(b).add(fillStruct);
				}
			}
		}
	}

	/**
	 * Returns a char Array of the symbols of the tiles on this MapChunk
	 */
	public char[][] toCharArray() {
		char[][] charReturn = new char[super.width()][super.length()];
		for (int y = 0; y < super.width(); y++) {
			for (int x = 0; x < super.width(); x++) {
				charReturn[y][x] = this.unitAt(new Point(x, y)).symbol();
			}
		}
		return charReturn;
	}

	/**
	 * A 2d line drawing algorithm that takes two points an draws a line between
	 * them It determines the slope of the segment and then simplifies the slope
	 * With a nested for loop it draws a segment as many times as the slope has been
	 * simplified It then breaks the slope segments up by each square and adds the
	 * proper slice of the slope From here, tilePlace is called to place the tile
	 * 
	 * @param startPoint
	 *            Line goes from this point on the map
	 * @param endPoint
	 *            to this point on the map
	 */
	/*
	 * public void drawLine(Point startPoint, Point endPoint) { double dx =
	 * endPoint.x - startPoint.x; // System.out.println("dx:" + dx); double dy =
	 * endPoint.y - startPoint.y; // System.out.println("dy:" + dy); double rise;
	 * double run; int simpFactor = Util.GCD(Math.abs(dx), Math.abs(dy)); //
	 * System.out.println("simpFactor:" + simpFactor); if (dx < 0 && dy < 0) { rise
	 * = Math.abs(dy); run = Math.abs(dx); } else if (dx < 0 && !(dy < 0)) { rise =
	 * (int) dy * -1; run = Math.abs(dx); } else { rise = dy; run = dx; } rise /=
	 * simpFactor; run /= simpFactor; double slopeMax = Math.max(Math.abs(rise),
	 * Math.abs(run)); // System.out.println("slopeMax:" + slopeMax); //
	 * System.out.println("rise:" + rise); System.out.println("run:" +run); double x
	 * = startPoint.x; double y = startPoint.y; place(startPoint, Tile.WALL); for
	 * (int n = 1; n <= simpFactor; n++) { for (int m = 1; m <= slopeMax; m++) { try
	 * { y += rise / slopeMax; x += run / slopeMax; // System.out.println("x:" + x);
	 * System.out.println("y:" + y); place(new Point((int) x, (int) y), Tile.WALL);
	 * } catch (IndexOutOfBoundsException e) { break; } } } }
	 */

	/**
	 * Scans the map to create a list of 1 or 0 for each tile, representing whether
	 * or not the tile is opaque or transparent
	 * 
	 * @return
	 */
	public int[][][] opaqueScan() {
		int[][][] opaqueMap = new int[height()][width()][length()];
		for (int z = 0; z < super.height(); z++) {
			for (int y = 0; y < super.width(); y++) {
				for (int x = 0; x < super.length(); x++) {
					if (!(unitAt(new Point3D(x, y, z)).transparent())) {
						opaqueMap[z][y][x] = 1;
					} else {
						opaqueMap[z][y][x] = 0;
					}
				}
			}
		}
		return opaqueMap;
	}

	/**
	 * Scans the map to create a list of 1 or 0 for each tile, representing whether
	 * or no the tile is passable or impassible
	 * 
	 * @return
	 */
	public int[][][] blockingScan() {
		int[][][] blockMap = new int[height()][width()][length()];
		for (int z = 0; z < height(); z++) {
			for (int y = 0; y < width(); y++) {
				for (int x = 0; x < length(); x++) {
					if (!(unitAt(new Point3D(x, y, z)).passable())) {
						blockMap[z][y][x] = 1;
					} else {
						blockMap[z][y][x] = 0;
					}
				}
			}
		}
		return blockMap;
	}
	/*
	 * public int[][] calculateFOV(int startx, int starty, int radius) { int[][]
	 * visMap = new int[super.width()][super.length()]; visMap[starty][startx] = 1;
	 * 
	 * for (Direction d : Direction.DIAGONAL) { castLight(1, 1.0f, 0.0f, 0,
	 * d.xOffset(), d.yOffset(), 0, radius, startx, starty, visMap,
	 * this.opaqueScan()); castLight(1, 1.0f, 0.0f, d.xOffset(), 0, 0, d.yOffset(),
	 * radius, startx, starty, visMap, this.opaqueScan()); } return visMap; }
	 * 
	 * /**
	 * 
	 * @TODO Stop the scan from exceeding the boundaries of the map. This causes the
	 * program to screw up. After this the algorithm should function correctly
	 */
	/*
	 * private int[][] castLight(int row, float start, float end, int xx, int xy,
	 * int yx, int yy, int radius, int startx, int starty, int[][] visMap, int[][]
	 * opaqueMap) { float newStart = 0.0f; if (start < end) { return visMap; }
	 * boolean blocked = false; for (int distance = row; distance <= radius &&
	 * distance < super.width() + super.length(); distance++) { int deltaY =
	 * -distance; for (int deltaX = -distance; deltaX <= 0; deltaX++) { int currentX
	 * = startx + (deltaX * xx) + (deltaY * xy); int currentY = starty + (deltaX *
	 * yx) + (deltaY * yy); float leftSlope = (deltaX - 0.5f) / (deltaY + 0.5f);
	 * float rightSlope = (deltaX + 0.5f) / (deltaY - 0.5f); if (!(currentX >= 0 &&
	 * currentY >= 0 && currentX < super.width() && currentY < super.length() ||
	 * start < rightSlope)) { continue; } else if (end > leftSlope || currentX < 0
	 * || currentY < 0) { break; } if (distance <= radius) {
	 * visMap[currentY][currentX] = 1; } if (blocked) { if
	 * (opaqueMap[currentY][currentX] >= 1) { newStart = rightSlope; } else {
	 * blocked = false; start = newStart; } } else { if
	 * (opaqueMap[currentY][currentX] >= 1 && distance < radius) { blocked = true;
	 * visMap = castLight(distance + 1, start, leftSlope, xx, xy, yx, yy, radius,
	 * startx, starty, visMap, opaqueMap); newStart = rightSlope; } } } } return
	 * visMap; }
	 */
}
