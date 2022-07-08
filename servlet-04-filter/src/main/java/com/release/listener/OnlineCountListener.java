package com.release.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * 统计网站在线人数：统计Session
 * @author yancheng
 * @since 2022/7/6
 */
public class OnlineCountListener implements HttpSessionListener {


    /**
     * 一旦创建Session就会触发一次这个事件
     * @param httpSessionEvent
     */
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        ServletContext servletContext = httpSessionEvent.getSession().getServletContext();
        Integer onlineCount = (Integer) servletContext.getAttribute("OnlineCount");
        if (onlineCount == null){
            onlineCount = new Integer(1);
        }else {
            int i = onlineCount.intValue();
            onlineCount = new Integer(i+1);
        }
        servletContext.setAttribute("OnlineCount",onlineCount);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        ServletContext servletContext = httpSessionEvent.getSession().getServletContext();
        Integer onlineCount = (Integer) servletContext.getAttribute("OnlineCount");
        if (onlineCount == null){
            onlineCount = new Integer(0);
        }else {
            int i = onlineCount.intValue();
            onlineCount = new Integer(i-1);
        }
        servletContext.setAttribute("OnlineCount",onlineCount);
    }
}
