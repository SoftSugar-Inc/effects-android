package softsugar.senseme.com.effects.view.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.databinding.ViewTabEffectsBinding
import softsugar.senseme.com.effects.entity.BasicEffectEntity
import softsugar.senseme.com.effects.utils.LocalDataStore

class TryOnBasicFragment @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs), TabTryOnViewI {

    private var mIndex = 0

    init {
        initView()
        initListener()
    }
    private lateinit var mBinding: ViewTabEffectsBinding

    private fun initView() {
        mBinding = ViewTabEffectsBinding.inflate(LayoutInflater.from(context), this, true)
        //val view = LayoutInflater.from(context).inflate(R.layout.view_tab_effects, this, true)

        onStyleClick(mBinding.llStyle1, false)
    }

    private fun initListener() {
        for (tab in arrayOf(mBinding.llStyle1, mBinding.llStyle2)) {
            tab.setOnClickListener {
                onStyleClick(it, true)
            }
        }
    }

    private fun noneSelectedStyle(view: View) {
        when (view) {
            mBinding.llStyle1 -> {
                mBinding.tvStyle1.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                mBinding.siStyle1.setStrokeColorResource(android.R.color.transparent)
            }
            mBinding.llStyle2 -> {
                mBinding.tvStyle2.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                mBinding.siStyle2.setStrokeColorResource(android.R.color.transparent)
            }
        }
        view.isSelected = false
        mListener?.onClickItemEffect(null)
    }

    private fun onStyleClick(view: View, callBack: Boolean) {
        if (view.isSelected) {
            noneSelectedStyle(view)
            return
        }

        mBinding.llStyle1.isSelected = false
        mBinding.llStyle2.isSelected = false
        mBinding.tvStyle2.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        mBinding.tvStyle1.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        for (tabView in arrayOf(mBinding.siStyle1, mBinding.siStyle2)) {
            tabView.setStrokeColorResource(android.R.color.transparent)
            tabView.isSelected = false
        }

        view.isSelected = true
        when (view) {
            mBinding.llStyle1 -> {
                mBinding.tvStyle1.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                mBinding.siStyle1.setStrokeColorResource(R.color.color_dd90fa)
                if (callBack)
                    mListener?.onClickItemEffect(LocalDataStore.getInstance().tryOnGirlDefParams)
            }
            mBinding.llStyle2 -> {
                mBinding.tvStyle2.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                mBinding.siStyle2.setStrokeColorResource(R.color.color_dd90fa)
                if (callBack)
                    mListener?.onClickItemEffect(LocalDataStore.getInstance().tryOnBoyDefParams)
            }
        }
    }

    private var mListener: Listener? = null

    fun setListener(listener: Listener) {
        mListener = listener
    }

    interface Listener {
        fun onClickItemEffect(params: MutableList<BasicEffectEntity>?)
    }
}