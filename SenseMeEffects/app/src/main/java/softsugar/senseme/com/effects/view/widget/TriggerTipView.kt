package softsugar.senseme.com.effects.view.widget

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import com.blankj.utilcode.util.LogUtils
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.softsugar.stmobile.STMobileHumanActionNative
import com.softsugar.stmobile.model.STMobileHandInfo
import com.softsugar.stmobile.sticker_module_types.STCustomEvent
import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.databinding.ViewTriggerTipBinding
import softsugar.senseme.com.effects.utils.MultiLanguageUtils
import kotlin.system.measureTimeMillis

class TriggerTipView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    companion object {
        private const val TAG = "TriggerTipView"
    }
    private lateinit var mBinding: ViewTriggerTipBinding

    private var mCustomEventTipsRunnable: Runnable? = null
    private var mTriggerTipsRunnable: Runnable? = null
    private val mTipsHandler = Handler()

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        val time = measureTimeMillis {
            mBinding = ViewTriggerTipBinding.inflate(LayoutInflater.from(context), this, true)
            //LayoutInflater.from(context).inflate(R.layout.view_trigger_tip, this, true)
        }
        LogUtils.i("TriggerTipView cost time $time ms")
    }

    private var mCustomEvent = 0L

    fun hasOneClick(): Boolean {
        val ret = ((mCustomEvent and STCustomEvent.ST_CUSTOM_EVENT_SCREEN_TAP) > 0)
        LogUtils.iTag(TAG, "hasOneClick() called ret: $ret")
        return ret
    }

    fun hasDoubleClick(): Boolean {
        val ret = ((mCustomEvent and STCustomEvent.ST_CUSTOM_EVENT_SCREEN_DOUBLE_TAP) > 0)
        LogUtils.iTag(TAG, "hasDoubleClick() called ret: $ret")
        return ret
    }

    fun showCustomEventTips(actionNum: Long) {
        mCustomEvent = actionNum
        LogUtils.iTag(TAG, "showActiveTips() called with: actionNum = $actionNum")
        if (actionNum != -1L && actionNum != 0L) {
            mBinding.llCustomEvent.visibility = VISIBLE
        }
        var triggerTips = ""
        mBinding.ivCustomTip.setImageDrawable(null)
        LogUtils.iTag(TAG, "showCustomEventTips() called with: actionNum = $actionNum")
        if ((actionNum and STCustomEvent.ST_CUSTOM_EVENT_SCREEN_TAP) > 0) {
            mBinding.ivCustomTip.setImageResource(R.drawable.ic_trigger_screen_tag_selected)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_screen_tap)
            LogUtils.iTag(TAG, "showActiveTips click")
        }
        if ((actionNum and STCustomEvent.ST_CUSTOM_EVENT_SCREEN_DOUBLE_TAP) > 0) {
            mBinding.ivCustomTip.setImageResource(R.drawable.ic_trigger_screen_tag_selected)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_screen_double_tap)
            LogUtils.iTag(TAG, "showActiveTips double click")
        }
        mBinding.tvCustomTip.text = triggerTips
        mBinding.llCustomEvent.visibility = VISIBLE
        mCustomEventTipsRunnable?.let {
            mTipsHandler.removeCallbacks(it)
        }
        mCustomEventTipsRunnable = Runnable { mBinding.llCustomEvent.visibility = GONE }
        mCustomEventTipsRunnable?.let {
            mTipsHandler.postDelayed(it, 2000)
        }
    }

    fun showActiveTips(actionNum: Long) {
        LogUtils.iTag(TAG, "showActiveTips() called with: actionNum = $actionNum")
        if (actionNum != -1L && actionNum != 0L) {
            mBinding.tvLayoutTips.visibility = VISIBLE
        }
        var triggerTips = ""
        mBinding.ivImageTips.setImageDrawable(null)
        if (actionNum and STMobileHumanActionNative.ST_MOBILE_EYE_BLINK > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_blink)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_blink)
        }
        if (actionNum and STMobileHumanActionNative.ST_MOBILE_MOUTH_AH > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_mouth)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_mouth)
        }
        if (actionNum and STMobileHumanActionNative.ST_MOBILE_HEAD_YAW > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_shake)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_head)
        }
        if (actionNum and STMobileHumanActionNative.ST_MOBILE_HEAD_PITCH > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_nod)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_put_nod)
        }
        if (actionNum and STMobileHumanActionNative.ST_MOBILE_BROW_JUMP > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_frown)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_jump)
        }
        if (actionNum and STMobileHumanActionNative.ST_MOBILE_FACE_LIPS_UPWARD > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_lips_upward)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_lips_upward)
        }
        if (actionNum and STMobileHumanActionNative.ST_MOBILE_FACE_LIPS_POUTED > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_lips_pouted)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_put_mouth)
        }
        if (actionNum and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_PALM > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_palm_selected)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_palm)
        }
        if (actionNum and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_LOVE > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_heart_hand_selected)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_heart)
        }
        if (actionNum and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_HOLDUP > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_palm_up_selected)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_hands)
        }
        if (actionNum and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_CONGRATULATE > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_congratulate_selected)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_hold_a_fist)
        }
        if (actionNum and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_FINGER_HEART > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_finger_heart_selected)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_put_one_hand)
        }
        if (actionNum and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_GOOD > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_thumb_selected)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_thumb)
        }
        if (actionNum and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_OK > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_ok_selected)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_ok)
        }
        if (actionNum and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_SCISSOR > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_scissor_selected)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_scissor)
        }
        if (actionNum and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_PISTOL > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_pistol_selected)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_pistol)
        }
        if (actionNum and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_FINGER_INDEX > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_one_finger_selected)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_finger)
        }
        if (actionNum and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_FIST > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_first_selected)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_raise)
        }
        if (actionNum and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_666 > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_sixsixsix_selected)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_666)
        }
        if (actionNum and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_BLESS > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_handbless_selected)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_bless)
        }
        if (actionNum and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_ILOVEYOU > 0) {
            mBinding.ivImageTips.setImageResource(R.drawable.ic_trigger_love_selected)
            triggerTips += MultiLanguageUtils.getStr(R.string.action_tip_love)
        }
        mBinding.tvTextTips.text = triggerTips
        mBinding.tvLayoutTips.visibility = VISIBLE
        mTriggerTipsRunnable?.let {
            mTipsHandler.removeCallbacks(it)
        }
        mTriggerTipsRunnable = Runnable { mBinding.tvLayoutTips.visibility = GONE }
        mTriggerTipsRunnable?.let {
            mTipsHandler.postDelayed(it, 2000)
        }
    }

}