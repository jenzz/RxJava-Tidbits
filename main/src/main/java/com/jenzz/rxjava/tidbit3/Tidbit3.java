package com.jenzz.rxjava.tidbit3;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

public class Tidbit3 {

    private static final String FIRST_URL = "http://first.url";
    private static final String SECOND_URL = "http://second.url";
    private static final String THIRD_URL = "http://third.url";

    private static final Response FIRST_RESPONSE = new Response(null, SECOND_URL);
    private static final Response SECOND_RESPONSE = new Response(null, THIRD_URL);
    private static final Response THIRD_RESPONSE = new Response("third_result", null);

    private static final Map<String, Response> URL_RESPONSE_MAP = new HashMap<String, Response>() {{
        put(FIRST_URL, FIRST_RESPONSE);
        put(SECOND_URL, SECOND_RESPONSE);
        put(THIRD_URL, THIRD_RESPONSE);
    }};

    public static void main(String[] args) {
        recursiveSolution();

        subjectSolution();
    }

    private static void subjectSolution() {
        final BehaviorSubject<String> subject = BehaviorSubject.create();
        subject.onNext(FIRST_URL);
        subject.flatMap(new Func1<String, Observable<Response>>() {
            @Override
            public Observable<Response> call(String url) {
                return getResponse(url);
            }
        }).flatMap(new Func1<Response, Observable<Response>>() {
            @Override
            public Observable<Response> call(Response response) {
                if (response.result == null && response.nextUrl != null) {
                    subject.onNext(response.nextUrl);
                    return Observable.empty();
                } else {
                    subject.onCompleted();
                    return Observable.just(response);
                }
            }
        }).subscribe(response -> {
            System.out.println(response.result);
        });
    }

    private static void recursiveSolution() {
        downloadFromNetwork(FIRST_URL)
                .subscribe(response -> {
                    System.out.println(response.result);
                });
    }

    private static Observable<Response> downloadFromNetwork(String url) {
        return getResponse(url)
                .flatMap(new Func1<Response, Observable<Response>>() {
                    @Override
                    public Observable<Response> call(Response response) {
                        if (response.result == null && response.nextUrl != null) {
                            return downloadFromNetwork(response.nextUrl);
                        } else {
                            return Observable.just(response);
                        }
                    }
                });
    }

    private static Observable<Response> getResponse(String url) {
        return Observable.just(URL_RESPONSE_MAP.get(url));
    }

    private static class Response {
        private final Object result;
        private final String nextUrl;

        private Response(Object result, String nextUrl) {
            this.result = result;
            this.nextUrl = nextUrl;
        }
    }
}
