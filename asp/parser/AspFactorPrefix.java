package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFactorPrefix extends AspSyntax {
    TokenKind tokenKind;
    AspFactorPrefix(int n) {
        super(n);
    }

    static AspFactorPrefix parse(Scanner s) {
        enterParser("factor prefix");

        AspFactorPrefix afp = new AspFactorPrefix(s.curLineNum());

        if (s.curToken().kind == plusToken) {
            afp.tokenKind = s.curToken().kind;
            skip(s, plusToken);
            }
        else if (s.curToken().kind == minusToken) {
            afp.tokenKind = s.curToken().kind;
            skip(s, minusToken);
            }
        else {
            parserError("Error related to factor prefix, the desired was not found!",
            s.curLineNum());
            }
        
        leaveParser("factor prefix");
        return afp;
    }

    @Override
    public void prettyPrint() {
        TokenKind prettyTokenKind = tokenKind;
        switch (prettyTokenKind) {
            case plusToken:
                prettyWrite(" + ");
                break;
            case minusToken:
                prettyWrite(" - ");
                break;
            default:
                System.out.println("Something wrong in factorPrefix prettyPrint");
                break;
        }
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	    //-- Must be changed in part 3:
	    return null;
    }
}