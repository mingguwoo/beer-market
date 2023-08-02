package com.nio.ngfs.plm.bom.configuration.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author wangchao.wang
 */
@Slf4j
public class GZIPUtils {

    private static final String GZIP_ENCODE_UTF_8 = "UTF-8";


    /**
     * @param bytes
     * @param encoding
     * @return 87
     */
    public static String uncompressToString(byte[] bytes, String encoding) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[10240];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(encoding);
        } catch (IOException e) {
            log.error("uncompressToString error", e);
            return null;
        }
    }


    public static String compress(String str) {
        try {
            if (StringUtils.isBlank(str)) {
                return str;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes());
            gzip.close();
            String outStr = new String(Base64.encodeBase64(out.toByteArray()));
            return outStr;
        } catch (Exception e) {
            log.error("compress error", e);
            return null;
        }
    }

    public static String uncompress(String str) {
        try {
            if (StringUtils.isBlank(str)) {
                return str;
            }
            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(Base64.decodeBase64(str)));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(gis, out);
            return new String(out.toByteArray());
        } catch (Exception e) {
            log.error("uncompress error", e);
            return null;
        }
    }
}
