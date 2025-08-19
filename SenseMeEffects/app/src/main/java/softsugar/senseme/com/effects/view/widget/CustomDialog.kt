package softsugar.senseme.com.effects.view.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.blankj.utilcode.util.LogUtils
import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.databinding.DialogCustomBinding

class CustomDialog(context: Context) : Dialog(context) {
    companion object {
        const val TAG = "CustomDialog"
        const val DLG_TYPE_1 = 0
        const val DLG_TYPE_2 = 1
    }
    private lateinit var mBinding: DialogCustomBinding

    private var mTitle: String = ""
    private var mContent: String = ""
    private var mDlgType = 0

    // 定义标题，内容
    constructor(context: Context, title: String, content: String, type: Int, listener:OnClickListener) : this(context) {
        mTitle = title
        mContent = content
        mDlgType = type
        mListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogCustomBinding.inflate(LayoutInflater.from(context))
        //setContentView(R.layout.dialog_custom)
        setContentView(mBinding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initView()
    }

    private fun initView() {
        mBinding.tvTitle.text = mTitle
        mBinding.tvContent.text = mContent
        mBinding.tvOk.setOnClickListener {
            dismiss()
            LogUtils.iTag(TAG, "点击了---")
            mListener?.onClickOk()
        }

        when (mDlgType) {
            0 -> {
                mBinding.tvCancel.visibility = View.GONE
            }
            1 -> {
                mBinding.tvCancel.visibility = View.VISIBLE
                mBinding.tvOk.visibility = View.VISIBLE
            }
        }
    }

    private var mListener: OnClickListener? = null

    fun setOnClickListener(listener: OnClickListener) {
        mListener = listener
    }

    interface OnClickListener {
        fun onClickOk() {}
        fun onClickCancel() {}
    }
}

