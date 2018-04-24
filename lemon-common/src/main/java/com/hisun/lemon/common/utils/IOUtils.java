package com.hisun.lemon.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Optional;

/**
 * IO 相关工具类
 * @author yuzhou
 * @date 2017年7月4日
 * @time 下午3:40:55
 *
 */
public class IOUtils extends org.apache.commons.io.IOUtils{
    /**
     * InputStreamReader 转 String
     * @param reader
     * @return
     * @throws IOException
     */
    public static String toString(Reader reader) throws IOException {
        StringBuilder sb = null;
        BufferedReader br = new BufferedReader(reader);
        String line = null;
        while((line = br.readLine()) != null) {
            if(sb == null) {
                sb = new StringBuilder(line);
            } else {
                sb.append(ResourceUtils.ENTER_NEW_LINE).append(line);
            }
        }
        return Optional.ofNullable(sb).map(sbb -> sbb.toString()).orElse(null);
    }
    
    public static String toStringIgnoreException(InputStream input, String encoding) {
        try {
            return toString(input, encoding);
        } catch (IOException e) {
            return null;
        }
    }
    
    /**
     * inputstrem 转 String
     * @param inputStream
     * @param charset
     * @return
     * @throws IOException
     
    public static String toString(InputStream inputStream, String charset) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while((length = inputStream.read(buffer)) != -1) {  
            bos.write(buffer, 0, length); 
        }
        return new String(bos.toByteArray(), charset);
    }
    */
}
