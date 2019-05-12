package interpreter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter {

	public static Map<String, Command> cmdsMap = new HashMap<>();
	static Map<String, Double> symTable = new HashMap<>();
	static Map<String, String> varToPath = new HashMap<>();
	static Command cp = new ConditionParser();
	
	public Interpreter() {
		Interpreter.cmdsMap.put("print", new PrintCommand());				// Assigning Commands
		Interpreter.cmdsMap.put("var", new DeclareVarCommand());
		Interpreter.cmdsMap.put("openDataServer", new OpenServerCommand());
		Interpreter.cmdsMap.put("connect", new ConnectCommand());
		Interpreter.cmdsMap.put("if", new ConditionParser());
		Interpreter.cmdsMap.put("while", new ConditionParser());
		Interpreter.cmdsMap.put("sleep", new SleepCommand());
		Interpreter.cmdsMap.put("return", new ReturnCommand());
		Interpreter.cmdsMap.put("disconnect", new DisconnectCommand());
	}
	
	public int interpret(String[] lines) {
		return new Parser().parse(lines);
	}
	
	public String[] lexer(String fileName) {
		List<String> lines = new ArrayList<>();
		try {
			Files.lines(Paths.get("./resources/"+fileName)).forEach(fileLine -> {
				lines.add(fileLine);
			});
		} catch (IOException e) {}

		return lines.stream().toArray(String[]::new);
	}

	/*
	
	public static void main(String[] args) throws InterruptedException {
		cmdsMap.put("print", new PrintCommand());		//Assigning cmnds
		cmdsMap.put("var", new DeclareVarCommand());
		cmdsMap.put("openDataServer", new OpenServerCommand());
		cmdsMap.put("connect", new ConnectCommand());
		cmdsMap.put("if", new ConditionParser());
		cmdsMap.put("while", new ConditionParser());
		cmdsMap.put("sleep", new SleepCommand());
		cmdsMap.put("return", new ReturnCommand());
		symTable.put("throttle", null);     			//Assigning vars
		symTable.put("breaks", null);
		symTable.put("x", null);
		
		Parser.parse(lexer("inputFromSimulator.txt"));	
		ConnectCommand.closeClient.run();				//closing client
		OpenServerCommand.drs.stop();					//closing server
		
		symTable.entrySet().forEach(e -> System.out.println("line in symTable : " + e.getKey() + " " + e.getValue()));
	} */

}
