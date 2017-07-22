package net.erabbit;

import java.util.ArrayList;

/**
 * Created by ziv on 2017/7/21.
 */

public class CSLUtility {

    /**
     * 低位在前
     *
     * @param bytes  字节数组
     * @param offset 开始位置
     * @param len    偏移长度
     * @return
     */
    public static int toIntLE(byte[] bytes, int offset, int len) {
        int value = 0;
        for (int i = len - 1; i >= 0; i--) {//低位在前
            value = value << 8;
            value = value | toInt(bytes[offset + i]);
        }
        return value;
    }

    /**
     * 低位在前
     * @param bytes  字节数组
     * @return int
     */
    public static int toIntLE(byte[] bytes) {
        int value = 0;
        for (int i = bytes.length - 1; i >= 0; i--) {//低位在前
            value = value << 8;
            value = value | toInt(bytes[i]);
        }
        return value;
    }



    /**
     * 高位在前
     *
     * @param bytes  字节数组
     * @param offset 开始位置
     * @param len    偏移长度
     * @return
     */
    public static int toIntBE(byte[] bytes, int offset, int len) {
        int value = 0;
        for (int i = 0; i < len; i++) {
            value = value << 8;//高位在前
            value = value | toInt(bytes[offset + i]);
        }
        return value;
    }

    /**
     * 高位在前
     * @param bytes  字节数组
     * @return
     */
    public static int toIntBE(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < bytes.length; i++) {
            value = value << 8;//高位在前
            value = value | toInt(bytes[i]);
        }
        return value;
    }

    /**
     * byte[]转十六进制String
     *
     * @param src  字节数组
     * @param glue 返回的十六进制String的分隔符 如 "0A-0A-0A"
     * @return String
     */
    public static String toHexString(byte[] src, char glue) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }

        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v).toUpperCase();
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            if (i < src.length - 1) {
                stringBuilder.append(hv).append(glue);
            } else {
                stringBuilder.append(hv);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * byte[]转十六进制String
     *
     * @param src    字节数组
     * @param offset 开始位置
     * @param len    偏移长度
     * @param glue   返回的十六进制String的分隔符 如 "0A-0A-0A"
     * @return
     */
    public static String toHexString(byte[] src, int offset, int len, char glue) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        if (len <= 0 || offset < 0 || len + offset > src.length) {
            return null;
        }

        for (int i = offset; i < len + offset; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v).toUpperCase();
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            if (i < src.length - 1) {
                stringBuilder.append(hv).append(glue);
            } else {
                stringBuilder.append(hv);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * byte[]转十六进制String
     *
     * @param src 字节数组
     * @return
     */
    public static String toHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 十六进制转String byte[]
     *
     * @param str 需要转换的String
     * @return
     */
    public static byte[] fromHexString(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return new byte[0];
        }
        byte[] byteArray = null;
        if (str.length() / 2 == 0) {
            byteArray = new byte[str.length() / 2];
            for (int i = 0; i < byteArray.length; i++) {
                String subStr = str.substring(2 * i, 2 * i + 2);
                byteArray[i] = ((byte) Integer.parseInt(subStr, 16));
            }
        } else {
            byteArray = new byte[str.length() / 2 + 1];
            for (int i = 0; i < byteArray.length; i++) {
                if (i < byteArray.length - 1) {
                    String subStr = str.substring(2 * i, 2 * i + 2);
                    byteArray[i] = ((byte) Integer.parseInt(subStr, 16));
                } else {
                    String subStr = str.substring(2 * i, 2 * i + 1);
                    byteArray[i] = ((byte) Integer.parseInt(subStr, 16));
                }
            }
        }
        return byteArray;
    }

    /**
     * 十六进制转String byte[]
     *
     * @param str  需要转换的String
     * @param glue 需要转换的String中包含的分隔符，如"0F-0F-0F" 则glue="-"
     * @return byte[]
     */
    public static byte[] fromHexString(String str, String glue) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return new byte[0];
        }

        String[] s = str.split(glue);

        byte[] byteArray = new byte[str.length() / 2];
        for (int i = 0; i < s.length; i++) {
            String subStr = s[i];
            byteArray[i] = ((byte) Integer.parseInt(subStr, 16));
        }
        return byteArray;
    }


    private static byte[] fromHexString2(String hexStr) {
        ArrayList<Integer> ints = new ArrayList<Integer>();
        char[] hexChars = hexStr.toCharArray();
        int hValue = -1;
        for (int i = 0; i < hexChars.length; i++) {
            int value = fromHexChar(hexChars[i]);
            if (hValue == -1)
                hValue = value;
            else if (value != -1) {
                ints.add(hValue * 16 + value);
                hValue = -1;
            }
        }
        int intCount = ints.size();
        if (intCount > 0) {
            byte[] bytes = new byte[intCount];
            for (int i = 0; i < intCount; i++)
                bytes[i] = toByte(ints.get(i));
            return bytes;
        } else
            return null;
    }


    private static byte toByte(int x) {
        return (byte) (x & 0x000000ff);
    }

    private static int toInt(byte b) {
        return 0x000000ff & b;
    }

    private static int fromHexChar(char ch) {
        if ((ch >= '0') && (ch <= '9'))
            return ch - '0';
        else if ((ch >= 'a') && (ch <= 'f'))
            return ch - 'a' + 10;
        else if ((ch >= 'A') && (ch <= 'F'))
            return ch - 'A' + 10;
        else
            return -1;
    }


    private static String bytesToHexStringReversal(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = src.length - 1; i >= 0; i--) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
