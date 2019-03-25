package com.example.devsawe.duka;

import android.content.Context;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class Controller {

    public void toast(String msg, Context context, int icon) {
        StyleableToast st = new StyleableToast(context, msg, Toast.LENGTH_LONG);
        st.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

        st.setTextColor(context.getResources().getColor(R.color.whitetext));
        try {
            st.setIcon(icon);
            st.setDuration(Toast.LENGTH_LONG);
            st.setMaxAlpha();
            st.show();
        } catch (Exception m) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }
}
