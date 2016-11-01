package com.jenzz.rxjava.tidbit2;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

public class Tidbit2 {

    private static long startTime;

    public static void main(String[] args) {
        Observable.zip(
                sleepyObservable(300),
                sleepyObservable(200),
                sleepyObservable(100),
                new Func3<Object, Object, Object, Object>() {
                    @Override
                    public Object call(Object o1, Object o2, Object o3) {
                        return null; // combine objects...
                    }
                }
        ).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                startTime = System.currentTimeMillis();
            }
        }).doOnTerminate(new Action0() {
            @Override
            public void call() {
                long totalDuration = System.currentTimeMillis() - startTime;
                System.out.println("Total execution time: " + totalDuration + "ms");
            }
        }).subscribe();

        Observable.zip(
                sleepyObservable(300).subscribeOn(Schedulers.newThread()),
                sleepyObservable(200).subscribeOn(Schedulers.newThread()),
                sleepyObservable(100).subscribeOn(Schedulers.newThread()),
                new Func3<Object, Object, Object, Object>() {
                    @Override
                    public Object call(Object o1, Object o2, Object o3) {
                        return null; // combine objects...
                    }
                }
        ).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                startTime = System.currentTimeMillis();
            }
        }).doOnTerminate(new Action0() {
            @Override
            public void call() {
                long totalDuration = System.currentTimeMillis() - startTime;
                System.out.println("Total execution time: " + totalDuration + "ms");
            }
        }).toBlocking().subscribe();
    }

    private static Observable<Object> sleepyObservable(final long duration) {
        return Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println("Sleeping " + duration + "ms");
                Thread.sleep(duration);
                return null;
            }
        });
    }
}
