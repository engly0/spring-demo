package me.yangtao.spring.demo.common.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public JsonUtil() {
    }

    public static String GetJsonResult(Object src) {
        StringWriter sw = new StringWriter();

        try {
            objectMapper.writeValue(sw, src);
            String var2 = sw.getBuffer().toString();
            return var2;
        } catch (Exception var12) {
            log.error("GetJsonResult err:", var12);
        } finally {
            sw.flush();

            try {
                sw.close();
            } catch (IOException var11) {
                var11.printStackTrace();
            }

        }

        return "";
    }

    public static <T> T GetObjByJson(String json, Class<T> type) throws Exception {
        try {
            T result = objectMapper.readValue(json, type);
            return result;
        } catch (Exception var3) {
            throw new Exception("JsonHelper.GetObjByJson Error:" + var3.getMessage() + var3.getStackTrace());
        }
    }

    public static void SaveJsonFile(Object src, String filePath) {
        try {
            FileWriter fw = new FileWriter(filePath, false);
            objectMapper.writeValue(fw, src);
        } catch (Exception var3) {
            throw new RuntimeException("JsonHelper.SaveJsonFile Error:" + var3.getMessage() + var3.getStackTrace(), var3);
        }
    }

    public static <T> T ReadJsonFile(String filePath, Class<T> type) {
        try {
            T result = objectMapper.readValue(new File(filePath), type);
            return result;
        } catch (Exception var3) {
            throw new RuntimeException("JsonHelper.ReadJsonFile Error:" + var3.getMessage() + var3.getStackTrace(), var3);
        }
    }

    static {
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
