package com.gm.javaeaseframe.common.util;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(NetUtil.class);
    private static final String LOCALHOST_IP = "127.0.0.1";
    private static final String EMPTY_IP = "0.0.0.0";
    private static final List<String> FILTER_IP = new ArrayList<>();
    private static final List<String> FILTER_IP_RANGE = new ArrayList<>();
    private static final Pattern IP_PATTERN = Pattern.compile("[0-9]{1,3}(\\.[0-9]{1,3}){3,}");
    public static final String Network_Name = getNetworkName();
    public static final String ADVERTISE_IP_ENV_NAME = "instance.ip";
    private static String hostIP;
    private static final String network_name_bond0 = "bond0";
    private static final String network_name_eth0 = "eth0";
    private static final String network_name_eth1 = "eth1";
    private static final String network_name_eth2 = "eth2";
    
    static {
//        FILTER_IP_RANGE.add("172.27.0.0/16");
    }

    public static String getLocalHostIP() {
        if (hostIP == null) {
        	InetAddress address = findFirstNonLoopbackAddress();
        	if (address != null) {
        		hostIP = address.getHostAddress();
        	} else {
        		hostIP = getCurrentLocalHostIP();
        	}
        }
        return hostIP;
    }

    public static String getCurrentLocalHostIP() {
        String localIP = System.getProperty(ADVERTISE_IP_ENV_NAME);
        if (localIP != null && IP_PATTERN.matcher(localIP).matches()) {
            return localIP;
        } else {
            try {
                InetAddress localAddress = InetAddress.getLocalHost();
                if (isValidHostAddress(localAddress)) {
                    localIP = localAddress.getHostAddress();
                }
            } catch (Throwable var7) {
                LOGGER.warn("Failed to get ip address, " + var7.getMessage());
            }

            try {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                if (interfaces != null) {
                    List<String> hostIPList = new ArrayList<>();
                    while(interfaces.hasMoreElements()) {
                        try {
                            NetworkInterface network = (NetworkInterface)interfaces.nextElement();
                            Enumeration<InetAddress> addresses = network.getInetAddresses();
                            if (addresses != null) {
                                while(addresses.hasMoreElements()) {
                                    try {
                                        InetAddress address = (InetAddress)addresses.nextElement();
                                        if (isValidHostAddress(address)) {
                                            hostIPList.add(address.getHostAddress());
                                        }
                                    } catch (Throwable var6) {
                                        LOGGER.warn("Failed to get network card ip address. cause:" + var6.getMessage());
                                    }
                                }
                            }
                        } catch (Throwable var8) {
                            LOGGER.warn("Failed to get network card ip address. cause:" + var8.getMessage());
                        }
                    }
                    if (localIP != null && hostIPList.contains(localIP)) {
                        return localIP;
                    }
                    return (String)hostIPList.get(0);
                }
            } catch (IOException var9) {
                LOGGER.error("Failed to get network card ip address. cause:" + var9.getMessage());
            }
            return localIP;
        }
    }

    private static boolean isValidHostAddress(InetAddress address) {
        if (address != null && !address.isLoopbackAddress()) {
            String name = address.getHostAddress();
            if (FILTER_IP.contains(name)) {
                return false;
            } else {
                if (FILTER_IP_RANGE != null && FILTER_IP_RANGE.size() > 0) {
                    Iterator<String> var2 = FILTER_IP_RANGE.iterator();
                    while(var2.hasNext()) {
                        String cidr = var2.next();
                        if (isInRange(name, cidr)) {
                            return false;
                        }
                    }
                }
                return name != null && !EMPTY_IP.equals(name) && !LOCALHOST_IP.equals(name) && IP_PATTERN.matcher(name).matches();
            }
        } else {
            return false;
        }
    }

    private static Map<String, String> getHostNames() throws SocketException {
        Map<String, String> hostNames = new HashMap<>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while(true) {
            NetworkInterface network;
            Enumeration<InetAddress> addresses;
            do {
                do {
                    if (!interfaces.hasMoreElements()) {
                        return hostNames;
                    }
                    network = (NetworkInterface)interfaces.nextElement();
                    LOGGER.debug("networkInterface name is:{},displayName is:{},isVirtual:{} ", new Object[]{network.getName(), network.getDisplayName(), network.isVirtual()});
                    addresses = network.getInetAddresses();
                } while(addresses == null);
            } while(network.isVirtual());
            while(addresses.hasMoreElements()) {
                try {
                    InetAddress address = (InetAddress)addresses.nextElement();
                    String ip = address.getHostAddress();
                    if (!LOCALHOST_IP.equals(ip) && !"Inet6Address".equals(address.getClass().getSimpleName())) {
                        LOGGER.debug("Get to the valid IP address,host address is:{},class name is:{},networkInterface name is:{}", new Object[]{address.getHostAddress(), address.getClass().getSimpleName(), network.getName()});
                        if (ip != null && !"".equals(ip.trim())) {
                            hostNames.put(network.getName(), ip);
                        }
                    } else {
                        LOGGER.debug("IP address is not valid,host address is:{},class name is:{},networkInterface name is:{}", new Object[]{address.getHostAddress(), address.getClass().getSimpleName(), network.getName()});
                    }
                } catch (Throwable var6) {
                    LOGGER.warn("Failed to get network card ip address. cause:{}", var6.getMessage());
                }
            }
        }
    }

    public static String getInet4Address() {
        String hostName = null;
        try {
            InetAddress localAddress = InetAddress.getLocalHost();
            hostName = localAddress.getHostAddress();
        } catch (Exception var4) {
            LOGGER.warn("get localhost ip on error:{}", var4.getMessage());
        }
        try {
            Map<String, String> hostNames = getHostNames();
            if (hostNames.size() <= 0) {
                return hostName;
            }
            if (hostNames.containsKey(network_name_bond0)) {
                return (String)hostNames.get(network_name_bond0);
            }
            if (hostNames.containsKey(network_name_eth0)) {
                return (String)hostNames.get(network_name_eth0);
            }
            if (hostNames.containsKey(network_name_eth1)) {
                return (String)hostNames.get(network_name_eth1);
            }
            if (hostNames.containsKey(network_name_eth2)) {
                return (String)hostNames.get(network_name_eth2);
            }
            Iterator<String> var2 = hostNames.values().iterator();
            while(var2.hasNext()) {
                String ip = var2.next();
                if (!FILTER_IP.contains(ip)) {
                    return ip;
                }
            }
        } catch (Exception var5) {
            LOGGER.warn("Error getting IP address:{}", var5.getMessage());
        }
        return hostName;
    }

    public static String getNetworkName() {
        String networkName = network_name_eth0;
        try {
            Map<String, String> hostNames = getHostNames();
            if (hostNames.size() <= 0) {
                return networkName;
            }
            if (hostNames.containsKey(network_name_bond0)) {
                return network_name_bond0;
            }
            if (hostNames.containsKey(network_name_eth0)) {
                return network_name_eth0;
            }
            if (hostNames.containsKey(network_name_eth1)) {
                return network_name_eth1;
            }
            if (hostNames.containsKey(network_name_eth2)) {
                return network_name_eth2;
            }
            Iterator<String> var2 = hostNames.keySet().iterator();
            if (var2.hasNext()) {
                String name = var2.next();
                return name;
            }
        } catch (Exception var4) {
            LOGGER.warn("Error getting IP address:{}", var4.getMessage());
        }
        return networkName;
    }

    public static boolean isInRange(String ip, String cidr) {
        String[] ips = ip.split("\\.");
        int ipAddr = Integer.parseInt(ips[0]) << 24 | Integer.parseInt(ips[1]) << 16 | Integer.parseInt(ips[2]) << 8 | Integer.parseInt(ips[3]);
        int type = Integer.parseInt(cidr.replaceAll(".*/", ""));
        int mask = -1 << 32 - type;
        String cidrIp = cidr.replaceAll("/.*", "");
        String[] cidrIps = cidrIp.split("\\.");
        int cidrIpAddr = Integer.parseInt(cidrIps[0]) << 24 | Integer.parseInt(cidrIps[1]) << 16 | Integer.parseInt(cidrIps[2]) << 8 | Integer.parseInt(cidrIps[3]);
        return (ipAddr & mask) == (cidrIpAddr & mask);
    }

    public static InetAddress findFirstNonLoopbackAddress() {
		InetAddress result = null;
		try {
			int lowest = Integer.MAX_VALUE;
			for (Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces(); nics
					.hasMoreElements();) {
				NetworkInterface ifc = nics.nextElement();
				if (ifc.isUp()) {
					LOGGER.trace("Testing interface: " + ifc.getDisplayName());
					if (ifc.getIndex() < lowest || result == null) {
						lowest = ifc.getIndex();
					}
					else if (result != null) {
						continue;
					}

					// @formatter:off
					if (!ignoreInterface(ifc.getDisplayName())) {
						for (Enumeration<InetAddress> addrs = ifc
								.getInetAddresses(); addrs.hasMoreElements();) {
							InetAddress address = addrs.nextElement();
							if (address instanceof Inet4Address
									&& !address.isLoopbackAddress()
									&& isPreferredAddress(address)) {
								LOGGER.trace("Found non-loopback interface: "
										+ ifc.getDisplayName());
								result = address;
							}
						}
					}
					// @formatter:on
				}
			}
		}
		catch (IOException ex) {
			LOGGER.error("Cannot get first non-loopback address", ex);
		}
		if (result != null) {
			return result;
		}
		try {
			return InetAddress.getLocalHost();
		}
		catch (UnknownHostException e) {
			LOGGER.warn("Unable to retrieve localhost");
		}
		return null;
	}
    
    static boolean ignoreInterface(String interfaceName) {
		return false;
	}
    static boolean isPreferredAddress(InetAddress address) {
		return true;
	}

}
