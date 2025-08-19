package softsugar.senseme.com.effects.utils

import android.view.View
import android.view.ViewGroup

object ReplaceViewHelper {

    fun toReplaceView(targetView: View?, containerView: ViewGroup?) {
        if (null == targetView) {
            removeAllViewsInLayout(containerView)
        }
        if (containerView == null || targetView == null) return

        if (containerView.visibility != View.VISIBLE)
            containerView.visibility = View.VISIBLE

        targetView.parent?.apply {
            if (this is ViewGroup) {
                this.removeAllViewsInLayout()
            }
        }
        containerView.removeAllViewsInLayout()
        containerView.addView(targetView)
    }

    fun removeAllViewsInLayout(containerView: ViewGroup?) {
        containerView?.apply {
            removeAllViewsInLayout()
        }
    }
}