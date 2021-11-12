package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSuite extends AspSyntax {
    AspSmallStmtList smallStmtList;
    ArrayList <AspStmt> stmt = new ArrayList<>();
    Boolean isSmallStmtList = true;
    
    AspSuite(int n) {
        super(n);
    }

    static AspSuite parse(Scanner s) {
        enterParser("suite");

        AspSuite as = new AspSuite(s.curLineNum());
        switch (s.curToken().kind) {
            case nameToken:
                as.smallStmtList = AspSmallStmtList.parse(s);
                break;
            case passToken:
                as.smallStmtList = AspSmallStmtList.parse(s);
                break;
            case returnToken:
                as.smallStmtList = AspSmallStmtList.parse(s);
                break;
            case notToken:
                as.smallStmtList = AspSmallStmtList.parse(s);
                break;
            case plusToken:
                as.smallStmtList = AspSmallStmtList.parse(s);
                break;
            case minusToken:
                as.smallStmtList = AspSmallStmtList.parse(s);
                break;
            case integerToken:
                as.smallStmtList = AspSmallStmtList.parse(s);
                break;
            case floatToken:
                as.smallStmtList = AspSmallStmtList.parse(s);
                break;
            case stringToken:
                as.smallStmtList = AspSmallStmtList.parse(s);
                break;
            case noneToken:
                as.smallStmtList = AspSmallStmtList.parse(s);
                break;
            case leftParToken:
                as.smallStmtList = AspSmallStmtList.parse(s);
                break;
            case leftBracketToken:
                as.smallStmtList = AspSmallStmtList.parse(s);
                break;
            case leftBraceToken:
                as.smallStmtList = AspSmallStmtList.parse(s);
                break;
            case trueToken:
                as.smallStmtList = AspSmallStmtList.parse(s);
                break;
            case falseToken:
                as.smallStmtList = AspSmallStmtList.parse(s);
                break;
            case newLineToken:
                as.isSmallStmtList = false;
                skip(s, newLineToken);
                skip(s, indentToken);
                while (true) {
                    as.stmt.add(AspStmt.parse(s));
                    if (s.curToken().kind == dedentToken) {
                        skip(s, dedentToken);
                        break;
                    }
                }
            default: break;
        }
        leaveParser("suite");
        return as;
    }


    @Override
    public void prettyPrint() {
        Boolean prettyIsSmallStmtList = isSmallStmtList;
        if (isSmallStmtList) {
            AspSmallStmtList prettySmallStmtList = smallStmtList;
            prettySmallStmtList.prettyPrint();
        }
        else if (!isSmallStmtList) {
            prettyWriteLn();
            prettyIndent();
            for (AspStmt prettyStmt: stmt) {
                prettyStmt.prettyPrint();
            }
            prettyDedent();
        }
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	    if (smallStmtList == null) {
            for (AspStmt stmt1: stmt) {
                stmt1.eval(curScope);
            }
        }
        else {
            smallStmtList.eval(curScope);
        }
	    return null;
    }
}