package com.hard.function.tool;

import android.content.Context;
import android.content.Intent;

public class Utils {

    public static void startActivity(Context context, Class<?> cls){
        context.startActivity(new Intent(context,cls));
    }

}
