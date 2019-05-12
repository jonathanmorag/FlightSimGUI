package interpreter;

public class SleepCommand implements Command {

	@Override
	public void execute(String[] line) {     // sleep 250
		try {
			Thread.sleep(Integer.parseInt(line[0]));
		}
		catch (NumberFormatException e) {} 
		catch (InterruptedException e) {}
	}

}