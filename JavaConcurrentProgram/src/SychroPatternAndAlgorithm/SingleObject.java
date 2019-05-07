package SychroPatternAndAlgorithm;

/**
 * @author wukai
 * @date 2019/5/1
 */
//第一次初始化就会被创建，不能控制创建时期
class SingleSimple{
    private SingleSimple() {

    }

    private static SingleSimple singleSimple = new SingleSimple();
    public static SingleSimple get() {
        return singleSimple;
    }
}


//这种方式比较好，利用静态内部类和类的初始化方式，不仅控制创建时间还避免加锁
//不需要加锁的原因是类的初始化肯定是线程安全的，一定只会有一个
class SingleSimple2{
    //构造器私有
    private SingleSimple2() {

    }
    private static class SingleHolder{
        private static SingleSimple2 singleSimple2 = new SingleSimple2();
    }

    public static SingleSimple2 get() {
        return SingleHolder.singleSimple2;
    }
}
public class SingleObject {
}
