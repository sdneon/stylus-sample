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

package org.lecturestudio.stylus.demo.awt.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;

//import org.lecturestudio.stylus.demo.awt.control.ColorChooserButton;
import org.lecturestudio.stylus.demo.awt.beans.converter.ColorConverter;
import org.lecturestudio.stylus.demo.awt.util.AwtUtils;
import org.lecturestudio.stylus.demo.paint.StylusBrush;
import org.lecturestudio.stylus.demo.view.Action;
import org.lecturestudio.stylus.demo.view.ToolbarView;

import java.awt.event.*;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Hashtable;
import javax.swing.*;
import org.lecturestudio.stylus.demo.geometry.StylusPoint;
import org.lecturestudio.stylus.demo.tool.ClipboardTool;
import org.lecturestudio.stylus.demo.tool.ComicsFrameTool;
import org.lecturestudio.stylus.demo.tool.ToolController;
import org.lecturestudio.stylus.demo.view.CanvasView;

public class AwtToolbarView extends Box implements ToolbarView, ActionListener {

	private StylusBrush brush, brush2;
	private JButton undoButton;
	private JButton redoButton;
	private JSlider widthSlider;

    public final static int DEFAULT_LINE_THICKNESS = 3;
    public final static double ANGLE_RAD_90 = Math.PI * 0.5,
        //ANGLE_RAD_180 = Math.PI,
        COS_90 = Math.cos(ANGLE_RAD_90),
        SIN_90 = Math.sin(ANGLE_RAD_90),
        COS_NEG_90 = Math.cos(-ANGLE_RAD_90),
        SIN_NEG_90 = Math.sin(-ANGLE_RAD_90);
    //public final static double ANGLE_RAD_NEG_90 = -ANGLE_RAD_90;
    public final static double ANGLE_CALLI_NIB = Math.PI * 30.0 / 180.0, //30degs in rads
        //COS_CALLI_NIB = Math.cos(ANGLE_CALLI_NIB), //CCW
        //SIN_CALLI_NIB = Math.sin(ANGLE_CALLI_NIB),
        COS_CALLI_NEG_NIB = Math.cos(-ANGLE_CALLI_NIB), //CW
        SIN_CALLI_NEG_NIB = Math.sin(-ANGLE_CALLI_NIB);

    private static final String
        LABEL_SHAPE_ROUND = "<html>(H) <font color='green'>&#x25CF;</font>/&#x25A1;</html>",
        LABEL_SHAPE_RECT = "<html>(H) &#x25CB;/<font color='green'>&#x25A0;</font></html>",
        LABEL_TOOL_LINE = "<html>(T) <font color='green'>&#x25AC; Line</font>/<font color='gray'>&#x25A0;</font></html>",
        LABEL_TOOL_RECT = "<html>(T) <font color='gray'>&#x25AC;</font>/<font color='green'>&#x25A0; Rect</font></html>",
        LABEL_TOOL_CALLI_NOT = "<html>(A) <font color='green'>A (Normal)</font> / <font color='gray'>&#x00A3; Calli</font></html>",
        LABEL_TOOL_CALLI = "<html>(A) <font color='gray'>A (Normal)</font> / <font color='green'>&#x00A3; Calli~</font></html>",
        LABEL_COLOUR_SOLID = "<html>(W) <font color='green'>&#x2588;</font>/&#x2591;</html>",
        LABEL_COLOUR_WATER = "<html>(W) &#x2588;/<font color='green'>&#x2591;</font></html>",
        LABEL_BRUSH_NORMAL = "<html>(L) <font color='green'>&#x25CB;</font>/&#x221E;</html>",
        LABEL_BRUSH_LEAK = "<html>(L) &#x25CB;/<font color='green'>&#x221E;</font></html>";

    private String imageChooserStartDir = ".";

    // Default app size variables
    public Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	protected int menuXDimension = 85;
//	protected int appXDimension  = (int) screenSize.getWidth() - 20; //385;
	protected int appYDimension  = (int) screenSize.getHeight() - menuXDimension - 20; //300;
//	protected int cnvsXDimension = appXDimension - menuXDimension;
//    protected int nMidX = (cnvsXDimension + 1) / 2,
//        nMidY = (appYDimension + 1) / 2;

	// Current paint setting variables
	private boolean roundBrushShape = true, snapToEdge = false,
        lineTool = true,
        calligraphy = false;
    private JColorLabel colorSelector;
	private JSlider slider;
	private JButton	btnFileSave, btnFileClear;
	private JToggleButton btnShape, btnTool, btnCalli;
	private JToggleButton btnWatercolour;
    private JToggleButton btnBrushFlow;

    // Last mouse position during drag
	private boolean bHasStartPos = false;
    private int nStartX = 0, nStartY = 0,
        nLastWidth = 0,
        nStartRectX = 0, nStartRectY = 0;
    private int[] xPoints = new int[4],
        yPoints = new int[4];
    private boolean bHasLastTaperingPoint = false;
    private double dLastAngle = 0.0;
    private CanvasView canvasView;
    private ToolController toolController;


	public AwtToolbarView() {
		super(BoxLayout.Y_AXIS);

		initialize();
	}

	@Override
	public void setBrush(StylusBrush brush) {
		this.brush = brush;
		widthSlider.setValue((int) brush.getWidth());
        this.brush.setColor(ColorConverter.INSTANCE.from(colorSelector.getForegroundColor()));
	}

	@Override
	public void setBrush2(StylusBrush brush) {
		this.brush2 = brush;
        brush2.setColor(ColorConverter.INSTANCE.from(colorSelector.getBackgroundColor()));
	}

	@Override
	public void setEnableUndo(boolean enable) {
		AwtUtils.invoke(() -> undoButton.setEnabled(enable));
	}

	@Override
	public void setEnableRedo(boolean enable) {
		AwtUtils.invoke(() -> redoButton.setEnabled(enable));
	}

	@Override
	public void setOnClear(Action action) {
		AwtUtils.bindAction(btnFileClear, action);
	}

	@Override
	public void setOnUndo(Action action) {
		AwtUtils.bindAction(undoButton, action);
	}

	@Override
	public void setOnRedo(Action action) {
		AwtUtils.bindAction(redoButton, action);
	}

	private void initialize() {
		setBorder(new EmptyBorder(2, 5, 0, 5));

        //setLayout(new BorderLayout());

		undoButton = new JButton("Undo");
        undoButton.setMnemonic(KeyEvent.VK_U);
		redoButton = new JButton("Redo");
        redoButton.setMnemonic(KeyEvent.VK_E);

		setEnableRedo(false);
		setEnableUndo(false);

		JLabel widthLabel = new JLabel();
		widthLabel.setPreferredSize(AwtUtils.getTextSize(widthLabel, 100, "%.0f"));


		widthSlider = new JSlider();
		widthSlider.setMinimum(1);
		widthSlider.setMaximum(100);
		widthSlider.setMajorTickSpacing(1);
		widthSlider.setMinorTickSpacing(0);
		widthSlider.setPreferredSize(new Dimension(150, 20));
		widthSlider.setMaximumSize(new Dimension(150, 20));
		widthSlider.putClientProperty("Slider.paintThumbArrowShape", true);
		widthSlider.addChangeListener(e -> {
			JSlider source = (JSlider) e.getSource();

			widthLabel.setText(String.format("%d", source.getValue()));

			brush.setWidth(source.getValue());
		});

        //add(p, BorderLayout.NORTH);
		for (Component component : getComponents()) {
			component.setFocusable(false);
        }

        JPanel paneMain = new JPanel();
        add(paneMain);
        JPanel paneGrafitoCtrls = new JPanel();
        add(paneGrafitoCtrls);

        Color lightGray = new Color(232, 232, 232);

		// Set up the controls
		btnFileSave = new JButton("Save"); btnFileSave.setMnemonic(KeyEvent.VK_S); btnFileSave.setActionCommand("savegrafito");        //btnFileSave.addActionListener(this);
		JButton btnFileRefresh = new JButton("Refresh"); btnFileRefresh.setMnemonic(KeyEvent.VK_R); btnFileRefresh.setActionCommand("gfrefresh"); btnFileRefresh.addActionListener(this);
		JButton btnSwapColours = new JButton("Swap"); btnSwapColours.setMnemonic(KeyEvent.VK_P); btnSwapColours.setActionCommand("gfswapcolours");     btnSwapColours.addActionListener(this);
		btnFileClear = new JButton("Clear"); btnFileClear.setMnemonic(KeyEvent.VK_C); //btnFileClear.setActionCommand("gfclear");     //btnFileClear.addActionListener(this);
		JButton btnComics = new JButton("Comics"); btnComics.setMnemonic(KeyEvent.VK_M); btnComics.setActionCommand("gfcomics");     btnComics.addActionListener(this);
        JButton btnPaste = new JButton("(V) Paste"); btnPaste.setToolTipText("Paste clipboard image"); btnPaste.setMnemonic(KeyEvent.VK_V); btnPaste.setActionCommand("paste-from-clipboard");     btnPaste.addActionListener(this);
        JButton btnPasteTL = new JButton("(7) TL"); btnPasteTL.setToolTipText("Paste clipboard image to top left"); btnPasteTL.setMnemonic(KeyEvent.VK_7); btnPasteTL.setActionCommand("paste-from-clipboard-tl");     btnPasteTL.addActionListener(this);
        JButton btnPasteTR = new JButton("(8) TR"); btnPasteTR.setToolTipText("Paste clipboard image to top right"); btnPasteTR.setMnemonic(KeyEvent.VK_8); btnPasteTR.setActionCommand("paste-from-clipboard-tr");     btnPasteTR.addActionListener(this);
        JButton btnPasteBL = new JButton("(9) BL"); btnPasteBL.setToolTipText("Paste clipboard image to bottom left"); btnPasteBL.setMnemonic(KeyEvent.VK_9); btnPasteBL.setActionCommand("paste-from-clipboard-bl");     btnPasteBL.addActionListener(this);
        JButton btnPasteBR = new JButton("(0) BR"); btnPasteBR.setToolTipText("Paste clipboard image to bottom right"); btnPasteBR.setMnemonic(KeyEvent.VK_0); btnPasteBR.setActionCommand("paste-from-clipboard-br");     btnPasteBR.addActionListener(this);
		JButton btnOpen = new JButton("Open"); btnOpen.setMnemonic(KeyEvent.VK_O); btnOpen.setActionCommand("gfopen");     btnOpen.addActionListener(this);
		btnPaste.setToolTipText("<html>Paste picture from clipboard<br>Click for smart fit<br>CTRL-Click for as is<br>ALT-Click fits to height<br>SHF-Click fits to width</html>");
		JButton btnClose = new JButton("(X) Close"); btnClose.setMnemonic(KeyEvent.VK_X); btnClose.setActionCommand("gfclose");     btnClose.addActionListener(this);

		JPanel pnlButtons = new JPanel();
		pnlButtons.setBackground(lightGray);
		pnlButtons.setLayout(new GridLayout(0, 1));
		//create colour selectior
		colorSelector = new JColorLabel(Color.black, Color.white);
		colorSelector.setPreferredSize(new Dimension(menuXDimension, 15));
		pnlButtons.add(colorSelector);
		//add remaining buttons
        pnlButtons.add(btnSwapColours);

		JPanel pnlButtons2 = new JPanel();
		pnlButtons2.setBackground(lightGray);
		pnlButtons2.setLayout(new GridLayout(1, 2));
		btnShape = new JToggleButton(LABEL_SHAPE_ROUND); btnShape.setMnemonic(KeyEvent.VK_H); btnShape.setActionCommand("gfchangeshape");     btnShape.addActionListener(this);
        btnShape.setToolTipText("Toggle between round or rectangular shaped brush");
        btnShape.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
               if (ev.getStateChange() == ItemEvent.SELECTED){
                   btnShape.setText(LABEL_SHAPE_RECT);
               }
               else //if (ev.getStateChange() == ItemEvent.DESELECTED)
               {
                   btnShape.setText(LABEL_SHAPE_ROUND);
               }
            }
        });
        btnTool = new JToggleButton(LABEL_TOOL_LINE); btnTool.setMnemonic(KeyEvent.VK_T); btnTool.setActionCommand("gfchangetool");     btnTool.addActionListener(this);
        btnTool.setToolTipText("Toggle between line or rectangle tool");
        btnTool.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
               if (ev.getStateChange() == ItemEvent.SELECTED){
                   btnTool.setText(LABEL_TOOL_RECT);
                   toolController.setLineMode(false);
               }
               else //if (ev.getStateChange() == ItemEvent.DESELECTED)
               {
                   btnTool.setText(LABEL_TOOL_LINE);
                   toolController.setLineMode(true);
               }
            }
        });
        pnlButtons.add(btnTool);
        btnCalli = new JToggleButton(LABEL_TOOL_CALLI_NOT); btnCalli.setMnemonic(KeyEvent.VK_A); btnCalli.setActionCommand("gfgocalli");     btnCalli.addActionListener(this);
        btnCalli.setToolTipText("Toggle between normal brush and calligraphy brush");
        btnCalli.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
               if (ev.getStateChange() == ItemEvent.SELECTED){
                   btnCalli.setText(LABEL_TOOL_CALLI);
               }
               else //if (ev.getStateChange() == ItemEvent.DESELECTED)
               {
                   btnCalli.setText(LABEL_TOOL_CALLI_NOT);
               }
            }
        });
        pnlButtons.add(btnCalli);
		btnWatercolour = new JToggleButton(LABEL_COLOUR_SOLID); btnWatercolour.setMnemonic(KeyEvent.VK_W); //btnWatercolour.setActionCommand("gftogglewater");     btnWatercolour.addActionListener(this);
        btnWatercolour.setToolTipText("Toggle between solid or water colours");
        btnWatercolour.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
               if (ev.getStateChange() == ItemEvent.SELECTED){
                   btnWatercolour.setText(LABEL_COLOUR_WATER);
                   brush.setVaryAlphaWithPressure(true);
                   brush2.setVaryAlphaWithPressure(true);
               }
               else //if (ev.getStateChange() == ItemEvent.DESELECTED)
               {
                   btnWatercolour.setText(LABEL_COLOUR_SOLID);
                   brush.setVaryAlphaWithPressure(false);
                   brush2.setVaryAlphaWithPressure(false);
               }
            }
        });
        pnlButtons2.add(btnShape);
        pnlButtons2.add(btnWatercolour);
        pnlButtons.add(pnlButtons2);

//		JPanel pnlButtons6 = new JPanel();
//		pnlButtons6.setBackground(lightGray);
//		pnlButtons6.setLayout(new GridLayout(1, 1));
//		btnBrushFlow = new JToggleButton(LABEL_BRUSH_NORMAL); btnBrushFlow.setMnemonic(KeyEvent.VK_L); //btnBrushFlow.setActionCommand("gftoggleflow");     btnBrushFlow.addActionListener(this);
//        btnBrushFlow.setToolTipText("<html>Toggle between normal drawing when pen is active,<br>or drawing whenever pen moves</html>");
//        btnBrushFlow.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent ev) {
//               if (ev.getStateChange() == ItemEvent.SELECTED){
//                   btnBrushFlow.setText(LABEL_BRUSH_LEAK);
//               }
//               else //if (ev.getStateChange() == ItemEvent.DESELECTED)
//               {
//                   bHasStartPos = false;
//                   bHasLastTaperingPoint = false;
//                   btnBrushFlow.setText(LABEL_BRUSH_NORMAL);
//               }
//            }
//        });
//        pnlButtons6.add(btnBrushFlow);
//        pnlButtons.add(pnlButtons6);

        //Add quick (FG) colour selections
		JPanel pnlButtons3 = new JPanel();
		pnlButtons3.setBackground(lightGray);
		pnlButtons3.setLayout(new GridLayout(1, 2));
		JPanel pnlButtons4 = new JPanel();
		pnlButtons4.setBackground(lightGray);
		pnlButtons4.setLayout(new GridLayout(1, 2));
		JPanel pnlButtons5 = new JPanel();
		pnlButtons5.setBackground(lightGray);
		pnlButtons5.setLayout(new GridLayout(1, 2));
		JButton btnBlack = new JButton("<html><font color='black'>(1) &#x25A0;&#x25A0;</font></html>");
        btnBlack.setMnemonic(KeyEvent.VK_1);
        btnBlack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePrimaryColour(Color.black);
            }
        });
		JButton btnWhite = new JButton("<html><font color='black'>(2) &#x25A1;&#x25A1;</font></html>");
        btnWhite.setMnemonic(KeyEvent.VK_2);
        btnWhite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePrimaryColour(Color.white);
            }
        });
		JButton btnRed = new JButton("<html><font color='red'>(3) &#x25A0;&#x25A0;</font></html>");
        btnRed.setMnemonic(KeyEvent.VK_3);
        btnRed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePrimaryColour(Color.red);
            }
        });
		JButton btnBlue = new JButton("<html><font color='blue'>(4) &#x25A0;&#x25A0;</font></html>");
        btnBlue.setMnemonic(KeyEvent.VK_4);
        btnBlue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePrimaryColour(Color.blue);
            }
        });
		JButton btnGreen = new JButton("<html><font color='green'>(5) &#x25A0;&#x25A0;</font></html>");
        btnGreen.setMnemonic(KeyEvent.VK_5);
        btnGreen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePrimaryColour(Color.green);
            }
        });
		JButton btnOrange = new JButton("<html><font color='orange'>(6) &#x25A0;&#x25A0;</font></html>");
        btnOrange.setMnemonic(KeyEvent.VK_6);
        btnOrange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePrimaryColour(Color.orange);
            }
        });
        btnBlack.setToolTipText("Change colour to black");
        btnWhite.setToolTipText("Change colour to white");
        btnRed.setToolTipText("Change colour to red");
        btnBlue.setToolTipText("Change colour to blue");
        btnGreen.setToolTipText("Change colour to green");
        btnOrange.setToolTipText("Change colour to orange");
        pnlButtons3.add(btnBlack);
        pnlButtons3.add(btnWhite);
        pnlButtons4.add(btnRed);
        pnlButtons4.add(btnBlue);
        pnlButtons5.add(btnGreen);
        pnlButtons5.add(btnOrange);
        pnlButtons.add(pnlButtons3);
        pnlButtons.add(pnlButtons4);
        pnlButtons.add(pnlButtons5);

        pnlButtons.add(btnFileClear);
        pnlButtons.add(btnComics);
		pnlButtons.add(btnPaste);
        JPanel pnlPasteCorner = new JPanel();
        pnlPasteCorner.setLayout(new GridLayout(0, 2));
        pnlPasteCorner.add(btnPasteTL);
        pnlPasteCorner.add(btnPasteTR);
        pnlButtons.add(pnlPasteCorner);
        pnlPasteCorner = new JPanel();
        pnlPasteCorner.setLayout(new GridLayout(0, 2));
        pnlPasteCorner.add(btnPasteBL);
        pnlPasteCorner.add(btnPasteBR);
        pnlButtons.add(pnlPasteCorner);
        JPanel paneUndoRedo = new JPanel();
        paneUndoRedo.setLayout(new GridLayout(0,2));
		paneUndoRedo.add(undoButton);
		paneUndoRedo.add(redoButton);
        pnlButtons.add(paneUndoRedo);
		pnlButtons.add(btnFileRefresh);
        JPanel paneOpenSave = new JPanel();
        paneOpenSave.setLayout(new GridLayout(0, 2));
        paneOpenSave.add(btnOpen);
		paneOpenSave.add(btnFileSave);
        pnlButtons.add(paneOpenSave);
        pnlButtons.add(btnClose);
        JLabel labBrush = new JLabel("BRUSH");
		pnlButtons.add(labBrush);
		labBrush.setToolTipText("<html>CTRL-middle click toggles b/w round & square brush!<br>SHIFT-middle click toggles snap to edge"
			+ "<br>Middle mouse paints in watercolour colour (use ALT to change colour)<br>SHIFT-left/right click picks colour from picture for Fore/Background colour</html>");

		slider = new JSlider(JSlider.VERTICAL, 1, 100, DEFAULT_LINE_THICKNESS);
		slider.setMajorTickSpacing(20);
		slider.setMinorTickSpacing(10);
		slider.setSnapToTicks(false);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
        slider.addChangeListener(e -> {
			JSlider source = (JSlider) e.getSource();
			brush.setWidth(source.getValue());
            brush2.setWidth(source.getValue());
		});
		Hashtable<Integer, JLabel> labels = new Hashtable(6);
		labels.put(1, new JLabel("Fine"));
		//labels.put(10, new JLabel("10"));
		labels.put(20, new JLabel("20"));
		//labels.put(30, new JLabel("30"));
		labels.put(40, new JLabel("40"));
		//labels.put(50, new JLabel("50"));
		labels.put(60, new JLabel("60"));
		//labels.put(70, new JLabel("70"));
		labels.put(80, new JLabel("80"));
		//labels.put(90, new JLabel("90"));
		labels.put(100, new JLabel("Huge"));
		slider.setLabelTable(labels);

		JPanel sidePane = new JPanel();
		sidePane.setLayout(new BorderLayout());
		sidePane.add(slider, BorderLayout.WEST);
		sidePane.add(pnlButtons, BorderLayout.NORTH);
		sidePane.setSize(menuXDimension, appYDimension);
		sidePane.setBounds(0, 0, menuXDimension, appYDimension);
		sidePane.setBackground(lightGray);

//		add(canvas, BorderLayout.CENTER);
		paneGrafitoCtrls.add(sidePane, BorderLayout.WEST);     
    }

    protected void changePrimaryColour(Color c)
    {
        colorSelector.setForegroundColor(c);
        brush.setColor(ColorConverter.INSTANCE.from(c));
    }

    protected void changeSecondaryColour(Color c)
    {
        colorSelector.setBackgroundColor(c);
        brush2.setColor(ColorConverter.INSTANCE.from(c));
    }

	public void actionPerformed(ActionEvent ae)
	{
        //parent.touch();
        String strAction = ae.getActionCommand();
		if (strAction.equals("exit"))
		{
			//this.dispose();
			System.exit(0);
		}
		else if (strAction.equals("savegrafito"))
		{
			//writeToJPEG("grafito.jpg");
			//writeToGIF("grafitoapp.gif");
		}
        else if (strAction.equals("gfswapcolours"))
        {
            Color c = colorSelector.getForegroundColor();
            changePrimaryColour(colorSelector.getBackgroundColor());
            changeSecondaryColour(c);
        }
        else if (strAction.equals("gfchangeshape"))
        {
            roundBrushShape = !btnShape.isSelected();
        }
        else if (strAction.equals("gfchangetool"))
        {
            lineTool = !btnTool.isSelected();
        }
        else if (strAction.equals("gfgocalli"))
        {
            calligraphy = btnCalli.isSelected();
        }
        //else if (strAction.equals("gftogglewater")) {}
		else if (strAction.equals("gfrefresh"))
		{
			paint(this.getGraphics());
//            if (imageStore == null) { initForPaint(); }
            repaint();
		}
		else if (strAction.equals("gfopen"))
		{
            //SD: Change to a file selection dialog for easier choosing of image
//            File f = getImageFromChooser(imageChooserStartDir, EkitCore.extsIMG, Translatrix.getTranslationString("FiletypeIMG"));
//            if (f != null)
//            {
//                //save image folder
//                imageChooserStartDir = f.getParent().toString();
//                //import image. Maybe scale to fit in future
//                importPicture(ImageUtilities.getImage(this, f.getAbsolutePath()), ae);
//            }
		}
		else if (strAction.equals("gfcomics"))
		{
            paintComicsPanel(ae.getModifiers());
        }
		else if (strAction.equals("gfclose"))
		{
            this.setVisible(false);
        }
		else if (strAction.startsWith("paste-from-clipboard"))
		{
			Clipboard clipboard = getToolkit().getSystemClipboard();
			Transferable trans = null;

			// any of these calls may throw IllegalStateException
			try {
				if (clipboard != null) {
					trans = clipboard.getContents(null);
                    if (strAction.equals("paste-from-clipboard"))
                    {
//                        importPicture(trans, ae);
                        pastePicture(trans, ae, 0, 0, false, -1.0, -1.0);
                    }
                    else
                    {
                        double w = -0.5, h = -0.5;
                        if (strAction.equals("paste-from-clipboard-tl"))
                        {
                            pastePicture(trans, ae, 0, 0, false, w, h);
                        }
                        else if (strAction.equals("paste-from-clipboard-tr"))
                        {
                            pastePicture(trans, ae, 0.5, 0, true, w, h);
                        }
                        else if (strAction.equals("paste-from-clipboard-bl"))
                        {
                            pastePicture(trans, ae, 0, 0.5, true, w, h);
                        }
                        else if (strAction.equals("paste-from-clipboard-br"))
                        {
                            pastePicture(trans, ae, 0.5, 0.5, true, w, h);
                        }
                    }
				} else
					System.out.println("Clipboard unavailable!");
			} catch (IllegalStateException ise) {
				// clipboard was unavailable
				System.out.println("Clipboard unavailable!\n" + ise);
			}
		}
	}

    
	protected void pastePicture(Transferable trans, ActionEvent ae, double x, double y, boolean relPos, double width, double height) {
		if (trans == null) return;

		// this is a paste action, import data into the component
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor))
        {
            try
            {
                Image img = (Image) transferable.getTransferData(DataFlavor.imageFlavor);
                ClipboardTool tool = new ClipboardTool(img, new StylusPoint(x, y, 255), relPos, width, height,
                    toolController.getRenderSurface());
                toolController.executeTool(tool);
            }
            catch (UnsupportedFlavorException e)
            {
                // handle this as desired
                e.printStackTrace();
            }
            catch (IOException e)
            {
                // handle this as desired
                e.printStackTrace();
            }
        }
	}

    public void setCanvas(CanvasView canvasView)
    {
        this.canvasView = canvasView;
    }
    
    public void setToolController(ToolController toolController)
    {
        this.toolController = toolController;
    }

    private void paintComicsPanel(int modifiers) {
        int width = slider.getValue();
        if ((modifiers & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK) //does NOT work with mnemonic which triggers only with ALT+M but not CTRL+ALT+M!
        {
            //if CTRL is pressed, use default width
            width = 10;
        }
        Color color = colorSelector.getForegroundColor();
        if ((modifiers & ActionEvent.SHIFT_MASK) == ActionEvent.SHIFT_MASK) //does NOT work owing to above reason
        {
            color = colorSelector.getBackgroundColor();
        }
        ComicsFrameTool tool = new ComicsFrameTool(color, width, toolController.getRenderSurface());
        toolController.executeTool(tool);
    }

	class JColorButton extends JButton {
		public JColorButton(String text, Color color) {
			super(text);
			setBackground(color);
		}
		public void setBackground(Color color) {
			super.setBackground(color);
			setForeground((color.getRed() > 240 || color.getGreen() > 240)?
				Color.black: Color.white);
		}
	}

	class JColorLabel extends JPanel implements ActionListener {
		private JColorButton fgColorButton, bgColorButton;

		public JColorLabel(Color fgColor, Color bgColor) {
			super();
			setLayout(new GridLayout(1, 2));
			//create colour selection buttons
			fgColorButton = new JColorButton("F", fgColor);
            fgColorButton.setMnemonic(KeyEvent.VK_F);
			fgColorButton.setActionCommand("choosecolor");
			fgColorButton.addActionListener(this);
			fgColorButton.setToolTipText("<html>Foreground color<p>ALT-click to swap colours<br>CTRL-click to copy from Background colour<br>SHF-click to copy to Background color</html>");
			add(fgColorButton);
			bgColorButton = new JColorButton("B", bgColor);
            bgColorButton.setMnemonic(KeyEvent.VK_B);
			bgColorButton.setActionCommand("choosecolor");
			bgColorButton.addActionListener(this);
			bgColorButton.setToolTipText("<html>Background color<p>ALT-click to swap colours<br>CTRL-click to copy from Foreground colour<br>SHF-click to copy to Foreground color</html>");
			add(bgColorButton);
            setForegroundColor(fgColor);
            setBackgroundColor(bgColor);
		}

		public Color getForegroundColor() {
			return fgColorButton.getBackground();
		}

		public Color getBackgroundColor() {
			return bgColorButton.getBackground();
		}

		public void setForegroundColor(Color c) {
			if (c == null) return;
			fgColorButton.setBackground(c);
		}

		public void setBackgroundColor(Color c) {
			if (c == null) return;
			bgColorButton.setBackground(c);
		}

		public void actionPerformed(ActionEvent e) {
			if (!e.getActionCommand().equals("choosecolor")) return;

			JButton button = (JButton) e.getSource();
			int mask = (ActionEvent.ALT_MASK
				| ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK),
				mod = e.getModifiers() & mask;
			if ((mod & mask) == ActionEvent.ALT_MASK) {
				//swap colours
				Color color = fgColorButton.getBackground(),
                    newFgColor = bgColorButton.getBackground();
				fgColorButton.setBackground(newFgColor);
				bgColorButton.setBackground(color);
                changePrimaryColour(newFgColor);
                changeSecondaryColour(color);
			} else if ((mod & mask) == ActionEvent.CTRL_MASK) {
				//copy colour to this
				if (button == fgColorButton)
                {
                    Color newFgColor = bgColorButton.getBackground();
					fgColorButton.setBackground(newFgColor);
                    changePrimaryColour(newFgColor);
                }
				else
                {
                    Color newBgColor = fgColorButton.getBackground();
					bgColorButton.setBackground(newBgColor);
                    changeSecondaryColour(newBgColor);
                }
			} else if ((mod & mask) == ActionEvent.SHIFT_MASK) {
				//make both colours same as selection
				if (button == fgColorButton)
                {
                    Color newBgColor = fgColorButton.getBackground();
					bgColorButton.setBackground(newBgColor);
                    changeSecondaryColour(newBgColor);
                }
				else
                {
                    Color newFgColor = bgColorButton.getBackground();
					fgColorButton.setBackground(newFgColor);
                    changePrimaryColour(newFgColor);
                }
			} else {
				Color color;
				try {
					color = JColorChooser.showDialog(this, "Pick Colour", ((JColorLabel)(button.getParent())).getBackgroundColor());
				} catch (HeadlessException ex) {
					return;
				}
				if (color != null) {
					if (button == fgColorButton)
                    {
						fgColorButton.setBackground(color);
                        changePrimaryColour(color);
                    }
					else
                    {
						bgColorButton.setBackground(color);
                        changeSecondaryColour(color);
                    }
				}
			}
		}
	}
}
