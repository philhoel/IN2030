package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

abstract class AspPrimarySuffix extends AspSyntax {
    public AspPrimarySuffix(int n){
        super(n);
    }
    static AspPrimarySuffix parse(Scanner s) {

        AspPrimarySuffix aps = null;
        if (s.curToken().kind == leftParToken) {
            aps = AspArguments.parse(s);
        }
        else if (s.curToken().kind == leftBracketToken){
            aps = AspSubscription.parse(s);
        }
        else {
            parserError("Error related to primary suffix, the desired token was not found", s.curLineNum());
        }

        return aps;
    }
}