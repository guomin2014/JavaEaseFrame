package com.gm.javaeaseframe.core.context.web;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gm.javaeaseframe.common.util.HttpUtil;
import com.gm.javaeaseframe.core.constains.SysConstains;

public class TokenProcessor {

    private static TokenProcessor instance = new TokenProcessor();
    
    private static ThreadLocal<String> tokenLocal = new ThreadLocal<String>();

    public static TokenProcessor getInstance() {
        return instance;
    }

    protected TokenProcessor() {
        super();
    }

    private long previous;

    public synchronized boolean isTokenValid(HttpServletRequest request) {
        return this.isTokenValid(request, false);
    }

    public synchronized boolean isTokenValid(HttpServletRequest request, boolean reset) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        String saved = (String) session.getAttribute(SysConstains.TOKEN);
        if (saved == null) {
            return false;
        }

        if (reset) {
            this.resetToken(request);
        }
        String token = request.getParameter(SysConstains.TOKEN);
        if (token == null) {
            return false;
        }

        return saved.equals(token);
    }

    public synchronized void resetToken(HttpServletRequest request) {

    	tokenLocal.remove();
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(SysConstains.TOKEN);
    }

    public synchronized void saveToken(HttpServletRequest request) {

        HttpSession session = request.getSession();
        String token = generateToken(request);
        if (token != null) {
            session.setAttribute(SysConstains.TOKEN, token);
        }
        tokenLocal.set(token);
    }

    public synchronized String generateToken(HttpServletRequest request) {

        HttpSession session = request.getSession();
        try {
            byte id[] = session.getId().getBytes();
            long current = System.currentTimeMillis();
            if (current == previous) {
                current++;
            }
            previous = current;
            byte now[] = new Long(current).toString().getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(id);
            md.update(now);
            return toHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

    }

    private String toHex(byte buffer[]) {
        StringBuffer sb = new StringBuffer(buffer.length * 2);
        for (int i = 0; i < buffer.length; i++) {
            sb.append(Character.forDigit((buffer[i] & 0xf0) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 0x0f, 16));
        }
        return sb.toString();
    }
    
    
    public synchronized boolean isTokenValid(HttpServletRequest request, HttpServletResponse response) {
        return this.isTokenValid(request, response, false);
    }

    public synchronized boolean isTokenValid(HttpServletRequest request, HttpServletResponse response, boolean reset) {
        String saved = HttpUtil.getCookieValue(request, SysConstains.TOKEN);
        if (saved == null) {
            return false;
        }

        if (reset) {
            this.resetToken(request, response);
        }
        String token = request.getParameter(SysConstains.TOKEN);
        if (token == null) {
            return false;
        }

        return saved.equals(token);
    }

    public synchronized void resetToken(HttpServletRequest request, HttpServletResponse response) {
        HttpUtil.deleteCookie(request, response, SysConstains.TOKEN);
        tokenLocal.remove();
    }
    public synchronized void saveToken(HttpServletRequest request, HttpServletResponse response)
    {
    	String host = request.getServerName();
    	String token = generateToken(request);
    	HttpUtil.setCookieValue(response, SysConstains.TOKEN, token, host, 3000, false);
    	tokenLocal.set(token);
    	
    }
    
    public String getCurrToken()
    {
    	return tokenLocal.get();
    }

}