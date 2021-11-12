package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFloatLiteral extends AspAtom {
    Token token;

    AspFloatLiteral(int n) {
        super(n);
    }

    static AspFloatLiteral parse(Scanner s) {
        enterParser("float literal");

        AspFloatLiteral afl = new AspFloatLiteral(s.curLineNum());

        if (s.curToken().kind == floatToken) {
            afl.token = s.curToken();
            skip(s, floatToken);
        }
        else {
            parserError("Expected an floatToken but found a " + s.curToken().kind 
            + "!" , s.curLineNum());
        }

        leaveParser("float literal");
        return afl;
    }

    @Override
    public void prettyPrint() {
        Token prettyToken = token;
        String floatLiteral = Double.toString(prettyToken.floatLit); 
        prettyWrite(" " + floatLiteral + " ");
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        System.out.println("returning a float value  line: " + this.lineNum);
	    return new RuntimeFloatValue(token.floatLit);
    }
}
