/*
 */
package org.lecturestudio.stylus.demo.tool;

import java.awt.Color;
import org.lecturestudio.stylus.StylusAxesData;
import org.lecturestudio.stylus.demo.action.ActionController;
import org.lecturestudio.stylus.demo.action.NewShapeAction;
import org.lecturestudio.stylus.demo.geometry.StylusPoint;
import org.lecturestudio.stylus.demo.model.ComicsFrameShape;
import org.lecturestudio.stylus.demo.render.RenderSurface;

/**
 *
 * @author Neon
 */
public class ComicsFrameTool implements StylusTool {
    private RenderSurface renderSurface;
    private ComicsFrameShape shape;
    private Color color;
    int thickness;

    public ComicsFrameTool(Color color, int thickness, RenderSurface renderSurface)
    {
        this.color = color;
        this.thickness = thickness;
        this.renderSurface = renderSurface;
    }

	@Override
	public void execute(ActionController actionController) {
		shape = new ComicsFrameShape(color, thickness);
		actionController.execute(new NewShapeAction(shape));
	}

	@Override
	public void process(StylusAxesData axesData) {
		StylusPoint p = new StylusPoint(axesData.getX(), axesData.getY(), axesData.getPressure());
		renderSurface.render(shape);
	}

	@Override
	public void finish() {}
}
