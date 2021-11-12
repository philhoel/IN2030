package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeDictValue extends RuntimeValue {
    HashMap<String, RuntimeValue> dict;

    public RuntimeDictValue(HashMap<String, RuntimeValue> inputDict) {
        dict = inputDict;
    }

    public HashMap<String, RuntimeValue> getDict(String what, AspSyntax where) {
        return dict;
    }

	@Override
	public String typeName() {
		return "dictionairy";
	}
	
	@Override
	public String toString() {
		String info = "{";
		List<String> keys = new ArrayList<>(dict.keySet());
		List<RuntimeValue> values = new ArrayList<>(dict.values());
		for (int i = 0; i < keys.size(); i++) {
			if (i == 0) {
				info = info + keys.get(i) + " = " + values.get(i).toString();
			}
			else {
				info = info + ", " + keys.get(i) + " = " + values.get(i).toString();
			}
		}
		info = info + "}";
		return info;
	}
	
	@Override
	protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
		String info = "{";
		List<String> keys = new ArrayList<>(dict.keySet());
		List<RuntimeValue> values = new ArrayList<>(dict.values());
		for (int i = 0; i < keys.size(); i++) {
			if (i == 0) {
				info = info + keys.get(i) + " = " + values.get(i).toString();
			}
			else {
				info = info + ", " + keys.get(i) + " = " + values.get(i).toString();
			}
		}
		info = info + "}";
		return info;
	}

	@Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
	    if (v instanceof RuntimeNoneValue) {
			return new RuntimeBoolValue(false);
      	}
	    runtimeError("Type error for ==.", where);
	    return null;  // Required by the compiler
	}
	
	@Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
	    if (v instanceof RuntimeNoneValue) {
	      return new RuntimeBoolValue(true);
      	}
	    runtimeError("Type error for !=.", where);
	    return null;  // Required by the compiler
	}
	
	@Override
	public RuntimeValue evalNot(AspSyntax where) {
		if (dict.isEmpty()) {
			return new RuntimeBoolValue(true);
		}
		else {
			return new RuntimeBoolValue(false);
		}
	}

	public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(dict.size());
    }


	@Override
	public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
		if (v instanceof RuntimeStringValue) {
			return dict.get(v.getStringValue(v.typeName(), where));
		}
		runtimeError("Type error for subscription", where);
		return null;
	}
}