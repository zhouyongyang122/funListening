package com.alipay.sdk.pay;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

/**
 * Created by Alan on 2016/9/19.
 */
public class AlipayModule  {

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2016080801717843";

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "";
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAOJN1hfw9todSJXF3PP5hvGApvahXwWLcCd6JL8HM/fQVkvX6lHtwxJ8ExIL0gJVAQkFOioTk/9mqsednF0W+UtS0BawDE1hRayMTVHTEhRxsm6zHSLSa8HABwt5wjtnoTpLJkfYkQpkcCbpg+wThKUP/ywxZGDOr89EOw02UQZ1AgMBAAECgYAA3BzZIHA/qebRstvBDYymtD9uJnTO95WogL1NAIEBCwiY5ywbvZSFKHWGFwdoPNxxJVTwPfzM9p3lfkLO07yEu+qWly9Oer7d063p9u61/VsyVEN7O4ffCZXgIe2Ksc1TaiZxGVQTaj3ujmMLr/593CAfCzK+hu+ul9NW/EaowQJBAPDiOozbTkdjwQEYhcH/xCJK0jR8q6/0LkGqCQYTvybFsNiAfGPbdfVoyqxEHnJb6gBg/w0s569Bzcp7HMQat7ECQQDwgWMNtPCnaAOoQodXURvOsvyqAJkMxSDnUf2DcCifbEcoxWj+S/Kxf9tlwUTZhWk0pjotsAtxdrbt/tkxznAFAkAZ3G/kx3az+2WxNGzH0ym/dMD0mZ3lGhYNPrARUvVmuDqwQ7sXDWywmDuKGxYZuPx6Ze97/qIxEqOBZY5FD4lBAkApvHphDY5Zrs47q9fqyjOGKj94jQQCWE8+dVcGMTeLevHaXG+8+ZcTQHnlbaWyDdnU/ifId+10ckKKiJhOcWG1AkBw80743RtzeVhS7vlV0HTU9VcTK19YTvmMD/UiBCqSyU3wvutbwXBdUP9PudcAOXTVqfyIeG650RfJkSN3ao7G";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    Activity activity;

    public AlipayModule(Activity activity) {
        this.activity =activity;
    }

    public void pay(final String options) {
        Log.i("__________1111:", options.toString());
        if (options == null) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(options, true);
                Log.i("msp", result.toString());
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    }
}
