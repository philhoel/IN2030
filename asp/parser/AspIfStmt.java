package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspIfStmt extends AspCompoundStmt {
    ArrayList <AspExpr> expr = new ArrayList<>();
    ArrayList <AspSuite> suite = new ArrayList<>();
    Boolean hasElse = false;

    AspIfStmt(int n) {
        super(n);
    }

    static AspIfStmt parse(Scanner s) {
        enterParser("if stmt");

        AspIfStmt ais = new AspIfStmt(s.curLineNum());

        skip(s, ifToken);

        while (true) {
            ais.expr.add(AspExpr.parse(s));
            skip(s, colonToken);
            ais.suite.add(AspSuite.parse(s));
            if (s.curToken().kind != elifToken) {
                break;
            }
            skip(s, elifToken);
        }
        if (s.curToken().kind == elseToken) {
            ais.hasElse = true;
            skip(s, elseToken);
            skip(s, colonToken);
            ais.suite.add(AspSuite.parse(s));
        }
        leaveParser("if stmt");
        return ais;
    }

    @Override
    public void prettyPrint() {
        int nPrinted = 0;
        ArrayList<AspSuite> prettySuite = suite;
        Boolean prettyHasElse = hasElse;
        AspSuite numberSuite;

        prettyWrite("if ");
        for (AspExpr prettyExpr: expr) {
            numberSuite = prettySuite.get(nPrinted);
            if (nPrinted > 0) {
                prettyWrite("elif ");
            }

            prettyExpr.prettyPrint();
            prettyWrite(": ");
            numberSuite.prettyPrint();

            nPrinted++;
        }
        if (prettyHasElse == true) {
            int last = prettySuite.size()-1;
            numberSuite = prettySuite.get(last);

            prettyWrite("else");
            prettyWrite(": ");
            numberSuite.prettyPrint();
        }
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = expr.get(0).eval(curScope);
        int i = 0;
        for (AspExpr evalexpr: expr) {
            v = evalexpr.eval(curScope);
            if (v.getBoolValue(v.typeName(), this)) {
                trace("if test True: ");
                suite.get(i).eval(curScope);
            }
            trace("if test false: ");
            i++;
        }
        if (hasElse) {
            trace("no if-tests true, this is else");
            int last = suite.size()-1;
            suite.get(last).eval(curScope);
        }
	    return null;
    }
}