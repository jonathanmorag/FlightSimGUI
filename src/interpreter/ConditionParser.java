package interpreter;

import java.util.ArrayList;
import java.util.List;

public class ConditionParser implements Command {

	List<String[]> commands;
	boolean cond;
	boolean whileFlag;
	String[] condLine;

	public ConditionParser() {
		commands = new ArrayList<>();
	}
	
	private boolean update(String[] line) { // while throttle > 0 {
		boolean cond = false;
		String operator = line[2];
		Double x = Interpreter.symTable.get(line[1]); // val of var
		if (x == null)
			return false;
		if (operator.equals(">") || operator.equals("<") || operator.equals("<=") || operator.equals(">=")
				|| operator.equals("==") || operator.equals("!=")) {
			switch (operator) {
			case "<":
				cond = x < Integer.parseInt(line[3]);
				break;
			case ">":
				cond = x > Integer.parseInt(line[3]);
				break;
			case "<=":
				cond = x <= Integer.parseInt(line[3]);
				break;
			case ">=":
				cond = x >= Integer.parseInt(line[3]);
				break;
			case "==":
				cond = x == Integer.parseInt(line[3]);
				break;
			case "!=":
				cond = x != Integer.parseInt(line[3]);
				break;
			}
		}
		return cond;
	}
	
	@Override
	public void execute(String[] line) {
		if (line[0].equals("while") || line[0].equals("if")) { // while throttle > 0 {
			condLine = line;
			whileFlag = line[0].equals("while");
			cond = update(condLine);
		} else {
			if (!line[0].equals("}")) {
				String firstWord = line[0].substring(1, line[0].length()); // \tprint -> print
				line[0] = firstWord;
			}
			commands.add(line);
		}
		if (line[0].equals("}") && cond) { // if the condition is met after reading all the cmnds successfully
			if (cond && whileFlag) {
				while (cond) {
					commands.forEach(lines -> {
						StringBuilder sb = new StringBuilder();
						if (lines.length <= 3) { 			// if the command is only applying value to a var
							for (int i = 0; i < lines.length; i++)
								sb.append(lines[i] + " ");
							sb.substring(0, sb.length() - 1);

							String[] temp = new String[1];
							temp[0] = sb.toString();
							new Parser().parse(temp);
							cond = update(condLine);
						} else { 							// the command contains math expression
							MathParser.parse(lines);
							cond = update(condLine);
						}
					});
				}
				
			} else {
				commands.forEach(lines -> {
					StringBuilder sb = new StringBuilder();
					if (lines.length <= 3) { 				// if the command is only applying value to a var
						for (int i = 0; i < lines.length; i++)
							sb.append(lines[i] + " ");
						sb.substring(0, sb.length() - 1);

						String[] temp = new String[1];
						temp[0] = sb.toString();
						new Parser().parse(temp);
					} else { 								// the command contains math expression
						MathParser.parse(lines);
					}
				});
				
			}
		}
		
	}
}
