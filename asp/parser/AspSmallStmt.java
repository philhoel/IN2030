package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

abstract class AspSmallStmt extends AspSyntax {
    public AspSmallStmt(int n){
        super(n);
    }
    static AspSmallStmt parse(Scanner s){
        enterParser("small stmt");
        
        AspSmallStmt ass = null;

        switch (s.curToken().kind) {
            case nameToken:
                if (s.anyEqualToken()) {
                    ass = AspAssignment.parse(s);
                    break;
                }
                else if (!s.anyEqualToken()) {
                    ass = AspExprStmt.parse(s);
                    break;
                }
            case passToken:
                ass = AspPassStmt.parse(s);
                break;
            case returnToken:
                ass = AspReturnStmt.parse(s);
                break;
            case notToken:
                ass = AspExprStmt.parse(s);
                break;
            case plusToken:
                ass = AspExprStmt.parse(s);
                break;
            case minusToken:
                ass = AspExprStmt.parse(s);
                break;
            case integerToken:
                ass = AspExprStmt.parse(s);
                break;
            case floatToken:
                ass = AspExprStmt.parse(s);
                break;
            case stringToken:
                ass = AspExprStmt.parse(s);
                break;
            case noneToken:
                ass = AspExprStmt.parse(s);
                break;
            case leftParToken:
                ass = AspExprStmt.parse(s);
                break;
            case leftBracketToken:
                ass = AspExprStmt.parse(s);
                break;
            case leftBraceToken:
                ass = AspExprStmt.parse(s);
                break;
            case trueToken:
                ass = AspExprStmt.parse(s);
                break;
            case falseToken:
                ass = AspExprStmt.parse(s);
                break;
            default:
                parserError("The token does not match any required token for a small stmt" + s.curToken().kind, s.curLineNum());
                break;
        }
        leaveParser("small stmt");
        return ass;
    }
}