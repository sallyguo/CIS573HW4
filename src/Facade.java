
public class Facade {
	DataStore _dataStore;
	DataProcessor _dataProcessor;
	UserInterface _cli;
	Log _log;
	
	public static void main(String[] args) {
		Facade worldSeriesInfo = new Facade();
		worldSeriesInfo._cli.start();
	}
	
	Facade(){
		_log = Log.getInstance();
		_dataStore = new DataStore("WorldSeries.csv");
		_dataProcessor = new DataProcessor(_dataStore);
		_cli = new CommandLineUserInterface(_dataProcessor);
	}
}
