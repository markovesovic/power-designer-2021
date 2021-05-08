package classEditor;

import modelEditor.figure.Entity;
import modelEditor.figure.FigureGraphic;
import modelEditor.figure.Relationship;

public enum Mode {
	MOVE, 
	SELECT, 
	DELETE,
	DRAW_LINE,
	DRAW_RECTANGLE,
	DRAW_ACTOR;
	
	public Class<? extends FigureGraphic> getDrawClass() {
		switch(this) {
		case DRAW_RECTANGLE: return Entity.class;
		case DRAW_ACTOR: return Entity.class;
		case DRAW_LINE: return Relationship.class;
		default:
			break;
		}
		return null;
	}
}