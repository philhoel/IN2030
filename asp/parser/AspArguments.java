package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspArguments extends AspPrimarySuffix {
    ArrayList<AspExpr> expr = new ArrayList<>();

    AspArguments(int n) {
        super(n);
    }

    static AspArguments parse(Scanner s) {
        enterParser("arguments");

        AspArguments aArg = new AspArguments(s.curLineNum());

        skip(s, leftParToken);
        if (s.curToken().kind != rightParToken) {
            while (true) {
                aArg.expr.add(AspExpr.parse(s));
                if (s.curToken().kind != commaToken) {
                    break;
                }
                skip(s, commaToken);
            }
        }
        skip(s, rightParToken);

        leaveParser("arguments");
        return aArg;
    }

    @Override
    public void prettyPrint() {
        int nPrinted = 0;

        prettyWrite("(");
        for (AspExpr ae: expr) {
            if (nPrinted > 0) {
                prettyWrite(", ");
            }
            ae.prettyPrint();
            nPrinted += 1;
        }
        prettyWrite(")");
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        ArrayList<RuntimeValue> v = new ArrayList<>();

        System.out.println("inn i argument eval  line: " + this.lineNum);
        for (AspExpr argExpr: expr) {
            v.add(argExpr.eval(curScope));
        }
        return new RuntimeListValue(v);
    }
}