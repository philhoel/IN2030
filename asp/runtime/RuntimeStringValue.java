package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class RuntimeStringValue extends RuntimeValue {
    public String text;

    public RuntimeStringValue(String letters) {
        text = letters;
    }

    @Override
    public String toString() {
        if (text.indexOf('\'') >= 0) {
            return '"' + text + '"';
        }
        else {
            return "'" + text + "'";
        }
    }
    
    @Override
    public String showInfo() {
        return toString();
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (text == "") {
            return false;
        }
        return true;
    }

    @Override
    public String getStringValue(String what, AspSyntax where) {
        return text;
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            String charAtIndex = "";
            char c = text.charAt((int)v.getIntValue(typeName(), where));
            charAtIndex = charAtIndex + c;
            return new RuntimeStringValue(charAtIndex);
        }
        runtimeError("Type error for subscription", where);
        return null;
    }

    @Override 
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeStringValue(text + v.getStringValue(v.typeName(), where));
        }
        runtimeError("Type error for +", where);
	    return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        System.out.println("string multiplication line: " + where.lineNum);
        if (v instanceof RuntimeIntValue) {
            System.out.println("multiplying a string with an int, line: " + where.lineNum);
            int i = 0;
            String newText = "";
            while (i < v.getIntValue(v.typeName(), where)) {
                newText = newText + text;
                i++;
            }
            return new RuntimeStringValue(newText);
        }
        runtimeError("Type error for *.", where);
	    return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue(v.getStringValue(v.typeName(), where) == text);
        }
        return new RuntimeBoolValue(false);
    }
    
    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeBoolValue(text != v.getStringValue("not equal", where));
        }
        else if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for !=", where);
        return null;
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            for (int i = 0; i < text.length(); i++) {
                char textChar = text.charAt(i);
                char vChar = v.getStringValue(v.typeName(), where).charAt(i);

                int textCharValue = (int)textChar;
                int vCharValue = (int)vChar;

                if (textCharValue < vCharValue) {
                    return new RuntimeBoolValue(true);
                }
                else {
                    return new RuntimeBoolValue(false);
                }
            }
        }
        runtimeError("Type error for <.", where);
	    return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            for (int i = 0; i < text.length(); i++) {
                char textChar = text.charAt(i);
                char vChar = v.getStringValue(v.typeName(), where).charAt(i);

                int textCharValue = (int)textChar;
                int vCharValue = (int)vChar;

                if (textCharValue < vCharValue) {
                    return new RuntimeBoolValue(true);
                }
                else if(text == v.getStringValue(v.typeName(), where)){
                    return new RuntimeBoolValue(true);
                }
                else {
                    return new RuntimeBoolValue(false);
                }
            }
        }
        runtimeError("Type error for <=.", where);
	    return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            for (int i = 0; i < text.length(); i++) {
                char textChar = text.charAt(i);
                char vChar = v.getStringValue(v.typeName(), where).charAt(i);

                int textCharValue = (int)textChar;
                int vCharValue = (int)vChar;

                if (textCharValue > vCharValue) {
                    return new RuntimeBoolValue(true);
                }
                else {
                    return new RuntimeBoolValue(false);
                }
            }
        }
        runtimeError("Type error for >.", where);
	    return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            for (int i = 0; i < text.length(); i++) {
                char textChar = text.charAt(i);
                char vChar = v.getStringValue(v.typeName(), where).charAt(i);

                int textCharValue = (int)textChar;
                int vCharValue = (int)vChar;

                if (textCharValue > vCharValue) {
                    return new RuntimeBoolValue(true);
                }
                else if(text == v.getStringValue(v.typeName(), where)){
                    return new RuntimeBoolValue(true);
                }
                else {
                    return new RuntimeBoolValue(false);
                }
            }
        }
        runtimeError("Type error for >=.", where);
	    return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        if (text == "") {
            return new RuntimeBoolValue(true);
        }  
        else {
            return new RuntimeBoolValue(false);
        }
    }

    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(text.length());
    }



    @Override
    public String typeName() {
        return "String";
    }

    @Override
    protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
        return "String";
    }
}