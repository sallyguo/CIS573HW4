import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;


public class CommandLineUserInterface implements UserInterface {
	
    protected PrintStream _out; // subclasses must instantiate this
    
    // subclasses must implement these methods
    //protected String getInputString();
    //protected int getInputInt();

    private DataStore _dataStore;
    private DataProcessor _dataProcessor;
    private Log log;
    
    // map of teams to the years they won
    private TreeMap<String, ArrayList<Integer>> winningTeams;
    
    protected CommandLineUserInterface() {
        _dataStore = new DataStore("WorldSeries.csv");
        _dataProcessor = new DataProcessor(_dataStore);
        _out = System.out;
        _in = new Scanner(System.in);
        log = Log.getInstance();
    }
    
	protected Scanner _in;
	
	public static void main(String[] args) {
		UserInterface ui = new CommandLineUserInterface();
		ui.start();
	}
	
	public String getInputString() {
		return _in.nextLine();
	}

	public int getInputInt() {
		return _in.nextInt();
	}
	
	public void start() {
        
        println("Welcome to the World Series database!");
        log.printLog("Starting application");

        String choice = null; // the thing that the user chooses to do

        do {
            showPrompt();
            
            choice = getInputString();
            log.printLog("User input: " + choice);
            
            if (choice.equals("1")) { 
                // if they want to search by year
                searchByYear(); 
            }
            else if (choice.equals("2")) {
                // search by team
                searchByTeam(askUserForTeamName(), askUserForWinLossOrAll());
            }
            else if (choice.equals("3")) {
                // for a range of years
                searchByRange();
            }
            else if (choice.equals("4")) {
                // show all the teams and the years they won a World Series
                assembleWinnersByTeam();
                _dataProcessor.displayTeams(winningTeams, this);
            }
            else if (!quit(choice)) {
                println("That is not a valid selection.");
            }
            
        }
        while (!quit(choice));
        println("Good bye");
    }

    
    public boolean quit(String choice) {
        return choice.equals("Q") || choice.equals("q");
    }
    
    public void showPrompt() {
        println("");
        println("Please make your selection:");
        println("1: Search for World Series info by year");
        println("2: Search for World Series info by team");
        println("3: Show all World Series for a range of years");
        println("4: Show all teams' World Series wins");
        println("Q: Quit");
        print("> ");
    }

    public void searchByYear() {
        print("Please enter the year: ");
        try {
            int year = getInputInt();
            log.printLog("Trying to search for year: " + year);
            WorldSeriesInstance wsi = _dataProcessor.showDataForYear(year);
            if (wsi == null) println("No World Series held in " + year);
            else println(wsi.toString());
        }
        catch (Exception e) { 
            println("That is not a valid year.");
            log.printLog("User entered invalid year");
        }
    }

    public String askUserForWinLossOrAll() {
        print("Do you want to see World Series the team has (W)on, (L)ost, or (A)ll? ");
        return getInputString();

    }
    
    public String askUserForTeamName() {
        print("Please enter the team name: ");
        return getInputString();
    }

    public void searchByTeam(String team, String which) {
        log.printLog("Trying to search for: team=" + team + ", which=" + which);

        if (which.equalsIgnoreCase("W"))
            println(_dataProcessor.showDataForTeamWins(team));
        else if (which.equalsIgnoreCase("L"))
            println(_dataProcessor.showDataForTeamLosses(team));
        else if (which.equalsIgnoreCase("A"))
            println(_dataProcessor.showDataForTeamAll(team));
        else
            println(which + " is not a valid selection.");
    }
    
    public void searchByRange() {
        int startYear, endYear;
        print("Please enter the starting year: ");
        try {
            startYear = getInputInt();
            log.printLog("Trying to search for range starting with: " + startYear);
        }
        catch (Exception e) { 
            println("That is not a valid year.");
            log.printLog("User entered invalid year");
            return;	
        }
        print("Please enter the ending year: ");
        try {
            endYear = getInputInt();
            log.printLog("Trying to search for range starting with: " + endYear);
        }
        catch (Exception e) { 
            println("That is not a valid year.");
            log.printLog("User entered invalid year");
            return;
        }
        String yearData = _dataProcessor.showDataForRange(startYear, endYear);
        println(yearData);
    }
    
    
    public void assembleWinnersByTeam() {
        log.printLog("Trying to assemble winners by team");

        ArrayList<WorldSeriesInstance> list = _dataStore.allWorldSeriesInstances();

        winningTeams = new TreeMap<String, ArrayList<Integer>>();
        
        for (WorldSeriesInstance wsi : list) {

            // see if the winner is already in the list of teams
            if (winningTeams.containsKey(wsi.winner())) {
                winningTeams.get(wsi.winner()).add(wsi.year());
            }
            else {
                // create an entry in the wins list
                ArrayList<Integer> newEntry = new ArrayList<Integer>();
                newEntry.add(wsi.year());
                winningTeams.put(wsi.winner(), newEntry);
            }
        }
                
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
