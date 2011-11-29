import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.TreeMap;

/*
 * This class is declared abstract because the getInputString and getInputInt methods
 * need to be overridden, depending on what type of UI you're using.
 * 
 * Additionally, the PrintStream object is not initialized here.
 * It is up to a subclass to initialize the PrintStream to the appropriate instance.
 */

public interface UserInterface {
			
	public void start();
	
	public boolean quit(String choice);
	
	public void showPrompt();

	public void searchByYear();

	public String askUserForWinLossOrAll();
	
	public String askUserForTeamName();

	public void searchByTeam(String team, String which);
	
	public void searchByRange();
	
	public void assembleWinnersByTeam();
	
	public void print(String data);

	public void println(int data);

	public void println(String data);
	
	public void log(String event);
	
}
