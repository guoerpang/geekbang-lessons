package org.geektimes.projects.user.jolokia.demo;

import org.jolokia.client.J4pClient;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pReadResponse;

import javax.management.MalformedObjectNameException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author GH
 */
public class JolokiaConfigDemo {
    public static void main(String[] args) throws Exception {
        J4pClient j4pClient = new J4pClient("http://localhost:8778/userManager/");

        J4pReadRequest req = new J4pReadRequest("jolokia:name=userManager");

        J4pReadResponse resp = j4pClient.execute(req);
        Map<String, Object> valueMap = resp.getValue();
        Set<String> keys = valueMap.keySet();
        Iterator<String> iter = keys.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            System.out.println(key);
            System.out.println(valueMap.get(key));

        }
    }
}
