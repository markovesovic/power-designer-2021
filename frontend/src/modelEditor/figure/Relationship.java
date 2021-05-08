package modelEditor.figure;

import java.awt.Graphics;

import classEditor.EditRelationshipDialog;
import classEditor.Env;
import useCaseEditor.EnvUse;

public class Relationship extends FigureGraphic{
	
	private Entity e1, e2;	
	private String to, from;
	private Point_2D drawingDummyPoint;
	
	public Relationship() {
		super("");
	}
	
	public void init(Env env, int x, int y) {
		setColors(env);	    
        setSelected(true);
        setBuilding(true);
        drawingDummyPoint = new Point_2D(x, y);
	}

	public Point_2D getA() {
		if(e2==null)
			return e1.getCenter();
		else 
			return getXYLesserPoint(e1, e2);
	}
	 public Point_2D getB() {
		if(e2==null)
			return drawingDummyPoint;
		else 
			return getXYGreaterPoint(e1, e2);
	}
	
	private Point_2D getXYLesserPoint(Entity e1, Entity e2) {
		int x, y;
		if(e1.getXDistance(e2) > e1.getYDistance(e2)) {
			if(e1.getTopLeft().getX() < e2.getTopLeft().getX()) {
				x = e1.getCenter().getX() + e1.getWidth()/2;
				y = e1.getCenter().getY();
			}
			else {
				x = e2.getCenter().getX() + e2.getWidth()/2;
				y = e2.getCenter().getY();
			}
		}
		else {
			if(e1.getTopLeft().getY() < e2.getTopLeft().getY()) {
				x = e1.getCenter().getX();
				y = e1.getCenter().getY() + e1.getHeight()/2;
			}
			else {
				x = e2.getCenter().getX();
				y = e2.getCenter().getY() + e2.getHeight()/2;
			}
		}
		return new Point_2D(x, y);
	}
	
	private Point_2D getXYGreaterPoint(Entity e1, Entity e2) {
		int x, y;
		if(e1.getXDistance(e2) > e1.getYDistance(e2)) {
			if(e1.getTopLeft().getX() > e2.getTopLeft().getX()) {
				x = e1.getCenter().getX() - e1.getWidth()/2;
				y = e1.getCenter().getY();
			}
			else {
				x = e2.getCenter().getX() - e2.getWidth()/2;
				y = e2.getCenter().getY();
			}
		}
		else {
			if(e1.getTopLeft().getY() > e2.getTopLeft().getY()) {
				x = e1.getCenter().getX();
				y = e1.getCenter().getY() - e1.getHeight()/2;
			}
			else {
				x = e2.getCenter().getX();
				y = e2.getCenter().getY() - e2.getHeight()/2;
			}
		}
		return new Point_2D(x, y);
	}
			
	public Point_2D getCenter() {
		return new Point_2D ((getA().getX()+getB().getX())/2, (getA().getY()+getB().getY())/2);
	}		
	
	public void moveA(int dx, int dy) {
		getA().move(dx, dy);		
	}
	
	public void moveB(int dx, int dy) {
		getB().move(dx, dy);		
	}

	public void draw(Graphics g,int number) {
		g.setColor(getStrokeForCurrentState());
		g.drawLine(getA().getX(), getA().getY(), getB().getX(), getB().getY());
		afterDraw(g);
	}
	
	public boolean contain(Point_2D p) {
		return( Point_2D.distance(getA(), p) + Point_2D.distance(p, getB())) <= Point_2D.distance(getA(), getB())+0.5;
	}
	
	public Entity getEntity1() {
		return e1;
	}

	public Entity getEntity2() {
		return e2;
	}

	public void setEntity1(Entity e) {
		e1 = e;
	}
	
	public void setEntity2(Entity e) {
	    e2 = e;
	}
	
    public boolean canBeFinished() {
    	return e2!=null;
    }

    
	@Override
	public boolean onFigureFinish() {
		return new EditRelationshipDialog(this).showDialog();
	}

    private void setDrawingDummyPoint(int x, int y) {
    	drawingDummyPoint.setX(x);
		drawingDummyPoint.setY(y);
	}

	@Override
	public void onMovePoint(int x, int y) {
		setDrawingDummyPoint(x, y);
	}

	@Override
	public String getShapeName() {
		return "Line";
	}

	@Override
	public void move(int dx, int dy) {
		getA().move(dx, dy);
		getB().move(dx, dy);
		
	}

	@Override
	public String getName() {
		return from+"-"+to;
	}
	
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		if(to!=null)
			this.to = to;
		else to = "";
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public void init(EnvUse env, int x, int y) {
		setColors(env);	    
        setSelected(true);
        setBuilding(true);
        drawingDummyPoint = new Point_2D(x, y);		
	}

	

}
