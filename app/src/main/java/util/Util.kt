package util

import android.content.Context
import android.content.Intent
import java.util.Objects

class Util {
    fun openActivity (context: Context,objClass: Class<*>){
         val intento= Intent(context,objClass)
        context.startActivity(intento)
    }
}