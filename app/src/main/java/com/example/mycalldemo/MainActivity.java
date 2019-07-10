package com.example.mycalldemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView bt,tv;
    RecyclerView rv;
    ArrayList<String> sendList = new ArrayList<String>();

    // 所需的全部权限
    static final String[] all_permissions = new String[]{
            Manifest.permission.SEND_SMS,Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission(all_permissions);

        sendList.add("13023121711");
        sendList.add("18317893228");
        sendList.add("18317893230");
        bt=findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                requestPermission(all_permissions);
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });
        tv=findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,PieActivity.class));
            }
        });
    }
    private void requestPermission(String... permissions) {
        AndPermission.with(this)
                .runtime()
                .permission(permissions)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //允许
//                        sendSMS("18317893228","hello");
//                        sendSMSList(sendList);
//                        callPhone("13023121711");
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                            //拒绝-不再询问
                            showMissingPermissionDialog();
                        }else {
                            requestPermission(all_permissions);
                        }
                    }
                })
                .start();
    }
    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("权限申请失败");
        builder.setMessage("我们需要的一些权限被您拒绝或者系统发送错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！");
        // 拒绝, 退出应用
        builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });
        builder.show();
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0){
            requestPermission(all_permissions);
        }
    }
    /**
     * 拨打电话（直接拨打电话）
     * @param phoneNum 电话号码
     */
    @SuppressLint("MissingPermission")
    public void callPhone(String phoneNum){
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    //跳转到发短信界面（无收件人，无内容）
    private void sendSMS()
    {
        Uri smsToUri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", "");
        startActivity(intent);
    }

    //跳转到发短信界面（无收件人，有内容）
    private void sendSMS(String smsBody)
    {
        Uri smsToUri = Uri.parse("smsto:"+"");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", smsBody);
        startActivity(intent);
    }

    //跳转到发短信界面，直接发送（有收件人，有内容）
    private void sendSMS(String phoneNum,String smsBody)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("address", phoneNum);
        intent.putExtra("sms_body", smsBody);
        intent.setType("vnd.android-dir/mms-sms");
        startActivity(intent);
    }

    public void sendSMSList(ArrayList<String> sendList){
        SmsManager sManager=SmsManager.getDefault();
        // 记录需要群发的号码列表
        for (String number : sendList)
        {
            // 创建一个PendingIntent对象
            PendingIntent pi = PendingIntent.getActivity(
                    MainActivity.this, 0, new Intent(), 0);
            // 发送短信
            sManager.sendTextMessage(number, null, "hello", pi, null);
        }
        // 提示短信群发完成
        Toast.makeText(MainActivity.this, "短信群发完成"
                , Toast.LENGTH_SHORT).show();

    }


}
