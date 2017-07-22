package net.erabbit;

import sun.applet.Main;

public class MyClass {

    public static void main(String []args) {
        System.out.println(CSLUtility.toIntLE(new byte[]{1,2,3},0,3));
        System.out.println(CSLUtility.toIntBE(new byte[]{1,2,3},0,3));
    }
}
