package com.jeeplus.modules.sys.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import okhttp3.MediaType;

/**
 * Created by ysisl on 17/11/24.
 */
public class HttpClientUtil {

	private static PoolingHttpClientConnectionManager connMgr;
	private static RequestConfig requestConfig;
	private static final int MAX_TIMEOUT = 7000;

	static {
		// 设置连接池
		connMgr = new PoolingHttpClientConnectionManager();
		// 设置连接池大小
		connMgr.setMaxTotal(100);
		connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

		RequestConfig.Builder configBuilder = RequestConfig.custom();
		// 设置连接超时
		configBuilder.setConnectTimeout(MAX_TIMEOUT);
		// 设置读取超时
		configBuilder.setSocketTimeout(MAX_TIMEOUT);
		// 设置从连接池获取连接实例的超时
		configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
		// 在提交请求之前 测试连接是否可用
		configBuilder.setStaleConnectionCheckEnabled(true);
		requestConfig = configBuilder.build();
	}

	/**
	 * 获取请求中Body内容
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestBodyStr(HttpServletRequest request) {
		int contentLength = request.getContentLength();
		if (contentLength < 0) {
			return null;
		}
		byte buffer[] = new byte[contentLength];
		for (int i = 0; i < contentLength;) {
			int readlen = 0;
			try {
				readlen = request.getInputStream().read(buffer, i, contentLength - i);
			} catch (IOException e) {
				return e.getMessage();
			}

			if (readlen == -1) {
				break;
			}
			i += readlen;
		}

		String charEncoding = request.getCharacterEncoding();
		if (charEncoding == null) {
			charEncoding = "UTF-8";
		}
		try {
			return new String(buffer, charEncoding);
		} catch (UnsupportedEncodingException e) {
			return "不支持的编码格式";
		}

	}

	/**
	 * 创建SSL连接，带连接池管理
	 * 
	 * @return
	 */
	public static CloseableHttpClient createSSLClientDefault() {

		try {
			// SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new
			// TrustStrategy() {
			// 在JSSE中，证书信任管理器类就是实现了接口X509TrustManager的类。我们可以自己实现该接口，让它信任我们指定的证书。
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			// 信任所有
			X509TrustManager x509mgr = new X509TrustManager() {

				// 该方法检查客户端的证书，若不信任该证书则抛出异常
				public void checkClientTrusted(X509Certificate[] xcs, String string) {
				}

				// 该方法检查服务端的证书，若不信任该证书则抛出异常
				public void checkServerTrusted(X509Certificate[] xcs, String string) {
				}

				// 返回受信任的X509证书数组。
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[] { x509mgr }, null);
			//// 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			// HttpsURLConnection对象就可以正常连接HTTPS了，无论其证书是否经权威机构的验证，只要实现了接口X509TrustManager的类MyX509TrustManager信任该证书。
			return HttpClients.custom().setSSLSocketFactory(sslsf).setConnectionManager(connMgr)
					.setDefaultRequestConfig(requestConfig).build();

		} catch (KeyManagementException e) {

			e.printStackTrace();

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		}

		// 创建默认的httpClient实例.
		return HttpClients.createDefault();

	}

	/**
	 * 发送POST请求（Https)
	 * 
	 * @param url
	 * @param body
	 * @return
	 */
	public static String postHttps(String url, String body) {

		CloseableHttpClient httpClient = createSSLClientDefault();
		CloseableHttpResponse response = null;
		StringEntity entity = new StringEntity(body, "utf-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		HttpPost httpPost = new HttpPost();
		httpPost.setEntity(entity);
		try {
			httpPost.setURI((new URL(url)).toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		String respContent = "";
		try {
			httpPost.setConfig(requestConfig);
			response = httpClient.execute(httpPost);
			HttpEntity he = response.getEntity();
			respContent = EntityUtils.toString(he, "UTF-8");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			/*
			 * if(response!=null) { try { EntityUtils.consume(response.getEntity()); } catch
			 * (IOException e) { e.printStackTrace(); } }
			 */
		}
		return respContent;
	}

	/**
	 * 发送 POST 请求（HTTP），JSON形式
	 * 
	 * @param apiUrl
	 * @param json
	 *            json对象
	 * @return
	 */
	public static String postHttp(String apiUrl, Object json) {
		HttpClient httpClient = HttpClients.createDefault();
		String httpStr = null;
		HttpPost httpPost = new HttpPost(apiUrl);
		HttpResponse response = null;

		try {
			httpPost.setConfig(requestConfig);
			StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");// 解决中文乱码问题
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			httpStr = EntityUtils.toString(entity, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return httpStr;
	}
	
	/**
	 * 发送 POST 请求（HTTP），JSON形式
	 * 
	 * @param apiUrl
	 * @param json
	 *            json对象
	 * @return
	 */
	public static String postHttp(String apiUrl, Object json, String filePath) {
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(apiUrl);
		HttpResponse response = null;

		try {
			httpPost.setConfig(requestConfig);
			StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");// 解决中文乱码问题
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			response = httpClient.execute(httpPost);
			
			DataOutputStream out = new DataOutputStream(new FileOutputStream(filePath));
            
			HttpEntity entity = response.getEntity();
			entity.writeTo(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return filePath;
	}

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	/**
	 * 采用OKHttp调用外部请求
	 * 
	 * @param url
	 * @param json
	 * @return
	 * @throws IOException
	 */
	public static String okPost(String url, String json) throws IOException {
		OkHttpClient client = new OkHttpClient();
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url).post(body).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}

	/**
	 * 发起Get请求
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String okGet(String url) throws IOException {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(url).build();

		Response response = client.newCall(request).execute();
		return response.body().string();
	}

}
