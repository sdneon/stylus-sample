/*
 */
package org.lecturestudio.stylus.demo.model;

import org.lecturestudio.stylus.demo.paint.StylusBrush;
import org.lecturestudio.stylus.demo.paint.StylusStroke;

/**
 *
 * @author Neon
 */
public class RectShape implements Shape {
	private final StylusBrush brush;
	private final StylusStroke stroke;

    public RectShape(StylusStroke stroke, StylusBrush brush) {
        this.stroke = stroke;
		this.brush = brush;
    }

	public StylusBrush getBrush() {
		return brush;
	}

	public StylusStroke getStroke() {
		return stroke;
	}
}
