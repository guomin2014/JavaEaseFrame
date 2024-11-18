package com.gm.javaeaseframe.common.util;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

public class SnowflakeGenerator {

    private static final Snowflake SNOWFLAKE = new Snowflake(getWorkId(), getDataCenterId());

    /**
     * 生成下一个ID
     * @return 下一个ID
     */
    public static Long nextId() {
        return SNOWFLAKE.nextId();
    }



    /**
     * workId使用IP生成
     * @return workId
     */
    private static Long getWorkId() {
        try {
            String hostAddress = NetUtil.getLocalHostIP();
            int[] ints = StringUtils.toCodePoints(hostAddress);
            int sums = 0;
            for (int b : ints) {
                sums = sums + b;
            }
            return (long) (sums % 32);
        }
        catch (Exception e) {
            // 失败就随机
            return RandomUtils.nextLong(0, 31);
        }
    }


    /**
     * dataCenterId使用hostName生成
     * @return dataCenterId
     */
    private static Long getDataCenterId() {
        try {
            String hostName = SystemUtils.getHostName();
            int[] ints = StringUtils.toCodePoints(hostName);
            int sums = 0;
            for (int i: ints) {
                sums = sums + i;
            }
            return (long) (sums % 32);
        }
        catch (Exception e) {
            // 失败就随机
            return RandomUtils.nextLong(0, 31);
        }
    }


    public static void main(String[] args) {
        Long workId = getWorkId();
        System.out.println("workId = " + workId);

        Long dataCenterId = getDataCenterId();
        System.out.println("dataCenterId = " + dataCenterId);

        String networkName = NetUtil.getNetworkName();
        System.out.println("networkName = " + networkName);

    }


}
