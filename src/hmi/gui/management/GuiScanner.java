package hmi.gui.management;

import hmi.gui.panels.init.InitPanel;
import hmi.gui.panels.operations.OperationsPanel;

/**
 * Scan through each gui element and do the appropriate operation depending on it.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class GuiScanner {
	
	public GuiScanner() {
	
	}
	
	public void scanInitPanel(InitPanel initPanel) throws IllegalArgumentException{
		// TODO : trouver la méthode d'init et les args dépendant de cette méthode
	}
	
	public void scanOperationsPanel(OperationsPanel operationsPanel) throws IllegalArgumentException{
		// TODO : faire une op par case cochée, + vérifier automate si besoin
	}
	
	public void scanExportPanel(OperationsPanel operationsPanel) throws IllegalArgumentException{
		// TODO : trouver la méthode d'export et les args dépendant de cette méthode
	}
}
