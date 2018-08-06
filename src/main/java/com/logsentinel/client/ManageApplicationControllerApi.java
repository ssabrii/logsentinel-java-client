/*
 * LogSentinel RESTful API
 * Read more at http://localhost:8080/docs
 *
 * OpenAPI spec version: 1
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.logsentinel.client;

import com.google.gson.reflect.TypeToken;
import com.logsentinel.*;
import com.logsentinel.client.model.Application;
import com.logsentinel.client.model.UpdateApplicationRequest;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Code generated client for LogSentinel RESTful API
 * Management controller for administrating applications
 * Read more at https://logsentinel.com/documentation/
 */
public class ManageApplicationControllerApi {
    private ApiClient apiClient;

    public ManageApplicationControllerApi() {
        this(Configuration.getDefaultApiClient());
    }

    public ManageApplicationControllerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for createApplication
     *
     * @param application             application (required)
     * @param progressListener        Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public Call createApplicationCall(UpdateApplicationRequest application, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = application;

        // create path and map variables
        String localVarPath = "/api/application";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
                "application/xml", "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
                "application/json", "*/*"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if (progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                            .build();
                }
            });
        }

        String[] localVarAuthNames = new String[]{"basicAuth"};
        return apiClient.buildCallNew(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private Call createApplicationValidateBeforeCall(UpdateApplicationRequest application, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {

        // verify the required parameter 'application' is set
        if (application == null) {
            throw new ApiException("Missing the required parameter 'application' when calling createApplication(Async)");
        }


        Call call = createApplicationCall(application, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Create a new application
     *
     * @param application application (required)
     * @return Application
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Application createApplication(UpdateApplicationRequest application) throws ApiException {
        ApiResponse<Application> resp = createApplicationWithHttpInfo(application);
        return resp.getData();
    }

    /**
     * Create a new application
     *
     * @param application application (required)
     * @return ApiResponse&lt;Application&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Application> createApplicationWithHttpInfo(UpdateApplicationRequest application) throws ApiException {
        Call call = createApplicationValidateBeforeCall(application, null, null);
        Type localVarReturnType = new TypeToken<Application>() {
        }.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create a new application (asynchronously)
     *
     * @param application application (required)
     * @param callback    The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public Call createApplicationAsync(UpdateApplicationRequest application, final ApiCallback<Application> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        Call call = createApplicationValidateBeforeCall(application, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Application>() {
        }.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }

    /**
     * Build call for getApplicationsUsingGET
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getApplications(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {

        if(progressListener != null) {
            Object localVarPostBody = null;
        }

        // create path and map variables
        String localVarPath = "/api/application";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
                "application/xml", "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
                "*/*"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                            .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getApplicationsValidateBeforeCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {


        com.squareup.okhttp.Call call = getApplications(progressListener, progressRequestListener);
        return call;

    }

    /**
     * Get all applications of an organization
     *
     * @return List&lt;UUID&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public List<UUID> getApplications() throws ApiException {
        ApiResponse<List<UUID>> resp = getApplicationsWithHttpInfo();
        return resp.getData();
    }

    /**
     * Get all applications of an organization
     *
     * @return ApiResponse&lt;List&lt;UUID&gt;&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<List<UUID>> getApplicationsWithHttpInfo() throws ApiException {
        com.squareup.okhttp.Call call = getApplicationsValidateBeforeCall(null, null);
        Type localVarReturnType = new TypeToken<List<UUID>>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get all applications of an organization (asynchronously)
     *
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getApplicationsAsync(final ApiCallback<List<UUID>> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getApplicationsValidateBeforeCall(progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<List<UUID>>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }

    /**
     * Build call for deleteApplication
     *
     * @param applicationId           applicationId (required)
     * @param progressListener        Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public Call deleteApplicationCall(String applicationId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/api/application";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (applicationId != null)
            localVarQueryParams.addAll(apiClient.parameterToPair("applicationId", applicationId));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
                "application/xml", "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
                "application/json", "*/*"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if (progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                            .build();
                }
            });
        }

        String[] localVarAuthNames = new String[]{"basicAuth"};
        return apiClient.buildCallNew(localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private Call deleteApplicationValidateBeforeCall(String applicationId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {

        // verify the required parameter 'applicationId' is set
        if (applicationId == null) {
            throw new ApiException("Missing the required parameter 'applicationId' when calling deleteApplication(Async)");
        }


        Call call = deleteApplicationCall(applicationId, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Delete an existing application
     *
     * @param applicationId applicationId (required)
     * @return Application
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Application deleteApplication(String applicationId) throws ApiException {
        ApiResponse<Application> resp = deleteApplicationWithHttpInfo(applicationId);
        return resp.getData();
    }

    /**
     * Delete an existing application
     *
     * @param applicationId applicationId (required)
     * @return ApiResponse&lt;Application&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Application> deleteApplicationWithHttpInfo(String applicationId) throws ApiException {
        Call call = deleteApplicationValidateBeforeCall(applicationId, null, null);
        Type localVarReturnType = new TypeToken<Application>() {
        }.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete an existing application (asynchronously)
     *
     * @param applicationId applicationId (required)
     * @param callback      The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public Call deleteApplicationAsync(String applicationId, final ApiCallback<Application> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        Call call = deleteApplicationValidateBeforeCall(applicationId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Application>() {
        }.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }

    /**
     * Build call for updateApplication
     *
     * @param application             application (required)
     * @param progressListener        Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public Call updateApplicationCall(UpdateApplicationRequest application, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = application;

        // create path and map variables
        String localVarPath = "/api/application";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
                "application/xml", "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
                "application/json", "*/*"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if (progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                            .build();
                }
            });
        }

        String[] localVarAuthNames = new String[]{"basicAuth"};
        return apiClient.buildCallNew(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private Call updateApplicationValidateBeforeCall(UpdateApplicationRequest application, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {

        // verify the required parameter 'application' is set
        if (application == null) {
            throw new ApiException("Missing the required parameter 'application' when calling updateApplication(Async)");
        }


        Call call = updateApplicationCall(application, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Update an existing application
     *
     * @param application application (required)
     * @return Application
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Application updateApplication(UpdateApplicationRequest application) throws ApiException {
        ApiResponse<Application> resp = updateApplicationWithHttpInfo(application);
        return resp.getData();
    }

    /**
     * Update an existing application
     *
     * @param application application (required)
     * @return ApiResponse&lt;Application&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Application> updateApplicationWithHttpInfo(UpdateApplicationRequest application) throws ApiException {
        Call call = updateApplicationValidateBeforeCall(application, null, null);
        Type localVarReturnType = new TypeToken<Application>() {
        }.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Update an existing application (asynchronously)
     *
     * @param application application (required)
     * @param callback    The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public Call updateApplicationAsync(UpdateApplicationRequest application, final ApiCallback<Application> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        Call call = updateApplicationValidateBeforeCall(application, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Application>() {
        }.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
