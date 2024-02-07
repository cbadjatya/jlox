package jlox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Lox {
	
	static boolean hadError = false;
	
	public static void main(String[] args) throws IOException {
		
		// Lox can be run either as a script or as prompt. Like Python.
		
		if(args.length > 1) {
			System.out.println("Usage : lox [script]");
			System.exit(64);
		}
		else if(args.length == 1) {
			runFile(args[0]);
		}
		else {
			runPrompt();
		}
		
	}
	
	private static void runFile(String path) throws IOException{
		
		byte[] bytes = Files.readAllBytes(Paths.get(path));			// why not read directly as string...converting anyway
		run(new String(bytes, Charset.defaultCharset()));
		
		if(hadError) System.exit(65);
		
	}
	
	private static void runPrompt() throws IOException {
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		
		// REPL : Read Evaluate Print Loop
			
		while(true) {
			System.out.print(">>");
			String line = reader.readLine();
			if(line == null) break;
			run(line);
			hadError = false;
		}
	}
	
	private static void run(String source) {
		
		Scanner scanner = new Scanner(source);
		ArrayList<Token> tokens = (ArrayList<Token>) scanner.scanTokens();
		
		
		Parser parser = new Parser(tokens);
		Expr expression = parser.parse();
		
		// If syntax error found...
		if(hadError) return;
		
		
		System.out.println(new ASTPrinter().print(expression));
	}
	
	
	// Error Handling  Implementation
	public static void error(int line, String message) {
		report(line, "", message);
	}
	
	private static void report(int line, String where, String what) {
		System.err.println("line["+line+"] Error" + where +": " + what);
		hadError = true;
	}
	
	public static void error(Token token, String message) {
		if(token.type == TokenType.EOF) {
			report(token.line, " at End", message);
		}
		else report(token.line, " at "+token.lexeme+" ", message);
	}
	
	
	
	
	
	
	
}
