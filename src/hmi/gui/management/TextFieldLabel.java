package hmi.gui.management;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextFieldLabel extends JPanel {
	
	private JLabel label;
	private JTextField textField = new JTextField();
	
	public TextFieldLabel(String label) {
		this.label = new JLabel(label);
		add(this.label);
		add(textField);
	}
}
