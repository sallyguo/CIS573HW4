import java.util.Scanner;


public class CommandLineUserInterface extends UserInterface {
	
	protected Scanner _in;
	
	public CommandLineUserInterface() {
		super();
		_out = System.out;
		_in = new Scanner(System.in);
	}
	
	public static void main(String[] args) {
		UserInterface ui = new CommandLineUserInterface();
		ui.start();
	}
	
	protected String getInputString() {
		return _in.nextLine();
	}

	protected int getInputInt() {
		return _in.nextInt();
	}
}
