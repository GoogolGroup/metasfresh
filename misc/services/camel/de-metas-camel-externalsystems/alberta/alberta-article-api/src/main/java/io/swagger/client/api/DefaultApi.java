/*
 * #%L
 * alberta-article-api
 * %%
 * Copyright (C) 2021 metas GmbH
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

/*
 * Artikel - Warenwirtschaft (Basis)
 * Synchronisation der Artikel mit Kumavision
 *
 * OpenAPI spec version: 1.0.2
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.api;

import com.google.gson.reflect.TypeToken;
import io.swagger.client.ApiCallback;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.Configuration;
import io.swagger.client.Pair;
import io.swagger.client.ProgressRequestBody;
import io.swagger.client.ProgressResponseBody;
import io.swagger.client.model.Article;
import io.swagger.client.model.ArticleMapping;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultApi {
    private ApiClient apiClient;

    public DefaultApi() {
        this(Configuration.getDefaultApiClient());
    }

    public DefaultApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for addArticle
     * @param albertaApiKey  (required)
     * @param tenant  (required)
     * @param body article to add (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call addArticleCall(String albertaApiKey, String tenant, Article body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/article";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        if (albertaApiKey != null)
        localVarHeaderParams.put("alberta-api-key", apiClient.parameterToString(albertaApiKey));
        if (tenant != null)
        localVarHeaderParams.put("tenant", apiClient.parameterToString(tenant));

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
    private com.squareup.okhttp.Call addArticleValidateBeforeCall(String albertaApiKey, String tenant, Article body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'albertaApiKey' is set
        if (albertaApiKey == null) {
            throw new ApiException("Missing the required parameter 'albertaApiKey' when calling addArticle(Async)");
        }
        // verify the required parameter 'tenant' is set
        if (tenant == null) {
            throw new ApiException("Missing the required parameter 'tenant' when calling addArticle(Async)");
        }
        
        com.squareup.okhttp.Call call = addArticleCall(albertaApiKey, tenant, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * neuen Artikel in Alberta anlegen
     * Legt einen neuen Artikel in Alberta an
     * @param albertaApiKey  (required)
     * @param tenant  (required)
     * @param body article to add (optional)
     * @return ArticleMapping
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ArticleMapping addArticle(String albertaApiKey, String tenant, Article body) throws ApiException {
        ApiResponse<ArticleMapping> resp = addArticleWithHttpInfo(albertaApiKey, tenant, body);
        return resp.getData();
    }

    /**
     * neuen Artikel in Alberta anlegen
     * Legt einen neuen Artikel in Alberta an
     * @param albertaApiKey  (required)
     * @param tenant  (required)
     * @param body article to add (optional)
     * @return ApiResponse&lt;ArticleMapping&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ArticleMapping> addArticleWithHttpInfo(String albertaApiKey, String tenant, Article body) throws ApiException {
        com.squareup.okhttp.Call call = addArticleValidateBeforeCall(albertaApiKey, tenant, body, null, null);
        Type localVarReturnType = new TypeToken<ArticleMapping>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * neuen Artikel in Alberta anlegen (asynchronously)
     * Legt einen neuen Artikel in Alberta an
     * @param albertaApiKey  (required)
     * @param tenant  (required)
     * @param body article to add (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call addArticleAsync(String albertaApiKey, String tenant, Article body, final ApiCallback<ArticleMapping> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = addArticleValidateBeforeCall(albertaApiKey, tenant, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ArticleMapping>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for addInsuranceContract
     * @param albertaApiKey  (required)
     * @param tenant  (required)
     * @param body insuranceContract to add (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call addInsuranceContractCall(String albertaApiKey, String tenant, Article body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/insuranceContract";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        if (albertaApiKey != null)
        localVarHeaderParams.put("alberta-api-key", apiClient.parameterToString(albertaApiKey));
        if (tenant != null)
        localVarHeaderParams.put("tenant", apiClient.parameterToString(tenant));

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
    private com.squareup.okhttp.Call addInsuranceContractValidateBeforeCall(String albertaApiKey, String tenant, Article body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'albertaApiKey' is set
        if (albertaApiKey == null) {
            throw new ApiException("Missing the required parameter 'albertaApiKey' when calling addInsuranceContract(Async)");
        }
        // verify the required parameter 'tenant' is set
        if (tenant == null) {
            throw new ApiException("Missing the required parameter 'tenant' when calling addInsuranceContract(Async)");
        }
        
        com.squareup.okhttp.Call call = addInsuranceContractCall(albertaApiKey, tenant, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * neuen Krankenkassenvertrag in Alberta anlegen
     * Legt einen neuen Krankenkassenvertrag in Alberta an
     * @param albertaApiKey  (required)
     * @param tenant  (required)
     * @param body insuranceContract to add (optional)
     * @return ArticleMapping
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ArticleMapping addInsuranceContract(String albertaApiKey, String tenant, Article body) throws ApiException {
        ApiResponse<ArticleMapping> resp = addInsuranceContractWithHttpInfo(albertaApiKey, tenant, body);
        return resp.getData();
    }

    /**
     * neuen Krankenkassenvertrag in Alberta anlegen
     * Legt einen neuen Krankenkassenvertrag in Alberta an
     * @param albertaApiKey  (required)
     * @param tenant  (required)
     * @param body insuranceContract to add (optional)
     * @return ApiResponse&lt;ArticleMapping&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ArticleMapping> addInsuranceContractWithHttpInfo(String albertaApiKey, String tenant, Article body) throws ApiException {
        com.squareup.okhttp.Call call = addInsuranceContractValidateBeforeCall(albertaApiKey, tenant, body, null, null);
        Type localVarReturnType = new TypeToken<ArticleMapping>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * neuen Krankenkassenvertrag in Alberta anlegen (asynchronously)
     * Legt einen neuen Krankenkassenvertrag in Alberta an
     * @param albertaApiKey  (required)
     * @param tenant  (required)
     * @param body insuranceContract to add (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call addInsuranceContractAsync(String albertaApiKey, String tenant, Article body, final ApiCallback<ArticleMapping> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = addInsuranceContractValidateBeforeCall(albertaApiKey, tenant, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ArticleMapping>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateArticle
     * @param albertaApiKey  (required)
     * @param tenant  (required)
     * @param customerNumber  (required)
     * @param body article to update (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateArticleCall(String albertaApiKey, String tenant, String customerNumber, Article body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/article/{customerNumber}"
            .replaceAll("\\{" + "customerNumber" + "\\}", apiClient.escapeString(customerNumber.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        if (albertaApiKey != null)
        localVarHeaderParams.put("alberta-api-key", apiClient.parameterToString(albertaApiKey));
        if (tenant != null)
        localVarHeaderParams.put("tenant", apiClient.parameterToString(tenant));

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
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call updateArticleValidateBeforeCall(String albertaApiKey, String tenant, String customerNumber, Article body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'albertaApiKey' is set
        if (albertaApiKey == null) {
            throw new ApiException("Missing the required parameter 'albertaApiKey' when calling updateArticle(Async)");
        }
        // verify the required parameter 'tenant' is set
        if (tenant == null) {
            throw new ApiException("Missing the required parameter 'tenant' when calling updateArticle(Async)");
        }
        // verify the required parameter 'customerNumber' is set
        if (customerNumber == null) {
            throw new ApiException("Missing the required parameter 'customerNumber' when calling updateArticle(Async)");
        }
        
        com.squareup.okhttp.Call call = updateArticleCall(albertaApiKey, tenant, customerNumber, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Artikel in Alberta ändern
     * ändert einen Artikel in Alberta
     * @param albertaApiKey  (required)
     * @param tenant  (required)
     * @param customerNumber  (required)
     * @param body article to update (optional)
     * @return ArticleMapping
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ArticleMapping updateArticle(String albertaApiKey, String tenant, String customerNumber, Article body) throws ApiException {
        ApiResponse<ArticleMapping> resp = updateArticleWithHttpInfo(albertaApiKey, tenant, customerNumber, body);
        return resp.getData();
    }

    /**
     * Artikel in Alberta ändern
     * ändert einen Artikel in Alberta
     * @param albertaApiKey  (required)
     * @param tenant  (required)
     * @param customerNumber  (required)
     * @param body article to update (optional)
     * @return ApiResponse&lt;ArticleMapping&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ArticleMapping> updateArticleWithHttpInfo(String albertaApiKey, String tenant, String customerNumber, Article body) throws ApiException {
        com.squareup.okhttp.Call call = updateArticleValidateBeforeCall(albertaApiKey, tenant, customerNumber, body, null, null);
        Type localVarReturnType = new TypeToken<ArticleMapping>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Artikel in Alberta ändern (asynchronously)
     * ändert einen Artikel in Alberta
     * @param albertaApiKey  (required)
     * @param tenant  (required)
     * @param customerNumber  (required)
     * @param body article to update (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateArticleAsync(String albertaApiKey, String tenant, String customerNumber, Article body, final ApiCallback<ArticleMapping> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateArticleValidateBeforeCall(albertaApiKey, tenant, customerNumber, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ArticleMapping>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateInsuranceContract
     * @param albertaApiKey  (required)
     * @param tenant  (required)
     * @param id  (required)
     * @param body article to update (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateInsuranceContractCall(String albertaApiKey, String tenant, String id, Article body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/insuranceContract/{id}"
            .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        if (albertaApiKey != null)
        localVarHeaderParams.put("alberta-api-key", apiClient.parameterToString(albertaApiKey));
        if (tenant != null)
        localVarHeaderParams.put("tenant", apiClient.parameterToString(tenant));

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
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call updateInsuranceContractValidateBeforeCall(String albertaApiKey, String tenant, String id, Article body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'albertaApiKey' is set
        if (albertaApiKey == null) {
            throw new ApiException("Missing the required parameter 'albertaApiKey' when calling updateInsuranceContract(Async)");
        }
        // verify the required parameter 'tenant' is set
        if (tenant == null) {
            throw new ApiException("Missing the required parameter 'tenant' when calling updateInsuranceContract(Async)");
        }
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateInsuranceContract(Async)");
        }
        
        com.squareup.okhttp.Call call = updateInsuranceContractCall(albertaApiKey, tenant, id, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Krankenkassenvertrag in Alberta ändern
     * ändert einen Krankenkassenvertrag in Alberta
     * @param albertaApiKey  (required)
     * @param tenant  (required)
     * @param id  (required)
     * @param body article to update (optional)
     * @return ArticleMapping
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ArticleMapping updateInsuranceContract(String albertaApiKey, String tenant, String id, Article body) throws ApiException {
        ApiResponse<ArticleMapping> resp = updateInsuranceContractWithHttpInfo(albertaApiKey, tenant, id, body);
        return resp.getData();
    }

    /**
     * Krankenkassenvertrag in Alberta ändern
     * ändert einen Krankenkassenvertrag in Alberta
     * @param albertaApiKey  (required)
     * @param tenant  (required)
     * @param id  (required)
     * @param body article to update (optional)
     * @return ApiResponse&lt;ArticleMapping&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ArticleMapping> updateInsuranceContractWithHttpInfo(String albertaApiKey, String tenant, String id, Article body) throws ApiException {
        com.squareup.okhttp.Call call = updateInsuranceContractValidateBeforeCall(albertaApiKey, tenant, id, body, null, null);
        Type localVarReturnType = new TypeToken<ArticleMapping>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Krankenkassenvertrag in Alberta ändern (asynchronously)
     * ändert einen Krankenkassenvertrag in Alberta
     * @param albertaApiKey  (required)
     * @param tenant  (required)
     * @param id  (required)
     * @param body article to update (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateInsuranceContractAsync(String albertaApiKey, String tenant, String id, Article body, final ApiCallback<ArticleMapping> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updateInsuranceContractValidateBeforeCall(albertaApiKey, tenant, id, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ArticleMapping>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}