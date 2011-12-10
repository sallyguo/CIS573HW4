import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;


public class DataProcessor {
	
	private DataStore _dataStore;
	
	public DataProcessor(DataStore dataStore) {
		_dataStore = dataStore;
	}
	

	public WorldSeriesInstance showDataForYear(int year) {
		log("showing data for year: " + year);
		WorldSeriesInstance wsi = getDataForYear(year);
		
		if (wsi != null) return wsi;
		// if we made it here, we didn't find it
		else return null;
		
	}
	
	private WorldSeriesInstance getDataForYear(int year) {
		log("getting data for year: " + year);
		// look through all the instances
		ArrayList<WorldSeriesInstance> list = _dataStore.allWorldSeriesInstances();

		for (WorldSeriesInstance wsi : list) {
			if (wsi.year() == year) {
				// found it!
				return wsi;
			}
		}
		
		// if we get here, we didn't find one for that year
		return null;
	}
	
	public String showDataForRange(int start, int end) {
		log("showing data for range: " + start + " to " + end);

		// make sure we have valid data
		if (end < start) {
			return "Invalid year range";
		}
				
		// look in the cache first
		if (_dataStore.lookup(start+"-"+end) != null) {
			return _dataStore._resultsCache.get(start+"-"+end);
		}
		
		StringBuffer result = new StringBuffer();
		
		// this is a counter of how many we've added to the buffer
		int count = 0;
		
		for (int year = start; year <= end; year++) {
			WorldSeriesInstance wsi = getDataForYear(year);
			if (wsi != null) {
				result.append(wsi.toString());
				result.append("\n");
				count++;
			}
		}

		// if we didn't see any results, return that
		if (count == 0) {
			return "No World Series held between " + start + " and " + end;
		}
		else {
			_dataStore._resultsCache.put(start+"-"+end, result.toString());
			return result.toString();
		}
		
	}


	public String showDataForTeamWins(String team) {
		log("showing wins for team: " + team);

		
		// look in the cache first
		if (_dataStore.lookup(team+"-wins") != null) {
			return _dataStore._resultsCache.get(team+"-wins");
		}
		
		// to hold the result
		StringBuffer result = new StringBuffer();
	
		// keep track of the number of wins for the team
		int wins = 0;
			
		// look through all the instances
		ArrayList<WorldSeriesInstance> list = _dataStore.allWorldSeriesInstances();
		for (WorldSeriesInstance wsi : list) {
			// convert to uppercase and use "contains" for partial matching
			if (wsi.winner().toUpperCase().contains(team.toUpperCase())) {
				// we found an instance when the team won
				result.append(wsi.toString());
				result.append("\n");
				wins++;
			}
		}
		// if none found, print a message
		if (wins == 0) {
			result.append("The " + team + " have not won any World Series");
			result.append("\n");
		}
		else {
			result.append("The " + team + " have won " + wins + " World Series");
			result.append("\n");
		}
		
		// put it in the cache
		_dataStore._resultsCache.put(team+"-wins", result.toString());

		return result.toString();
	}
	
	public String showDataForTeamLosses(String team) {
		log("showing losses for team: " + team);

		// look in the cache first
		if (_dataStore.lookup(team+"-losses") != null) {
			return _dataStore._resultsCache.get(team+"-losses");
		}
		
		// to hold the result
		StringBuffer result = new StringBuffer();

		// keep track of the number of losses for the team
		int losses = 0;
		
		// look through all the instances
		ArrayList<WorldSeriesInstance> list = _dataStore.allWorldSeriesInstances();
		for (WorldSeriesInstance wsi : list) {
			// convert to uppercase and use "contains" for partial matching
			if (wsi.loser().toUpperCase().contains(team.toUpperCase())) {
				result.append("In " + wsi.year() + " the " + wsi.loser() + " lost to the " + wsi.winner() + " by " + wsi.score());
				result.append("\n");
				losses++;
			}	
		}
		// if none found, print a message
		if (losses == 0) {
			result.append("The " + team + " have not lost any World Series");
			result.append("\n");
			}
		else {
			result.append("The " + team + " have lost " + losses + " World Series");
			result.append("\n");
		}

		// put it in the cache
		_dataStore._resultsCache.put(team+"-losses", result.toString());
		
		return result.toString();

	}
			
	public String showDataForTeamAll(String team) {
		log("showing wins and losses for team: " + team);

		// look in the cache first
		if (_dataStore.lookup(team+"-all") != null) {
			return _dataStore._resultsCache.get(team+"-all");
		}

		// to hold the result
		StringBuffer result = new StringBuffer();

		// keep track of the number of wins and losses for the team
		int wins = 0, losses = 0;
			
		// look through all the instances
		ArrayList<WorldSeriesInstance> list = _dataStore.allWorldSeriesInstances();
		for (WorldSeriesInstance wsi : list) {
			// convert to uppercase and use "contains" for partial matching
			if (wsi.winner().toUpperCase().contains(team.toUpperCase())) {
				// we found an instance when the team won
				result.append(wsi.toString());
				result.append("\n");
				wins++;
			}
			else if (wsi.loser().toUpperCase().contains(team.toUpperCase())) {
				result.append("In " + wsi.year() + " the " + wsi.loser() + " lost to the " + wsi.winner() + " by " + wsi.score());
				result.append("\n");
				losses++;
			}
		}
		// if none found, print a message
		if (wins + losses == 0) {
			result.append("The " + team + " have not played in any World Series");
			result.append("\n");
		}
		else {
			result.append("The " + team + " have won " + wins + " World Series and lost " + losses);
			result.append("\n");
		}
		
		// put it in the cache
		_dataStore._resultsCache.put(team+"-all", result.toString());
		
		return result.toString();
		
	}
	
	
	protected void displayTeams(TreeMap<String, ArrayList<Integer>> teams, UserInterface ui) {
		log("Trying to display all teams");

		Set<String> keys = teams.keySet();
		for (String key : keys) {
			ui.print(key + ": ");
			ArrayList<Integer> years = teams.get(key);
			for (int i = 0; i < years.size()-1; i++) ui.print(years.get(i) + ", ");
			ui.println(years.get(years.size()-1));
		}

		
	}


	protected void log(String event) {
		try {
			FileWriter writer = new FileWriter(new File("log.txt"), true);
			writer.write(System.currentTimeMillis() + " (DataProcessor): " + event +"\n");
			writer.flush();
		}
		catch (Exception e) { e.printStackTrace(); }
	}

}
