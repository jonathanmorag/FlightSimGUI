package interpreter;

public class PrintCommand implements Command {

	@Override
	public void execute(String[] line) {
		if (Interpreter.symTable.containsKey(line[0]))                       // print x
			System.out.println(Interpreter.symTable.get(line[0]));
		else
			System.out.println(line[0].substring(1, line[0].length() - 1));        // print "x"
	}

}