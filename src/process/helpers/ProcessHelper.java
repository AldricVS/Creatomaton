package process.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Helper class used to make console commands and retrieve result
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class ProcessHelper {
	/**
	 * If an error occurs while waiting for process ending, this code is returned
	 */
	public static final int LAUNCH_ERROR_CODE = -255;
	
	private String command;
	
	public ProcessHelper(String command) throws IllegalArgumentException{
		setCommand(command);
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) throws IllegalArgumentException{
		if(command == null) {
			throw new IllegalArgumentException("cannot pass 'null' as a command");
		}
		this.command = command;
	}
	
	/**
	 * Run the command in  a console, but don't wait for the end of the process. 
	 */
	public void runCommand() {
		try {
			Runtime.getRuntime().exec(command);
		}catch (IOException e) {
			System.err.println("Cannot launch command properly : " + e.getMessage());
		}
	}

	/**
	 * Run the command in  a console, but and wait for the end of the process.
	 * @return the exit code of the process
	 */
	public int runCommandAndWait() {
		try {
			Process proc = Runtime.getRuntime().exec(command);
			
			// wait until process is done
			try {
				System.out.println("Process finished with code " + proc.waitFor());
			} catch (InterruptedException e) {
				e.printStackTrace();
				return LAUNCH_ERROR_CODE;
			}

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String s = null;
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			while ((s = stdError.readLine()) != null) {
				System.err.println(s);
			}
			return proc.exitValue();
			
		}catch (IOException e) {
			//basic error code
			e.printStackTrace();
			return LAUNCH_ERROR_CODE;
		}
		
	}
}
