package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspAssignment extends AspSmallStmt {
    ArrayList<AspSubscription> subscript = new ArrayList<>();
    AspName name;
    AspExpr expr;

    AspAssignment(int n) {
       super(n);
    }

    public static AspAssignment parse(Scanner s) {
        enterParser("assignment");
        AspAssignment aassign = new AspAssignment(s.curLineNum());
        
        aassign.name = AspName.parse(s);

        if (s.curToken().kind == leftBracketToken) {
            while (true) {
                aassign.subscript.add(AspSubscription.parse(s));
                if (s.curToken().kind == equalToken) {
                    skip(s, equalToken);
                    break;
                }
            }
        }
        else if (s.curToken().kind == equalToken) {
            skip(s, equalToken);
        }
        else {
            parserError("Unexpected token in assignment", s.curLineNum());
        }
        aassign.expr = AspExpr.parse(s);

        leaveParser("assignment");
        return aassign;
    }


    @Override
    public void prettyPrint() {
        name.prettyPrint();

        for (AspSubscription aSub: subscript) {
            aSub.prettyPrint();
        }
        
        prettyWrite(" = ");
        expr.prettyPrint();
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        System.out.println("assigner en verdi");
        if (subscript.isEmpty()) {
            curScope.assign(name.id, expr.eval(curScope));
        }
        else {
            RuntimeValue v = name.eval(curScope);
            int i = 0;
            while (i < subscript.size()-1) {
                v = v.evalSubscription(subscript.get(i).eval(curScope), this);
                i++;
            }
            v.evalAssignElem(subscript.get(i).eval(curScope), expr.eval(curScope), this);
        }
	    return null;
    }
}