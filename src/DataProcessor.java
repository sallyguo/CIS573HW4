import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;


public class DataProcessor {
	
	private DataStore _dataStore;
	private Log _log;
	private ArrayList<WorldSeriesInstance> list;
	
	public DataProcessor(DataStore dataStore) {
		_dataStore = dataStore;
		_log = Log.getInstance();
		list = _dataStore.allWorldSeriesInstances();
	}
	

	public WorldSeriesInstance showDataForYear(int year) {
		_log.log("showing data for year: " + year);
		return getDataForYear(year);
	}
	
	private WorldSeriesInstance getDataForYear(int year) {
		_log.log("getting data for year: " + year);

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
		_log.log("showing data for range: " + start + " to " + end);

		// make sure we have valid data
		if (end < start) return "Invalid year range";
				
		// look in the cache first
		String cachedValue = _dataStore.cacheLookup(start+"-"+end);
		if (cachedValue != null) return cachedValue;
		
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
			_dataStore.cacheStore(start+"-"+end, result.toString());
			return result.toString();
		}
		
	}

	public String showDataForTeam(String team, boolean isWin, boolean isLoss) {
		if (!isLoss) {
			_log.log("showing wins for team: " + team);
			String cachedValue = _dataStore.cacheLookup(team+"-wins");
			if (cachedValue != null) return cachedValue;
		}
		else if (!isWin) {
			_log.log("showing losses for team: " + team);
			String cachedValue = _dataStore.cacheLookup(team+"-losses");
			if (cachedValue != null) return cachedValue;
		}
		else {
			_log.log("showing wins and losses for team: " + team);
			String cachedValue = _dataStore.cacheLookup(team+"-all");
			if (cachedValue != null) return cachedValue;
		}
		int wins = 0;
		int losses = 0;
		StringBuffer result = new StringBuffer();
		
		for (WorldSeriesInstance wsi : list) {
			if (isWin) {
				if (stringCompare(wsi.winner(), team)) {
					// we found an instance when the team won
					result.append(wsi.toString());
					result.append("\n");
					wins++;
				}
			}
			if (isLoss) {
				if (stringCompare(wsi.loser(), team)) {
					result.append("In " + wsi.year() + " the " + wsi.loser() + " lost to the " + wsi.winner() + " by " + wsi.score());
					result.append("\n");
					losses++;
				}
			}
		}
		if (!isLoss) {
			if (wins == 0) {
				result.append("The " + team + " have not won any World Series");
				result.append("\n");
			}
			else {
				result.append("The " + team + " have won " + wins + " World Series");
				result.append("\n");
			}
			_dataStore.cacheStore(team+"-wins", result.toString());
		}
		else if (!isWin) {
			if (losses == 0) {
				result.append("The " + team + " have not lost any World Series");
				result.append("\n");
				}
			else {
				result.append("The " + team + " have lost " + losses + " World Series");
				result.append("\n");
			}
			_dataStore.cacheStore(team+"-losses", result.toString());
		}
		else {
			if (wins + losses == 0) {
				result.append("The " + team + " have not played in any World Series");
				result.append("\n");
			}
			else {
				result.append("The " + team + " have won " + wins + " World Series and lost " + losses);
				result.append("\n");
			}
			_dataStore.cacheStore(team+"-all", result.toString());
		}
		return result.toString();
	}
	
	private boolean stringCompare(String wsi, String team) {
		if (wsi.toUpperCase().contains(team.toUpperCase())) 
			return true;
		else
			return false;
	}

	public String showDataTeamsYears() {
		_log.log("Trying to display all teams");
		
		TreeMap<String, ArrayList<Integer>> teams = _dataStore.getWinningTeams();
		if (_dataStore.getWinningTeams() == null) teams = assembleWinnersByTeam();

		StringBuffer result = new StringBuffer();
		Set<String> keys = teams.keySet();
		for (String key : keys) {
			result.append(key + ": ");
			ArrayList<Integer> years = teams.get(key);
			for (int i = 0; i < years.size()-1; i++) result.append(years.get(i) + ", ");
			result.append(years.get(years.size()-1)+"\n");
		}
		return result.toString();
	}
	
	protected TreeMap<String, ArrayList<Integer>> assembleWinnersByTeam() {
		_log.log("Trying to assemble winners by team");

		TreeMap<String, ArrayList<Integer>> winningTeams = new TreeMap<String, ArrayList<Integer>>();
		
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
		_dataStore.setWinningTeams(winningTeams);
		return winningTeams;
	}
}
