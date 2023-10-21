package org.lecturestudio.stylus.demo.model;

import java.awt.Dimension;
import java.awt.Image;
import org.lecturestudio.stylus.demo.geometry.StylusPoint;

public class ImageShape implements Shape {
	private final Image image;
    private StylusPoint centre;
    private boolean relativePos;
    private double w = -1.0, h = -1.0;

    /*
    width & height:
      +ve: size in px
      -ve: negate it to get ratio of canvas
    */
	public ImageShape(Image image, StylusPoint centre, boolean relativePos, double width, double height) {
		this.image = image;
        this.centre = centre;
        this.relativePos = relativePos;
        w = width;
        h = height;
	}

	public Image getImage() {
		return image;
	}

	public StylusPoint getCentre() {
		return centre;
	}

    public double getWidth()
    {
        return w;
    }

    public double getHeight()
    {
        return h;
    }

    public boolean isRelativePosition()
    {
        return relativePos;
    }
}
