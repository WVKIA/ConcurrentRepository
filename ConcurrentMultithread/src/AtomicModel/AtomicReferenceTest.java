package AtomicModel;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceTest {
    public static void main(String[] args) {
        Person person1 = new Person(1);
        Person person2 = new Person(2);
        AtomicReference atomicReference = new AtomicReference(person1);
        atomicReference.compareAndSet(person1, person2);

        Person person3 = (Person) atomicReference.get();
        System.out.println("person3 " + person3);
        System.out.println("person3 equals to person1 = "+person3.equals(person1)); //false
        System.out.println("person3 equals to person2 = "+person3.equals(person2)); //true
    }
}
class Person{
    volatile  long id;

    public Person(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id "+id;
    }
}
