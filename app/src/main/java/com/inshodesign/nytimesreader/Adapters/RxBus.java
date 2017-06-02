package com.inshodesign.nytimesreader.Adapters;

import com.inshodesign.nytimesreader.Fragments.ArticleListFragment;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Emits objects (from {@link com.inshodesign.nytimesreader.Adapters.ArticleAdapter} that
 * are observed in {@link ArticleListFragment} (essentially passing button clicks
 * from adapter to fragment)
 */
public class RxBus {
private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

    public void send(Object o) {
        _bus.onNext(o);
    }

    public Observable<Object> toObserverable() {
        return _bus;
    }

}