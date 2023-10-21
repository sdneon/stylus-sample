/*
 */
package org.lecturestudio.stylus.demo.awt.render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import org.lecturestudio.stylus.demo.model.ComicsFrameShape;
import org.lecturestudio.stylus.demo.render.RenderSurfaceRenderer;

/**
 *
 * @author Neon
 */
public class ComicsFrameRenderer implements RenderSurfaceRenderer<ComicsFrameShape, Graphics2D> {
    @Override
	public void render(ComicsFrameShape shape, Graphics2D gc) {
        Color color = shape.getColor();
        float width = (float)shape.getThickness();

        Rectangle bounds = gc.getClipBounds();
        double imgW = bounds.getWidth(),
            imgH = bounds.getHeight();
        int imgHalfW = (int)(imgW * 0.5),
            imgHalfH = (int)(imgH * 0.5);

        gc.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
        gc.setStroke(new BasicStroke(width));
        gc.setColor(color);
        gc.drawLine(imgHalfW, 0, imgHalfW, (int)imgH);
        gc.drawLine(0, imgHalfH, (int)imgW, imgHalfH);
    }
}
