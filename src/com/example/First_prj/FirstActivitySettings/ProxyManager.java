package com.example.First_prj.FirstActivitySettings;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.First_prj.ForAllCode.DesigneElements.Lines.BlackToWhiteHeaderGradientLine;
import com.example.First_prj.ForAllCode.DesigneElements.SerifTextView;
import com.example.First_prj.ForAllCode.DesigneElements.Lines.TransparentHorizontalLine;
import com.example.First_prj.ForAllCode.GlobalConfig;
import com.example.First_prj.ForAllCode.GlobalConstants;
import com.example.First_prj.ForAllCode.DesigneElements.Lines.BubbleHorizontalGradientLine;

public class ProxyManager extends LinearLayout implements View.OnClickListener {

    private static final int MAX_PORT_VALUE = 65536;
    private static final char MAX_IP_VALUE = 255;
    private static final byte OCTETS_COUNT = 4;
    private static final byte OCTET_LEN = 3;
    private static final byte PORT_LEN = 5;

    private static final String DOT = ".";
    private static final String IP_TITLE = " IP :";
    private static final String PORT_TITLE = " Port :";
    private static final String PROXY_TITLE = "\tНастройка прокси";
    private static final String PROXY_MESSAGE_ON = "Прокси включена";
    private static final String CLEAR_PROXY_TITLE = "Очистить прокси";
    private static final String PROXY_MESSAGE_OFF = "Прокси отключена";
    private static final String IP_ERROR_MESSAGE = "Число не может быть больше ";
    private static final String PORT_ERROR_MESSAGE = "Порт не может быть больше ";
    private static final String EMPTY_FIELD_ERROR_MESSAGE = "Поля не должны быть пустыми";
    private static final String FIRST_OCTET_ERROR_MESSAGE = "Адрес не может начинаться с нуля или быть пустым";

    private ProxyEditText portForm;
    private ProxyEditText[] ipOctet;
    private LinearLayout addressLay;
    private LinearLayout portLay;
    private SerifTextView clear;
    private CheckBox proxySet;
    private Context context;

    private String bufferStr;

    public ProxyManager(Context context) {
        super(context);
        super.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        super.setOrientation(VERTICAL);

        this.context = context;
        ipOctet = new ProxyEditText[OCTETS_COUNT];

        super.addView(new SerifTextView(context, PROXY_TITLE, GlobalConstants.HEADER_TEXT_SIZE));
        super.addView(GlobalConfig.getHeaderLine(context));
        super.addView(new TransparentHorizontalLine(context,
                GlobalConfig.MainSettingsConfig.getTransparentViewHeight()));
        super.addView(new BubbleHorizontalGradientLine(context));
        initAddress();
        super.addView(addressLay);
        super.addView(new BubbleHorizontalGradientLine(context));
        initPort();
        super.addView(portLay);
        super.addView(new BubbleHorizontalGradientLine(context));
        super.addView(new TransparentHorizontalLine(context,
                GlobalConfig.MainSettingsConfig.getTransparentViewHeight()));

    }

    private void initPort() {

        TextWatcher portWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                bufferStr = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                proxySet.setChecked(false);
                try {
                    if (!editable.toString().equals(GlobalConstants.EMPTY_STRING))
                        if (Integer.parseInt(editable.toString()) > MAX_PORT_VALUE) {
                            portForm.setText(GlobalConstants.EMPTY_STRING);
                            Toast.makeText(context, (PORT_ERROR_MESSAGE + MAX_PORT_VALUE), Toast.LENGTH_LONG).show();
                        }
                    if (!isNumber(editable)) portForm.setText(bufferStr);
                } catch (Exception ex) {
                    portForm.setText(bufferStr);
                }
            }
        };

        portLay = new LinearLayout(context);
        portForm = new ProxyEditText(context, PORT_LEN);
        clear = new SerifTextView(context, CLEAR_PROXY_TITLE);

        portForm.setPortLayParam();
        portForm.addTextChangedListener(portWatcher);

        portLay.setBackgroundColor(Color.TRANSPARENT);
        portLay.addView(new SerifTextView(context, PORT_TITLE));
        portLay.addView(portForm);
        portLay.addView(clear);

        clear.setOnClickListener(this);
        proxySet.setOnClickListener(this);
    }

    private void initAddress() {

        TextWatcher addressWatcher = new TextWatcher() {
            private byte indexer = GlobalConstants.ONE;
            private boolean errorFlag;
            private String bufferedStr;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                bufferedStr = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                proxySet.setChecked(false);
                errorFlag = true;
                for (byte i = 0; i < OCTETS_COUNT; i++) {
                    if (ipOctet[i].isFocused()) {
                        indexer = i;
                        break;
                    }
                }
                try {
                    if (Integer.parseInt(s.toString()) > MAX_IP_VALUE) {
                        ipOctet[indexer].setText(GlobalConstants.EMPTY_STRING);
                        indexer--;
                        Toast.makeText(context, IP_ERROR_MESSAGE + MAX_IP_VALUE, Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    //
                }
                if (!s.toString().equals(GlobalConstants.EMPTY_STRING) && !isNumber(s)) {
                    ipOctet[indexer].setText(bufferedStr);
                    errorFlag = false;
                }
                if (s.length() == OCTET_LEN && errorFlag) {
                    if (indexer != ipOctet.length - GlobalConstants.ONE) indexer++;
                    ipOctet[indexer].requestFocus();
                }
            }
        };

        proxySet = new CheckBox(context);
        proxySet.setBackgroundDrawable(GlobalConfig.MainSettingsConfig.getCheckBoxOnWhiteBackgroundCube());

        addressLay = new LinearLayout(context);
        addressLay.addView(new SerifTextView(context, IP_TITLE));
        addressLay.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        addressLay.setBackgroundColor(Color.TRANSPARENT);
        addressLay.setGravity(Gravity.CENTER_VERTICAL);

        for (byte i = 0; i < OCTETS_COUNT; i++) {
            ipOctet[i] = new ProxyEditText(context, OCTET_LEN);
            ipOctet[i].addTextChangedListener(addressWatcher);
            addressLay.addView(ipOctet[i]);
            if (i < OCTETS_COUNT - GlobalConstants.ONE)
                addressLay.addView(new SerifTextView(context, DOT));
        }
        addressLay.addView(proxySet);
    }

    public boolean isNumber(CharSequence str) {
        if (str.length() == 0) return true;
        byte result = 0;
        for (byte symbol = 48; symbol < 58; symbol++) // аски 0(48) - 9(57)
            for (byte i = 0; i < str.length(); i++)
                if (str.charAt(i) == (char) symbol) result++;
        return (result == str.length());
    }

    public void clearForms() {
        portForm.setText(GlobalConstants.EMPTY_STRING);
        for (ProxyEditText text : ipOctet) text.setText(GlobalConstants.EMPTY_STRING);
        ipOctet[0].requestFocus();
    }

    public String getAddress() {
        StringBuilder ipAddress = new StringBuilder();
        for (ProxyEditText actet : ipOctet) {
            ipAddress.append(actet.getText());
            ipAddress.append(DOT);
        }
        ipAddress.deleteCharAt(ipAddress.length() - GlobalConstants.ONE);
        return ipAddress.toString();
    }

    public boolean ipIsEmpty() {
        for (byte i = 0; i < OCTETS_COUNT; i++)
            try {
                if (ipOctet[i].getText().toString().equals(GlobalConstants.EMPTY_STRING))
                    return true;
            } catch (NullPointerException ex) {
                return true;
            }
        return false;
    }

    public boolean fistActetEqualsZero() {
        try {
            return (Integer.parseInt(ipOctet[0].getText().toString()) < GlobalConstants.ONE);
        } catch (NullPointerException ex) {
            return true;
        }
    }

    public void loadAddress(String kit) {
        StringBuilder buff = new StringBuilder().append(GlobalConstants.EMPTY_STRING);
        for (byte i = 0, k = 0; i < kit.length(); i++) {
            if (kit.charAt(i) == DOT.charAt(0)) {
                ipOctet[k++].setText(buff);
                buff = new StringBuilder();
                buff.append(GlobalConstants.EMPTY_STRING);
            } else
                buff.append(kit.charAt(i));
            if (kit.length() - GlobalConstants.ONE == i)
                ipOctet[k].setText(buff);
        }
    }

    @Override
    public void onClick(View view) {
        if (clear.equals(view)) {
            clearForms();
        } else if (view.equals(proxySet))
            proxyBoxLogic();
    }

    public void loadPort(String port) {
        portForm.setText(port);
    }

    public String getPort() {
        try {
            return portForm.getText().toString();
        } catch (NullPointerException ex) {
            return GlobalConstants.EMPTY_STRING;
        }
    }

    public boolean portIsEmpty() {
        try {
            return portForm.getText().toString().equals(GlobalConstants.EMPTY_STRING);
        } catch (NullPointerException ex) {
            return true;
        }
    }

    private void proxyBoxLogic() {
        if (proxySet.isChecked()) {
            if (noErrorsOnForms()) {
                Toast.makeText(context, PROXY_MESSAGE_ON, Toast.LENGTH_SHORT).show();
            } else proxySet.setChecked(false);
        } else
            Toast.makeText(context, PROXY_MESSAGE_OFF, Toast.LENGTH_SHORT).show();
    }

    private boolean noErrorsOnForms() {
        if (ipIsEmpty() || portIsEmpty()) {
            Toast.makeText(context, EMPTY_FIELD_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            return false;
        } else if (fistActetEqualsZero()) {
            Toast.makeText(context, FIRST_OCTET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean isProxySet() {
        return proxySet.isChecked();
    }

    public void setProxyCheckBoxState(boolean state) {
        proxySet.setChecked(state);
    }

    private class ProxyEditText extends EditText {

        public ProxyEditText(Context context, byte maxSymbolCount) {
            super(context);
            super.setInputType(InputType.TYPE_CLASS_PHONE);
            super.setGravity(Gravity.CENTER);
            super.setTextColor(GlobalConfig.MainSettingsConfig.getFormsTextColor());
            super.setLayoutParams(new ViewGroup.LayoutParams(
                    GlobalConfig.MainSettingsConfig.getOctetWidth(),
                    GlobalConfig.MainSettingsConfig.getOctetHeight()));
            super.setTextSize(GlobalConstants.DEFAULT_TEXT_SIZE);
            super.setText(GlobalConstants.EMPTY_STRING);
            super.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxSymbolCount)});
            super.setTypeface(Typeface.SERIF);
        }

        public void setPortLayParam() {
            super.setLayoutParams(new ViewGroup.LayoutParams(
                    GlobalConfig.MainSettingsConfig.getPortWidth(),
                    GlobalConfig.MainSettingsConfig.getPortHeight()));

        }
    }
}