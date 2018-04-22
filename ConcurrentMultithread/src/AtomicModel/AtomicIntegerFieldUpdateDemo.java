package AtomicModel;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * AotimcIntegerFieldUpdate用于更新对应对象的字段，int型属性必须为pulic volatile
 *
 */
public class AtomicIntegerFieldUpdateDemo {
    private static AtomicIntegerFieldUpdater atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(User.class, "age");

    public static void main(String[] args) {
        User con = new User("ha", 23);
        System.out.println(atomicIntegerFieldUpdater.getAndIncrement(con));
        System.out.println(atomicIntegerFieldUpdater.get(con));
    }


    public static class User{
        private String name;
        public volatile int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }




        public int getAge() {
            return age;
        }

    }
}
