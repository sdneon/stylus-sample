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

package org.lecturestudio.stylus.demo.tool;

import static java.util.Objects.nonNull;

import java.util.function.Consumer;

import static org.lecturestudio.stylus.StylusButton.LEFT;

import org.lecturestudio.stylus.StylusCursor;
import org.lecturestudio.stylus.StylusEvent;
import org.lecturestudio.stylus.StylusListener;
import org.lecturestudio.stylus.demo.action.ActionController;
import org.lecturestudio.stylus.demo.paint.StylusBrush;
import org.lecturestudio.stylus.demo.render.RenderSurface;

public class ToolController implements StylusListener {

	private final ActionController actionController;
	private RenderSurface renderSurface;
	private Consumer<StylusEvent> eventListener;
	private Consumer<StylusCursor> cursorListener;
	private StylusBrush brush, brush2;
	private StylusTool tool;
	private boolean process = false;
    private boolean lineMode = true;
    protected static ToolController instance = null;

	public ToolController(ActionController actionController) {
		this.actionController = actionController;
		this.brush = new StylusBrush();
        this.brush2 = new StylusBrush();
	}
    
    public static ToolController getInstance()
    {
        if (instance == null)
            instance = new ToolController(ActionController.getInstance());
        return instance;
    }

	@Override
	public void onCursorChange(StylusEvent event) {
		notifyCursorListeners(event.getCursor());
		notifyEventListeners(event);
	}

	@Override
	public void onCursorMove(StylusEvent event) {
		if (process) {
			process(event);
		}

		notifyEventListeners(event);
	}

	@Override
	public void onButtonDown(StylusEvent event) {
		process = true;

		begin(event);
		notifyEventListeners(event);
	}

	@Override
	public void onButtonUp(StylusEvent event) {
		process = false;

		finish(event);
		notifyEventListeners(event);
	}

	public void setStylusEventListener(Consumer<StylusEvent> listener) {
		eventListener = listener;
	}

	public void setStylusCursorListener(Consumer<StylusCursor> listener) {
		cursorListener = listener;
	}

	public StylusBrush getBrush() {
		return brush;
	}

	public StylusBrush getBrush2() {
		return brush2;
	}

	public void setBrush(StylusBrush brush) {
		this.brush = brush;
	}

    public void setLineMode(boolean lineMode) {
        this.lineMode = lineMode;
    }
    
	public ActionController getActionController() {
		return actionController;
	}

	public void setRenderSurface(RenderSurface renderSurface) {
		this.renderSurface = renderSurface;
	}

	public void clear() {
		executeTool(new ClearTool());
	}

	public void undo() {
		executeTool(new UndoTool());
	}

	public void redo() {
		executeTool(new RedoTool());
	}

	private void begin(StylusEvent event) {
		switch (event.getCursor()) {
			case PEN:
			case MOUSE:
                if (event.getButton() == LEFT)
                {
                    if (lineMode)
                        tool = new PenTool(brush, renderSurface);
                    else
                        tool = new RectTool(brush, renderSurface);
                }
                else
                {
                    if (lineMode)
                        tool = new PenTool(brush2, renderSurface);
                    else
                        tool = new RectTool(brush2, renderSurface);
                }
				break;

			case ERASER:
				tool = new EraserTool(brush, renderSurface);
				break;

			case NONE:
				break;
		}

		executeTool(tool);
		process(event);
	}

	private void process(StylusEvent event) {
		tool.process(event.getAxesData());
	}

	private void finish(StylusEvent event) {
		process(event);
		tool.finish();
	}

	public void executeTool(Tool tool) {
		tool.execute(actionController);
	}

	private void notifyEventListeners(StylusEvent event) {
		if (nonNull(eventListener)) {
			eventListener.accept(event);
		}
	}

	private void notifyCursorListeners(StylusCursor cursor) {
		if (nonNull(cursorListener)) {
			cursorListener.accept(cursor);
		}
	}

    public RenderSurface getRenderSurface() {
        return renderSurface;
    }
}
