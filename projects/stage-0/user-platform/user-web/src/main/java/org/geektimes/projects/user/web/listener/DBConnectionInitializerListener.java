package org.geektimes.projects.user.web.listener;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

public class DBConnectionInitializerListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        try {
            System.out.println("数据库加载开始===");
            Context ic = new InitialContext();
            Context context = (Context) ic.lookup("java:comp/env");
            DataSource dataSource = (DataSource) context.lookup("jdbc/UserPlatformDB");
            dataSource.getConnection();
            System.out.println("数据库加载成功===");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("contextDestroyed=====");
    }
}
