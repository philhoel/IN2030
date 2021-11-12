package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspBooleanLiteral extends AspAtom {
    Token token;
    boolean value;

    AspBooleanLiteral(int n) {
        super(n);
    }

    static AspBooleanLiteral parse(Scanner s) {
        enterParser("boolean literal");

        AspBooleanLiteral abl = new AspBooleanLiteral(s.curLineNum());

        if (s.curToken().kind == trueToken || s.curToken().kind == falseToken) {
            abl.token = s.curToken();
            if (s.curToken().kind == falseToken) {
                skip(s, falseToken);
                abl.value = false;
            }
            else if (s.curToken().kind == trueToken) {
                skip(s, trueToken);
                abl.value = true;
            }
        }
        else {
            parserError("Expected a boolean literal but found a " + s.curToken().kind
            + "!", s.curLineNum());
        }
        
        leaveParser("booleanLiteral");
        return abl;
    }

    @Override
    public void prettyPrint() {
        String booleanToken = token.toString();
        prettyWrite(booleanToken);
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	    return new RuntimeBoolValue(value);
    }
}
