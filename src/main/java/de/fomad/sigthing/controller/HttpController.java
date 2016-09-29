package de.fomad.sigthing.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.fomad.sigthing.model.AuthData;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Arrays;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author binary gamura
 */
public class HttpController {

    private static final Logger LOGGER = LogManager.getLogger(HttpController.class);

    private final AuthData authData;

    private final HttpHost loginHost;

    private final Gson gson;

    private final CloseableHttpClient httpClient;

    private final HttpClientContext loginContext;

    private final URI verificationUrl;

    public HttpController(URI verificationUrl, String clientId, String clientSecret) {

	this.verificationUrl = verificationUrl;
	authData = new AuthData();
	int port = verificationUrl.getScheme().equalsIgnoreCase("https") ? 443 : 80;
	loginHost = new HttpHost(verificationUrl.getHost(), port, verificationUrl.getScheme());
	CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	credentialsProvider.setCredentials(new AuthScope(loginHost.getHostName(), port), new UsernamePasswordCredentials(clientId, clientSecret));
	gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

	AuthCache authCache = new BasicAuthCache();
	BasicScheme basicAuth = new BasicScheme();
	authCache.put(loginHost, basicAuth);

	loginContext = HttpClientContext.create();
	loginContext.setAuthCache(authCache);

	httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
    }

    public void setAccessCode(String accessCode) {
	authData.setAccessCode(accessCode);
    }

    private void retrieveAccessToken(boolean useRefreshtoken) throws UnsupportedEncodingException, IOException {
	UrlEncodedFormEntity form;
	authData.setLastTokenRefresh(System.currentTimeMillis());
	if (!useRefreshtoken) {
	    form = new UrlEncodedFormEntity(Arrays.asList(
		    new BasicNameValuePair("code", authData.getAccessCode()),
		    new BasicNameValuePair("grant_type", "authorization_code")));
	}
	else {
	    form = new UrlEncodedFormEntity(Arrays.asList(
		    new BasicNameValuePair("refresh_token", authData.getRefreshToken()),
		    new BasicNameValuePair("grant_type", "refresh_token")));
	}

	HttpUriRequest request = RequestBuilder
		.post()
		.setEntity(form)
		.setUri(verificationUrl)
		.build();
	try (CloseableHttpResponse response = httpClient.execute(loginHost, request, loginContext)) {
	    if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
		String responseMessage = EntityUtils.toString(response.getEntity());
		throw new IOException("unable to trade the access code to a token! " + responseMessage);
	    }
	    String responseBody = EntityUtils.toString(response.getEntity());
	    JsonParser parser = new JsonParser();
	    JsonObject responseObject = parser.parse(responseBody).getAsJsonObject();
	    String refreshToken = responseObject.get("refresh_token").getAsString();
	    long expiresIn = responseObject.get("expires_in").getAsLong();
	    String accessToken = responseObject.get("access_token").getAsString();

	    authData.setAccessToken(accessToken);
	    authData.setRefreshToken(refreshToken);
	    authData.setExpiresIn(expiresIn);

	    LOGGER.info("got token from server " + accessToken + " which expires in " + expiresIn + "s.");

	}
    }

    public void shutDown() {
	try {
	    httpClient.close();
	}
	catch (Exception ex) {
	    LOGGER.warn("error while shutting down http client.", ex);
	}
    }
    
    public <T> T makeApiPostRequest(URI target, Class<T> responseType, boolean useAuth, Object payload) throws IOException, UnsupportedEncodingException {

	if (!authData.hasAccessToken()) {
	    retrieveAccessToken(false);
	}
	if (authData.isAccessTokenExpired()) {
	    retrieveAccessToken(true);
	}

	RequestBuilder builder = RequestBuilder
		.post()
                .setEntity(new StringEntity(gson.toJson(payload)))
		.setUri(target);
        if(useAuth){
            builder.addHeader(new BasicHeader("Authorization", "Bearer " + authData.getAccessToken()));
        }
        HttpUriRequest request = builder.build();
	try (CloseableHttpResponse response = httpClient.execute(request)) {
	    if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
		String responseMessage = EntityUtils.toString(response.getEntity());
		throw new IOException("unable to perform post query! " + responseMessage);
	    }

	    return gson.fromJson(EntityUtils.toString(response.getEntity()), responseType);
	}
    }
    
    public <T> T makeApiGetRequest(URI target, Class<T> responseType, boolean useAuth) throws IOException, UnsupportedEncodingException {

	if (!authData.hasAccessToken()) {
	    retrieveAccessToken(false);
	}
	if (authData.isAccessTokenExpired()) {
	    retrieveAccessToken(true);
	}

	RequestBuilder builder = RequestBuilder
		.get()
		.setUri(target);
        if(useAuth){
            builder.addHeader(new BasicHeader("Authorization", "Bearer " + authData.getAccessToken()));
        }
        HttpUriRequest request = builder.build();
	try (CloseableHttpResponse response = httpClient.execute(request)) {
	    if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
		String responseMessage = EntityUtils.toString(response.getEntity());
		throw new HttpControllerException("unable to perform get query! " + responseMessage, response.getStatusLine().getStatusCode());
	    }

	    return gson.fromJson(EntityUtils.toString(response.getEntity()), responseType);
	}
    }
}
