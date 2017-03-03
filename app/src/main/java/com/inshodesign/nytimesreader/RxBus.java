package com.inshodesign.nytimesreader;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by JClassic on 2/23/2017.
 */

public class RxBus {
//    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());
//    public void send(Object o) { _bus.onNext(o); }
//    public Observable<Object> toObserverable() { return _bus; }
//    public boolean hasObservers() { return _bus.hasObservers(); }
//
//    PublishSubject<Integer> onClickSubject = PublishSubject.create();
private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

    public void send(Object o) {
        _bus.onNext(o);
    }

    public Observable<Object> toObserverable() {
        return _bus;
    }

}