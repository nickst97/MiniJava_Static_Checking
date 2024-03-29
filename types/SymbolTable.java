package types;
import java.util.Hashtable;
import types.ClassInfo;
import types.MethodInfo;

public class SymbolTable{

	private Hashtable<String, ClassInfo> stClasses = new Hashtable<String, ClassInfo>();
	private Hashtable<String, MethodInfo> stMethods = new Hashtable<String, MethodInfo>();

	public String className;
	public String[] classMethod;

	public Hashtable<String, ClassInfo> getClasses(){
		return stClasses;
	}

	public Hashtable<String, MethodInfo> getMethods(){
		return stMethods;
	}

	public void print(){
		System.out.println(stClasses);
	}

	public String getVarType(String idName, String varName) throws Exception{

		MethodInfo currMethod = stMethods.get(idName);
		if (currMethod != null){

			if (currMethod.parNames != null){
				int totalPars = currMethod.parNames.length;
				for (int currPar = 0; currPar < totalPars; currPar++){
					if (currMethod.parNames[currPar].equals(varName))
						return currMethod.parTypes[currPar];
				}
			}

			if (currMethod.varNames != null){
				int totalVars = currMethod.varNames.length;
				for (int currVar = 0; currVar < totalVars; currVar++){
					if (currMethod.varNames[currVar].equals(varName)){
						return currMethod.varTypes[currVar];
					}
				}
			}


			ClassInfo currClass = stClasses.get(currMethod.nameClass);
			if (currClass.varNames != null){
				int totalVars = currClass.varNames.length;
				for (int currVar = 0; currVar < totalVars; currVar++){
					if (currClass.varNames[currVar].equals(varName))
						return currClass.varTypes[currVar];
				}
			}

			if (idName.contains(".")){
				String[] parts = idName.split("\\.");
				String[] parentClassNames = getParentClassesNames(parts[0]);
				for (int currParentClasseNamePos = 0; currParentClasseNamePos < parentClassNames.length; currParentClasseNamePos++){
					return getVarType(parentClassNames[currParentClasseNamePos], varName);
				}
			}
			throw new Exception("\terror: cannot find symbol " + varName);
		}

		ClassInfo currClass = stClasses.get(idName);
		if (currClass != null){
			int totalVars = currClass.varNames.length;
			for (int currVar = 0; currVar < totalVars; currVar++){
				if (currClass.varNames[currVar].equals(varName))
					return currClass.varTypes[currVar];
			}
			throw new Exception("\terror: cannot find symbol " + varName);
		}
		throw new Exception("\terror: cannot find symbol " + varName);
	}


	public String[] getParentClassesNames(String className){

		String parentClassNamesList = "";
		ClassInfo currClass = stClasses.get(className);
		if (currClass != null){
			while (currClass.nameExtends != null){
				parentClassNamesList = currClass.nameExtends + "|" + parentClassNamesList;
				currClass = stClasses.get(currClass.nameExtends);
				if (currClass == null)
					break;
			}
		}
		return parentClassNamesList.split("\\|");
	}

}
