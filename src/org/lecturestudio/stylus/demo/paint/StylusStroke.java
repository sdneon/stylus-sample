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
package org.lecturestudio.stylus.demo.paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lecturestudio.stylus.demo.geometry.StylusPoint;

public class StylusStroke {

	private final List<StylusPoint> points;

	private boolean changing, wasChanging;


	public StylusStroke() {
		this.points = Collections.synchronizedList(new ArrayList<>());
		this.changing = false;
        this.wasChanging = false;
	}

	public void addPoint(StylusPoint point) {
		points.add(point);
	}

	public List<StylusPoint> getPoints() {
		return points;
	}

	public boolean isChanging() {
		return changing;
	}

	public void setChanging(boolean changing) {
		this.changing = changing;
        if (changing) wasChanging = true;
	}
    
	public boolean wasChanging() {
		return wasChanging;
	}
    
	public void resetWasChanging() {
		wasChanging = false;
	}
}
