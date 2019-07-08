package com.dingmk.comm.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileUtil {
    
    public static void saveToFile(List<String> list, String filePath, String prefix) {
        String fileName = filePath + prefix + ".txt";
        Path path = Paths.get(fileName);
        BufferedWriter bufferedWriter = null;

        try {
            bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND, StandardOpenOption.CREATE);

            int size = list.size();
            for (int i = 0; i < size; i++) {
                bufferedWriter.write(list.get(i));
                bufferedWriter.write(System.lineSeparator());
            }

            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (null != bufferedWriter) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    
    public static Set<String> readFromFile(String fileName) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(fileName);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            List<String> list = new ArrayList<>();
            String line = br.readLine();
            while (line != null) {
                list.add(line);
                line = br.readLine();
            }
            Set<String> set = new HashSet<>();
            for (String item : list) {
                if (null != item && !(item = item.trim()).isEmpty()) {
                    set.add(item);
                }
            }
            return set;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != isr) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static Map<String, String> convertToMap(Set<String> set) {
        Map<String, String> map = new HashMap<>();
        if (null != set && !set.isEmpty()) {
            for (String str : set) {
                if (null != str && !(str = str.trim()).isEmpty()) {
                    String[] array = str.split("=");
                    if (null != array && array.length == 2) {
                        String key = array[0];
                        String value = array[1];
                        if (null != key && !(key = key.trim()).isEmpty() && null != value
                                && !(value = value.trim()).isEmpty()) {
                            map.put(key, value);
                        }
                    }
                }
            }
        }
        return map;
    }

}
