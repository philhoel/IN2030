package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspName extends AspAtom {
    Token token;
    public String id;

    AspName(int n) {
        super(n);
    }

    static AspName parse(Scanner s) {
        enterParser("name");

        AspName an = new AspName(s.curLineNum());

        if (s.curToken().kind == nameToken) {
            an.token = s.curToken();
            skip(s, nameToken);
            an.id = an.token.name;
        }
        else {
            parserError("Expected an nameToken but found a " + s.curToken().kind 
            + "!" , s.curLineNum());
        }

        leaveParser("name");
        return an;
    }

    @Override
    public void prettyPrint() {
        Token prettyToken = token;
        String prettyName = prettyToken.name;
        prettyWrite(prettyName);
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	    return curScope.find(id, this);
    }
}