package com.suragreat.base.util;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @title 		IP地址工具类
 * @description	
 * @author		wangxiao <wangxiao.shawn@snda.com>
 * @version		$Id: IPUtil.java,v 1.2 2010-12-30 上午11:16:09 wangxiao.shawn Exp $
 * @create		2010-12-30 上午11:16:09
 */
public class IpUtil {
    public static void main(String[] args) {
        System.out.println(getLocalAddr());
        System.out.println(isInternalIp(getLocalAddr()));
        System.out.println(isInternalIp("10.255.255.255"));
        System.out.println(isInternalIp("172.31.255.255"));
        System.out.println(isInternalIp("192.168.255.255"));
        System.out.println(isInternalIp("192.167.255.255"));
    }

    // A类 10.0.0.0--10.255.255.255 　　B类 172.16.0.0--172.31.255.255 　　C类 192.168.0.0--192.168.255.255
    private static final String ALLOWABLE_IP_REGEX = "(127[.]0[.]0[.]1)|" + "(localhost)|"
            + "(10[.]\\d{1,3}[.]\\d{1,3}[.]\\d{1,3})|" + "(172[.]((1[6-9])|(2\\d)|(3[01]))[.]\\d{1,3}[.]\\d{1,3})|"
            + "(192[.]168[.]\\d{1,3}[.]\\d{1,3})";
    private static final Pattern  IP_PATTERN = Pattern.compile(ALLOWABLE_IP_REGEX);

    public static boolean isInternalIp(String ip){
        return StringUtils.isNotEmpty(ip) && IP_PATTERN.matcher(ip).find();
    }
    /**
     * 获取本地IP地址 
     * @return
     */
    public static String getLocalAddr() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface interfacei = interfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = interfacei.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress addr = inetAddresses.nextElement();
                    if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                        String host = addr.getHostAddress();
                        return host;
                    }
                }
            }
        } catch (SocketException e) {
        }
        return "";
    }

    /**
     * 把IPV4的ip地址转换为数字格式:
     * a.b.c.d=>a*256*256*256+b*256*256+c*256+d
     * @param ip
     * @return
     */
    public static int formatIp2Int(String ip) {
        String[] numbers = ip.split("\\.");
        if (numbers.length != 4) {
            return 0;
        } else {
            try {
                return 256 * 256 * 256 * Integer.valueOf(numbers[0]) + 256 * 256 * Integer.valueOf(numbers[1])
                        + 256 * Integer.valueOf(numbers[2]) + Integer.valueOf(numbers[3]);
            } catch (NumberFormatException nfe) {
                return 0;
            }
        }
    }

    /**
     * 获取ip地址
     * 
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 解决用户通过了多级反向代理访问时, ip为多个的问题
        String[] ips = ip.split(",");
        if (ips.length > 1) {
            ip = ips[0];
        }
        return ip;
    }

    /**
     * 获取本机所有IP
     * 
     * @return
     * @throws SocketException
     */
    public static List<String> getServerIps() throws SocketException {
        List<String> ips = new ArrayList<String>();
        // 根据网卡取本机配置的IP
        Enumeration<?> e1 = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
        while (e1.hasMoreElements()) {
            NetworkInterface ni = (NetworkInterface) e1.nextElement();
            Enumeration<?> e2 = ni.getInetAddresses();
            while (e2.hasMoreElements()) {
                InetAddress ia = (InetAddress) e2.nextElement();
                if (ia instanceof Inet6Address)
                    continue;
                String ip = ia.getHostAddress();
                ips.add(ip);
            }
        }
        return ips;
    }

    /**
     * 获取本机除127.0.0.1外的所有IP
     * 
     * @return
     * @throws SocketException
     */
    public static List<String> getServerIpsExceptLocalHost() throws SocketException {
        List<String> allIps = getServerIps();
        List<String> ips = new ArrayList<String>();
        for (String ip : allIps) {
            if (!StringUtils.equalsIgnoreCase(ip, "127.0.0.1")) {
                ips.add(ip);
            }
        }
        return ips;
    }

    /**
     * 是否是本机IP
     * 
     * @param compareIp
     * @return
     * @throws SocketException
     */
    public static boolean isThisServer(String compareIp) throws SocketException {
        List<String> ips = IpUtil.getServerIps();
        for (String ip : ips) {
            if (StringUtils.equalsIgnoreCase(ip, compareIp))
                return true;
        }
        return false;
    }

    /**
     * 得到客户端真实IP地址。若存在反向代理header：http_x_forwarded_for，则取其值，否则，取header:
     * remote_addr
     * 
     * @return
     */
    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase(ip, "unknown")) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    /**
     * 返回服务器IP字符串
     * 
     * @return
     * @throws SocketException
     */
    public static String getServerIPsStr() throws SocketException {
        StringBuilder sb = new StringBuilder();
        List<String> ips = IpUtil.getServerIpsExceptLocalHost();
        for (String ip : ips) {
            sb.append(ip);
            sb.append("   ");
        }
        return sb.toString();
    }

    public static String getServerHostAndIPsStr() throws SocketException, UnknownHostException {
        String hostName = InetAddress.getLocalHost().getHostName();
        StringBuilder sb = new StringBuilder();
        sb.append(hostName + ": ");
        List<String> ips = getServerIps();
        for (String ip : ips) {
            sb.append(ip);
            sb.append(";");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
