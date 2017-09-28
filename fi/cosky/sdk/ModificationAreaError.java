package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

 import java.util.List;

 public class ModificationAreaError extends BaseData {
    
    private ModificationAreaData ModificationArea;
    private List<ErrorData> Errors;

    public ModificationAreaData getModificationArea(){
        return ModificationArea;
    }

    public void setModificationArea(ModificationAreaData modificationArea){
        ModificationArea = modificationArea;
    }

    public List<ErrorData> getErrors() {
		return Errors;
	}

	public void setErrors(List<ErrorData> errors) {
		Errors = errors;
	}
 }