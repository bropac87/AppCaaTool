import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class Click extends MouseAdapter {

	private boolean premuto = false;

	public void singoloClick(MouseEvent e) throws IOException {
	}

	public void mousePress(MouseEvent e) {
	}

	public void mouseRelease(MouseEvent e) {
	}

	public void mouseExit(MouseEvent e) {
	}

	@Override
	final public void mousePressed(MouseEvent e) {
		premuto = true;
		mousePress(e);
	}

	@Override
	final public void mouseReleased(MouseEvent e) {

		if (premuto) {
			try {
				singoloClick(e);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		premuto = false;
		mouseRelease(e);
	}

	@Override
	final public void mouseExited(MouseEvent e) {
		premuto = false;
		mouseExit(e);
	}

}
