package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;

import no.uio.ifi.asp.main.*;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;




public class Scanner {
    private LineNumberReader sourceFile = null;
    private String curFileName;
    private ArrayList<Token> curLineTokens = new ArrayList<>();
    private Stack<Integer> indents = new Stack<>();
    private final int TABDIST = 4;


    public Scanner(String fileName) {
	curFileName = fileName;
	indents.push(0);

	try {
	    sourceFile = new LineNumberReader(
			    new InputStreamReader(
				new FileInputStream(fileName),
				"UTF-8"));
	} catch (IOException e) {
	    scannerError("Cannot read " + fileName + "!");
	}
    }


    private void scannerError(String message) {
	String m = "Asp scanner error";
	if (curLineNum() > 0)
	    m += " on line " + curLineNum();
	m += ": " + message;

	Main.error(m);
    }


    public Token curToken() {
	while (curLineTokens.isEmpty()) {
	    readNextLine();
	}
	return curLineTokens.get(0);
    }


    public void readNextToken() {
	if (! curLineTokens.isEmpty())
	    curLineTokens.remove(0);
    }


    private void readNextLine() {
	curLineTokens.clear();

	// Read the next line:
	String line = null;
	try {
		line = sourceFile.readLine();
	    if (line == null) {
		while (indents.peek() > 0) {
			Token dedent = new Token(dedentToken, curLineNum());
			curLineTokens.add(dedent);
			indents.pop();
		}

		Token eofT = new Token(eofToken, curLineNum());
		curLineTokens.add(eofT);
		sourceFile.close();
		sourceFile = null;

		for (Token tok: curLineTokens){
			Main.log.noteToken(tok);
		}
			return;
	    } else {
			Main.log.noteSourceLine(curLineNum(), line);
	    }
	} catch (IOException e) {
	    sourceFile = null;
	    scannerError("Unspecified I/O error!");
	}

	
	//Omformer alle innledende TAB-er til blanke
	line = expandLeadingTabs(line);
	

	//Sjekker om linjen er blank eller er en kommentar
	String lineWithoutWhitespace = line.strip();
	if (lineWithoutWhitespace.isBlank() || lineWithoutWhitespace.charAt(0) == '#') {
		return;
	}
	
	//Finner hvor mange innledende blanke på linjen vi leser og sjekker opp mot den
	//Forrige linjen vi leste
	if (findIndent(line) > indents.peek()) {
		indents.push(findIndent(line));
		Token indent = new Token(indentToken, curLineNum());
		curLineTokens.add(indent);
	}
	else if (findIndent(line) < indents.peek()) {
		indents.pop();
		Token dedent = new Token(dedentToken, curLineNum());
		curLineTokens.add(dedent);
		while (findIndent(line) < indents.peek()){
			indents.pop();
			curLineTokens.add(dedent);
		}
	}
	else if (findIndent(line) != indents.peek()) {
		Main.panic("Indentation Error", curLineNum());
	}

	
	//går igjennom hver character i linjen
	for (int i = 0; i < line.length(); i++) {
		char currentChar = line.charAt(i);
		//Lager en instans av nameToken hvis en character eller en blanding av bokstaver
		//og digits 
		if (isLetterAZ(currentChar)) {
			String nameString = "";
			int j = i+1;
			nameString = nameString + currentChar;
			while (j < line.length()) {
				char nextChar = line.charAt(j);
				if (isDigit(nextChar) || isLetterAZ(nextChar)){
					nameString = nameString + nextChar;
					j++;
				}
				else {
					break;
				}
			}
			switch (nameString) {
				case "and": Token andT = new Token(andToken, curLineNum());
							i = j-1;
							curLineTokens.add(andT);
							break;
				case "def": Token defT = new Token(defToken, curLineNum());
							i = j-1;
							curLineTokens.add(defT);
							break;
				case "elif": Token elifT = new Token(elifToken, curLineNum());
							 i = j-1;
							 curLineTokens.add(elifT);
							 break;
				case "else": Token elseT = new Token(elseToken, curLineNum());
							 i = j-1;
							 curLineTokens.add(elseT);
							 break;
				case "False": Token falseT = new Token(falseToken, curLineNum());
							  i = j-1;
							  curLineTokens.add(falseT);
							  break;
				case "for": Token forT = new Token(forToken, curLineNum());
							i = j-1;
							curLineTokens.add(forT);
							break;
				case "if": Token ifT = new Token(ifToken, curLineNum());
						   i = j-1;
						   curLineTokens.add(ifT);
						   break;
				case "in": Token inT = new Token(inToken, curLineNum());
						   i = j-1;
						   curLineTokens.add(inT);
						   break;
				case "None": Token noneT = new Token(noneToken, curLineNum());
							 i = j-1;
							 curLineTokens.add(noneT);
							 break;
				case "not": Token notT = new Token(notToken, curLineNum());
							i = j-1;
							curLineTokens.add(notT);
							break;
				case "or": Token orT = new Token(orToken, curLineNum());
						   i = j-1;
						   curLineTokens.add(orT);
						   break;
				case "pass": Token passT = new Token(passToken, curLineNum());
							 i = j-1;
							 curLineTokens.add(passT);
							 break;
				case "return": Token returnT = new Token(returnToken, curLineNum());
							   i = j-1;
							   curLineTokens.add(returnT);
							   break;
				case "True": Token trueT = new Token(trueToken, curLineNum());
							 i = j-1;
							 curLineTokens.add(trueT);
							 break;
				case "while": Token whileT = new Token(whileToken, curLineNum());
							  i = j-1;
							  curLineTokens.add(whileT);
							  break;
				default: Token name = new Token(nameToken, curLineNum());
						 name.name = nameString;
						 curLineTokens.add(name);
						 i = j-1;
						 break;
			}	
			
		}
		else if (Character.isWhitespace(currentChar)) {	
			;			
		}
		//Integer eller float Token
		else if (isDigit(currentChar)) {
			String number = "";
			number = number + currentChar;
			Boolean floatNumber = false;
			//Så lenge neste character er et tall eller et punktum skal vi fortsette
			//å lese inn tall
			int j = i+1;
			while (j < line.length()){
				char nextChar = line.charAt(j);
				int dotCount = 0;
				if (isDigit(nextChar)) {
					number = number + nextChar;
					j++;
				}
				else if (nextChar == '.' && dotCount == 0){
					dotCount++;
					number = number + nextChar;
					j++;
					floatNumber = true;
					while (j < line.length()){
						nextChar = line.charAt(j);
						if (isDigit(nextChar)){
							break;
						}
						else {
							Main.error("Kan ikke avslutte et tall med punktum");
						}
					}
				}
				else {
					break;
				}
				
			}
			if (floatNumber == true) {
				Token numberFloat = new Token(floatToken, curLineNum());
				float f = Float.parseFloat(number);
				numberFloat.floatLit = f;
				curLineTokens.add(numberFloat);
				i = j-1;
			}
			else {
				Token numberInt = new Token(integerToken, curLineNum());
				long n = Integer.parseInt(number);
				numberInt.integerLit = n;
				curLineTokens.add(numberInt);
				i = j-1;
			}
		}
		//Lage stringtoken
		else if (currentChar == '"' || currentChar == '\'') {
			String containedInString = "";
			boolean endOfString = false;
			i++;
			currentChar = line.charAt(i);
			while (i < line.length()){
				if (currentChar == '"' || currentChar == '\''){
					endOfString = true; 
					break;
					
				}
				else {
					containedInString = containedInString + currentChar;
				}
				i++;
				currentChar = line.charAt(i);
			}
			if (endOfString != true){
				Main.error("Incomplete string found on line: " + curLineNum());
			}
			Token scannedString = new Token(stringToken, curLineNum());
			scannedString.stringLit = containedInString;
			curLineTokens.add(scannedString);
		}
		else {
			switch (currentChar) {
				case '*': Token ast = new Token(astToken, curLineNum());
						  curLineTokens.add(ast);
						  break;
	
				case '>': char nextChar = line.charAt(i+1);
						  if (nextChar == '=' ) {
							  Token greaterEqual = new Token(greaterEqualToken, curLineNum());
							  curLineTokens.add(greaterEqual);
							  i += 1;
							  break;
						  }
						  else {
							Token greaterThan = new Token(greaterToken, curLineNum());
							curLineTokens.add(greaterThan);
							  break;
						  }
	
				case '<': nextChar = line.charAt(i+1);
						  if (nextChar == '=' ) {
							  Token lessEqual = new Token(lessEqualToken, curLineNum());
							  curLineTokens.add(lessEqual);
							  i += 1;
							  break;
						  }
						  else {
							Token greaterThan = new Token(lessToken, curLineNum());
							curLineTokens.add(greaterThan);
							  break;
						  }
				case '-': Token minus = new Token(minusToken, curLineNum());
						  curLineTokens.add(minus);
						  break;
				case '!': nextChar = line.charAt(i+1);
						  if (nextChar == '='){
							  Token notEqual = new Token(notEqualToken, curLineNum());
							  curLineTokens.add(notEqual);
							  i += 1;
							  break;
						  }
	
				case '=': nextChar = line.charAt(i+1);
						  if (nextChar == '=') {
							Token doubleEqual = new Token(doubleEqualToken, curLineNum());
							curLineTokens.add(doubleEqual);
							i += 1;
							break;
						  }
						  else {
							  Token equalTok = new Token(equalToken, curLineNum());
							  curLineTokens.add(equalTok);
							  break;
						  }
				case '/': nextChar = line.charAt(i+1);
						  if (nextChar == '/') {
							Token doubleSlash = new Token(doubleSlashToken, curLineNum());
							curLineTokens.add(doubleSlash);
							i += 1;
							break;
						  }
						  else {
							Token slash = new Token(slashToken, curLineNum());
							curLineTokens.add(slash);
							break;
						  }
				case '%': Token percent = new Token(percentToken, curLineNum());
						  curLineTokens.add(percent);
						  break;
				case '+': Token plus = new Token(plusToken, curLineNum());
						  curLineTokens.add(plus);
						  break;
				case ':': Token colon = new Token(colonToken, curLineNum());
						  curLineTokens.add(colon);
						  break;
				case ',': Token comma = new Token(commaToken, curLineNum());
						  curLineTokens.add(comma);
						  break;
				case '{': Token leftBrace = new Token(leftBraceToken, curLineNum());
						  curLineTokens.add(leftBrace);
						  break;
				case '}': Token rightBrace = new Token(rightBraceToken, curLineNum());
						  curLineTokens.add(rightBrace);
						  break;
				case '[': Token leftBracket = new Token(leftBracketToken, curLineNum());
						  curLineTokens.add(leftBracket);
						  break;
				case ']': Token rightBracket = new Token(rightBracketToken, curLineNum());
						  curLineTokens.add(rightBracket);
						  break;
				case '(': Token leftPar = new Token(leftParToken, curLineNum());
						  curLineTokens.add(leftPar);
						  break;
				case ')': Token rightPar = new Token(rightParToken, curLineNum());
						  curLineTokens.add(rightPar);
						  break;
				case ';': Token semicolon = new Token(semicolonToken, curLineNum());
						  curLineTokens.add(semicolon);
						  break;
				default: if (currentChar == '#'){
							curLineTokens.add(new Token(newLineToken, curLineNum()));
							for (Token t: curLineTokens)
								Main.log.noteToken(t);
							return;
							}
						 else if (!Character.isWhitespace(currentChar)){
								Main.error(String.format("Illegal symbol %c found on line %d", currentChar, curLineNum()));
								break;
							 }
		
						}
					}

				}
		// Terminate line:
		curLineTokens.add(new Token(newLineToken, curLineNum()));
		for (Token t: curLineTokens)
			Main.log.noteToken(t);
	}

	


    public int curLineNum() {
	return sourceFile!=null ? sourceFile.getLineNumber() : 0;
    }

    private int findIndent(String s) {
	int indent = 0;

	while (indent<s.length() && s.charAt(indent)==' ') indent++;
	return indent;
    }

    private String expandLeadingTabs(String s) {
	String newS = "";
	for (int i = 0;  i < s.length();  i++) {
	    char c = s.charAt(i);
	    if (c == '\t') {
		do {
		    	newS += " ";
			} while (newS.length()%TABDIST > 0);
	    } else if (c == ' ') {
			newS += " ";
	    } else {
			newS += s.substring(i);
			break;
	    }
	}
	return newS;
    }


    private boolean isLetterAZ(char c) {
	return ('A'<=c && c<='Z') || ('a'<=c && c<='z') || (c=='_');
    }


    private boolean isDigit(char c) {
	return '0'<=c && c<='9';
    }


    public boolean isCompOpr() {
	TokenKind k = curToken().kind;
	//-- Must be changed in part 2:
	return false;
    }


    public boolean isFactorPrefix() {
	TokenKind k = curToken().kind;
	//-- Must be changed in part 2:
	return false;
    }


    public boolean isFactorOpr() {
	TokenKind k = curToken().kind;
	//-- Must be changed in part 2:
	return false;
    }
	

    public boolean isTermOpr() {
	TokenKind k = curToken().kind;
	//-- Must be changed in part 2:
	return false;
    }


    public boolean anyEqualToken() {
	for (Token t: curLineTokens) {
	    if (t.kind == equalToken) return true;
	    if (t.kind == semicolonToken) return false;
	}
	return false;
    }
}
