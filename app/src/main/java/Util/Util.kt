package Util

import android.content.Context
import android.content.Intent

class Util {
    fun openActivity(context: Context, target: Class<*>) {
        val intent = Intent(context, target)
        context.startActivity(intent)
    }
}