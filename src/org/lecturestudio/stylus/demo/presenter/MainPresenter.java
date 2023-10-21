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

package org.lecturestudio.stylus.demo.presenter;

import org.lecturestudio.stylus.demo.awt.view.AwtCanvasView;
import org.lecturestudio.stylus.demo.awt.view.AwtInfoView;
import org.lecturestudio.stylus.demo.awt.view.AwtToolbarView;
import org.lecturestudio.stylus.demo.model.Shapes;
import org.lecturestudio.stylus.demo.tool.ToolController;
import org.lecturestudio.stylus.demo.view.CanvasView;
import org.lecturestudio.stylus.demo.view.InfoView;

import org.lecturestudio.stylus.demo.view.MainView;
import org.lecturestudio.stylus.demo.view.ToolbarView;

public class MainPresenter extends Presenter<MainView> {
	private ToolbarPresenter toolbarPresenter;
	private CanvasPresenter canvasPresenter;
	private InfoPresenter infoPresenter;

    public MainPresenter(MainView view) {
		super(view);
        ToolController toolController = ToolController.getInstance();
        toolbarPresenter = new ToolbarPresenter(new AwtToolbarView(), toolController);
        canvasPresenter = new CanvasPresenter(new AwtCanvasView(),
            Shapes.getInstance(), toolController);
        infoPresenter = new InfoPresenter(new AwtInfoView(), toolController);
	}

	public void close() {
		view.close();
	}

	public void initialize() {
        ToolbarView toolbarView = toolbarPresenter.getView();
		view.setToolbar(toolbarView);
		view.setInfo(infoPresenter.getView());
        CanvasView canvasView = canvasPresenter.getView();
		view.setCanvas(canvasView);
        toolbarView.setCanvas(canvasView);
        ToolController toolController = toolbarPresenter.getToolController();
        toolbarView.setToolController(toolController);
	}
}
