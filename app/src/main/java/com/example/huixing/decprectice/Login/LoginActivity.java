package com.example.huixing.decprectice.Login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.example.huixing.decprectice.R;
import io.reactivex.*;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private  Student [] students = new Student[101];
    private  final String TAG = "rxtest";

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

//        test(students);

        test3();

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
                Log.d(TAG, "Observable thread is : " + Thread.currentThread().getName());
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
            }
        });

        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "Observer thread is :" + Thread.currentThread().getName());
                Log.d(TAG, "onNext: " + integer);
            }
        };

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);


    }

}
