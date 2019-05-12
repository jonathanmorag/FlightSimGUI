package interpreter;

public class DeclareVarCommand implements Command {// y=x+3
	
	@Override
	public void execute(String[] line) {   // throttle = bind " /controls/engines/current-engine/throttle"
		
		if(line.length==4) { // [y, =, bind, simX]
			if(!Interpreter.symTable.containsKey(line[3]))
				Interpreter.varToPath.put(line[0],line[3]);
			else {
				Interpreter.symTable.put(line[0],Interpreter.symTable.get(line[3]));
				Interpreter.varToPath.put(line[0],line[3]);
			}
		}
		if (line.length == 3) { // assigning a known var's value to a different var for exm: throttle = 10
			if (Interpreter.symTable.containsKey(line[2]))
				Interpreter.symTable.put(line[0], Interpreter.symTable.get(line[2]));
			else {
				Interpreter.symTable.put(line[0], Double.parseDouble(line[2]));
			}
		}
		else {
//			StringBuilder sb = new StringBuilder(line[3]);
//			Interpreter.symTable.putIfAbsent(line[0],0.0);
//			Interpreter.varToPath.put(line[0], sb.substring(1,sb.length()-1).toString());
			if(line.length==1) {
				if(line[0].contains("=")) { 
					Command ec = new EqualsCommand();
					ec.execute(line);
				}else
				Interpreter.symTable.put(line[0],0.0);
				//Interpreter.varToPath.putIfAbsent(line[0],"");
			}
		}
	}

}
