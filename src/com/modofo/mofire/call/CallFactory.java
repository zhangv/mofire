package com.modofo.mofire.call;

import com.modofo.mofire.domain.Post;
import com.modofo.mofire.domain.Setting;

public class CallFactory {
	public static Call getNewPostCall(Setting setting,Post post){
		return new NewPostCall(setting,post);
	}
	
	public static Call getValidateCall(Setting setting){
		return new ValidateCall(null,setting);
	}
}
