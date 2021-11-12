package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspWhileStmt extends AspCompoundStmt {
    AspExpr expr;
    AspSuite suite;

    AspWhileStmt(int n){
        super(n);
    }

    static AspWhileStmt parse(Scanner s) {
        enterParser("while stmt");

        AspWhileStmt aws = new AspWhileStmt(s.curLineNum());
        skip(s, whileToken);
        aws.expr = AspExpr.parse(s);
        skip(s, colonToken);
        aws.suite = AspSuite.parse(s);

        leaveParser("while stmt");
        return aws;
    }

    @Override
    public void prettyPrint() {
        AspExpr whileExpr = expr;
        AspSuite whileSuite = suite;

        prettyWrite("while ");
        whileExpr.prettyPrint();
        prettyWrite(":");
        whileSuite.prettyPrint();
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        while (true) {
            RuntimeValue v = expr.eval(curScope);
            if (v.getBoolValue("while loop test", this)){
                break;
            }
            trace("while True: ");
            suite.eval(curScope);
        }
        trace("while False: ");
	    return null;
    }
}