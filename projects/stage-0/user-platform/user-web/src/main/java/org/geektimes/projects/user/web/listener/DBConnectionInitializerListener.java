package org.geektimes.projects.user.web.listener;

import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.Connection;

@WebListener
public class DBConnectionInitializerListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext servletContext = sce.getServletContext();
        try {
            System.out.println("数据库加载开始===");
            InitialContext ic = new InitialContext();
            DataSource dataSource = (DataSource) ic.lookup("java:comp/env/jdbc/UserPlatformDB");
            Connection connection = dataSource.getConnection();
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
