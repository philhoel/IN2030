package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspAndTest extends AspSyntax {
    ArrayList <AspNotTest> notTest = new ArrayList<>();

    AspAndTest(int n) {
        super(n);
    }

    static AspAndTest parse(Scanner s) {
        enterParser("and test");

        AspAndTest aat = new AspAndTest(s.curLineNum());


        while (true) {
            aat.notTest.add(AspNotTest.parse(s));
            if (s.curToken().kind != andToken){
                break;
            }
            skip(s, andToken);
        }

        leaveParser("and test");
        return aat;
    }

    @Override
    public void prettyPrint() {
        int nPrinted = 0;

        for (AspNotTest ant: notTest) {
            if (nPrinted > 0) {
                prettyWrite(" and ");
            }
            ant.prettyPrint();
            nPrinted++;
        }
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = notTest.get(0).eval(curScope);
        for (int i = 1; i < notTest.size(); i++) {
            System.out.println(v.showInfo());
            if (! v.getBoolValue(v.typeName(), this)) {
                return v;
            }
            v = notTest.get(i).eval(curScope);
        }
        return v;
    }
}