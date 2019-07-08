package com.dingmk.comm.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class IOUtils {
    
    private IOUtils() {
    }

	public static void close(Closeable... closeses) {
		if (closeses != null) {
			for (Closeable close : closeses) {
				close(close);
			}
		}
	}

	public static void close(Closeable close) {
		try {
			if (close != null) {
				close.close();
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	/** 快 */
	public static void bytesToFile(byte[] bytes, String filename) {
	    bytesToFile(bytes, 0, bytes.length, filename);
	}

	/** 快 */
    public static void bytesToFile(byte[] bytes, int off, int len, String filename) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(filename, "rw");
            raf.write(bytes, off, len);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            close(raf);
        }
    }

}
