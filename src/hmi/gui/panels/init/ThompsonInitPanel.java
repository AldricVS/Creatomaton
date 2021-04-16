package hmi.gui.panels.init;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ThompsonInitPanel extends JPanel {
	
	private JTextArea expressionField = new JTextArea();
	
	public ThompsonInitPanel() {
		initLayout();
	}
	
	public String getExpression() {
		return expressionField.getText();
	}

	private void initLayout() {
		setLayout(new GridLayout(0, 1));
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		panel.add(new JLabel("Expression :"), gridBagConstraints);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.weightx = 2;
		initExpressionField();
		JScrollPane scrollPane = initScrollPane();
		panel.add(scrollPane, gridBagConstraints);
		add(panel);
	}

	private JScrollPane initScrollPane() {
		JScrollPane scrollPane = new JScrollPane(expressionField);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return scrollPane;
	}

	private void initExpressionField() {
		expressionField.setLineWrap(true);
		expressionField.setRows(4);
	}
}
