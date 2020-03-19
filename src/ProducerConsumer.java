//Using the blockingQueue
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer {
    private static volatile Integer itemNumber = 0;

    static class ConsumerThread extends Thread {

        private BlockingQueue<String> blockingQueue;
        public ConsumerThread(BlockingQueue<String> blockingQueue){
            this.blockingQueue = blockingQueue;
        }
        @Override
        public void run() {
            for(int i = 0; i < 100; i++){
                try {
                        System.out.println("Adding Item " + ++itemNumber + " to queue.");
                        blockingQueue.put("Item " + (itemNumber));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    static class ProducerThread extends Thread {
        private BlockingQueue<String> blockingQueue;

        public ProducerThread(BlockingQueue<String> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        @Override
        public void run() {
            while(true){
                        try {
                            System.out.println("Consuming " + blockingQueue.take());
//                            Thread.sleep(1000);
                        }catch (InterruptedException e) {
                            e.printStackTrace();
                        }
            }
        }
    }

    public static void main(String[] args) {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(10);

        (new ConsumerThread(blockingQueue)).start();
        (new ConsumerThread(blockingQueue)).start();

        (new ProducerThread(blockingQueue)).start();
        (new ProducerThread(blockingQueue)).start();
        (new ProducerThread(blockingQueue)).start();
        (new ProducerThread(blockingQueue)).start();
        (new ProducerThread(blockingQueue)).start();


    }
}
