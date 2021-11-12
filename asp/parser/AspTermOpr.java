package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspTermOpr extends AspSyntax {
    TokenKind tokenKind;
    
    AspTermOpr(int n) {
        super(n);
    }

    static AspTermOpr parse(Scanner s) {
        enterParser("term opr");

        AspTermOpr ato = new AspTermOpr(s.curLineNum());

        switch (s.curToken().kind) {
            case plusToken:
                ato.tokenKind = s.curToken().kind;
                skip(s, plusToken);
                break;
            case minusToken:
                ato.tokenKind = s.curToken().kind;
                skip(s, minusToken);
                break;
            default: 
                parserError("Error related to AspTermOpr, expected a minusToken or plusToken!", 
                s.curLineNum());
        }

        leaveParser("term opr");
        return ato;
    }

    @Override
    public void prettyPrint() {
        TokenKind thisTokenKind = tokenKind;
        switch (thisTokenKind) {
            case plusToken:
                prettyWrite(" + ");
                break;
            case minusToken:
                prettyWrite(" - ");
                break;
            default: 
                break;
        }
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	    //-- Must be changed in part 3:
	    return null;
    }
}