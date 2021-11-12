package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.RuntimeListValue;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspPrimary extends AspSyntax {
    AspAtom atom;
    ArrayList<AspPrimarySuffix> primarySuffix = new ArrayList<>();

    AspPrimary(int n){
        super(n);
    }

    static AspPrimary parse(Scanner s) {

        enterParser("primary");

        AspPrimary ap = new AspPrimary(s.curLineNum()); 
        ap.atom = AspAtom.parse(s);
        while (true){
            if (s.curToken().kind == leftParToken) {
                ap.primarySuffix.add(AspPrimarySuffix.parse(s));
            }
            else if (s.curToken().kind == leftBracketToken) {
                ap.primarySuffix.add(AspPrimarySuffix.parse(s));
            }
            else {
                break;
            }
        }

        leaveParser("primary");
        return ap;
    }

    @Override
    public void prettyPrint() {
        AspAtom prettyAtom = atom;
        
        prettyAtom.prettyPrint();
        for (AspPrimarySuffix prettyPrimarySuffix: primarySuffix) {
            prettyPrimarySuffix.prettyPrint();
        }
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        System.out.println("kjører primary eval  line: " + this.lineNum);
        RuntimeValue v = atom.eval(curScope);

        if (primarySuffix.isEmpty() != true) {
            int i = 0;
            while (i < primarySuffix.size()) {
                if (primarySuffix.get(i) instanceof AspArguments) {
                    ArrayList<RuntimeValue> params = new ArrayList<>();
                    RuntimeListValue x = (RuntimeListValue)primarySuffix.get(i).eval(curScope);
                    
                    params = x.list;
 
                    v = curScope.find(v.toString(), this);
                    v = v.evalFuncCall(params, this);
                }
                else {
                    v = v.evalSubscription(primarySuffix.get(i).eval(curScope), this);
                }
                i++;
            }
        }
        return v;
    }
}