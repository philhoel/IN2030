package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class RuntimeListValue extends RuntimeValue {
    public ArrayList<RuntimeValue> list;

    public RuntimeListValue(ArrayList<RuntimeValue> inputList) {
        list = inputList;
    }

    @Override
    public String toString() {
        String text = "[";
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size()-1){
                text = text + list.get(i).toString();
            }
            else {
                text = text + list.get(i).toString() + ", ";
            }
            
        }
        text = text + "]";
        return text;
    }

    @Override
    public String showInfo() {
        return toString();
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            int i = 0;
            ArrayList<RuntimeValue> newList = new ArrayList<>();
            while (i < v.getIntValue(v.typeName(), where)) {
                for (RuntimeValue value: list) {
                    newList.add(value);
                }
                i++;
            }
            return new RuntimeListValue(newList);
        }
        runtimeError("Type error for *.", where);
	    return null;  // Required by the compiler
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for ==.", where);
        return null;
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for ==.", where);
        return null;
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        if (list.isEmpty()) {
            return new RuntimeBoolValue(true);
        }  
        else {
            return new RuntimeBoolValue(false);
        }
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return list.get((int)v.getIntValue(v.typeName(), where));
        }
        runtimeError("Type error for subscription", where);
        return null;
    }

    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(list.size());
    }

    @Override
    public String typeName() {
        return "list";
    }

    @Override
    protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
        return "list";
    }
}