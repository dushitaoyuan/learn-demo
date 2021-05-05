package com.taoyuanx.thrift.core.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class IpUtil {

    /**
     * @param netPrefix
     * @return
     * @throws Exception
     */
    public static String getNetAddress(String netPrefix)  {
        try {
            if (netPrefix == null) {
                return getNetAddress();
            }
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                    .getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface network = networkInterfaces.nextElement();
                Enumeration<InetAddress> net = network.getInetAddresses();
                while (net.hasMoreElements()) {
                    InetAddress addr = net.nextElement();
                    if (addr.isSiteLocalAddress() && addr.getHostAddress().startsWith(netPrefix)) {
                        return addr.getHostAddress();
                    }
                }
            }
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * @return
     * @throws Exception
     */
    public static String getNetAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                    .getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface network = networkInterfaces.nextElement();
                Enumeration<InetAddress> net = network.getInetAddresses();
                while (net.hasMoreElements()) {
                    InetAddress addr = net.nextElement();
                    if (addr.isSiteLocalAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
            return InetAddress.getLocalHost().getHostAddress();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
