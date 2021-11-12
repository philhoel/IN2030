package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspForStmt extends AspCompoundStmt {
    AspName name;
    AspExpr expr;
    AspSuite suite;

    AspForStmt(int n) {
        super(n);
    }

    static AspForStmt parse(Scanner s) {
        enterParser("for stmt");

        AspForStmt afs = new AspForStmt(s.curLineNum());

        skip(s, forToken);
        afs.name = AspName.parse(s);
        skip(s, inToken);
        afs.expr = AspExpr.parse(s);
        skip(s, colonToken);
        afs.suite = AspSuite.parse(s);

        leaveParser("forStmt");
        return afs;
    }


    @Override
    public void prettyPrint() {
        AspName prettyName = name;
        AspExpr prettyExpr = expr;
        AspSuite prettySuite = suite;
        
        prettyWrite("for ");
        prettyName.prettyPrint();
        prettyWrite(" in ");
        prettyExpr.prettyPrint();
        prettyWrite(": ");
        prettySuite.prettyPrint();
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = expr.eval(curScope);
        if (!(v instanceof RuntimeListValue)) {
            parserError("for stmt expr is not a list", this.lineNum);
        }
        RuntimeListValue vList = (RuntimeListValue)v;
        for (RuntimeValue vElement: vList.list) {
            curScope.assign(name.eval(curScope).toString(), vElement);
            suite.eval(curScope);
        }
	    return null;
    }
}