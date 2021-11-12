package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspTerm extends AspSyntax {
    ArrayList<AspFactor> factor = new ArrayList<>();
    ArrayList<AspTermOpr> termOpr = new ArrayList<>();

    AspTerm(int n){
        super(n);
    }

    static AspTerm parse(Scanner s){
        enterParser("term");

        AspTerm at = new AspTerm(s.curLineNum());
        while (true) {
            at.factor.add(AspFactor.parse(s));
            if (s.curToken().kind != plusToken && s.curToken().kind != minusToken){
                break;
                }
            at.termOpr.add(AspTermOpr.parse(s));
        }

        leaveParser("term");
        return at;
    }


    @Override
    public void prettyPrint() {
        int nPrinted = 0;
        for (AspFactor factors: factor) {
            if (nPrinted > 0){
                AspTermOpr termOprs = termOpr.get(nPrinted-1);
                termOprs.prettyPrint();
            }
            factors.prettyPrint();
            nPrinted++;
        }
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        System.out.println("kjører term eval" + "  line: " + this.lineNum);
        RuntimeValue v = factor.get(0).eval(curScope);
        for (int i = 1; i < factor.size(); i++) {
            TokenKind k = termOpr.get(i-1).tokenKind;
            switch (k) {
                case plusToken:
                    v = v.evalAdd(factor.get(i).eval(curScope), this);
                    break;
                case minusToken:
                    v = v.evalSubtract(factor.get(i).eval(curScope), this);
                    break;
                default:
                    Main.panic("Illegal term operator: " + k + "!");
            }
        }
	    return v;
    }
}
