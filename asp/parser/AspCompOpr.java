package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspCompOpr extends AspSyntax {
    TokenKind tokenKind;

    AspCompOpr(int n){
        super(n);
    }

    static AspCompOpr parse(Scanner s){
        enterParser("comp opr");

        AspCompOpr aco = new AspCompOpr(s.curLineNum());
        switch (s.curToken().kind) {
            case lessToken:
                aco.tokenKind = s.curToken().kind;
                skip(s, lessToken);
                break;
            case greaterToken:
                aco.tokenKind = s.curToken().kind;
                skip(s, greaterToken);
                break;
            case doubleEqualToken:
                aco.tokenKind = s.curToken().kind;
                skip(s, doubleEqualToken);
                break;
            case lessEqualToken:
                aco.tokenKind = s.curToken().kind;
                skip(s, lessEqualToken);
                break;
            case greaterEqualToken:
                aco.tokenKind = s.curToken().kind;
                skip(s, greaterEqualToken);
                break;
            case notEqualToken:
                aco.tokenKind = s.curToken().kind;
                skip(s, notEqualToken);
                break;
            default: 
                parserError("Token is not included in Comp Opr", s.curLineNum());
                break;
        }

        leaveParser("comp opr");
        return aco;
    }

    @Override
    public void prettyPrint() {
        TokenKind thisTokenKind = tokenKind;
        switch (thisTokenKind) {
            case lessToken:
                prettyWrite(" < ");
                break;
            case greaterToken:
                prettyWrite(" > ");
                break;
            case doubleEqualToken:
                prettyWrite(" == ");
                break;
            case lessEqualToken:
                prettyWrite(" <= ");
                break;
            case greaterEqualToken:
                prettyWrite(" >= ");
                break;
            case notEqualToken:
                prettyWrite(" != ");
                break;
            default: 
                System.out.println("Something wrong in CompOpr prettyprint");
                break;
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // TODO Auto-generated method stub
        return null;
    }
}