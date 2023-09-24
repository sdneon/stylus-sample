package sd.gui;

import java.awt.Dimension;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.lecturestudio.stylus.StylusEvent;
import org.lecturestudio.stylus.StylusListener;
import org.lecturestudio.stylus.awt.AwtStylusManager;


public class StylusSample extends JFrame implements StylusListener {
	private static final Logger LOG = System.getLogger(Logger.class.getName());

	JPanel pane;
	AwtStylusManager mgr;
	private long nativeHandle; //unused in Java codes, but needed by Stylus JNI!

	public StylusSample() {}

	public void start() {
		mgr = AwtStylusManager.getInstance();

		setTitle("Stylus Demo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pane = new JPanel();
		pane.setPreferredSize(new Dimension(800, 500));
		add(pane);
		pack();
		setVisible(true);

		mgr.attachStylusListener(pane, this);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				StylusSample app = new StylusSample();
				app.start();
				System.out.println("Started");
			}
			catch (Exception e) {
				LOG.log(Level.ERROR, "Create window failed.", e);
			}
		});
	}

	@Override
	public void onCursorChange(StylusEvent event) {
		System.out.println("onCursorChange: " + event.toString());
	}

	@Override
	public void onCursorMove(StylusEvent event) {
		System.out.println("onCursorMove: " + event.toString());
	}

	@Override
	public void onButtonDown(StylusEvent event) {
		System.out.println("onButtonDown: " + event.toString());
	}

	@Override
	public void onButtonUp(StylusEvent event) {
		System.out.println("onButtonUp: " + event.toString());
	}
}
