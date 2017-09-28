package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

 import java.util.List;

 public class ModificationError extends BaseData {
    
    private ModificationData Modification;
    private List<ErrorData> Errors;

    public ModificationData getModification(){
        return Modification;
    }

    public void setModification(ModificationData modification){
        Modification = modification;
    }

    public List<ErrorData> getErrors() {
		return Errors;
	}

	public void setErrors(List<ErrorData> errors) {
		Errors = errors;
	}
 }