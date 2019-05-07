package com.hard.function.tool;

import android.content.Context;
import android.widget.Toast;

import com.hard.function.R;

public class PrintOut {

    public static void printToast(Context context, String string){
        Toast.makeText(context,string, Toast.LENGTH_SHORT).show();
    }
}
