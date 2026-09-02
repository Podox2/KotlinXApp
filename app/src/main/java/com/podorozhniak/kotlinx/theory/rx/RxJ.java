package com.podorozhniak.kotlinx.theory.rx;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;

public class RxJ {

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void testRx() {

        createSingle().subscribe(new SingleObserver<String>() {
            @Override
            public void onSubscribe(Disposable d) { }

            @Override
            public void onSuccess(String s) { }

            @Override
            public void onError(Throwable e) { }
        });

        createSingle().subscribe(new DisposableSingleObserver<String>() {
            @Override
            public void onSuccess(String s) { }

            @Override
            public void onError(Throwable e) { }
        });

        compositeDisposable.add(
                createSingle().subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception { }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception { }
                })
        );

        compositeDisposable.add(
                createSingle().subscribe(
                        s -> { },
                        throwable -> { }
                )
        );
    }

    static public Single<String> createSingle() {
        return Single.just("test");
    }
}
