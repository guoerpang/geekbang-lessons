package org.geektimes.projects.user.jolokia.demo;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.management.UserManager;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author GH
 */
public class JolokiaDemo {
    public static void main(String[] args) throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();

        ObjectName userManager = new ObjectName("jolokia:name=userManager");
        User user = new User();
        server.registerMBean(new UserManager(user), userManager);

        while (true) {
            TimeUnit.SECONDS.sleep(1);
            System.out.println(user.toString());
        }
    }
}
