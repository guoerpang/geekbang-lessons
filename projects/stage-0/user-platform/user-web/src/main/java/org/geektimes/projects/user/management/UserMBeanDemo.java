package org.geektimes.projects.user.management;

import org.geektimes.projects.user.domain.User;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author GH
 */
public class UserMBeanDemo {
    public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, InterruptedException {
        MBeanServer  server = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("org.geektimes.projects.user.management:type=user");

        User user = new User();
        server.registerMBean(createUserMBean(user), objectName);
        while (true) {
            TimeUnit.SECONDS.sleep(1);
            System.out.println(user.toString());
        }
    }

    private static Object createUserMBean(User user) {
        return new UserManager(user);
    }
}
