package classEditor;

import metaschemaEditor.figure.Entity;
import metaschemaEditor.figure.FigureGraphic;
import metaschemaEditor.figure.Relationship;

public enum Mode {
	MOVE, 
	SELECT, 
	DELETE,
	DRAW_LINE,
	DRAW_RECTANGLE;
	
	public Class<? extends FigureGraphic> getDrawClass() {
		switch(this) {
		case DRAW_RECTANGLE: return Entity.class;
		case DRAW_LINE: return Relationship.class;
		default:
			break;
		}
		return null;
	}
}