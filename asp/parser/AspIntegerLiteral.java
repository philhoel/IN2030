package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspIntegerLiteral extends AspAtom {
    Token token;

    AspIntegerLiteral(int n) {
        super(n);
    }

    static AspIntegerLiteral parse(Scanner s) {
        enterParser("integer literal");

        AspIntegerLiteral ail = new AspIntegerLiteral(s.curLineNum());

        if (s.curToken().kind == integerToken) {
            ail.token = s.curToken();
            skip(s, integerToken);
        }
        else {
            parserError("Expected an integerToken but found a " + s.curToken().kind 
            + "!" , s.curLineNum());
        }

        leaveParser("integerLiteral");
        return ail;
    }

    @Override
    public void prettyPrint() {
        Token prettyToken = token;
        String integerLiteral = Long.toString(prettyToken.integerLit);
        prettyWrite(integerLiteral);
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	    return new RuntimeIntValue(token.integerLit);
    }
}