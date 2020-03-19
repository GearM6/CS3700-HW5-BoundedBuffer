import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockedBoundedBuffer {
    private static LinkedList<String> boundedBuffer = new LinkedList<>();
    private static Lock bufferLock = new ReentrantLock();
    private static int capacity = 10;
    private static CountDownLatch latch = new CountDownLatch(5);
    
    private static class ProducerThread extends Thread{
        String threadName;
        public ProducerThread(String name) {
            this.threadName = name;
        }

        @Override
        public void run() {
            int createdItems = 0;
            while(createdItems < 100) {
                if(boundedBuffer.size() <= capacity && bufferLock.tryLock()){
                    boundedBuffer.add("Item " + (++createdItems));
                    System.out.println(this.threadName + " produced item " + (createdItems));
                    bufferLock.unlock();
                }
            }
            System.out.println("        " +
                    this.threadName + " has terminated");
            latch.countDown();
        }
    }

    private static class ConsumerThread extends Thread {
        String threadName;
        public ConsumerThread(String name) {
            this.threadName = name;
        }

        @Override
        public void run() {
            while(true){
                while(boundedBuffer.size() != 0){
                    if(bufferLock.tryLock()) {
                        String item = boundedBuffer.remove();
                        if(!item.equals("finished")){
                            System.out.println("  " + this.threadName + " consumed " +  item);
                        try {
                            Thread.currentThread().sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                            bufferLock.unlock();
                        }
                        else {
                            System.out.println("    " + this.threadName + " has terminated");
                            bufferLock.unlock();
                            return;
                        }

                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        new ConsumerThread("Consumer 1").start();
        new ConsumerThread("Consumer 2").start();


        new ProducerThread("Producer 1").start();
        new ProducerThread("Producer 2").start();
        new ProducerThread("Producer 3").start();
        new ProducerThread("Producer 4").start();
        new ProducerThread("Producer 5").start();

        latch.await();

        boundedBuffer.add("finished");
        boundedBuffer.add("finished");
    }
}
