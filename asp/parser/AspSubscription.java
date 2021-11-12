package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSubscription extends AspPrimarySuffix {
    AspExpr expr;

    AspSubscription(int n){
        super(n);

    }

    static AspSubscription parse(Scanner s) {
        enterParser("subscription");

        AspSubscription aSub = new AspSubscription(s.curLineNum());

        skip(s, leftBracketToken);
        aSub.expr = AspExpr.parse(s);
        skip(s, rightBracketToken);

        leaveParser("subscription");
        return aSub;
    }

    @Override
    public void prettyPrint() {
        AspExpr prettyExpr = expr;

        prettyWrite("[");
        prettyExpr.prettyPrint();
        prettyWrite("]");
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        System.out.println("inne i subscription eval  line: " + this.lineNum);
        AspExpr subExpr = expr;
        RuntimeValue v = subExpr.eval(curScope);
	    return v;
    }
}