package com.example.First_prj.LoginForm;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import com.example.First_prj.ForAllCode.LiteMatrixDraw;
import com.example.First_prj.ForAllCode.TransparentEmptyView;

public class MainWindow extends LinearLayout implements View.OnClickListener, View.OnTouchListener {

    private SharedPreferences keyValueStorage;
    private CustomLoginEditText userName;
    private CustomLoginEditText password;
    private Context context;
    private CheckBox saveMe;
    private Button login;

    public MainWindow(Context context) {
        super(context);
        super.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        super.setGravity(Gravity.CENTER);
        super.setOrientation(VERTICAL);
        super.setBackgroundDrawable(new LiteMatrixDraw(context));
        LinearLayout buttonBox = new LinearLayout(context);
        float metric = context.getResources().getDisplayMetrics().density;
        this.context = context;

        userName = new CustomLoginEditText(context);

        saveMe = new CheckBox(context);
        saveMe.setTextColor(Color.rgb(140, 140, 140));
        saveMe.setTextSize(14);
        saveMe.setText("Запомнить ");

        login = new Button(context);
        login.setLayoutParams(new ViewGroup.LayoutParams((int) (80 * metric), (int) (40 * metric)));
        login.setBackgroundColor(Color.argb(200, 25, 25, 25));
        login.setTextColor(Color.rgb(0, 100, 0));
        login.setText("Войти");

        password = new CustomLoginEditText(context);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        buttonBox.setOrientation(HORIZONTAL);
        buttonBox.setGravity(Gravity.RIGHT);
        buttonBox.setLayoutParams(new ViewGroup.LayoutParams((int) (200 * metric), (int) (40 * metric)));
        buttonBox.addView(saveMe);
        buttonBox.addView(login);

        super.addView(userName);
        super.addView(new TransparentEmptyView(context, 5));
        super.addView(password);
        super.addView(new TransparentEmptyView(context, 5));
        super.addView(buttonBox);

        userName.setOnClickListener(this);
        password.setOnClickListener(this);
        login.setOnTouchListener(this);
    }

    public String getPassword() {
        try {
            return password.getText().toString();
        } catch (NullPointerException ex) {
            return "";
        }
    }

    public String getUserName() {
        try {
            return userName.getText().toString();
        } catch (NullPointerException ex) {
            return "";
        }
    }

    public void saveWindowInfo() {
        keyValueStorage = context.getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = keyValueStorage.edit();
        editor.putString("UserName", getUserName());
        editor.putString("Password", getPassword());
        editor.putBoolean("SaveMe", saveMe.isChecked());
        editor.commit();
    }

    public void loadWindowInfo() {
        keyValueStorage = context.getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
        if (keyValueStorage.getBoolean("SaveMe", false)) {
            userName.setText(keyValueStorage.getString("UserName", ""));
            password.setText(keyValueStorage.getString("Password", ""));
            saveMe.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(userName)) {
            if (getUserName().equals("Имя пользователя"))
                userName.setText("");
        } else if (view.equals(password)) {
            if (getPassword().equals("Long hard password"))
                password.setText("");
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.equals(login) && motionEvent.getAction() == MotionEvent.ACTION_DOWN)
            login.setBackgroundColor(Color.rgb(0, 255, 0));
        else if (view.equals(login))
            login.setBackgroundColor(Color.argb(200, 25, 25, 25));
        return true;
    }
}
