package com.example.huixing.decprectice.Login;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.example.huixing.decprectice.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import io.reactivex.*;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private  Student [] students = new Student[101];
    private static final String TAG = "rxtest";

    private static Subscription mSubscription;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        for (int i=0;i<=100;i++){


            Student student = new Student();
            student.setAge(i);
            student.setName("name"+i);
            student.setHobby("hobby"+i);

            students[i] = student;

        }
        context  = getApplicationContext();

//        test(students);

//        test3();

        practice1();
    }


    public  void test(Student [] students){



        Observable.fromArray(students)
                .map(new Function<Student, String>() {
                    @Override
                    public String apply(Student student) throws Exception {
                        return student.getName();
                    }
                }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {

                Log.d(TAG,s);
            }

            @Override
            public void onError(Throwable e) {

                Log.d(TAG,"出错了"+e);

            }

            @Override
            public void onComplete() {

                Log.d(TAG,"完成啦");
            }
        });


    }

    public  void test2(Student [] students){



        Observable.fromArray(students)
                .map(new Function<Student, String>() {
                    @Override
                    public String apply(Student student) throws Exception {
                        return student.getName();
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG,s);
            }
        });


    }


    public void test3(){


        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.e(TAG, "Observable thread is : " + Thread.currentThread().getName());
                Log.e(TAG, "emit 1");
                Thread.sleep(10000);
                emitter.onNext(1);
            }
        });

        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {

                if (integer == 1){

                    Log.e(TAG, "Observer thread is :" + Thread.currentThread().getName());
                    Log.e(TAG, "onNext: " + integer);

                }

            }
        };

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);


    }


    public void practice1() {
        Flowable
                .create(new FlowableOnSubscribe<String>() {
                    @Override
                    public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                        try {


                            InputStream is = context.getAssets().open("test.txt");

//                            FileReader reader = new FileReader("/assets/test.txt");

                            InputStreamReader reader1 = new InputStreamReader(is);

                            BufferedReader br = new BufferedReader(reader1);


                            String str;

                            while ((str = br.readLine()) != null && !emitter.isCancelled()) {
                                while (emitter.requested() == 0) {
                                    if (emitter.isCancelled()) {
                                        break;
                                    }
                                }
                                emitter.onNext(str);
                            }

                            br.close();
                            reader1.close();

                            emitter.onComplete();
                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    }
                }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        mSubscription = s;
                        s.request(1);
                    }

                    @Override
                    public void onNext(String string) {
                        Log.e(TAG,string);
                        try {
                            Thread.sleep(2000);
                            mSubscription.request(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println(t);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

}
