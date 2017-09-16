package net.erabbit;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by ziv on 2017/7/21.
 */
public class CSLUtilityTest {
    @org.junit.Test
    public void toIntLE() throws Exception {
        assertEquals(0x030201, CSLUtility.toIntLE(new byte[]{1, 2, 3}, 0, 3));
        assertEquals(197121, CSLUtility.toIntLE(new byte[]{1, 2, 3}, 0, 3));
        assertEquals(0x030201, CSLUtility.toIntLE(new byte[]{1, 2, 3}));
    }

    @org.junit.Test
    public void toIntBE() throws Exception {
        assertEquals(0x010203, CSLUtility.toIntBE(new byte[]{1, 2, 3}, 0, 3));
        assertEquals(0x010203, CSLUtility.toIntBE(new byte[]{1, 2, 3}));
    }

    @org.junit.Test
    public void toHexString() throws Exception {
        assertEquals("01-0A-FF", CSLUtility.toHexString(new byte[]{1, 10, (byte) 255}, 0, 3, '-'));
    }

    @org.junit.Test
    public void fromHexString() throws Exception {
        assertArrayEquals(new byte[]{0x12, 0x0A, (byte) 0xFF}, CSLUtility.fromHexString("120AFF"));
        assertArrayEquals(new byte[]{0x12, 0x00, (byte) 0xAF}, CSLUtility.fromHexString("12-0-AF", "-"));
        assertArrayEquals(new byte[]{0x12, 0x0A, (byte) 0x0F}, CSLUtility.fromHexString("120AF"));
    }


}