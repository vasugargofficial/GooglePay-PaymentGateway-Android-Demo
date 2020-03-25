package com.example.paymentgateway;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etname,etamount,etupiid,etnote;
    Button busend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etname=findViewById(R.id.etname);
        etamount=findViewById(R.id.etamount);
        etupiid=findViewById(R.id.etupiid);
        etnote=findViewById(R.id.etNote);
        busend=findViewById(R.id.busend);

        busend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etname.getText().toString().isEmpty() || etamount.getText().toString().isEmpty()
                || etupiid.getText().toString().isEmpty() || etnote.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Invalid Info Please Check it Once",Toast.LENGTH_LONG).show();
                }
                else{
                    payUsingUpi(etname.getText().toString().trim(),etupiid.getText().toString().trim(),
                            etamount.getText().toString().trim(),etnote.getText().toString().trim());
                }
            }
        });

    }

    void payUsingUpi(String name, String upiid, String amount,String note){

        String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
        int GOOGLE_PAY_REQUEST_CODE = 123;

        Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", upiid)
                        .appendQueryParameter("pn", name)
                       // .appendQueryParameter("mc", "your-merchant-code")
                        //.appendQueryParameter("tr", "your-transaction-ref-id")
                        //.appendQueryParameter("tn", "your-transaction-note")
                        .appendQueryParameter("am",amount)
                        .appendQueryParameter("cu", "INR")
                       // .appendQueryParameter("url", "your-transaction-url")
                        .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main","responce"+resultCode);
        switch (requestCode){
            case 123:
                if(resultCode==RESULT_OK || resultCode==11){
                    if(data!=null) {
                        String response = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + response);
                        Toast.makeText(getApplicationContext(),"Succesfull",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Cancelled",Toast.LENGTH_SHORT).show();
                }

        }
    }
}
