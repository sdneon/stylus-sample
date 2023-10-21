/*
 * Copyright 2016 Alex Andres
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lecturestudio.stylus.demo.awt.render;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import org.lecturestudio.stylus.demo.geometry.StylusPoint;
import org.lecturestudio.stylus.demo.model.ImageShape;
import org.lecturestudio.stylus.demo.render.RenderSurfaceRenderer;

public class ImageRenderer implements RenderSurfaceRenderer<ImageShape, Graphics2D> {

	@Override
	public void render(ImageShape shape, Graphics2D gc) {
		Image image = shape.getImage();

		gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (image != null)
        {
            double desiredW = shape.getWidth(),
                desiredH = shape.getHeight();
            int width = 0,
                height = 0;
            Rectangle bounds = gc.getClipBounds();
            double imgW = bounds.getWidth(), //image.getWidth(null),
                imgH = bounds.getHeight(); //image.getHeight(null);
            if (desiredW <= 0.0)
            {
                width = (int)(imgW * (-desiredW));
            }
            else
            {
                width = (int)desiredW;
            }
            if (desiredH <= 0.0)
            {
                height = (int)(imgH * (-desiredW));
            }
            else
            {
                height = (int)desiredH;
            }
            StylusPoint centre = shape.getCentre();
            double x = centre.getX(),
                y = centre.getY();
            boolean isRelPos = shape.isRelativePosition();
            if (isRelPos)
            {
                x = imgW * x;
                y = imgH * y;
            }
            gc.drawImage(image, (int)x, (int)y, (int)width, (int)height, null);
        }
	}
}
