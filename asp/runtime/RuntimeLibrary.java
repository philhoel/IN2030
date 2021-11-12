package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.Scanner;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;
import no.uio.ifi.asp.runtime.RuntimeFunc;

public class RuntimeLibrary extends RuntimeScope {
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary() {
	    assign("len", new RuntimeFunc("len") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, 
            AspSyntax where) {
                checkNumParams(actualParams, 1, "len", where);
                return actualParams.get(0).evalLen(where);
        }});
    
        assign("float", new RuntimeFunc("float") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams,
            AspSyntax where) {
                checkNumParams(actualParams, 1, "float", where);
                RuntimeValue v = actualParams.get(0);
                return new RuntimeFloatValue(v.getFloatValue("float", where));
        }});

        assign("print", new RuntimeFunc("print") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams,
            AspSyntax where) {
                System.out.println("printing");
                System.out.println(actualParams.get(0).toString());
                for (int i = 0; i < actualParams.size(); i++) {
                    if (i > 0) {
                        System.out.println(" ");
                    }
                    System.out.println(actualParams.get(i).toString());
                }
                System.out.println();
                return new RuntimeNoneValue();
            }
        });
        

        assign("exit", new RuntimeFunc("exit") {
            @Override 
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, 
            AspSyntax where) {
                checkNumParams(actualParams, 1, "exit", where);
                System.exit((int) actualParams.get(0).getIntValue(actualParams.get(0).typeName(), where));
                return new RuntimeNoneValue();
            }
        });

        assign("range", new RuntimeFunc("range") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, 
            AspSyntax where) {
                checkNumParams(actualParams, 2, "range", where);
                ArrayList<RuntimeValue> rangeList = new ArrayList<>();
                RuntimeValue v1 = actualParams.get(0);
                RuntimeValue v2 = actualParams.get(1);

                while (v1.getIntValue(v1.typeName(), where) < v2.getIntValue(v2.typeName(), where)) {
                    rangeList.add(v1);
                    RuntimeIntValue addOne = new RuntimeIntValue(1);
                    v1.evalAdd(addOne, where);
                }
                return new RuntimeListValue(rangeList);
            }
        });

        assign("input", new RuntimeFunc("input") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, 
            AspSyntax where) {
                checkNumParams(actualParams, 1, "input", where);
                System.out.println(actualParams.get(0).toString());
                String inputString = keyboard.nextLine();
                return new RuntimeStringValue(inputString);
            }
        });
        
        assign("str", new RuntimeFunc("str") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, 
            AspSyntax where) {
                checkNumParams(actualParams, 1, "str", where);
                RuntimeValue v = actualParams.get(0);
                String stringValue = v.toString();
                return new RuntimeStringValue(stringValue);
            }
        });
        
        assign("int", new RuntimeFunc("int") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, 
            AspSyntax where) {
                checkNumParams(actualParams, 1, "int", where);
                RuntimeValue v = actualParams.get(0);
                System.out.println(v.showInfo());
                int intValue;
                if (v instanceof RuntimeIntValue) {
                    long longValue = v.getIntValue(v.typeName(), where);
                    return new RuntimeIntValue(longValue);
                }
                else if (v instanceof RuntimeFloatValue) {
                    long longValue = v.getIntValue(v.typeName(), where);
                    return new RuntimeIntValue(longValue);
                }
                else if (v instanceof RuntimeStringValue) {
                    String stringValue = v.getStringValue(v.typeName(), where);
                    intValue = Integer.parseInt(stringValue);
                    return new RuntimeIntValue(intValue);
                }
                runtimeError("cannot convert this datatype to int", where);
                return null;
            }
        });
    }


    private void checkNumParams(ArrayList<RuntimeValue> actArgs, 
				int nCorrect, String id, AspSyntax where) {
	if (actArgs.size() != nCorrect)
	    RuntimeValue.runtimeError("Wrong number of parameters to "+id+"!",where);
    }
}
