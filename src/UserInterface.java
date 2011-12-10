import java.io.PrintStream;

/*
 * This class is declared abstract because the getInputString and getInputInt methods
 * need to be overridden, depending on what type of UI you're using.
 * 
 * Additionally, the PrintStream object is not initialized here.
 * It is up to a subclass to initialize the PrintStream to the appropriate instance.
 */

public abstract class UserInterface {
	
	protected PrintStream _out; // subclasses must instantiate this
	
	// subclasses must implement these methods
	protected abstract String getInputString();
	protected abstract int getInputInt();

	private DataStore _dataStore;
	private DataProcessor _dataProcessor;
	
	// map of teams to the years they won
	private Log log;
	
	protected UserInterface() {
		_dataStore = new DataStore("WorldSeries.csv");
		_dataProcessor = new DataProcessor(_dataStore);
		log = new Log();
	}
			
	public void start() {
		
		println("Welcome to the World Series database!");
		log.log("Starting application");

		String choice = null; // the thing that the user chooses to do

		do {
			showPrompt();
			
			choice = getInputString();
			log.log("User input: " + choice);
			
			if (choice.equals("1")) searchByYear();	
			else if (choice.equals("2")) searchByTeam();
			else if (choice.equals("3")) searchByRange();
			else if (choice.equals("4")) showTeamsYears();
			else if (!quit(choice)) {
				println("That is not a valid selection.");
			}
			
		}
		while (!quit(choice));
		println("Good bye");
	}

	protected static boolean quit(String choice) {
		return choice.equals("Q") || choice.equals("q");
	}
	
	protected void showPrompt() {
		println("");
		println("Please make your selection:");
		println("1: Search for World Series info by year");
		println("2: Search for World Series info by team");
		println("3: Show all World Series for a range of years");
		println("4: Show all teams' World Series wins");
		println("Q: Quit");
		print("> ");

	}

	protected void searchByYear() {
		print("Please enter the year: ");
		try {
			int year = getInputInt();
			log.log("Trying to search for year: " + year);
			WorldSeriesInstance wsi = _dataProcessor.showDataForYear(year);
			if (wsi == null) println("No World Series held in " + year);
			else println(wsi.toString());
		}
		catch (Exception e) { 
			println("That is not a valid year.");
			log.log("User entered invalid year");
		}
	}

	protected String askUserForWinLossOrAll() {
		print("Do you want to see World Series the team has (W)on, (L)ost, or (A)ll? ");
		return getInputString();

	}
	
	protected String askUserForTeamName() {
		print("Please enter the team name: ");
		return getInputString();
	}

	protected void searchByTeam() {
		String team = askUserForTeamName();
		String which = askUserForWinLossOrAll();
		log.log("Trying to search for: team=" + team + ", which=" + which);

		if (which.equalsIgnoreCase("W"))
			println(_dataProcessor.showDataForTeam(team, true, false));
		else if (which.equalsIgnoreCase("L"))
			println(_dataProcessor.showDataForTeam(team, false, true));
		else if (which.equalsIgnoreCase("A"))
			println(_dataProcessor.showDataForTeam(team, true, true));
		else
			println(which + " is not a valid selection.");
	}
	
	protected void searchByRange() {
		int startYear, endYear;
		print("Please enter the starting year: ");
		try {
			startYear = getInputInt();
			log.log("Trying to search for range starting with: " + startYear);
		}
		catch (Exception e) { 
			println("That is not a valid year.");
			log.log("User entered invalid year");
			return;
		}
		print("Please enter the ending year: ");
		try {
			endYear = getInputInt();
			log.log("Trying to search for range starting with: " + endYear);
		}
		catch (Exception e) { 
			println("That is not a valid year.");
			log.log("User entered invalid year");
			return;
		}
		String yearData = _dataProcessor.showDataForRange(startYear, endYear);
		println(yearData);
	}
	
	protected void showTeamsYears(){
		String teamYearsData = _dataProcessor.showDataTeamsYears();
		println(teamYearsData);
	}
	
	public void print(String data) {
		_out.print(data);
	}

	public void println(int data) {
		_out.println(data);
	}

	public void println(String data) {
		_out.println(data);
	}
}
