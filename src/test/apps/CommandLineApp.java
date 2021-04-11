package test.apps;

import hmi.cli.CommandParser;
import hmi.gui.MainWindow;

public class CommandLineApp {

	public static void main(String[] args) {
		if(args.length == 0) {
			new MainWindow();
		}else {
			CommandParser commandParser = new CommandParser();
			commandParser.parseArguments(args);
		}
	}
}
