/*
Jsut the inner '+' of a 2x2 comics panel grid.
 */
package org.lecturestudio.stylus.demo.model;

import java.awt.Color;

/**
 *
 * @author Neon
 */
public class ComicsFrameShape implements Shape {
    private Color color;
    int thickness;

    public ComicsFrameShape(Color color, int thickness)
    {
        this.color = color;
        this.thickness = thickness;
    }

    public Color getColor()
    {
        return color;
    }

    public double getThickness()
    {
        return thickness;
    }
}
