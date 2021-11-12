package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class RuntimeIntValue extends RuntimeValue {
    long intValue;

    public RuntimeIntValue(long number) {
        intValue = number;
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
        return intValue;
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
		return (double)intValue;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where){
        if (intValue == 0) {
            return false;
        } 
        return true;
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue) {
            return new RuntimeIntValue(intValue + v.getIntValue(v.typeName(), where));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(intValue + v.getFloatValue(v.typeName(), where));
        }
        runtimeError("Type error for +.", where);
        return null; //required by compiler
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeIntValue(intValue - v.getIntValue(v.typeName(), where));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(intValue - v.getFloatValue(v.typeName(), where));
        }
        runtimeError("Type error for -.", where);
        return null; //required by compiler
    }

    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(intValue / v.getFloatValue(v.typeName(), where));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(intValue / v.getFloatValue(v.typeName(), where));
        }
        runtimeError("Type error for /.", where);
        return null; //required by compiler
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeIntValue(Math.floorDiv(intValue, v.getIntValue(v.typeName(), where)));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(Math.floor(intValue / v.getFloatValue(v.typeName(), where)));
        }
        runtimeError("Type error for //. (integer divide)", where);
        return null; //required by compiler
    }


    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeBoolValue(intValue == v.getIntValue(v.typeName(), where));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(intValue == v.getFloatValue(v.typeName(), where));
        }
        return new RuntimeBoolValue(false);
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeBoolValue(intValue < v.getIntValue(v.typeName(), where));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(intValue < v.getFloatValue(v.typeName(), where));
        }
        runtimeError("Type error for <", where);
        return null; //required by compiler
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeBoolValue(intValue > v.getIntValue(v.typeName(), where));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(intValue > v.getFloatValue(v.typeName(), where));
        }
        runtimeError("Type error for >", where);
        return null; //required by compiler
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeBoolValue(intValue >= v.getIntValue(v.typeName(), where));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(intValue >= v.getFloatValue(v.typeName(), where));
        }
        runtimeError("Type error for >=", where);
        return null; //required by compiler
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeBoolValue(intValue <= v.getIntValue(v.typeName(), where));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(intValue <= v.getFloatValue(v.typeName(), where));
        }
        runtimeError("Type error for <=", where);
        return null; //required by compiler
    }

    @Override
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeIntValue(Math.floorMod(intValue,v.getIntValue(v.typeName(), where)));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(intValue - 
            v.getFloatValue(v.typeName(), where)*Math.floor(intValue / v.getFloatValue(v.typeName(), where)));
        }
        runtimeError("Type error for %.", where);
        return null; //required by compiler
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeIntValue(intValue * v.getIntValue(v.typeName(), where));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(intValue * v.getFloatValue(v.typeName(), where));
        }
        runtimeError("Type error for *.", where);
        return null; //required by compiler
    }
    
    
    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeBoolValue(intValue != v.getIntValue(v.typeName(), where));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(intValue != v.getFloatValue(v.typeName(), where));
        }
        else if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for !=.", where);
        return null;
    }
    
    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        if (intValue == 0) {
            return new RuntimeBoolValue(true);
        }  
        else {
            return new RuntimeBoolValue(false);
        }
    }
    
    @Override
    public RuntimeValue evalNegate(AspSyntax where) {
        return new RuntimeIntValue(-1 * (intValue));
    }

    @Override
    public RuntimeValue evalPositive(AspSyntax where) {
        return new RuntimeIntValue(intValue);
    }

    @Override
    public String typeName() {
        return "integer";
    }

    @Override
    protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
        return toString();
    }

    @Override
    public String toString() {
        return String.valueOf(intValue);
    }
    


}