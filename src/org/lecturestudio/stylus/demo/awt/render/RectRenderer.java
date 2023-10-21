/*
 * Draw rectangle
 */
package org.lecturestudio.stylus.demo.awt.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Iterator;
import java.util.List;
import org.lecturestudio.stylus.demo.awt.beans.converter.ColorConverter;
import org.lecturestudio.stylus.demo.geometry.StylusPoint;
import org.lecturestudio.stylus.demo.model.RectShape;
import org.lecturestudio.stylus.demo.paint.StylusBrush;
import org.lecturestudio.stylus.demo.paint.StylusStroke;
import org.lecturestudio.stylus.demo.render.RenderSurfaceRenderer;

/**
 *
 * @author Neon
 */
public class RectRenderer implements RenderSurfaceRenderer<RectShape, Graphics2D> {
	private StylusStroke lastStroke,
        firstStroke = null;
	private int last;

    @Override
	public void render(RectShape shape, Graphics2D gc) {
		StylusStroke stroke = shape.getStroke();
		StylusBrush brush = shape.getBrush();

		gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gc.setPaint(ColorConverter.INSTANCE.to(brush.getColor()));

		if (stroke.isChanging()) {
            if (firstStroke == null)
                firstStroke = stroke;
			if (lastStroke != stroke) {
				resetState();

				lastStroke = stroke;
			}

			renderProgressive(stroke, brush, gc);
		}
		else {
			resetState();

            /*
            This guard is necessary to prevent stoke drawing over itself again,
            which inadvertently darkens a translucent line drawing!
            While allow for undo/redo to work correctly.
            */
            if (!stroke.wasChanging())
                renderWhole(stroke, brush, gc);
            stroke.resetWasChanging();
            firstStroke = null;
		}
    }

	private void resetState() {
		lastStroke = null;
		last = 0;
	}

    private int getNormalizedPressure(double p)
    {
        if (p <= 0.0) return 1;
        int pn = (int)Math.sqrt(p * 255); //balanced, tending to transparent end
        //int pn = (int)(p * p * 255); //x^2 curve //almost like above but easier to reach opaque
        //int pn = (int)(15.96872 * Math.sqrt(p * 255)); //16x^0.5 curve //most opqaue
        //int pn = (int)Math.exp(p / 46.01838514); //e^(x/46) curve //mostly transparent
        if (pn < 255) return pn;
        return 255;
    }
    
	private void renderProgressive(StylusStroke stroke, StylusBrush brush, Graphics2D gc) {
		List<StylusPoint> points = stroke.getPoints();
        boolean varyAlphaWithPressure = brush.getVaryAlphaWithPressure();
        Color color = ColorConverter.INSTANCE.to(brush.getColor());
        int r = color.getRed(),
            g = color.getGreen(),
            b = color.getBlue();

		synchronized (points) {
			Iterator<StylusPoint> iter = points.listIterator(last);

			if (!iter.hasNext()) {
				return;
			}

			StylusPoint p0 = iter.next();
            StylusPoint p00 = firstStroke.getPoints().get(0);

			if (!iter.hasNext()) {
                double pressure = p0.getPressure();
                if (varyAlphaWithPressure) {
                    color = new Color(r, g, b, getNormalizedPressure(pressure));
                    gc.setColor(color);
                }
				drawRectDot(brush, gc, p0.getX(), p0.getY(), pressure);
			}
			else {
				StylusPoint p1;

				while (iter.hasNext()) {
					p1 = iter.next();

					drawRect(brush, gc, p00, p0, p1);

					p0 = p1;

					last++;
				}
			}
		}
	}

	private void renderWhole(StylusStroke stroke, StylusBrush brush, Graphics2D gc) {
		List<StylusPoint> points = stroke.getPoints();
        boolean varyAlphaWithPressure = brush.getVaryAlphaWithPressure();
        Color color = ColorConverter.INSTANCE.to(brush.getColor());
        gc.setPaint(color);
        int r = color.getRed(),
            g = color.getGreen(),
            b = color.getBlue();

		if (points.isEmpty()) {
			return;
		}

		Iterator<StylusPoint> iter = points.iterator();
		StylusPoint p0 = iter.next(),
            p00 = p0;
		StylusPoint p1;

		if (!iter.hasNext()) {
            double pressure = p0.getPressure();
            if (varyAlphaWithPressure) {
                color = new Color(r, g, b, getNormalizedPressure(pressure));
                gc.setColor(color);
            }
			drawRectDot(brush, gc, p0.getX(), p0.getY(), pressure);
		}

		while (iter.hasNext()) {
			p1 = iter.next();

			drawRect(brush, gc, p00, p0, p1);

			p0 = p1;
		}
	}

	private void drawRect(StylusBrush brush, Graphics2D gc, StylusPoint p00, StylusPoint p0, StylusPoint p1) {
		boolean varyAlphaWithPressure = brush.getVaryAlphaWithPressure();
        Color color = ColorConverter.INSTANCE.to(brush.getColor());
        int r = color.getRed(),
            g = color.getGreen(),
            b = color.getBlue();

		double x0 = p00.getX(),
            x1 = p1.getX();
		double y0 = p00.getY(),
            y1 = p1.getY();
        double w = brush.getWidth(),
            w0 = p00.getPressure() * w,
            d0 = w0 / 2,
            d0y = d0,
            w1 = p1.getPressure() * w,
            d1 = w1 / 2,
            d1y = d1;

        if (x1 < x0)
        {
            //swap
            double t = x0;
            x0 = x1;
            x1 = t;
            t = d0;
            d0 = d1;
            d1 = t;
        }
        
        if (y1 < y0)
        {
            //swap
            double t = y0;
            y0 = y1;
            y1 = t;
            t = d0y;
            d0y = d1y;
            d1y = t;
        }

        double p = p1.getPressure();
        if (varyAlphaWithPressure) {
            color = new Color(r, g, b, getNormalizedPressure(p));
            gc.setColor(color);
        }

        int xx = (int)(x0 - d0),
            yy = (int)(y0 - d0),
            dx = (int)(x1 - x0 + d1 + d0),
            dy = (int)(y1 - y0 + d1y + d0y);
        gc.fillRect(xx, yy, dx, dy);
	}

	private void drawRectDot(StylusBrush brush, Graphics2D gc, double x, double y, double p) {
		double w = p * brush.getWidth();
		double d = w / 2;

		gc.fillRect((int)(x - d), (int)(y - d), (int)w, (int)w);
	}
}
