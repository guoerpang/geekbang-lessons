package org.geektimes.projects.user.web.listener;

import org.geektimes.context.ComponentContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.logging.Logger;

/**
 * @author GH
 */
public class ComponentContextInitializerListener implements ServletContextListener {
    private Logger logger = Logger.getLogger(ComponentContextInitializerListener.class.getName());

    private ServletContext servletContext;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("初始化ComponentContextInitializerListener");

        this.servletContext = sce.getServletContext();

        ComponentContext context = new ComponentContext();
        context.init(servletContext);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
