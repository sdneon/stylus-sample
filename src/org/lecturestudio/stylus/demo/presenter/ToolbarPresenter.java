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

import org.lecturestudio.stylus.demo.action.ActionController;
import org.lecturestudio.stylus.demo.tool.ToolController;
import org.lecturestudio.stylus.demo.view.ToolbarView;

public class ToolbarPresenter extends Presenter<ToolbarView> {
	private final ToolController toolController;

	public ToolbarPresenter(ToolbarView view, ToolController toolController) {
		super(view);

		this.toolController = toolController;

		initialize();
	}

	private void initialize() {
		ActionController actionController = toolController.getActionController();
		actionController.setOnAction(() -> {
			view.setEnableUndo(actionController.canUndo());
			view.setEnableRedo(actionController.canRedo());
		});

		view.setBrush(toolController.getBrush());
		view.setBrush2(toolController.getBrush2());
		view.setOnClear(toolController::clear);
		view.setOnRedo(toolController::redo);
		view.setOnUndo(toolController::undo);
	}
    
    //SD: dunno who the hack created toolController in the 1st place, so grab it here
    public ToolController getToolController()
    {
        return toolController;
    }
}
