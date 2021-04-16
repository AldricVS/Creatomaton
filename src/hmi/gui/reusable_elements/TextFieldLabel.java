package hmi.gui.reusable_elements;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextFieldLabel extends JPanel {
	
	private JLabel label;
	private JTextField textField = new JTextField();
	
	public TextFieldLabel(String label) {
		setLayout(new BorderLayout());
		this.label = new JLabel(label);
		add(this.label, BorderLayout.WEST);
		add(textField, BorderLayout.CENTER);
	}
}
