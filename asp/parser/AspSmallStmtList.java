package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSmallStmtList extends AspStmt {
    ArrayList<AspSmallStmt> smallStmt = new ArrayList<>();

    AspSmallStmtList(int n) {
        super(n);
    }

    static AspSmallStmtList parse(Scanner s){
        enterParser("small stmt list");

        AspSmallStmtList assl = new AspSmallStmtList(s.curLineNum());

        assl.smallStmt.add(AspSmallStmt.parse(s));
        while (true) {
            if (s.curToken().kind == semicolonToken){
                skip(s, semicolonToken);
            }
            if (s.curToken().kind == newLineToken){
                skip(s, newLineToken);
                break;
            }
            assl.smallStmt.add(AspSmallStmt.parse(s));
        }
        leaveParser("small stmt list");
        return assl;
    }

    @Override
    public void prettyPrint() {
        for (AspSmallStmt prettySmallStmt: smallStmt) {
            prettySmallStmt.prettyPrint();
            prettyWriteLn();
        }
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = null;
        for (AspSmallStmt stmts: smallStmt) {
            v = stmts.eval(curScope);
        }
	    return v;
    }
}