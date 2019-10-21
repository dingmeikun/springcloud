package com.dingmk.gateway.route.utils;

import java.io.Closeable;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IOUtil {

    public static void closeQuietly(final Closeable... closeables) {
        if (closeables != null && closeables.length > 0) {
            for (Closeable closeable : closeables) {
                closeQuietly(closeable);
            }
        }
    }

    public static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            // ignore exception
            log.warn("!!!closeable close error!!!");
        }
    }

}
