package com.gm.javaeaseframe.core.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.gm.javaeaseframe.core.constains.GlobalSysInfo;


@WebListener
@ConditionalOnWebApplication
public class ApplicationContextLoaderListener extends ContextLoaderListener {
    
    private Log logger = LogFactory.getLog(ApplicationContextLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        logger.info("Web context init...");
        ServletContext context = event.getServletContext();
        WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);
        logger.info("Refresh web context params...");
        GlobalSysInfo.context = webContext;
        GlobalSysInfo.realRootPath = context.getRealPath("/");
        GlobalSysInfo.contextPath = context.getContextPath();
    }

    
}
