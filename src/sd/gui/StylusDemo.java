/*
 */
package sd.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.lecturestudio.stylus.demo.awt.view.AwtMainView;
import org.lecturestudio.stylus.demo.presenter.MainPresenter;
import org.lecturestudio.stylus.demo.view.MainView;

/**
 *
 * @author N
 */
public class StylusDemo {
    private static final Logger LOG = System.getLogger(StylusDemo.class.getName());

	private void start() {
		MainView mainView = new AwtMainView();
		MainPresenter mainPresenter = new MainPresenter(mainView);
		mainPresenter.initialize();

		JFrame frame = new JFrame();
		frame.setTitle("Stylus Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add((Container) mainView, BorderLayout.CENTER);
        frame.setSize(new Dimension(800, 500));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				StylusDemo app = new StylusDemo();
				app.start();
			}
			catch (Exception e) {
				LOG.log(Level.ERROR, "Create window failed.", e);
			}
		});
    }
}
