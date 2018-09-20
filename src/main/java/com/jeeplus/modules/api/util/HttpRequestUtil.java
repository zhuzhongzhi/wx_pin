package com.jeeplus.modules.api.util;

import com.alibaba.fastjson.JSON;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.*;
import java.security.SecureRandom;
import java.util.Map;


public class HttpRequestUtil {

    /**
     * 日志对象
     */
    protected static Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);

  /*  *//**
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式
     * @param outputStr     提交的数据
     * @return
     *//*
    public static JSONObject httpRequestJSONObject(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        try {
            //创建SSLContext对象
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            //从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            //设置请求方式
            conn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                conn.connect();
            }

            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            //从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            //释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("请求异常: " + e.getMessage());
        }
        return jsonObject;
    }
*/

    public static <T> T getByParams(String url, Map<String, String> params, Class<T> resultType) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .sslSocketFactory(createSSLSocketFactory())
                    .hostnameVerifier(new TrustAllHostnameVerifier())
                    .build();
            boolean flag = url.indexOf("?") > -1;
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            if(params != null) {
                for (String key : params.keySet()) {
                    if (flag || pos > 0) {
                        tempParams.append("&");
                    } else {
                        tempParams.append("?");
                    }
                    try {
                        tempParams.append(String.format("%s=%s", key, URLEncoder.encode(params.get(key), "utf-8")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    pos++;
                }
            }
            Request request = new Request.Builder()
                    .url(url + tempParams.toString())
                    .build();
            Call call = client.newCall(request);

            Response response = call.execute();
            String json = response.body().string();
            System.out.println(json);
            if(resultType.equals(String.class)) {
                return (T)json;
            } else {
                return JSON.parseObject(json, resultType);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("请求异常: " + e.getMessage());
        }
        return null;
    }

    /**
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式
     * @param outputStr     提交的数据
     * @return
     */
    public static String reqData(String requestUrl, String requestMethod, String outputStr) {
        String res = null;
        try {
            //创建SSLContext对象
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            //从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            //设置请求方式
            conn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                conn.connect();
            }

            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            //从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            //释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            res = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("请求异常: " + e.getMessage());
        }
        return res;
    }

    public static String loadJSON(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }

        return json.toString();
    }

    /**
     * 通过流传递数据
     * Description: <br>
     *
     * @param url
     * @param data
     * @return
     * @throws IOException
     * @see
     */
    public static String postByStream(String url, String data, String ContentType) {
        HttpURLConnection httpConn = null;
        InputStream is = null;
        try {
            httpConn = (HttpURLConnection) new URL(url).openConnection();
            httpConn.setRequestMethod("POST");

            if (!StringUtils.isEmpty(ContentType)) {
                httpConn.setRequestProperty("Content-Type", ContentType);
            }
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            OutputStream out = httpConn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
            osw.write(data);
            osw.flush();
            osw.close();
            out.close();
            is = httpConn.getInputStream();
            return inputStreamTOString(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String inputStreamTOString(InputStream in) {
        int BUFFER_SIZE = 4096;
        String result = null;
        ByteArrayOutputStream outStream = null;
        if (in != null) {
            try {
                outStream = new ByteArrayOutputStream();
                byte[] data = new byte[BUFFER_SIZE];
                int count = -1;
                while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
                    outStream.write(data, 0, count);
                data = null;
                result = new String(outStream.toByteArray(), "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    outStream.close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }

    public static String getRequestParams(HttpServletRequest request)
            throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));

        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
        return null;
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null,  new TrustManager[] { new MyX509TrustManager() }, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }
}
