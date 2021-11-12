package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFuncDef extends AspCompoundStmt {
    public ArrayList<AspName> name = new ArrayList<>();
    public AspSuite suite;

    AspFuncDef(int n) {
        super(n);
    }

    static AspFuncDef parse(Scanner s) {
        enterParser("func def");
        AspFuncDef afd = new AspFuncDef(s.curLineNum());

        skip(s, defToken);
        afd.name.add(AspName.parse(s));
        skip(s, leftParToken);
        if (s.curToken().kind == nameToken) {
            while (true){
                afd.name.add(AspName.parse(s));
                if (s.curToken().kind != commaToken) {
                    break;
                }
                skip(s, commaToken);
            }
        }
        skip(s, rightParToken);
        skip(s, colonToken);
        afd.suite = AspSuite.parse(s);

        leaveParser("func def");
        return afd;
    }
    
    @Override
    public void prettyPrint() {
        AspSuite prettySuite = suite;
        int nPrinted = 0;

        prettyWrite("def ");

        for (AspName prettyName: name){
            if (nPrinted > 1) {
                prettyWrite(", ");
            }
            prettyName.prettyPrint();
            if (nPrinted == 0) {
                prettyWrite(" (");
            }
            nPrinted++;
        }
        prettyWrite(")");
        prettyWrite(": ");
        prettySuite.prettyPrint();
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        trace("def " + name.get(0).id);
        System.out.println("func def");
        ArrayList<String> params = new ArrayList<>();
        int i = 0;
        for (AspName param: name) {
            if (i > 0) {
                params.add(param.id);
                System.out.println("ye");
            }
            i++;
        }
        RuntimeFunc thisFunc = new RuntimeFunc(this, curScope, params);
        curScope.assign(name.get(0).id, thisFunc);
        return null;
    }
}