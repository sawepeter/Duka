package com.example.devsawe.duka;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class Controller {

    public void toast(String msg, Context context, int icon) {
        StyleableToast st = new StyleableToast(context, msg, Toast.LENGTH_LONG);
        st.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

        st.setTextColor(context.getResources().getColor(R.color.whitetext));
        try {
            st.setIcon(icon);
            st.setDuration(Toast.LENGTH_SHORT);
            st.setMaxAlpha();
            st.show();
        } catch (Exception m) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public FloatingActionButton fab_cart(Activity activity, boolean show, int image) {

        final FloatingActionButton fab_cart = activity.findViewById(R.id.fab_cart);

        if (show) {
            fab_cart.setImageResource(image);
            fab_cart.show();
        } else {
            fab_cart.setImageResource(image);
            fab_cart.hide();
        }
        return fab_cart;
    }
}
