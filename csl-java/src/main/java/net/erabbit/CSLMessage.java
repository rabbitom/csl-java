package net.erabbit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by ziv on 2017/9/6.
 */
public class CSLMessage {


    JSONArray pattern;
    JSONObject defaultField;

    HashMap<String, JSONObject> fields = new HashMap<>();
    HashMap<String, JSONObject> templates = new HashMap();

    public CSLMessage(Object pattern) {

        if (pattern instanceof JSONArray) {

            JSONArray patternTemp = (JSONArray) pattern;
            defaultField = patternTemp.optJSONObject(0);
            for (int i = 0; i < patternTemp.length(); i++) {
                try {
                    addField(patternTemp.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            this.defaultField = (JSONObject) pattern;
            addField(defaultField);
        }
    }

    //初始添加字段
    private void addField(JSONObject field) {

        String id = field.optString("id");
        String template = field.optString("as-template");
        String type = field.optString("type");


        if (!isEmpty(id)) {
            fields.put(id, field);
        }

        if (!isEmpty(template)) {
            templates.put(template, field);
        }

        if ("combination".equals(type) || "array".equals(type)) {
            JSONArray array = null;
            try {
                array = field.getJSONArray("value");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    try {
                        addField(array.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //编码
    byte[] encode(JSONObject object, String fieldId) {
        JSONObject field = defaultField;
        field = fields.get(fieldId);
        //LogUtil.d("csl", "fields.size=" + fields.size());
        //LogUtil.d("csl", "field=" + field);
        if (field != null) {
            return encodeField(object, field);
        }

        return null;
    }

    /**
     * 填充模版
     *
     * @param field
     */
    void inflateFieldTemplate(JSONObject field) {

        String template = field.optString("template");
        //LogUtil.d("csl", "template=" + template);
        if (!isEmpty(template)) {
            JSONObject templateObject = templates.get(template);
            if (templateObject != null) {
                //LogUtil.d("csl", "找到模版==" + template);
                Iterator<String> iterator = templateObject.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (("id" != key) && ("as-template" != key) && isEmpty(field.optString(key))) {
                        try {
                            field.put(key, templateObject.opt(key));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    field.put("template", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    byte[] encodeField(Object object, JSONObject field) {

        inflateFieldTemplate(field);
        LogUtil.d("csl", "template field=" + field);

        Object value = null;

        if (object != null)
            value = (isEmpty(field.optString("name"))) ? object : ((JSONObject) object).opt(field.optString("name"));
        LogUtil.d("csl", "value=" + value);
        LogUtil.d("csl", "type=" + field.optString("type"));
        switch (field.optString("type")) {
            case "fixed":
                //int
                LogUtil.d("csl", "format="+field.optString("format"));
                try {
                    return encodeValue(field.optInt("length"), field.optString("format"), field.optJSONArray("value").getInt(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            case "variable":
                //int

                return encodeValue(field.optInt("length"), field.optString("format"), value);

            case "index":
                //TODO：
                JSONArray valueArray = field.optJSONArray("value");

                for (int i = 0; i < valueArray.length(); i++) {
                    JSONObject item = valueArray.optJSONObject(i);
                    if (item != null && item.optString("value").equals(value + ""))
                        return encode((JSONObject) object, item.optString("id"));
                }
                break;
            case "combination":
                byte[] array = new byte[field.optInt("length")];
                int offset = 0;
                JSONArray valueArrayCombin = field.optJSONArray("value");
                for (int i = 0; i < valueArrayCombin.length(); i++) {
                    JSONObject objectCombin = valueArrayCombin.optJSONObject(i);
                    byte[] iArray = encodeField(value, objectCombin);
                    if (iArray != null) {
                        for (int j = 0; j < iArray.length; j++) {
                            array[offset] = iArray[j];
                            offset++;
                        }
                    } else {
                        LogUtil.d("csl", "value=" + value);
                        LogUtil.d("csl", "objectCombin=" + objectCombin);
                    }
                }
                return array;

            case "array":
                if (!(value instanceof JSONArray)) {
                    LogUtil.d("csl", "====array is not JSONArray");
                    return null;
                }

                byte[] array2 = new byte[field.optInt("length")];
                int offset2 = 0;
                JSONObject itemField = field.optJSONArray("value").optJSONObject(0);
                for (int i = 0; i < ((JSONArray) value).length(); i++) {
                    byte[] itemArray = this.encodeField(((JSONArray) value).optJSONObject(i), itemField);
                    if (offset2 + itemArray.length > field.optInt("length")) return null;
                    if (itemArray != null) {
                        for (int j = 0; j < itemArray.length; j++) {
                            array2[offset2] = itemArray[j];
                            offset2++;
                        }
                    } else {
                        LogUtil.d("csl", "value=" + value);

                        LogUtil.d("csl", "itemField=" + itemField);
                    }
                }
                LogUtil.d("csl", "====" + new String(array2));
                return array2;
        }
        return null;
    }


    byte[] encodeValue(int length, String format, Object value) {
        LogUtil.d("csl", "format=" + format);
        byte[] array = new byte[length];
        switch (format) {
            case "int":
            case "int.le":
                array = writeIntLE(Long.parseLong(value.toString()), array, 0, length);
                break;
            case "int.be":
                array = writeIntBE(Long.parseLong(value.toString()), array, 0, length);
                break;
            case "string":
                if (value == null) return array;
                byte[] arrayFromString = null;
                try {
                    arrayFromString = ((String) value).getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < length; i++) {
                    if (i < arrayFromString.length)
                        array[i] = arrayFromString[i];
                    else
                        array[i] = 0;
                }
                break;
            case "bcd":
                int valueBelowHundred = Integer.parseInt(value.toString()) % 100;
                double valueOfTens = Math.floor(valueBelowHundred / 10);
                int valueOfOnes = valueBelowHundred % 10;
                array[0] = (byte) (valueOfTens * 0x10 + valueOfOnes);
                break;
        }

        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                LogUtil.d("csl", "data.length=" + array.length);
            }
        } else {
            LogUtil.d("csl", "data=null");

        }

        return array;
    }


    JSONObject decode(byte[] buffer, int offset, int length, String fieldId) {
        JSONObject field = this.defaultField;
        if (fieldId != null)
            field = fields.get(fieldId);
        if (field != null) {
            Object result = decodeField(buffer, offset, length, field);
            if (result instanceof JSONObject) {
                return (JSONObject) result;
            } else {
                LogUtil.e("csl", "result is not a instanceof JSONObject");
            }
        } else {
            LogUtil.e("csl", "no field to decode: " + fieldId);
        }
        return null;
    }

    Object decodeField(byte[] buffer, int offset, int length, JSONObject field) {
        inflateFieldTemplate(field);

//        if (length < field.optInt("length"))
//            return null;
        Object result = null;
        LogUtil.d("csl", "type=" + field.optString("type"));
        switch (field.optString("type")) {
            case "index":
            case "fixed":
            case "variable":
                Object value = this.decodeValue(buffer, offset, field.optInt("length"), field.optString("format"));
                LogUtil.d("csl", "value=" + value);

                try {
                    //判断json格式中命令和待解析数据中命令是否相符
                    if (("fixed".equals(field.optString("type")) && !(String.valueOf(value).equals(field.optJSONArray("value").get(0).toString()))))
                        return null;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //类型为index时直接返回值，否则继续向下执行
                if ("index".equals(field.optString("type"))) {
                    JSONArray array = field.optJSONArray("value");
                    if (array != null) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject item = array.optJSONObject(i);
                            if ((value + "").equals(item.optInt("value") + ""))
                                return decode(buffer, offset, length, item.optString("id"));
                        }
                    }
                } else
                    result = value;
                break;
            case "combination":

                JSONObject object = new JSONObject();
                int iOffset = offset;
                JSONArray jsonArray = new JSONArray();
                jsonArray = field.optJSONArray("value");
                if (jsonArray != null) {
                    LogUtil.d("csl", "jsonArray.length()=" + jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject iField = jsonArray.optJSONObject(i);
                        Object iObject = decodeField(buffer, iOffset, iField.optInt("length"), iField);
                        iOffset += iField.optInt("length");
                        if (iObject instanceof JSONObject) {
                            Iterator<String> iterator = ((JSONObject) iObject).keys();
                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                try {
                                    object.put(key, ((JSONObject) iObject).opt(key));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    result = object;
                }
                break;

            case "array": {

                JSONArray array = new JSONArray();
                JSONObject objectField = field.optJSONArray("value").optJSONObject(0);
                int iOffset2 = 0;
                while (iOffset2 < field.optInt("length")) {
                    Object iObject = this.decodeField(buffer, offset + iOffset2, objectField.optInt("length"), objectField);
                    array.put(iObject);
                    iOffset2 += objectField.optInt("length");
                }
                result = array;
                break;
            }
        }

        if (isEmpty(field.optString("name"))) {
            LogUtil.d("csl", "====result=" + result);
            return result;
        } else {
            JSONObject object = new JSONObject();
            try {
                object.put(field.optString("name"), result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LogUtil.d("csl", "====object=" + object);
            return object;
        }
    }

    private Object decodeValue(byte[] buffer, int offset, int length, String format) {
        switch (format) {
            case "int":
            case "int.le":
                return CSLUtility.toIntLE(buffer, offset, length);
            case "int.be":
                return CSLUtility.toIntBE(buffer, offset, length);
            case "string":
                byte[] strByte = new byte[length];
                for (int i = 0; i < length; i++) {
                    strByte[i] = buffer[offset + i];
                }
                return new String(strByte).replace("\u0000", "");
            case "bcd":
                byte bcd = buffer[offset];
                return (bcd >> 4) * 10 + (bcd % 16);
            default:
                //TODO:
                LogUtil.e("csl", "unsupported format");
                //throw new Exception("unsupported format")
        }
        return 0;
    }


    byte[] writeIntLE(long value, byte[] array, int offset, int length) {
        LogUtil.d("csl", "value=" + value);
        for (int i = 0; i < length; i++)
            array[offset + i] = (byte) ((value >> (8 * i)) & 0xff);
        return array;
    }

    byte[] writeIntBE(long value, byte[] array, int offset, int length) {
        LogUtil.d("csl", "value=" + value);
        for (int i = 0; i < length; i++)
            array[offset + (length - 1 - i)] = (byte) ((value >> (8 * i)) & 0xff);
        return array;
    }

    private boolean isEmpty(String str) {
        if (str == null)
            return true;
        if (str.length() == 0)
            return true;

        return false;
    }


    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            StringBuffer sb = new StringBuffer();
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                line++;
                sb.append(tempString);
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        return null;
    }

}
