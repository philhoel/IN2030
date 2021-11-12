package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class RuntimeFloatValue extends RuntimeValue {
    double floatValue;
    
    public RuntimeFloatValue(double f){
        floatValue = f;
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return floatValue;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (floatValue != 0.0) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(floatValue);
    }

    @Override
    public RuntimeValue evalPositive(AspSyntax where) {
        return new RuntimeFloatValue(floatValue);
    }

    @Override
    public RuntimeValue evalNegate(AspSyntax where){
        return new RuntimeFloatValue(-floatValue);
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue + v.getFloatValue(v.typeName(),where));
        }
        else if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatValue + v.getFloatValue(v.typeName(), where));
        }
        runtimeError("Type error for +.", where);
        return null; //required by compiler
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue - v.getFloatValue(v.typeName(),where));
        }
        else if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatValue - v.getFloatValue(v.typeName(), where));
        }
        runtimeError("Type error for -.", where);
        return null; //required by compiler
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue * v.getFloatValue(v.typeName(),where));
        }
        else if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatValue * v.getFloatValue(v.typeName(), where));
        }
        runtimeError("Type error for *.", where);
        return null; //required by compiler
    }

    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue / v.getFloatValue(v.typeName(),where));
        }
        else if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatValue / v.getFloatValue(v.typeName(), where));
        }
        runtimeError("Type error for /.", where);
        return null; //required by compiler
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(Math.floor(floatValue / v.getFloatValue(v.typeName(),where)));
        }
        else if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(Math.floor(floatValue / v.getFloatValue(v.typeName(), where)));
        }
        runtimeError("Type error for //.", where);
        return null; //required by compiler
    }
    
    @Override
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue - 
            v.getFloatValue(v.typeName(),where) * Math.floor(floatValue / v.getFloatValue(v.typeName(),where)));
        }
        else if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatValue - 
            v.getFloatValue(v.typeName(),where) * Math.floor(floatValue / v.getFloatValue(v.typeName(),where)));
        }
        runtimeError("Type error for //.", where);
        return null; //required by compiler
    }

    
    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatValue < v.getFloatValue(v.typeName(), where));
        }
        else if (v instanceof RuntimeIntValue) {
            return new RuntimeBoolValue(floatValue < v.getIntValue(v.typeName(), where));
        }
        runtimeError("Type error for <.", where);
        return null; //required by compiler
    }
    
    
    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatValue > v.getFloatValue(v.typeName(), where));
        }
        else if (v instanceof RuntimeIntValue) {
            return new RuntimeBoolValue(floatValue > v.getIntValue(v.typeName(), where));
        }
        runtimeError("Type error for >.", where);
        return null; //required by compiler
    }
    
    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatValue >= v.getFloatValue(v.typeName(), where));
        }
        else if (v instanceof RuntimeIntValue) {
            return new RuntimeBoolValue(floatValue >= v.getIntValue(v.typeName(), where));
        }
        runtimeError("Type error for >=.", where);
        return null; //required by compiler
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatValue <= v.getFloatValue(v.typeName(), where));
        }
        else if (v instanceof RuntimeIntValue) {
            return new RuntimeBoolValue(floatValue <= v.getIntValue(v.typeName(), where));
        }
        runtimeError("Type error for <=.", where);
        return null; //required by compiler
    }
    
    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatValue != v.getFloatValue(v.typeName(), where));
        }
        else if (v instanceof RuntimeIntValue) {
            return new RuntimeBoolValue(floatValue != v.getFloatValue(v.typeName(), where));
        }
        else if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for !=.", where);
        return null;
    }

    @Override 
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatValue == v.getFloatValue(v.typeName(), where));
        }
        else if (v instanceof RuntimeIntValue) {
            return new RuntimeBoolValue(floatValue == v.getFloatValue(v.typeName(), where));
        }
        return new RuntimeBoolValue(false);
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        if (floatValue == 0) {
            return new RuntimeBoolValue(true);
        }  
        else {
            return new RuntimeBoolValue(false);
        }
    }


    

    @Override
    protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
        return toString();
    }

    @Override
    public String typeName() {
        return "float";
    }
}
