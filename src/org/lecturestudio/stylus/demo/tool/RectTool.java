/*
 */
package org.lecturestudio.stylus.demo.tool;

import org.lecturestudio.stylus.StylusAxesData;
import org.lecturestudio.stylus.demo.action.ActionController;
import org.lecturestudio.stylus.demo.action.NewShapeAction;
import org.lecturestudio.stylus.demo.geometry.StylusPoint;
import org.lecturestudio.stylus.demo.model.RectShape;
import org.lecturestudio.stylus.demo.paint.StylusBrush;
import org.lecturestudio.stylus.demo.paint.StylusStroke;
import org.lecturestudio.stylus.demo.render.RenderSurface;

/**
 *
 * @author Neon
 */
public class RectTool implements StylusTool {
	private RenderSurface renderSurface;
	private RectShape shape;
	private StylusBrush brush;
	private StylusStroke stroke;

    public RectTool(StylusBrush brush, RenderSurface renderSurface) {
		this.brush = brush;
		this.renderSurface = renderSurface;
	}

	@Override
	public void execute(ActionController actionController) {
		stroke = new StylusStroke();
		stroke.setChanging(true);

		shape = new RectShape(stroke, new StylusBrush(brush));

		actionController.execute(new NewShapeAction(shape));
	}

	@Override
	public void process(StylusAxesData axesData) {
		StylusPoint p = new StylusPoint(axesData.getX(), axesData.getY(), axesData.getPressure());

		stroke.addPoint(p);

		renderSurface.render(shape);
	}

	@Override
	public void finish() {
		stroke.setChanging(false);
	}
}
