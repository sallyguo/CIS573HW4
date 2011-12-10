import java.io.File;
import java.io.FileWriter;


public class Log {
	protected static Log singletonInstance = null;
	FileWriter writer = null;
	
	private Log() {
		try {
			writer = new FileWriter(new File("log.txt"), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Log getInstance() {
		if (singletonInstance == null)
			singletonInstance = new Log();
		return singletonInstance;
	}
	
	public void log(String event) {
		try {
			writer.write(System.currentTimeMillis() + " (DataProcessor): " + event +"\n");
			writer.flush();
		}
		catch (Exception e) { e.printStackTrace(); }
	}
}
