package useCaseEditor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import classEditor.Mode;
import useCaseEditor.ModeUse;
import modelEditor.figure.Attributes;
import modelEditor.figure.Entity;
import modelEditor.figure.FigureGraphic;
import modelEditor.figure.Point_2D;
import modelEditor.figure.Relationship;

public class CanvasMouseListenerUse implements MouseListener, MouseMotionListener {
	
	boolean mouseIsDown = false;
	EnvUse env;
	CanvasAreaUse canvas;
	Point_2D lastPosition;
	
	FigureGraphic buildingFigure = null;
	
	public CanvasMouseListenerUse(CanvasAreaUse c, EnvUse env) {
		this.canvas = c;
		this.env = env;
	}
	
	public FigureGraphic getBuildingFigure() {
		return buildingFigure;
	}
	
	public void finishBuildingFigure() {
		boolean finish = true;
		if(!finish) env.remove(buildingFigure);
		buildingFigure.setBuilding(false);
		buildingFigure = null;
	}
	
	public void onToolChanged(ModeUse mode) {
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
				new EditEntityDialogUse((Entity) f);
				canvas.repaint();
			}
			if(f instanceof Relationship)
				new EditRelationshipDialogUse((Relationship) f).showDialog();
		}
		else {
			ModeUse mode = canvas.getMode();
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
		ModeUse mode = canvas.getMode();
		canvas.setSelection(null);
		FigureGraphic figure = null;
		if(mode==ModeUse.MOVE) {
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
					String name = new NewEntityDialogUse().getName();
					if(name != null) {
						buildingFigure = new Entity(Mode.DRAW_RECTANGLE);
						buildingFigure.setName(name);
						buildingFigure.init(env, e.getX(), e.getY());					
						env.addEntity((Entity) buildingFigure);
						env.onSelectionChanged();
						finishBuildingFigure();
					}
				}
				catch (Exception exception) {}
			}
			break;
		case DRAW_ACTOR:
			if(env.getOneByPosition(new Point_2D(e.getX(),e.getY()))!=null)
				break;
			if(buildingFigure==null) {
				try {
					String name = new NewEntityDialogUse().getName();
					if(name != null) {
						buildingFigure = new Entity(Mode.DRAW_ACTOR);
						buildingFigure.setName(name);
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
		ModeUse mode = canvas.getMode();
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
		ModeUse mode = canvas.getMode();
		boolean mustRepaint = true;
		if(mode!=ModeUse.SELECT) canvas.setSelection(null);
		switch(mode) {
		case SELECT:
			SelectionUse s = new SelectionUse(new Color(120,120,120,150), new Color(120,120,120,50), 
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
