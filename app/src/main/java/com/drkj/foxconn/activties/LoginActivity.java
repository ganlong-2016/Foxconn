package com.drkj.foxconn.activties;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.drkj.foxconn.BaseActivity;
import com.drkj.foxconn.R;
import com.drkj.foxconn.net.NetClient;
import com.drkj.foxconn.util.SpUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText userName;
    @BindView(R.id.et_password)
    EditText password;
    @BindView(R.id.checkbox_remember)
    CheckBox remember;
    @BindView(R.id.button_login)
    ImageButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        userName.setText(SpUtil.getString(this, "username"));
        password.setText(SpUtil.getString(this, "password"));
        remember.setChecked(SpUtil.getBlooean(this, "check"));
    }

    @OnClick(R.id.button_login)
    void login() {
        loginButton.setEnabled(false);
        if (TextUtils.isEmpty(userName.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
            Toast.makeText(this, "帐号和密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        SpUtil.putBlooean(this,"check",remember.isChecked());
        if (remember.isChecked()) {
            SpUtil.putString(this, "username", userName.getText().toString());
            SpUtil.putString(this, "password", password.getText().toString());
        }

        NetClient.getInstance().getApi().getToken(userName.getText().toString(),password.getText().toString())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> response) throws Exception {
                        loginButton.setEnabled(true);
                        if (response.code()==200){
                            String token = response.body().string();
                            SpUtil.putString(LoginActivity.this, "token", token);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }else if (response.code()==404){
                            Toast.makeText(LoginActivity.this,"帐号或者密码错误",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(LoginActivity.this,"服务器异常",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        loginButton.setEnabled(true);
                        Toast.makeText(LoginActivity.this,"服务器连接失败",Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
