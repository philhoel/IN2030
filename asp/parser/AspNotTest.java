package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspNotTest extends AspSyntax {
    AspComparison comparison;
    TokenKind tokenKind;

    AspNotTest(int n) {
        super(n);
    }

    static AspNotTest parse(Scanner s) {
        enterParser("not test");

        AspNotTest ant = new AspNotTest(s.curLineNum());

        
        if (s.curToken().kind == notToken){
            ant.tokenKind = s.curToken().kind;
            skip(s, notToken);
        }
        ant.comparison = AspComparison.parse(s);

        leaveParser("not test");
        return ant;
    }

    @Override
    public void prettyPrint() {
        TokenKind prettyTokenKind = tokenKind;
        if (prettyTokenKind == notToken) {
            prettyWrite(" not ");
        }
        AspComparison prettyComparison = comparison;
        prettyComparison.prettyPrint();
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        System.out.println("kjører not test eval  line: " + this.lineNum);

        TokenKind runtimeTokenKind = tokenKind;
        RuntimeValue v = comparison.eval(curScope);
        
        if (runtimeTokenKind == notToken) {
            v = v.evalNot(this);
        }
	    return v;
    }
}