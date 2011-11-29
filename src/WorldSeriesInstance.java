/*
 * Represents one instance of a World Series, i.e. the teams
 * that played in a particular year, and the score (in number of games
 * won) by which the winning team won.
 */


public class WorldSeriesInstance {
	
	private int _year; // year when World Series was held
	private String _winner; // winning team
	private String _loser; // losing team
	private String _score; // score by which the winning team won (in terms of numbers of games)
	
	public WorldSeriesInstance(int year, String winner, String loser, String score) {
		_year = year;
		_winner = winner;
		_loser = loser;
		_score = score;
	}
	
	public int year() { return _year; }
	public String winner() { return _winner; }
	public String loser() { return _loser; }
	public String score() { return _score; }
	
	public String toString() {
		return "In " + _year + " the " + _winner + " defeated the " + _loser + " by " + _score;
	}

}
