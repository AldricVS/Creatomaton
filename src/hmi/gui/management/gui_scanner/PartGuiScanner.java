package hmi.gui.management.gui_scanner;

abstract class PartGuiScanner {

	private GuiScanner guiScanner;

	public PartGuiScanner(GuiScanner guiScanner) {
		this.guiScanner = guiScanner;
	}

	public GuiScanner getGuiScanner() {
		return guiScanner;
	}
	
	public abstract void scanPart();
}