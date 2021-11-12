package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFactor extends AspSyntax {
    ArrayList<AspFactorPrefix> factorPrefix = new ArrayList<>();
    ArrayList<AspPrimary> primary = new ArrayList<>();
    ArrayList<AspFactorOpr> factorOpr = new ArrayList<>();
    ArrayList<TokenKind> tokenKind = new ArrayList<>();
    ArrayList<Integer> isFactorPrefix = new ArrayList<>();

    AspFactor(int n){
        super(n);
    }

    static AspFactor parse(Scanner s) {
        enterParser("factor");

        AspFactor af = new AspFactor(s.curLineNum());
        Boolean goAgain = true;

        while (goAgain){
            af.tokenKind.add(s.curToken().kind);
            if (s.curToken().kind == plusToken || s.curToken().kind == minusToken){
                af.factorPrefix.add(AspFactorPrefix.parse(s));
                af.isFactorPrefix.add(1);
            }
            else {
                af.isFactorPrefix.add(0);
            }
            af.primary.add(AspPrimary.parse(s));
            switch (s.curToken().kind) {
                case astToken:
                    af.factorOpr.add(AspFactorOpr.parse(s));
                    break;
                case slashToken:
                    af.factorOpr.add(AspFactorOpr.parse(s));
                    break;
                case percentToken:
                    af.factorOpr.add(AspFactorOpr.parse(s));
                    break;
                case doubleSlashToken:
                    af.factorOpr.add(AspFactorOpr.parse(s));
                    break;
                default: 
                    goAgain = false;
                    break;
            }
        }
        
        leaveParser("factor");
        return af; 
    }

    @Override
    public void prettyPrint() {

        int nPrinted = 0;
        int factorPrefixCount = 0;
        for (AspPrimary prettyPrimary: primary) {
            Integer prettyIsFactorPrefix = isFactorPrefix.get(nPrinted);
            
            if (nPrinted > 0) {
                AspFactorOpr prettyFactorOpr = factorOpr.get(nPrinted-1);
                prettyFactorOpr.prettyPrint();
            }
            
            if (prettyIsFactorPrefix == 1) {
                AspFactorPrefix prettyFactorPrefix = factorPrefix.get(factorPrefixCount);
                prettyFactorPrefix.prettyPrint();
                factorPrefixCount++;
            }

            prettyPrimary.prettyPrint();
            nPrinted++;
        }   
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        System.out.println("kjører factor eval" + "  line: " + this.lineNum);
        RuntimeValue v = primary.get(0).eval(curScope);
        RuntimeValue withPrefix;
        int facPre = isFactorPrefix.get(0);
        int factorPrefixCount = 0;


        /*Sjekker om vi har en factor prefix operator og evt gjør primary 
        negativ eller positiv*/
        
        if (facPre == 1) {
            System.out.println("factor prefix line: " + this.lineNum);
            TokenKind tokenPre = factorPrefix.get(0).tokenKind;
            switch (tokenPre) {
                case plusToken:
                    v = v.evalPositive(this);
                    break;

                case minusToken:
                    v = v.evalNegate(this);
                    break;
                    
                default:
                    Main.panic("Illegal factor prefix: " + tokenPre + "!");
            }
            System.out.println("_____________  line:" + this.lineNum);
            factorPrefixCount++;
        }
        
        for (int i = 1; i < primary.size(); i++) {
            TokenKind k = null;
            TokenKind tokenPre = null;
            facPre = isFactorPrefix.get(i);
            
            /*Vi har allerede evaluert for første factor prefix og primary så vi må finne vår
            factor operator. Vi er ikke sikre på om det finnes en factor opr enda, men dette
            vet vi må være tilfellet om primary.size() er over 1.*/


            k = factorOpr.get(i-1).tokenKind;
            

            /*Hvis facPre igjen er lik 1 vil det si at vi har en ny factor prefix og vi må finne
            hvilken token dette er.*/
            if (facPre == 1) {
                tokenPre = factorPrefix.get(factorPrefixCount).tokenKind;
                withPrefix = primary.get(i).eval(curScope);

                System.out.println("_____________ line: " + this.lineNum);
                factorPrefixCount++;

                switch (tokenPre) {
                    case plusToken:
                        withPrefix = withPrefix.evalPositive(this);
                        break;
                    case minusToken:
                        System.out.println("minus foran !");
                        withPrefix = withPrefix.evalNegate(this);
                        break;
                    default:
                        Main.panic("Illegal factor prefix: " + tokenPre + "!");
                }
                switch (k) {
                    case astToken:
                        v = v.evalMultiply(withPrefix, this);
                        break;
                    case slashToken:
                        v = v.evalDivide(withPrefix, this);
                        break;
                    case percentToken:
                        v = v.evalModulo(withPrefix, this);
                        break;
                    case doubleSlashToken:
                        v = v.evalIntDivide(withPrefix, this);
                        break;
                    default:
                        Main.panic("Illegal factor operator: " + k + "!");
                }  
            }
            else {
                switch (k) {
                    case astToken:
                        v = v.evalMultiply(primary.get(i).eval(curScope), this);
                        break;
                    case slashToken:
                        v = v.evalDivide(primary.get(i).eval(curScope), this);
                        break;
                    case percentToken:
                        v = v.evalModulo(primary.get(i).eval(curScope), this);
                        break;
                    case doubleSlashToken:
                        v = v.evalIntDivide(primary.get(i).eval(curScope), this);
                        break;
                    default:
                        Main.panic("Illegal factor operator: " + k + "!");
                }
            }
        }
	    return v;
    }
}