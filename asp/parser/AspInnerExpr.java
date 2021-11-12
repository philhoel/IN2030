package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspInnerExpr extends AspAtom {
    AspExpr expr;

    AspInnerExpr(int n) {
        super(n);
    }

    static AspInnerExpr parse(Scanner s) {
        enterParser("inner expr");

        AspInnerExpr aie = new AspInnerExpr(s.curLineNum());

        skip(s, leftParToken);
        aie.expr = AspExpr.parse(s);
        skip(s, rightParToken);

        leaveParser("inner expr");
        return aie;
    }

    @Override
    public void prettyPrint() {
        AspExpr prettyExpr = expr;
        prettyWrite(" (");
        prettyExpr.prettyPrint();
        prettyWrite(") ");
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        System.out.println("inne i inner expr eval  line: " + this.lineNum);
        RuntimeValue v = expr.eval(curScope);
        return v;
    }
}