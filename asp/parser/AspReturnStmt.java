package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspReturnStmt extends AspSmallStmt {
    AspExpr expr;

    AspReturnStmt(int n) {
        super(n);
    }

    static AspReturnStmt parse(Scanner s) {
        enterParser("return stmt");
        AspReturnStmt ars = new AspReturnStmt(s.curLineNum());

        if (s.curToken().kind == returnToken){
            skip(s, returnToken);
        }
        else {
            parserError("Error in or related to AspReturnStmt, expected a returnToken", s.curLineNum());
        }
        ars.expr = AspExpr.parse(s);

        leaveParser("return stmt");
        return ars;
    }


    @Override
    public void prettyPrint() {
        AspExpr prettyExpr = expr;

        prettyWrite("return ");
        prettyExpr.prettyPrint();
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = expr.eval(curScope);
        trace("return " + v.showInfo());
        throw new RuntimeReturnValue(v, lineNum);
    }
}