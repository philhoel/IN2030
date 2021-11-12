package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspExprStmt extends AspSmallStmt {
    AspExpr expr;

    AspExprStmt(int n) {
        super(n);
    }

    static AspExprStmt parse(Scanner s) {
        enterParser("expr stmt");
        AspExprStmt aes = new AspExprStmt(s.curLineNum());

        aes.expr = AspExpr.parse(s);

        leaveParser("expr stmt");
        return aes;
    }

    @Override
    public void prettyPrint() {
        AspExpr exprStmtExpr = expr;
        exprStmtExpr.prettyPrint();
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	    //-- Must be changed in part 3:
	    return null;
    }
}