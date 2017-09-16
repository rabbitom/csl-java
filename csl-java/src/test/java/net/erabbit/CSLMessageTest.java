package net.erabbit;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by ziv on 2017/9/16.
 */

public class CSLMessageTest {

    @org.junit.Test
    public void fixed() throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "test");
        jsonObject.put("name", "command");
        jsonObject.put("length", 1);
        jsonObject.put("type", "fixed");
        jsonObject.put("format", "int");
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(1);
        jsonObject.put("value", jsonArray);

        CSLMessage csl = new CSLMessage(jsonObject);

        byte[] a = csl.encode(null, "test");
        assertEquals(a.length, 1);
        assertEquals(a[0], 1);
    }

    @org.junit.Test
    public void textEncode() throws Exception {

        JSONArray jsonArray = new JSONArray(JSON.str);
        CSLMessage csl = new CSLMessage(jsonArray);
        byte[] data = csl.encode(null, "battery-get");
        byte[] data1 = csl.encode(null, "item-name-get");
        byte[] data2 = csl.encode(null, "user-name-get");
        byte[] data3 = csl.encode(null, "config-get");
        byte[] data4 = csl.encode(null, "cur-temperature-get");
        byte[] data5 = csl.encode(null, "temperature-record-get");
        byte[] data6 = csl.encode(null, "update-time-get");
        byte[] data7 = csl.encode(null, "clean-temperature-records");
        byte[] data8 = csl.encode(null, "reset-device");
        assertEquals(data[0], 1);
        assertEquals(data1[0], 2);
        assertEquals(data2[0], 3);
        assertEquals(data3[0], 4);
        assertEquals(data4[0], 5);
        assertEquals(data5[0], 6);
        assertEquals(data6[0], 7);
        assertEquals(data7[0], 8);
        assertEquals(data8[0], 9);

        JSONObject itemData = new JSONObject();
        itemData.put("name", "哈喽");
        byte[] data9 = csl.encode(itemData, "item-name-set");
        assertArrayEquals(data9, new byte[]{0x02, (byte) 0xE5, (byte) 0x93, (byte) 0x88, (byte) 0xE5, (byte) 0x96, (byte) 0xBD, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});

        JSONObject userData = new JSONObject();
        userData.put("username", "adjoajd");
        byte[] data10 = csl.encode(userData, "user-name-set");
        assertArrayEquals(data10, new byte[]{0x03, 0x61, 0x64, 0x6A, 0x6F, 0x61, 0x6A, 0x64, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});

        //object = {"name":"哈喽"}; fieldId = item-name-set; array = 02 E5 93 88 E5 96 BD 00 00 00 00 00 00 00 00 00
        //object = {"username":"adjoajd"}; fieldId = user-name-set; array = 03 61 64 6A 6F 61 6A 64 00 00 00 00 00 00 00 00


        JSONObject configData = new JSONObject();
        //生产日期
        JSONObject pData = new JSONObject();
        Calendar pCal = Calendar.getInstance();
        pCal.setTime(new Date());
        int pYear = pCal.get(Calendar.YEAR) % 100;
        int pMonth = pCal.get(Calendar.MONTH) + 1;
        int pDay = pCal.get(Calendar.DAY_OF_MONTH);
        pData.put("year", pYear);
        pData.put("month", pMonth);
        pData.put("day", pDay);
        configData.put("production-date", pData);

        //过期日期
        JSONObject eData = new JSONObject();
        Calendar eCal = Calendar.getInstance();
        eCal.setTime(new Date(new Date().getTime() + 3600 * 24 * 1000));
        int eYear = pCal.get(Calendar.YEAR) % 100;
        int eMonth = pCal.get(Calendar.MONTH) + 1;
        int eDay = pCal.get(Calendar.DAY_OF_MONTH);
        eData.put("year", eYear);
        eData.put("month", eMonth);
        eData.put("day", eDay);

        configData.put("expiry-date", eData);
        //TODO:开始检测时间
        //configData.put("start-time", new Date().getTime() / 1000);
        configData.put("start-time", 0);
        //默认检查间隔
        configData.put("default-measure-interval", 1 + 1);

        JSONArray jsonArrayTimes = new JSONArray();
        //设置测量时间间隔
        for (int i = 0; i < 3; i++) {
            JSONObject cooldown = new JSONObject();
            cooldown.put("time", 1);
            cooldown.put("interval", 1 + 1);
            jsonArrayTimes.put(cooldown);
        }
        configData.put("cool-down-timers", jsonArrayTimes);


        //温度单位0-C，1-F
        configData.put("unit", 1);
        byte[] data11 = csl.encode(configData, "config-set");
        assertArrayEquals(data11, new byte[]{0x04, toBCD(pYear), toBCD(pMonth), toBCD(pDay), toBCD(eYear), toBCD(eMonth), toBCD(eDay), 0x00, 0x00, 0x00, 0x00, 0x02, 0x01, 0x02, 0x01, 0x02, 0x01, 0x02, 0x01});


        JSONObject updateTime = new JSONObject();
        updateTime.put("time", new Date().getTime() / 1000);
        byte[] data12 = csl.encode(updateTime, "update-time-set");
        assertEquals(data12.length, 5);
        assertEquals(data12[0], 7);

    }


    @org.junit.Test
    public void textDecode() throws Exception {

        JSONArray jsonArray = new JSONArray(JSON.str);
        CSLMessage csl = new CSLMessage(jsonArray);

        //配置设置信息
        //{"command":4,"production-date":{"year":17,"month":9,"day":16},"expiry-date":{"year":17,"month":9,"day":16},"start-time":1505528689,"default-measure-interval":2,"cool-down-timers":[{"time":1,"interval":2},{"time":1,"interval":2},{"time":1,"interval":2}],"unit":1}
        Calendar pCal = Calendar.getInstance();
        pCal.setTime(new Date());
        int pYear = pCal.get(Calendar.YEAR)%100;
        int pMonth = pCal.get(Calendar.MONTH)+1;
        int pDay = pCal.get(Calendar.DAY_OF_MONTH);

        Calendar eCal = Calendar.getInstance();
        eCal.setTime(new Date(new Date().getTime()+3600*24*1000));
        int eYear = eCal.get(Calendar.YEAR)%100;
        int eMonth = eCal.get(Calendar.MONTH)+1;
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);


        byte[] data1 = new byte[]{0x04, toBCD(pYear), toBCD(pMonth), toBCD(pDay), toBCD(eYear), toBCD(eMonth), toBCD(eDay), 0x00, 0x00, 0x00, 0x00, 0x02, 0x01, 0x02, 0x01, 0x02, 0x01, 0x02, 0x01};
        JSONObject object1 = csl.decode(data1, 0, data1.length, "csl-data");
        assertEquals(object1.get("command"),4);
        assertEquals(object1.optJSONObject("production-date").get("year"),pYear);
        assertEquals(object1.optJSONObject("production-date").get("month"),pMonth);
        assertEquals(object1.optJSONObject("production-date").get("day"),pDay);
        assertEquals(object1.optJSONObject("expiry-date").get("year"),eYear);
        assertEquals(object1.optJSONObject("expiry-date").get("month"),eMonth);
        assertEquals(object1.optJSONObject("expiry-date").get("day"),eDay);
        assertEquals(object1.get("start-time"),0);
        assertEquals(object1.get("default-measure-interval"),2);
        assertEquals(object1.getJSONArray("cool-down-timers").length(),3);
        assertEquals(object1.getJSONArray("cool-down-timers").getJSONObject(0).get("time"),1);
        assertEquals(object1.getJSONArray("cool-down-timers").getJSONObject(0).get("interval"),2);
        assertEquals(object1.getJSONArray("cool-down-timers").getJSONObject(1).get("time"),1);
        assertEquals(object1.getJSONArray("cool-down-timers").getJSONObject(1).get("interval"),2);
        assertEquals(object1.getJSONArray("cool-down-timers").getJSONObject(2).get("time"),1);
        assertEquals(object1.getJSONArray("cool-down-timers").getJSONObject(2).get("interval"),2);
        assertEquals(object1.get("unit"),1);

        //设置名字{"command":2,"name":"哈喽"}
        byte[] data2 = new byte[]{0x02, (byte) 0xE5, (byte) 0x93, (byte) 0x88, (byte) 0xE5, (byte) 0x96, (byte) 0xBD, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        JSONObject object2 = csl.decode(data2, 0, data2.length, "csl-data");
        assertEquals(object2.get("command"),2);

        //设置用户名{"command":3,"username":"adjoajd"}
        byte[] data3 = new byte[]{0x03, 0x61, 0x64, 0x6A, 0x6F, 0x61, 0x6A, 0x64, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        JSONObject object3 = csl.decode(data3, 0, data3.length, "csl-data");
        assertEquals(object3.get("command"),3);


        //更新时间{"time":0}
        byte[] data4 = new byte[]{0x07,0x00,0x00,0x00,0x00};
        JSONObject object4 = csl.decode(data4, 0, data4.length, "csl-data");
        assertEquals(object4.get("command"),7);
        assertEquals(object4.get("time"),0);

    }


    private byte toBCD(int value){
        int valueBelowHundred = value % 100;
        double valueOfTens = Math.floor(valueBelowHundred / 10);
        int valueOfOnes = valueBelowHundred % 10;
        byte result = (byte) (valueOfTens * 0x10 + valueOfOnes);
        return result;
    }

}
