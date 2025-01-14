/*
 * Patient - Warenwirtschaft (Basis)
 * Synchronisation der Patienten mit der Warenwirtschaft
 *
 * OpenAPI spec version: 1.0.7
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.api;

import io.swagger.client.ApiCallback;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.Configuration;
import io.swagger.client.Pair;
import io.swagger.client.ProgressRequestBody;
import io.swagger.client.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import io.swagger.client.model.PatientNote;
import io.swagger.client.model.PatientNoteMapping;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientNoteApi {
    private ApiClient apiClient;

    public PatientNoteApi() {
        this(Configuration.getDefaultApiClient());
    }

    public PatientNoteApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for patientNote
     * @param apiKey  (required)
     * @param body patientNote to add (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call patientNoteCall(String apiKey, PatientNote body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/patientNote";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        if (apiKey != null)
        localVarHeaderParams.put("api_key", apiClient.parameterToString(apiKey));

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
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

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call patientNoteValidateBeforeCall(String apiKey, PatientNote body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'apiKey' is set
        if (apiKey == null) {
            throw new ApiException("Missing the required parameter 'apiKey' when calling patientNote(Async)");
        }
        
        com.squareup.okhttp.Call call = patientNoteCall(apiKey, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * PatientenNotiz erstellen
     * Szenario - das WaWi erstellt eine PatientenNotiz in Alberta
     * @param apiKey  (required)
     * @param body patientNote to add (optional)
     * @return PatientNoteMapping
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public PatientNoteMapping patientNote(String apiKey, PatientNote body) throws ApiException {
        ApiResponse<PatientNoteMapping> resp = patientNoteWithHttpInfo(apiKey, body);
        return resp.getData();
    }

    /**
     * PatientenNotiz erstellen
     * Szenario - das WaWi erstellt eine PatientenNotiz in Alberta
     * @param apiKey  (required)
     * @param body patientNote to add (optional)
     * @return ApiResponse&lt;PatientNoteMapping&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<PatientNoteMapping> patientNoteWithHttpInfo(String apiKey, PatientNote body) throws ApiException {
        com.squareup.okhttp.Call call = patientNoteValidateBeforeCall(apiKey, body, null, null);
        Type localVarReturnType = new TypeToken<PatientNoteMapping>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * PatientenNotiz erstellen (asynchronously)
     * Szenario - das WaWi erstellt eine PatientenNotiz in Alberta
     * @param apiKey  (required)
     * @param body patientNote to add (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call patientNoteAsync(String apiKey, PatientNote body, final ApiCallback<PatientNoteMapping> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = patientNoteValidateBeforeCall(apiKey, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<PatientNoteMapping>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for patientNotePatch
     * @param apiKey  (required)
     * @param id Id des Patienten in Alberta (required)
     * @param body patientNote to update (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call patientNotePatchCall(String apiKey, String id, PatientNote body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/patientNote/{id}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        if (apiKey != null)
        localVarHeaderParams.put("api_key", apiClient.parameterToString(apiKey));

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
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

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "PATCH", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call patientNotePatchValidateBeforeCall(String apiKey, String id, PatientNote body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'apiKey' is set
        if (apiKey == null) {
            throw new ApiException("Missing the required parameter 'apiKey' when calling patientNotePatch(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling patientNotePatch(Async)");
        }
        
        com.squareup.okhttp.Call call = patientNotePatchCall(apiKey, id, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * PatientenNotiz ändern
     * Szenario - das WaWi ändert eine PatientenNotiz in Alberta
     * @param apiKey  (required)
     * @param id Id des Patienten in Alberta (required)
     * @param body patientNote to update (optional)
     * @return PatientNoteMapping
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public PatientNoteMapping patientNotePatch(String apiKey, String id, PatientNote body) throws ApiException {
        ApiResponse<PatientNoteMapping> resp = patientNotePatchWithHttpInfo(apiKey, id, body);
        return resp.getData();
    }

    /**
     * PatientenNotiz ändern
     * Szenario - das WaWi ändert eine PatientenNotiz in Alberta
     * @param apiKey  (required)
     * @param id Id des Patienten in Alberta (required)
     * @param body patientNote to update (optional)
     * @return ApiResponse&lt;PatientNoteMapping&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<PatientNoteMapping> patientNotePatchWithHttpInfo(String apiKey, String id, PatientNote body) throws ApiException {
        com.squareup.okhttp.Call call = patientNotePatchValidateBeforeCall(apiKey, id, body, null, null);
        Type localVarReturnType = new TypeToken<PatientNoteMapping>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * PatientenNotiz ändern (asynchronously)
     * Szenario - das WaWi ändert eine PatientenNotiz in Alberta
     * @param apiKey  (required)
     * @param id Id des Patienten in Alberta (required)
     * @param body patientNote to update (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call patientNotePatchAsync(String apiKey, String id, PatientNote body, final ApiCallback<PatientNoteMapping> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = patientNotePatchValidateBeforeCall(apiKey, id, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<PatientNoteMapping>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
