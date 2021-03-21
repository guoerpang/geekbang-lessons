package jdk.parameterizedtype.priorityqueue;

import org.junit.Test;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

/**
 * @author GH
 */
public class PriorityQueueTest {

    /**
     * integerPriorityQueue.add(随机数)，add时默认使用Integer#compare方法进行排序
     *
     * Processing Integer:6
     * Processing Integer:22
     * Processing Integer:54
     * Processing Integer:63
     * Processing Integer:68
     * Processing Integer:95
     * Processing Integer:99
     */

    @Test
    public void testInteger(){
        //优先队列自然排序示例
        Queue<Integer> integerPriorityQueue = new PriorityQueue<>(7);
        Random rand = new Random();
        for (int i = 0; i < 7; i++) {
            integerPriorityQueue.add(new Integer(rand.nextInt(100)));
        }

        for (int i = 0; i < 7; i++) {
            Integer in = integerPriorityQueue.poll();
            System.out.println("Processing Integer:" + in);
        }
    }

    /**
     * customerPriorityQueue.add() 使用了 传入的 idComparator 比较器
     *
     * Processing Customer with ID=30
     * Processing Customer with ID=59
     * Processing Customer with ID=60
     * Processing Customer with ID=67
     * Processing Customer with ID=74
     * Processing Customer with ID=93
     * Processing Customer with ID=99
     */
    @Test
    public void testDomain() {
        //优先队列使用示例
        Queue<Customer> customerPriorityQueue = new PriorityQueue<>(7, idComparator);

        addDataToQueue(customerPriorityQueue);
        pollDataFromQueue(customerPriorityQueue);
    }

    //匿名Comparator实现
    private static Comparator<Customer> idComparator = Comparator.comparingInt(Customer::getId);

    //用于往队列增加数据的通用方法
    private static void addDataToQueue(Queue<Customer> customerPriorityQueue) {
        Random rand = new Random();
        for (int index = 0; index < 7; index++) {
            int id = rand.nextInt(100);
            customerPriorityQueue.add(new Customer(id, "guohao " + id));
        }
    }

    //用于从队列取数据的通用方法
    private static void pollDataFromQueue(Queue<Customer> customerPriorityQueue) {
        while (true) {
            Customer cust = customerPriorityQueue.poll();
            if (cust == null) {
                break;
            }
            System.out.println("Processing Customer with ID=" + cust.getId());
        }
    }

}
