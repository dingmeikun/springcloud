/******************************************************************************* 
 * Copyright (C) 2012-2015 Microfountain Technology, Inc. All Rights Reserved. 
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.   
 * Proprietary and confidential
 * 
 * Last Modified: 2016-11-9 17:33:53
 ******************************************************************************/

package com.dingmk.comm.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhonenumValidator {

    static String pnRegex = "(?<!\\d)1[34578](?:\\d{9}|\\d[\\- ,，./_]\\d{4}[\\- ,，./_]\\d{4}|\\d\\d[\\- ,，./_](?:\\d{4}[\\- ,，./_]\\d{3}|\\d{3}[\\- ,，./_]\\d{4}))(?!\\d)";

    static Pattern pnPa = Pattern.compile(pnRegex);

    static Pattern numPa = Pattern.compile("^\\d+$");

    public static boolean isMobilePhone(String phonenum) {
        Matcher m = pnPa.matcher(phonenum);
        return m.find();
    }

    public static boolean isTelPhone(String phonenum) {
        phonenum = getPhoneNumberNo86(phonenum);
        Matcher m = FIXED_PHONE.matcher(phonenum);
        return m.find();
    }

    public static String toEnterprise(String phonenum) {
        if (isIllegalPhone(phonenum)) {
            return null;
        }
        if (isMobilePhone(phonenum)) {
            return null;
        }
        return getPhoneNumberNo86(phonenum);
    }

    public static boolean isIllegalPhone(String phonenum) {
        if (isNull(phonenum)) {
            return true;
        }
        if (isMobilePhone(phonenum)) {
            return false;
        }
        phonenum = getPhoneNumberNo86(phonenum);
        if (numPa.matcher(phonenum).find()) {
            return false;
        }
        return true;
    }

    /***
     * 验证号码有效性
     * @param pn
     *            源号码(不得为null)
     * @param p
     *            规则号码(不得为null)
     * @param len
     *            固定长
     * @param maxLen
     *            最大长
     * @param minLen
     *            最小长
     * @param pt
     *            类型
     * @return
     */
    public static boolean validPhone(String pn, String p, int len, int maxLen, int minLen, String pt) {

        int idx = p.indexOf("*");
        if (idx > -1) {
            String s1 = p.substring(0, idx);
            String s2 = p.substring(idx + 1);
            return validPhone(pn, s1, s2, len, maxLen, minLen, pt);
        } else {
            return pn.equals(p);
        }

    }

    /***
     * 验证号码有效性
     * @param pn
     *            源号码(不得为null)
     * @param s1
     *            规则前缀(不得为null)
     * @param s2
     *            规则后缀(不得为null)
     * @param len
     *            固定长
     * @param maxLen
     *            最大长
     * @param minLen
     *            最小长
     * @param pt
     *            类型
     * @return
     */
    public static boolean validPhone(String pn, String s1, String s2, int len, int maxLen, int minLen, String pt) {

        if (s1 == null) {
            s1 = "";
        }
        if (s2 == null) {
            s2 = "";
        }

        if (!pn.startsWith(s1) || !pn.endsWith(s2)) {
            return false;
        }
        int plen = s1.length() + s2.length();
        int nlen = pn.length();
        if (len > 0) {
            if (plen + len != nlen) {
                return false;
            }
        } else {
            if (maxLen > 0 && (nlen > plen + maxLen)) {
                return false;
            }
            if (minLen > 0 && (nlen < plen + minLen)) {
                return false;
            }
        }
        if ("sj".equals(pt)) {
            if (!isMobilePhone(pn.substring(s1.length(), nlen - s2.length()))) {
                return false;
            }
        }
        // } else {
        // return false;
        // }
        return true;

    }

    static Pattern phonePa1 = Pattern.compile("[ \\-\\(\\)\\+\\*]+");

    static Pattern phonePa2 = Pattern
            .compile("^(?:00)?86|^17951(?=\\d{6,})|^12593(?=\\d{6,})|^12520(?=\\d{6,})|^179110?|^10193|^1790[89]|^12583[123](?=\\d{3,})|^12583(?=[04-9]\\d{2,})");

    /**
     * 是否是固话正则
     */
    final static Pattern FIXED_PHONE = Pattern.compile("(?<![\\-0-9])0\\d{2,3}-?\\d{7,8}(?!\\d)");

    public static String getPhoneNumberNo86(String phoneNumber) {
        if (!isNull(phoneNumber)) {
            phoneNumber = phonePa1.matcher(phoneNumber).replaceAll("");
            phoneNumber = phonePa2.matcher(phoneNumber).replaceAll("");
        }
        return phoneNumber;
    }

    static String[] allArr = {"02512329", "02796568", "095188", "095583", "101555", "101560", "101901", "10198", "118010", "1180308", "11803080",
            "11803099", "11806", "118067", "118070", "1180711430", "118100", "118166", "118170", "118200", "1183119", "118388", "1186666", "11868",
            "11888", "12122", "12306", "12520", "12520010", "12520021", "12520024", "12520027", "12520028", "12520029", "1252003300000",
            "1252004411", "12582", "1258230", "13800138000", "195599", "196588", "55988195599", "85388895566", "88895566", "895566", "07561211019",
            "053287003810", "4007883333", "02080774049", "07562233371", "07568122629", "10101111", "10150", "10155", "10690607500513", "110",
            "118168", "12114", "121234404", "12312", "12315", "12316", "12320", "12321", "12328", "12329", "12333", "12345", "12355", "12360",
            "12365", "12366", "12520098563", "18918910000", "118064260", "12583", "00079961818", "00096181801", "01080100001", "01082875090",
            "01082875093", "025110205", "0251102183", "0251102184", "02511054321", "02583772304", "02583772310", "02583772312", "02583772314",
            "02583772320", "02583772321", "02583772323", "02583772324", "02583772325", "02583772326", "02583772327", "02583772328", "02583772329",
            "02583772330", "02583772340", "02583772341", "02583772345", "02583772346", "02583772347", "02583772348", "02583772350", "02583772351",
            "02583772353", "02583772357", "02583772359", "02796512", "051010001", "051011002", "051011009", "051212329", "05141101101",
            "05171100900005", "05171100900110", "05707025769", "0571118114", "057187231360", "057585119110", "057686208077", "05785128922",
            "05786827403", "05922081110", "05922110110", "05925313388", "0592588130300", "0592606660000", "0595221069620", "0595225740090",
            "05963221085", "05968310399", "075511010", "075582784509", "075612110019", "079110000", "089812301", "118062288", "121231100",
            "121231300", "121231401", "121231402", "121231403", "121231404", "121231405", "121231406", "121231407", "121231408", "121231410",
            "121231411", "1212323", "121232301", "121232306", "121233100", "121233201", "121233202", "121233204", "121233205", "1212332053205",
            "121233206", "121233211", "121233213", "121233302", "121233304", "1212335", "121233500", "121233601", "121233604", "121233607",
            "121233608", "121233609", "121233610", "121233611", "121233700", "121233701", "121233702", "121233713", "12123410041", "121234101",
            "121234102", "121234103", "121234107", "121234108", "121234111", "121234112", "121234113", "121234114", "121234115", "121234117",
            "121234141", "1212344", "121234401", "121234402", "121234405", "121234406", "121234407", "121234408", "121234409", "121234412",
            "121234413", "121234414", "121234416", "121234417", "121234418", "121234420", "121234451", "121234452", "121234453", "121234454",
            "121234455", "121235000", "1212351", "121235101", "121235201", "121236102", "121236103", "121236105", "121236109", "121236401",
            "1231536001", "12351", "12368", "12370", "12371", "1237105", "123712301", "1252009107", "12520098563", "4001002003", "4006129999",
            "4008203333", "4008270270", "87771001", "98000961818", "059527142000", "1252004411", "12520098563", "16571253953120"};

    static String[] startArr = { "106", "95", "96", "100", "10178", "11183", "11185", "116114", "11807115", "11807314", "118114", "12110", "12366",
            "12580", "85264506598", "85264504390023", "1188816", "118065", "12123" };

    static Pattern PAT_ETPNUM = Pattern.compile("(?:[48]00\\d{7})");

    public static boolean isEnterprise(String phoneNumber) {
        return isEnterprise(phoneNumber, null);
    }

    public static boolean isEnterprise(String phoneNumber, Map<String, String> extend) {
        if (isNull(phoneNumber)) {
            return false;
        }
        int len = allArr.length;
        phoneNumber = getPhoneNumberNo86(phoneNumber);
        for (int i = 0; i < len; i++) {
            if (allArr[i].equals(phoneNumber)) {
                return true;
            }
        }
        len = startArr.length;
        for (int i = 0; i < len; i++) {
            if (phoneNumber.startsWith(startArr[i])) {
                return true;
            }
        }

        if (phoneNumber.startsWith("1252003") && phoneNumber.length() <= 9) {
            // 开头总长度不超过10
            return true;
        }
        if (phoneNumber.startsWith("12520036") && phoneNumber.length() == 19) {
            // 开头且后面是手机号
            return isMobilePhone(phoneNumber.replace("12520036", ""));
        }

        if (PAT_ETPNUM.matcher(phoneNumber).matches()) {
            return true;
        }

        if (null != extend && extend.containsKey("FIXED_PHONE") && !"false".equals(extend.get("FIXED_PHONE"))) {
            try {
                // 是否是固定电话
                return isTelPhone(phoneNumber);
            } catch (Throwable e) {

            }
        }
        return false;
    }

    public static boolean isNull(String s) {
        return s == null || (s = s.trim()).length() == 0 || "null".equalsIgnoreCase(s);
    }

    public static void main(String[] args) {
        long st = System.currentTimeMillis();
        boolean b = false;
        b = validPhone("9555013267966666", "95550", "", 0, 0, 0, "sj");
        System.out.println(b);
        for (int i = 0; i < 10000; i++) {
            b = validPhone("9555013267966666", "95550", "", 0, 0, 0, "sj");
        }
        System.out.println(System.currentTimeMillis() - st);
        System.out.println(b);
        st = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            b = validPhone("9555013267966666", "95550*", 0, 0, 0, "sj");
        }
        System.out.println(System.currentTimeMillis() - st);
        System.out.println(b);
        // System.out.println(getPhoneNumberNo86("8613010451500"));
        System.out.println(isEnterprise("118100"));
    }
}
