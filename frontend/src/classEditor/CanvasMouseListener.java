package classEditor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import classEditor.Mode;
import metaschemaEditor.figure.Entity;
import metaschemaEditor.figure.FigureGraphic;
import metaschemaEditor.figure.Point_2D;
import metaschemaEditor.figure.Relationship;

public class CanvasMouseListener implements MouseListener, MouseMotionListener {
	
	boolean mouseIsDown = false;
	Env env;
	CanvasArea canvas;
	Point_2D lastPosition;
	
	FigureGraphic buildingFigure = null;
	
	public CanvasMouseListener(CanvasArea c, Env env) {
		this.canvas = c;
		this.env = env;
	}
	
	public FigureGraphic getBuildingFigure() {
		return buildingFigure;
	}
	
	public void finishBuildingFigure() {
		boolean finish = true;
//		if(buildingFigure.canBeFinished())
//			finish = buildingFigure.onFigureFinish();
		if(!finish) env.remove(buildingFigure);
		buildingFigure.setBuilding(false);
		buildingFigure = null;
	}
	
	public void onToolChanged(Mode mode) {
		if(buildingFigure==null) return;
		if(buildingFigure instanceof Entity)
			env.getEntities().remove(buildingFigure);
		else
			env.getRelationships().remove(buildingFigure);
		finishBuildingFigure();
		canvas.repaint();
	}
	
	private Point_2D amassMove(int x, int y) {
		Point_2D move = new Point_2D(0, 0);
		if(lastPosition!=null) {
			move.setX(x - lastPosition.getX());
			move.setY(y - lastPosition.getY());
		}
		lastPosition = new Point_2D(x, y);
		return move;
	}
	
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount()==2) {
			FigureGraphic f = env.getOneByPosition(new Point_2D(e.getX(), e.getY()));
			if(f instanceof Entity) {
				new EditEntityDialog((Entity) f);
				canvas.repaint();
			}
			if(f instanceof Relationship)
				new EditRelationshipDialog((Relationship) f).showDialog();
		}
		else {
			Mode mode = canvas.getMode();
			switch(mode) {
			case SELECT:
				env.selectOneByPosition(new Point_2D(e.getX(), e.getY()));
				env.getToolbox().move.doClick();
				canvas.repaint();
				break;
			default:
				break;
			}
		}
		
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {
		mouseIsDown = true;
		Mode mode = canvas.getMode();
		canvas.setSelection(null);
		FigureGraphic figure = null;
		if(mode==Mode.MOVE) {
			figure = env.getOneByPosition(new Point_2D(e.getX(), e.getY()));
			if(figure!=null && !figure.isSelected()) env.selectFigure(figure);
		}
		
		
		amassMove(e.getX(), e.getY());
		switch(mode) {
		case MOVE:
			if(figure==null) {
				env.unselectAll();
				env.getToolbox().select.doClick();
			}
			for(Entity ent : env.getEntities())
				if(ent.isSelected())
					ent.setTransparent(true);
			break;
		case DRAW_RECTANGLE:
			if(env.getOneByPosition(new Point_2D(e.getX(),e.getY()))!=null)
				break;
			if(buildingFigure==null) {
				try {
					String name = new NewEntityDialog().getName();
					if(name != null) {
						buildingFigure = new Entity();
						buildingFigure.setName(name);
						buildingFigure.getList().add("+ abc");
						buildingFigure.getList().add("- abc");
						buildingFigure.getList().add("+ abc");
						buildingFigure.getList().add("- abc");
						buildingFigure.getList().add("+ abc");
						buildingFigure.getList().add("- abc");
						buildingFigure.init(env, e.getX(), e.getY());					
						env.addEntity((Entity) buildingFigure);
						env.onSelectionChanged();
						finishBuildingFigure();
					}
				}
				catch (Exception exception) {}
			}
			break;
		case DRAW_LINE:
			try {
				buildingFigure = new Relationship();
				buildingFigure.init(env, e.getX(), e.getY());
				Entity ent = env.getEntityByPosition(new Point_2D(e.getX(), e.getY()));
				if(ent != null) {
					((Relationship) buildingFigure).setEntity1(ent);
					env.addRelationship((Relationship) buildingFigure);
					env.onSelectionChanged();
				}
				else buildingFigure = null;
			}
			catch (Exception exception) {}
			break;
		default:
			break;
		}
		canvas.repaint();
	}

	public void mouseReleased(MouseEvent e) {
		mouseIsDown = false;
		Mode mode = canvas.getMode();
		canvas.setSelection(null);
		switch(mode) {
		case SELECT:
			if(env.countSelected()>0)
				env.getToolbox().move.doClick();
			for(Entity ent : env.getEntities())
				ent.setTransparent(false);
			break;
		case MOVE:
			Point_2D move = amassMove(e.getX(), e.getY());
			env.moveSelected(move.getX(), move.getY());
			for(FigureGraphic figure:env.getSelected()) {
				for(Relationship r : env.getRelationships())
				{					
					if(figure.getCenter().getX()==r.getA().getX()&&figure.getCenter().getY()==r.getA().getY())
						r.moveA(e.getX(), e.getY());
					if(figure.getCenter().getX()==r.getB().getX()&&figure.getCenter().getY()==r.getB().getY())
						r.moveB(e.getX(), e.getY());
				}
			}
			for(Entity ent : env.getEntities())
				ent.setTransparent(false);
			break;
		case DRAW_RECTANGLE:
			if(buildingFigure!=null)
				finishBuildingFigure();
			break;
		case DRAW_LINE:
			if(buildingFigure!=null) {
				Entity ent = env.getEntityByPosition(new Point_2D(e.getX(), e.getY()));
				if(ent != null) {
					((Relationship) buildingFigure).setEntity2(ent);
				}
				finishBuildingFigure();
			}
		default:
			break;
		}
		canvas.repaint();
	}

	public void mouseDragged(MouseEvent e) {
		Mode mode = canvas.getMode();
		boolean mustRepaint = true;
		if(mode!=Mode.SELECT) canvas.setSelection(null);
		switch(mode) {
		case SELECT:
			Selection s = new Selection(new Color(120,120,120,150), new Color(120,120,120,50), 
										new Point_2D(e.getX(), e.getY()), lastPosition);
			env.selectPoints(s);
			canvas.setSelection(s);
			break;
		case MOVE:
			Point_2D move = amassMove(e.getX(), e.getY());
			env.moveSelected(move.getX(), move.getY());
			break;
		default:
			if(buildingFigure!=null) buildingFigure.onMovePoint(e.getX(), e.getY());
			else mustRepaint = false;
		}
		if(mustRepaint) canvas.repaint();
	}

	public void mouseMoved(MouseEvent e) {
		if (buildingFigure != null) {
			buildingFigure.onMovePoint(e.getX(), e.getY());
			canvas.repaint();
		}
	}

}
