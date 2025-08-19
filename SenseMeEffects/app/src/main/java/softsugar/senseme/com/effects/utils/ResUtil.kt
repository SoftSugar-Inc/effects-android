package softsugar.senseme.com.effects.utils

import android.content.Context
import android.util.Log

/**
 * 反射的
 *
 * ：https://blog.csdn.net/strangenightmare/article/details/52704199
 */
class ResUtil {

    companion object {
        fun getLayoutId(paramContext: Context, paramString: String?): Int {
            return paramContext.resources.getIdentifier(
                paramString, "layout",
                paramContext.packageName
            )
        }

        // 找不到返回 0
        // 传null会crash
        fun getStringId(paramContext: Context, paramString: String?): Int {
            //Log.i("ResUtil", "getStringId paramString=" + paramString)
            val ret = paramContext.resources.getIdentifier(
                paramString, "string",
                paramContext.packageName
            )
            //Log.i("ResUtil", "getStringId ret=" + ret)
            return ret;
        }

        fun getDrawableId(paramContext: Context, paramString: String?): Int {
            return paramContext.resources.getIdentifier(
                paramString,
                "drawable", paramContext.packageName
            )
        }

        fun getStyleId(paramContext: Context, paramString: String?): Int {
            return paramContext.getResources().getIdentifier(
                paramString,
                "style", paramContext.packageName
            )
        }

        fun getId(paramContext: Context, paramString: String?): Int {
            return paramContext.resources
                .getIdentifier(paramString, "id", paramContext.packageName)
        }

        fun getColorId(paramContext: Context, paramString: String?): Int {
            return paramContext.resources.getIdentifier(
                paramString,
                "color", paramContext.packageName
            )
        }

        fun getArrayId(paramContext: Context, paramString: String?): Int {
            return paramContext.resources.getIdentifier(
                paramString,
                "array", paramContext.packageName
            )
        }
    }

}