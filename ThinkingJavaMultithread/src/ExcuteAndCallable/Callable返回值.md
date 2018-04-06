###Callable任务接口
>1. 任务完成后获得一个返回值 
>2. 具有类型参数的泛型，类型参数表示从方法 call 返回的值的类型 
>3. 必须通过ExecutorService.submit() 方法调用，submit 方法会产生Future对象
> 可以通过 isDone() 方法查询Future是否已经完成。为了获取结果，可以调用 get() 方法获取该结果，也可以不调用 isDone()方法检测完成，直接调用 get() ，get()会阻塞到结果返回
>4. 还可以在试图调用 get 获取结果之前，先调用具有超时 的 get方法，或者调用 isDone方法查看任务是否完成

####注意区别 Runable和Callable
    二者都是执行工作的独立任务，为了描述一个任务
    但Runnable不返回值，而Callable返回值
   