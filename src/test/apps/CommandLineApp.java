package test.apps;

import com.formdev.flatlaf.FlatLightLaf;

import hmi.cli.CommandParser;
import hmi.gui.MainWindow;

public class CommandLineApp {

	public static void main(String[] args) {
		if(args.length == 0) {
			FlatLightLaf.install();
			new MainWindow();
		}else {
			CommandParser commandParser = new CommandParser();
			commandParser.parseArguments(args);
		}
	}
}
