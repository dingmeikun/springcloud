package com.dingmk.comm.utils;

import com.dingmk.comm.constvar.ResultConstVar;
import com.dingmk.comm.type.BasicException;

/**
 * 针对项目改进如下：
 * 0 ---00000 00000 00000 0000 ---000000000000
 * 第一位为未使用（实际上也可作为int的符号位），接下来的19位为毫米级的时间戳（8分钟），然后12位该毫秒内的当前毫秒内的计数（4096个），加起来刚好32位，为一个int型。
 *
 * @author lizhiming on 2015/7/15.
 */
public final class GenMsgIDUtil {
    private static final long timestampMask = -1L ^ -1L << 19;
    private static final long sequenceMask = -1L ^ -1L << 12;
    private static final short timestampLeftShift = 12;

    private static long lastTimestamp = -1L;
    private static long sequence = 0L;                         // 12位表示

    public synchronized static long nextId() {
        long timestamp = timeGen();

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        if (timestamp < lastTimestamp) {
            throw new BasicException(ResultConstVar.LOGIC_ERROR, String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                    lastTimestamp - timestamp));
        }

        lastTimestamp = timestamp;

        long nextId = (((timestamp & timestampMask) << timestampLeftShift)) | (sequence);

        return nextId;
    }

    private static long tilNextMillis(final long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private static long timeGen() {
        return System.currentTimeMillis();
    }
}
