package interpreter;
import java.util.Arrays;

import shuntingYard.Q3;

public class MathParser {
	public static void parse(String[] expression) {

		double val = 0;
		String[] newExp = Arrays.copyOfRange(expression, 2, expression.length); // (h0 - heading)/20
		StringBuilder sb = new StringBuilder();
		String var = "";
		for (int i = 0; i < newExp.length; i++) {
			char firstChar = newExp[i].charAt(0);
			if (firstChar == '(') {
				sb.append("(");
				var = newExp[i].substring(1, newExp[i].length());
				sb.append(Interpreter.symTable.get(var)); // "(h0-
				continue;
			} else if (newExp[i].indexOf(")") > 0
					&& Interpreter.symTable.containsKey(newExp[i].substring(0, newExp[i].indexOf(")")))) {
				sb.append(Interpreter.symTable.get(newExp[i].substring(0, newExp[i].indexOf(')'))));
				sb.append(newExp[i].substring(newExp[i].indexOf(")"), newExp[i].length()));
			} else if (Interpreter.symTable.containsKey(newExp[i])) { // if encountered known var from sym Table
				sb.append(Interpreter.symTable.get(newExp[i]));
			} else // assuming its an operator
				sb.append(newExp[i]);
		}
	//	System.out.println("Expression is: " + sb.toString() + " and the final result is: " + Q3.calc(sb.toString()));
		val = Q3.calc(sb.toString());
		Interpreter.symTable.put(expression[0], val);
		//System.out.println("var's val is: " + Interpreter.symTable.get(expression[0]));

	}

}
