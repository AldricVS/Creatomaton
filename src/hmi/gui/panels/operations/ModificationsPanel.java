package hmi.gui.panels.operations;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class ModificationsPanel extends JPanel{
	
	private JCheckBox deterministicCheckBox = new JCheckBox("Déterminisation");
	private JCheckBox synchronizedCheckBox = new JCheckBox("Synchronisation");
	private JCheckBox mirorCheckBox = new JCheckBox("Miroir");
	private JCheckBox minimalisticCheckBox = new JCheckBox("Minimalisation");
	
	public ModificationsPanel() {
		initLayout();
	}

	private void initLayout() {
		setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 3;
		
		JPanel checkboxPanel = createCheckboxesPanel();
		add(checkboxPanel, gridBagConstraints);
		
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weighty = 1;
		JPanel buttonPanel = createButtonPanel();
		add(buttonPanel, gridBagConstraints);
	}

	private JPanel createCheckboxesPanel() {
		JPanel checkboxPanel = new JPanel();
		checkboxPanel.setLayout(new GridLayout(0, 2));
		checkboxPanel.add(deterministicCheckBox);
		checkboxPanel.add(synchronizedCheckBox);
		checkboxPanel.add(mirorCheckBox);
		checkboxPanel.add(minimalisticCheckBox);
		checkboxPanel.setBorder(BorderFactory.createTitledBorder("            "));
		return checkboxPanel;
	}

	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		JButton checkAllButton = new JButton("Tout à la fois");
		checkAllButton.addActionListener((actionEvent) -> {
			deterministicCheckBox.setSelected(true);
			synchronizedCheckBox.setSelected(true);
			mirorCheckBox.setSelected(true);
			minimalisticCheckBox.setSelected(true);
		}); 
		buttonPanel.add(checkAllButton);
		return buttonPanel;
	}
}
