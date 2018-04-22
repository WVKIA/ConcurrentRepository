package AtomicModel;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDemo {
    private static final AtomicReference<String> ATOMIC_REFERENCE = new AtomicReference<>("abc");
//只有一个线程会修改，其他线程发现内存V已经被改变，就不会修改值
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            final int num = i;
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                TimeUnit.MICROSECONDS.sleep(100);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                            if (ATOMIC_REFERENCE.compareAndSet("abc",new String("abc"))){
                                System.out.println("Thread "+num+" ,get Lock and change value");
                            }
                        }
                    }
            ).start();
        }
    }
}
