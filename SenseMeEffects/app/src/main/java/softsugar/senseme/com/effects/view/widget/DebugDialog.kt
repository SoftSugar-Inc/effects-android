package softsugar.senseme.com.effects.view.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import com.blankj.utilcode.util.ActivityUtils
import softsugar.senseme.com.effects.R

class DebugDialog(context: Context) : Dialog(context) {

    private var mListener: Listener? = null

    companion object {
        fun show(listener: Listener) {
            DebugDialog(ActivityUtils.getTopActivity()).apply {
                setOnClickListener(listener)
                show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_debug)
        initView()
        bindEvent()
    }

    private fun bindEvent() {
        findViewById<Button>(R.id.btn_zero).setOnClickListener {
            mListener?.onDebugTest1()
            dismiss()
        }
    }

    private fun initView() {

    }

    fun setOnClickListener(listener: Listener) {
        mListener = listener
    }

    interface Listener {
        // 调试xx
        fun onDebugTest1() {}
        fun onDebugTest2() {}
        fun onDebugTest3() {}
    }

}