package FieldViewer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Cell {
	private final int DIMENSIONS = 2;
    private Color color;
	private int xOrigin;
	private int yOrigin;
    private int height;
	private int width;
	private double[] value;
	private double[] position;

	public Cell(int x, int y, int width, int height) {
		this.xOrigin = x;
		this.yOrigin = y;
        this.height = height;
        this.width = width;
	}

	public double[] getValue(){
		return this.value;
	}

	public void setValue(double[] v){
		this.value = v;
	}

	public Color getColor(){
		return this.color;
	}

	public void setColor(Color c){
		this.color = c;
	}

	public double[] getPosition(){
		return this.position;
	}

	public void setPosition(double[] p){
		double[] pos = new double[this.DIMENSIONS];
		pos[0] = p[0];
		pos[1] = p[1];
		this.position = pos;
	}

    public void drawOn(Graphics2D g2) {
		Rectangle box = new Rectangle(xOrigin,yOrigin,width,height);
		g2.setColor(color);
		g2.fill(box);
	}
}