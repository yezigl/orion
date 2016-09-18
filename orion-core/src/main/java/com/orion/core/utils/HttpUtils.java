/**
 * Copyright 2014 yezi.gl. All Rights Reserved.
 */
package com.orion.core.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

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

    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * GET
     * @param url
     * @return
     */
    public static String get(String url) {
        return get(url, null, DEFAULT_CHARSET);
    }

    /**
     * GET
     * @param url
     * @param charset
     * @return
     */
    public static String get(String url, String charset) {
        return get(url, null, charset);
    }

    /**
     * GET
     * @param url
     * @param header
     * @param charset
     * @return
     */
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
                int readCount;
                byte[] buffer = new byte[1024];
                while ((readCount = is.read(buffer)) > 0) {
                    out.write(buffer, 0, readCount);
                }
                is.close();
            } else {
                logger.warn("{} http response code is {}", url, responseCode);
            }
            connection.disconnect();
            result = out.toString(charset);
        } catch (IOException e) {
            logger.error("{}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * POST
     * @param url
     * @param params
     * @return
     */
    public static String post(String url, Map<String, String> params) {
        return post(url, params, DEFAULT_CHARSET);
    }

    /**
     * POST
     * @param url
     * @param params
     * @param charset
     * @return
     */
    public static String post(String url, Map<String, String> params, String charset) {
        return post(url, params, null, charset);
    }

    /**
     * POST
     * @param url
     * @param params
     * @param header
     * @param charset
     * @return
     */
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

            StringBuilder postData = new StringBuilder();
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    postData.append(entry.getKey() + "=" + entry.getValue() + "&");
                }
            }

            OutputStream out = connection.getOutputStream();
            out.write(postData.toString().getBytes(charset));
            out.flush();

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStream is = connection.getInputStream();
                int readCount;
                byte[] buffer = new byte[1024];
                while ((readCount = is.read(buffer)) > 0) {
                    bout.write(buffer, 0, readCount);
                }
                is.close();
            }
            connection.disconnect();
            result = bout.toString();
        } catch (IOException e) {
            logger.error("{}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * POST
     * @param url
     * @param content
     * @return
     */
    public static String post(String url, String content) {
        String result = "";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);

            String postData = content;

            OutputStream out = connection.getOutputStream();
            out.write(postData.getBytes(DEFAULT_CHARSET));
            out.flush();

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStream is = connection.getInputStream();
                int readCount;
                byte[] buffer = new byte[1024];
                while ((readCount = is.read(buffer)) > 0) {
                    bout.write(buffer, 0, readCount);
                }
                is.close();
            }
            connection.disconnect();
            result = bout.toString();
        } catch (IOException e) {
            logger.error("{}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * 获取byte流
     * @param url
     * @param header
     * @return
     */
    public static byte[] getBytes(String url, Map<String, String> header) {
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
                int readCount;
                byte[] buffer = new byte[1024];
                while ((readCount = is.read(buffer)) > 0) {
                    out.write(buffer, 0, readCount);
                }
                is.close();
            } else {
                logger.warn("{} http response code is {}", url, responseCode);
            }
            connection.disconnect();
            return out.toByteArray();
        } catch (IOException e) {
            logger.error("{}", e.getMessage(), e);
        }
        return new byte[0];
    }

    /**
     * 上传byte流
     * @param url
     * @param params
     * @param filename
     * @param bytes
     * @return
     */
    public static String upload(String url, Map<String, String> params, String filename, byte[] bytes) {
        String boundary = UUID.randomUUID().toString();
        String result = "";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            OutputStream out = connection.getOutputStream();

            byte[] endData = ("\r\n--" + boundary + "--\r\n").getBytes(DEFAULT_CHARSET);// 定义最后数据分隔线
            StringBuilder sb = new StringBuilder();
            // 添加form属性
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    sb.append("--");
                    sb.append(boundary);
                    sb.append("\r\n");
                    sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"");
                    sb.append("\r\n\r\n");
                    sb.append(entry.getValue());
                    out.write(sb.toString().getBytes(DEFAULT_CHARSET));
                    out.write("\r\n".getBytes(DEFAULT_CHARSET));
                }
            }

            sb = new StringBuilder();
            sb.append("--" + boundary + "\r\n");
            sb.append("Content-Disposition: form-data; name=\"" + filename + "\"; filename=\"showqrcode.jpg\"\r\n");
            sb.append("Content-Type: image/jpg\r\n\r\n");
            out.write(sb.toString().getBytes(DEFAULT_CHARSET));
            out.write(bytes);
            
            out.write(endData);
            out.flush();
            out.close();

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStream is = connection.getInputStream();
                int readCount;
                byte[] buffer = new byte[1024];
                while ((readCount = is.read(buffer)) > 0) {
                    bout.write(buffer, 0, readCount);
                }
                is.close();
            }
            result = bout.toString();
            connection.disconnect();
        } catch (Exception e) {
            logger.error("{}", e.getMessage(), e);
        }
        return result;
    }
}
