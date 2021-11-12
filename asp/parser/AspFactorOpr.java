package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFactorOpr extends AspSyntax {
    TokenKind tokenKind;
    AspFactorOpr(int n){
        super(n);
    }

    static AspFactorOpr parse(Scanner s){
        enterParser("factor opr");

        AspFactorOpr afp = new AspFactorOpr(s.curLineNum());

        switch (s.curToken().kind) {
            case astToken:
                afp.tokenKind = s.curToken().kind;
                skip(s, astToken);
                break;
            case slashToken:
                afp.tokenKind = s.curToken().kind;
                skip(s, slashToken);
                break;
            case percentToken:
                afp.tokenKind = s.curToken().kind;
                skip(s, percentToken);
                break;
            case doubleSlashToken:
                afp.tokenKind = s.curToken().kind;
                skip(s, doubleSlashToken);
                break;
            default:
                parserError("Error related to factor opr, did not find desired token!", s.curLineNum());
        }

        leaveParser("factor opr");
        return afp;
    }

    @Override
    public void prettyPrint() {
        TokenKind prettyTokenKind = tokenKind;
        switch (prettyTokenKind) {
            case astToken:
                prettyWrite(" * ");
                break;
            case slashToken:
                prettyWrite(" / ");
                break;
            case percentToken:
                prettyWrite(" % ");
                break;  
            case doubleSlashToken:
                prettyWrite(" // ");
                break;
            default:
                System.out.println("Something wrong in factorOpr prettyPrint");
                break;
        }
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	    return null;
    }
}