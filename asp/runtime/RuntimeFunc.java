package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspFuncDef;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeFunc extends RuntimeValue {
    AspFuncDef def;
    RuntimeScope defScope;
    ArrayList<String> initParams = new ArrayList<>();
    String libFuncName;

    public RuntimeFunc(AspFuncDef func, RuntimeScope outer, ArrayList<String> params){
        def = func;
        defScope = outer;
        initParams = params;
    }

    public RuntimeFunc(String funcName) {
        libFuncName = funcName;
    }


    @Override
    public String typeName() {
        return "func";
    }


    @Override
    public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
        if (!(actualParams.size() == initParams.size())){
            runtimeError("input parameters do not match formal parameters", where);
        }
        RuntimeScope thisFuncScope = new RuntimeScope(defScope);
        int i = 0;
        for (RuntimeValue actualParam: actualParams) {
            thisFuncScope.assign(initParams.get(i), actualParam);
            i++;
        }
        try {
            def.suite.eval(thisFuncScope);
        } catch (RuntimeReturnValue rrv) {
            return rrv.value;
        }
        return new RuntimeNoneValue();
    }

    @Override
    protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
        if (libFuncName == null) {
            return def.name.get(0).id;
        }
        return libFuncName;
    }
}