package com.logsentinel;

import java.util.List;
import java.util.Map;

/**
 * Empty implementation of the bacllback interface for easier use
 * 
 * @author bozho
 *
 * @param <T>
 */
public class ApiCallbackAdapter<T> implements ApiCallback<T> {

	
	@Override
	public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
		
	}

	@Override
	public void onSuccess(T result, int statusCode, Map<String, List<String>> responseHeaders) {
		
	}

	@Override
	public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
		
	}

	@Override
	public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
	}
}
