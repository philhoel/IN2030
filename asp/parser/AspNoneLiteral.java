package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspNoneLiteral extends AspAtom {
    Token token;

    AspNoneLiteral(int n) {
        super(n);
    }

    static AspNoneLiteral parse(Scanner s) {
        enterParser("none literal");

        AspNoneLiteral anl = new AspNoneLiteral(s.curLineNum());

        if (s.curToken().kind == noneToken) {
            anl.token = s.curToken();
            skip(s, noneToken);
        }
        else {
            parserError("Expected a noneToken, but found a " + 
            s.curToken().kind + "!", s.curLineNum());
        }

        leaveParser("none literal");
        return anl;
    }


    @Override
    public void prettyPrint() {
        prettyWrite(" None ");
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	    return new RuntimeNoneValue();
    }
}