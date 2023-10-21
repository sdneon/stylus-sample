package org.lecturestudio.stylus.demo.tool;

import java.awt.Image;
import org.lecturestudio.stylus.StylusAxesData;
import org.lecturestudio.stylus.demo.action.ActionController;
import org.lecturestudio.stylus.demo.action.NewShapeAction;
import org.lecturestudio.stylus.demo.geometry.StylusPoint;
import org.lecturestudio.stylus.demo.model.ImageShape;
import org.lecturestudio.stylus.demo.model.StrokeShape;
import org.lecturestudio.stylus.demo.paint.StylusBrush;
import org.lecturestudio.stylus.demo.paint.StylusStroke;
import org.lecturestudio.stylus.demo.render.RenderSurface;

public class ClipboardTool implements StylusTool {

	private RenderSurface renderSurface;

    private Image image;
	private ImageShape shape;
    private StylusPoint centre;
    private boolean relativePos;
    private double w, h;

	public ClipboardTool(Image image, StylusPoint centre, boolean relativePos, double width, double height, RenderSurface renderSurface) {
        this.image = image;
        this.centre = centre;
        this.relativePos = relativePos;
        w = width; h = height;
		this.renderSurface = renderSurface;
	}

	@Override
	public void execute(ActionController actionController) {
		shape = new ImageShape(image, centre, relativePos, w, h);

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
