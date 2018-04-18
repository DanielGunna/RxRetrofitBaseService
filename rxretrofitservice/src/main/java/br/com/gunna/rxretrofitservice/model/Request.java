package br.com.gunna.rxretrofitservice.model;

import java.util.HashMap;
import java.util.List;

public class Request<T> {
    private T requestBody;
    private HashMap<String,String> headers;
    private String url;
    private String pathParam;
    private List<String> queryParams;


    public T getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(T requestBody) {
        this.requestBody = requestBody;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPathParam() {
        return pathParam;
    }

    public void setPathParam(String pathParam) {
        this.pathParam = pathParam;
    }

    public List<String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(List<String> queryParams) {
        this.queryParams = queryParams;
    }
}
