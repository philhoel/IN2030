package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspStringLiteral extends AspAtom {
    Token token;

    AspStringLiteral(int n) {
        super(n);
    }

    static AspStringLiteral parse(Scanner s) {
        enterParser("string literal");

        AspStringLiteral asl = new AspStringLiteral(s.curLineNum());

        if (s.curToken().kind == stringToken) {
            asl.token = s.curToken();
            skip(s, stringToken);
        }
        else {
            parserError("Expected an stringToken but found a " + s.curToken().kind 
            + "!" , s.curLineNum());
        }

        leaveParser("string literal");
        return asl;
    }

    @Override
    public void prettyPrint() {
        String prettyStringLit = token.stringLit;
        prettyWrite("\"");
        prettyWrite(prettyStringLit);
        prettyWrite("\"");
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeStringValue(token.stringLit);
    }
}
