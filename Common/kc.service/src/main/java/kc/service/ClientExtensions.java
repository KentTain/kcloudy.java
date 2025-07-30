package kc.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import kc.framework.extension.StringExtensions;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@lombok.extern.slf4j.Slf4j
public class ClientExtensions {
	private final static String serviceName = "kc.service.ClientExtensions";
	/**
	 * application/json
	 */
	protected final static String DefaultContentType = "application/json; charset=UTF-8";
	/**
	 * application/x-www-form-urlencoded
	 */
	protected final static String FormContentType = "application/x-www-form-urlencoded";

	//private final String DefaultUserAgent = "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; WOW64; Trident/6.0) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.224 Safari/534.10";
	//private final String DefaultAccept = "application/json";
	//private final String DefaultAcceptLanguage = "en-US,en;q=0.8";

	private static HttpClientContext context = HttpClientContext.create();
	private static RequestConfig requestConfig = RequestConfig.custom()
			// .setConnectTimeout(TimeOutConstants.CacheShortTimeOut)
			// .setSocketTimeout(TimeOutConstants.CacheShortTimeOut)
			// .setConnectionRequestTimeout(TimeOutConstants.CacheShortTimeOut)
			.setCookieSpec(CookieSpecs.STANDARD_STRICT).setExpectContinueEnabled(true)
			.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
			.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();

	// https
	private static SSLConnectionSocketFactory socketFactory;
	private static TrustManager manager = new X509TrustManager() {

		@Override
		public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

		}

		@Override
		public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	};

	private static void enableSSL() {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[] { manager }, null);
			socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
		} catch (NoSuchAlgorithmException e) {
			log.error("ClientExtensions enableSSL throw error: " + e.getMessage());
		} catch (KeyManagementException e) {
			log.error("ClientExtensions enableSSL throw error: " + e.getMessage());
		}
	}

	/**
	 * 获取可自动销毁的HttpClient对象
	 * 
	 * @param isHttps 是否使用https
	 * @return CloseableHttpClient
	 */
	public static CloseableHttpClient GetClient(boolean isHttps) {
		// if i comment out the system properties, and don't set any jvm arguments, the
		// program runs and prints out the html fine.
		// System.setProperty("http.proxyHost", "localhost");
		// System.setProperty("http.proxyPort", "8888");

		CookieStore cookieStore = new BasicCookieStore();

		if (isHttps) {
			enableSSL();
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.INSTANCE).register("https", socketFactory).build();
			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
					socketFactoryRegistry);
			return HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig)
					.build();
		}

		return HttpClientBuilder.create().setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
				.setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultCookieStore(cookieStore)
				.setDefaultRequestConfig(requestConfig).build();
	}

	/**
	 * 使用Get方法，获取远程服务器相关数据
	 *
	 * @param isHttps      是否使用https
	 * @param url          远程服务器地址
	 * @param contentType  远程服务器返回的数据格式，默认为：application/json
	 * @param headers      Content-Header
	 * @param postJsonData Post的数据（JSon格式）
	 * @return CloseableHttpResponse
	 */
	public static CloseableHttpResponse doGet(boolean isHttps, String url, String contentType,
			Map<String, String> headers) {
		URI uri;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			log.error(String.format("Service's [%s] url is wrong. ", serviceName));
			return null;
		}
		String baseUri = uri.getScheme() + "://" + uri.getAuthority();
		String apiUri = uri.getPath() + "?" + uri.getQuery();

		HttpGet httpGet = new HttpGet(baseUri + apiUri);
		
		// httpGet.addHeader("Authorization", "Bearer " +
		// "eyJhbGciOiJSUzI1NiIsImtpZCI6IjZmZmE0YjFjMzJjOWJlYzAxNjIyZWU0ZWU3MTE5NTc3IiwidHlwIjoiSldUIn0.eyJuYmYiOjE1NjQzNzk3MTQsImV4cCI6MTU2NDM4MzMxNCwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDoxMDAxIiwiYXVkIjpbImh0dHA6Ly9sb2NhbGhvc3Q6MTAwMS9yZXNvdXJjZXMiLCJhY2NhcGkiXSwiY2xpZW50X2lkIjoiWTFSbGMzUT0iLCJzY29wZSI6WyJhY2NhcGkiXX0.k1uQjIB4ehkK35-kpzaM_3DF2TSGS3WrlNj3c36l_5bBxUeMmZ4WUxMBZ2RYF4UEgoXai4MfrI-tRR32f_w1em-y_2hWbrjzpu3-IMkgiPpP4EjNccwQUkNNLjo5WAXFuuY-Vw0S4PmNBZxRtjuM5sJRcavHMsrvaTeUja_cYKQ2s4SJT1DorVRmzm9wsoYJ1x9KbLZjoW34IWmpnRa3IS2d4kRd50Jp5-pROORN7j9Yb9aeiNCCY4Vr0fdMGFLWIVv6XaSBJaL4DcFAlXMgTajKsshFSHJAY1Yl-sxkVdJ5huKP2qRdQQq7-V718mJr-w7Kez3FT_J9DbFoJnJjkA");
		if (StringExtensions.isNullOrEmpty(contentType)) {
			httpGet.addHeader("Content-Type", DefaultContentType);
		} else {
			httpGet.setHeader("Content-Type", contentType);
		}

		if (headers != null) {
			for (String key : headers.keySet()) {
				httpGet.addHeader(key, headers.get(key));
			}
		}

		CloseableHttpClient httpClient = GetClient(isHttps);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpGet, context);
		} catch (Exception e) {
			log.error("ClientExtensions doGet throw error: " + e.getMessage());
		}

		return response;
	}

	/**
	 * 使用Post方法（Post的数据为JSon格式），获取远程服务器相关数据
	 *
	 * @param isHttps      是否使用https
	 * @param url          远程服务器地址
	 * @param contentType  远程服务器返回的数据格式，默认为：application/json
	 * @param headers      Content-Header
	 * @param postJsonData Post的数据（JSon格式）
	 * @return CloseableHttpResponse
	 */
	public static CloseableHttpResponse doPost(boolean isHttps, String url, String contentType,
			Map<String, String> headers, String postJsonData) {
		URI uri;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			log.error(String.format("Service's [%s] url is wrong. ", serviceName));
			return null;
		}
		String baseUri = uri.getScheme() + "://" + uri.getAuthority();
		String apiUri = uri.getPath() + "?" + uri.getQuery();

		HttpPost httpPost = new HttpPost(baseUri + apiUri);
		// httpPost.addHeader("Authorization", "Bearer " +
		// "eyJhbGciOiJSUzI1NiIsImtpZCI6IjZmZmE0YjFjMzJjOWJlYzAxNjIyZWU0ZWU3MTE5NTc3IiwidHlwIjoiSldUIn0.eyJuYmYiOjE1NjQzNzk3MTQsImV4cCI6MTU2NDM4MzMxNCwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDoxMDAxIiwiYXVkIjpbImh0dHA6Ly9sb2NhbGhvc3Q6MTAwMS9yZXNvdXJjZXMiLCJhY2NhcGkiXSwiY2xpZW50X2lkIjoiWTFSbGMzUT0iLCJzY29wZSI6WyJhY2NhcGkiXX0.k1uQjIB4ehkK35-kpzaM_3DF2TSGS3WrlNj3c36l_5bBxUeMmZ4WUxMBZ2RYF4UEgoXai4MfrI-tRR32f_w1em-y_2hWbrjzpu3-IMkgiPpP4EjNccwQUkNNLjo5WAXFuuY-Vw0S4PmNBZxRtjuM5sJRcavHMsrvaTeUja_cYKQ2s4SJT1DorVRmzm9wsoYJ1x9KbLZjoW34IWmpnRa3IS2d4kRd50Jp5-pROORN7j9Yb9aeiNCCY4Vr0fdMGFLWIVv6XaSBJaL4DcFAlXMgTajKsshFSHJAY1Yl-sxkVdJ5huKP2qRdQQq7-V718mJr-w7Kez3FT_J9DbFoJnJjkA");
		if (StringExtensions.isNullOrEmpty(contentType)) {
			httpPost.setHeader("Content-Type", DefaultContentType);
		} else {
			httpPost.setHeader("Content-Type", contentType);
		}

		if (headers != null) {
			for (String key : headers.keySet()) {
				httpPost.addHeader(key, headers.get(key));
			}
		}

		if (!StringExtensions.isNullOrEmpty(postJsonData)) {
			httpPost.setEntity(new StringEntity(postJsonData, Consts.UTF_8));
		}

		CloseableHttpClient httpClient = GetClient(isHttps);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpPost, context);
		} catch (Exception e) {
			log.error("ClientExtensions doPost throw error: " + e.getMessage());
		}

		return response;
	}

	/**
	 * 使用Post方法（Post的数据为Form格式），获取远程服务器相关数据
	 *
	 * @param isHttps      是否使用https
	 * @param url          远程服务器地址
	 * @param contentType  远程服务器返回的数据格式，默认为：application/x-www-form-urlencoded
	 * @param headers      Content-Header
	 * @param postJsonData Post的数据（Form格式）
	 * @return CloseableHttpResponse
	 */
	public static CloseableHttpResponse doPostFormData(String url, Map<String, String> headers,
			Map<String, String> postFormData) {
		CloseableHttpClient httpClient = GetClient(false);

		HttpPost httpPost = new HttpPost(url);
		if (headers != null) {
			for (String key : headers.keySet()) {
				httpPost.addHeader(key, headers.get(key));
			}
		}

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		if (postFormData != null) {
			for (String key : postFormData.keySet()) {
				nameValuePairs.add(new BasicNameValuePair(key, postFormData.get(key)));
			}
		}

		CloseableHttpResponse response = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8));
			response = httpClient.execute(httpPost);
		} catch (UnsupportedEncodingException e) {
			log.error("ClientExtensions doPost throw UnsupportedEncodingException: " + e.getMessage());
		} catch (ClientProtocolException e) {
			log.error("ClientExtensions doPost throw ClientProtocolException: " + e.getMessage());
		} catch (IOException e) {
			log.error("ClientExtensions doPost throw IOException: " + e.getMessage());
		}
		return response;
	}

	/**
	 * 直接把Response内的Entity内容转换成String
	 *
	 * @param httpResponse
	 * @return String
	 */
	public static String toString(CloseableHttpResponse httpResponse) {
		if(httpResponse == null)
			return null;
		
		// 获取响应消息实体
		String result = null;
		try {
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, Consts.UTF_8);
			}
		} catch (Exception e) {
			log.error("ClientExtensions toString throw error: " + e.getMessage());
		} finally {
			try {
				httpResponse.close();
			} catch (IOException e) {
				log.error("ClientExtensions toString throw error: " + e.getMessage());
			}
		}
		return result;
	}
}
