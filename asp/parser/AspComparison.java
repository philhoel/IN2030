package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspComparison extends AspSyntax {
    ArrayList<AspTerm> term = new ArrayList<>();
    ArrayList<AspCompOpr> compOpr = new ArrayList<>();

    AspComparison(int n){
        super(n);
    }

    static AspComparison parse(Scanner s) {
        enterParser("comparison");

        AspComparison ac = new AspComparison(s.curLineNum());
        Boolean notTrueAnymore = false;
        while (!notTrueAnymore) {
            ac.term.add(AspTerm.parse(s));
            switch (s.curToken().kind) {
                case lessToken:
                    ac.compOpr.add(AspCompOpr.parse(s));
                    break;
                case greaterToken:
                    ac.compOpr.add(AspCompOpr.parse(s));
                    break;
                case doubleEqualToken:
                    ac.compOpr.add(AspCompOpr.parse(s));
                    break;
                case lessEqualToken:
                    ac.compOpr.add(AspCompOpr.parse(s));
                    break;
                case greaterEqualToken:
                    ac.compOpr.add(AspCompOpr.parse(s));
                    break;
                case notEqualToken:
                    ac.compOpr.add(AspCompOpr.parse(s));
                    break;
                default: 
                    notTrueAnymore = true;
                    break;
            }
        }

        leaveParser("comparison");
        return ac;
    }

    @Override
    public void prettyPrint() {
        int nPrinted = 0;
        for (AspTerm terms: term) {
            if (nPrinted > 0){
                AspCompOpr prettyCompOpr = compOpr.get(nPrinted-1);
                prettyCompOpr.prettyPrint();
            }
            terms.prettyPrint();
            nPrinted++;
        }
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        System.out.println("kjører comparison eval  line: " + this.lineNum);
        RuntimeValue v = term.get(0).eval(curScope);
        for (int i = 1; i < term.size(); i++) {
            TokenKind k = compOpr.get(i-1).tokenKind;
            System.out.println("after comp opr nr:" + i + "  line: " + this.lineNum);

            switch (k) {
                case lessToken:
                    v = v.evalLess(term.get(i).eval(curScope), this);
                    if (!v.getBoolValue("comparison", this)){
                        return new RuntimeBoolValue(false);
                    }
                    v = term.get(i).eval(curScope);
                    break;
                case greaterToken:
                    v = v.evalGreater(term.get(i).eval(curScope), this);
                    if (!v.getBoolValue("comparison", this)){
                        return new RuntimeBoolValue(false);
                    }
                    v = term.get(i).eval(curScope);
                    break;
                case doubleEqualToken:
                    v = v.evalEqual(term.get(i).eval(curScope), this);
                    if (!v.getBoolValue("comparison", this)){
                        return new RuntimeBoolValue(false);
                    }
                    v = term.get(i).eval(curScope);
                    break;
                case lessEqualToken:
                    v = v.evalLessEqual(term.get(i).eval(curScope), this);
                    if (!v.getBoolValue("comparison", this)){
                        return new RuntimeBoolValue(false);
                    }
                    v = term.get(i).eval(curScope);
                    break;
                case greaterEqualToken:
                    v = v.evalGreaterEqual(term.get(i).eval(curScope), this);
                    if (!v.getBoolValue("comparison", this)){
                        return new RuntimeBoolValue(false);
                    }
                    v = term.get(i).eval(curScope);
                    break;
                case notEqualToken:
                    v = v.evalNotEqual(term.get(i).eval(curScope), this);
                    if (!v.getBoolValue("comparison", this)){
                        return new RuntimeBoolValue(false);
                    }
                    v = term.get(i).eval(curScope);
                    break;
                default:
                    Main.panic("Illegal comp operator: " + k + "!");
            }
            if (i == term.size()-1) {
                return new RuntimeBoolValue(true);
            }
        }
	    return v;
    }
}