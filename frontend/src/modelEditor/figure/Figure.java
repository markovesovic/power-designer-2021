package modelEditor.figure;

import java.awt.Graphics;

import modelEditor.figure.Point_2D;

public interface Figure
{
	public abstract Point_2D getCenter();

	public abstract void move(int dx, int dy);

	public abstract boolean contain(Point_2D p);
	
	public abstract String toString();

	public abstract void draw(Graphics gx, int number);
}