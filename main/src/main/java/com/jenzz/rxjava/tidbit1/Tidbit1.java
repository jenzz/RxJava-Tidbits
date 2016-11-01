package com.jenzz.rxjava.tidbit1;

import com.sun.tools.javac.util.Pair;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

public class Tidbit1 {

    public static void main(String[] args) {
        Observable.just("foo")
                  .flatMap(new Func1<String, Observable<Integer>>() {
                      @Override
                      public Observable<Integer> call(String s) {
                          return Observable.range(1, 5);
                      }
                  })
                  .subscribe(new Action1<Integer>() {
                      @Override
                      public void call(Integer i) {
                          // how do you access "foo" String?
                          System.out.println("Result: " + i);
                      }
                  });

        Observable.just("foo")
                  .flatMap(new Func1<String, Observable<Integer>>() {
                      @Override
                      public Observable<Integer> call(String foo) {
                          return Observable.range(1, 5);
                      }
                  }, new Func2<String, Integer, Pair<Integer, String>>() {
                      @Override
                      public Pair<Integer, String> call(String s, Integer i) {
                          return new Pair<>(i, s);
                      }
                  })
                  .subscribe(new Action1<Pair<Integer, String>>() {
                      @Override
                      public void call(Pair<Integer, String> pair) {
                          System.out.println("Result: " + pair.fst + " Foo: " + pair.snd);
                      }
                  });
    }
}
