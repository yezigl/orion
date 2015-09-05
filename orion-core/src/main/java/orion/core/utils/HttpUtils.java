/**
 * Copyright 2014 yezi.gl. All Rights Reserved.
 */
package orion.core.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description here
 *
 * @author yezi
 * @since 2014年8月24日
 */
public class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static int connectTimeout = 10000;

    private static int readTimeout = 15000;

    private static byte[] buffer = new byte[1024];

    private static final String DEFAULT_CHARSET = "UTF-8";

    public static String get(String url) {
        return get(url, null, DEFAULT_CHARSET);
    }

    public static String get(String url, String charset) {
        return get(url, null, charset);
    }

    public static String get(String url, Map<String, String> header, String charset) {
        String result = "";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setUseCaches(false);
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);

            if (header != null) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int responseCode = connection.getResponseCode();
            if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStream is = connection.getInputStream();
                int readCount = 0;
                while ((readCount = is.read(buffer)) > 0) {
                    out.write(buffer, 0, readCount);
                }
                is.close();
            } else {
                logger.warn("{} http response code is {}", url, responseCode);
            }
            connection.disconnect();
            result = out.toString();
        } catch (IOException e) {

        }
        return result;
    }

    public static String post(String url, Map<String, String> params, String charset) {
        return post(url, params, null, charset);
    }

    public static String post(String url, Map<String, String> params, Map<String, String> header, String charset) {
        String result = "";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);

            if (header != null) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            String postData = "";
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    postData += entry.getKey() + "=" + entry.getValue() + "&";
                }
            }

            OutputStream out = connection.getOutputStream();
            out.write(postData.getBytes(charset));
            out.flush();

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStream is = connection.getInputStream();
                int readCount = 0;
                while ((readCount = is.read(buffer)) > 0) {
                    bout.write(buffer, 0, readCount);
                }
                is.close();
            }
            connection.disconnect();
            result = out.toString();
        } catch (IOException e) {

        }
        return result;
    }
}
