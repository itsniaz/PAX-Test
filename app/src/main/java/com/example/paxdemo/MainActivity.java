package com.example.paxdemo;

import static io.reactivex.rxjava3.schedulers.Schedulers.start;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.pax.poslink.CommSetting;
import com.pax.poslink.LogSetting;
import com.pax.poslink.ManageRequest;
import com.pax.poslink.ManageResponse;
import com.pax.poslink.PaymentRequest;
import com.pax.poslink.PaymentResponse;
import com.pax.poslink.PosLink;
import com.pax.poslink.ProcessTransResult;

import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_send_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(() -> _sendTerminalRequest()).start();

            }
        });
    }

    private void _sendTerminalRequest() {
        PosLink posLink = new PosLink(this);
        CommSetting commSetting = new CommSetting();
        ManageRequest manageRequest = new ManageRequest();
        ProcessTransResult transResult = new ProcessTransResult();
        ManageResponse manageResponse = new ManageResponse();

        LogSetting.setLevel(LogSetting.LOGLEVEL.DEBUG);
        LogSetting.setOutputPath(Environment.getExternalStorageDirectory().getAbsolutePath());
        String outputpath = LogSetting.getOutputPath();

        commSetting.setType("TCP");
        commSetting.setTimeOut("100000");
        commSetting.setDestIP("192.168.31.211");
        commSetting.setDestPort("10009");
        posLink.SetCommSetting(commSetting);


        PaymentRequest pay = new PaymentRequest();
        pay.TenderType = pay.ParseTenderType("DEBIT");
        pay.TransType = pay.ParseTransType("SALE");
        pay.ECRRefNum = "" + getRandomNumber();
        pay.ECRTransID = "" + getRandomNumber();
        pay.InvNum = "" + getRandomNumber();
        pay.Amount = "2000";
        posLink.PaymentRequest = pay;
        transResult = posLink.ProcessTrans();
        PaymentResponse response = posLink.PaymentResponse;
        Gson gson = new Gson();
        String json = gson.toJson(response);

        String x = "a demo string to catch the breakpoint";
    }

    int getRandomNumber(){
        int min = 1;
        int max = 10000;
        return (int)Math.floor(Math.random() * (max - min + 1) + min);
    }



}