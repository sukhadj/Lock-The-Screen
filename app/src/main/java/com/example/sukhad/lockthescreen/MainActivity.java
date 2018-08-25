package com.example.sukhad.lockthescreen;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button lock,enable,disable;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName cmpNm;
    public static final int RESULT_ENABLE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        cmpNm = new ComponentName(this,MyAdmin.class);

        lock = (Button) findViewById(R.id.lock);
        enable = (Button) findViewById(R.id.enableBtn);
        disable = (Button) findViewById(R.id.disableBtn);

        lock.setOnClickListener(this);
        enable.setOnClickListener(this);
        disable.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isActive = devicePolicyManager.isAdminActive(cmpNm);
        disable.setVisibility(isActive ? View.VISIBLE : View.GONE);
        enable.setVisibility(isActive ? View.GONE : View.VISIBLE);
    }

    public void onClick(View view)
    {
        if (view==lock)
        {
            boolean active = devicePolicyManager.isAdminActive(cmpNm);
            if(active)
            {
                devicePolicyManager.lockNow();

            }
            else
            {
                Toast.makeText(this, "Please Enable the Admin Permission", Toast.LENGTH_SHORT).show();
            }
        }
        else if (view==enable)
        {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,cmpNm);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"We respect your device's security");
            startActivityForResult(intent,RESULT_ENABLE);
            // Toast.makeText(this, "Ignored", Toast.LENGTH_SHORT).show();
        }
        else if (view==disable)
        {
            devicePolicyManager.removeActiveAdmin(cmpNm);
            disable.setVisibility(View.GONE);
            enable.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode)
        {
            case RESULT_ENABLE :
                if(resultCode == Activity.RESULT_OK)
                {
                    Toast.makeText(MainActivity.this, "You have enabled the Admin Device features", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Problem occurred while enabling the Admin Device feature", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
