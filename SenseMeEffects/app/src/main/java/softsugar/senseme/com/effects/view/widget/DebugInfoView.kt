package softsugar.senseme.com.effects.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.blankj.utilcode.util.LogUtils
import com.softsugar.stmobile.STMobileHumanActionNative
import com.softsugar.stmobile.model.STMobileHandInfo
import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.databinding.ViewDebugBinding
import kotlin.system.measureTimeMillis

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 2021/7/15 8:59 下午
 */
class DebugInfoView :LinearLayout {

    constructor(context: Context) : super(context) {
        initView(context)
    }
    //import kotlinx.android.synthetic.main.view_debug.view.*
    private lateinit var mBinding: ViewDebugBinding
    private fun initView(context: Context) {
        val time = measureTimeMillis {
            mBinding = ViewDebugBinding.inflate(LayoutInflater.from(context), this, true)
            // LayoutInflater.from(context).inflate(R.layout.view_debug, this, true)
        }
        LogUtils.i("DebugInfoView cost time $time ms")
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun resetFaceExpression() {
        mBinding.ivFaceExpressionHeadNormal.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_head_normal)
        )
        mBinding.ivFaceExpressionSideFaceLeft.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_side_face_left)
        )
        mBinding.ivFaceExpressionSideFaceRight.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_side_face_right)
        )
        mBinding.ivFaceExpressionTiltedFaceLeft.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_tilted_face_left)
        )
        mBinding.ivFaceExpressionTiltedFaceRight.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_tilted_face_right)
        )
        mBinding.ivFaceExpressionHeadRise.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_head_rise)
        )
        mBinding.ivFaceExpressionHeadLower.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_head_lower)
        )
        mBinding.ivFaceExpressionTwoEyeOpen.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_two_eye_open)
        )
        mBinding.ivFaceExpressionTwoEyeClose.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_two_eye_close)
        )
        mBinding.ivFaceExpressionLefteyeCloseRighteyeOpen.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_lefteye_close_righteye_open)
        )
        mBinding.ivFaceExpressionLefteyeOpenRighteyeClose.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_lefteye_open_righteye_close)
        )
        mBinding.ivFaceExpressionMouthOpen.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_mouth_open)
        )
        mBinding.ivFaceExpressionMouthClose.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_mouth_close)
        )
        mBinding.ivFaceExpressionFaceLipsPouted.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_face_lips_pouted)
        )
        mBinding.ivFaceExpressionFaceLipsUpward.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_face_lips_upward)
        )
        mBinding.ivFaceExpressionLipsCurlLeft.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_lips_curl_left)
        )
        mBinding.ivFaceExpressionLipsCurlRight.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_lips_curl_right)
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun showFaceExpressionInfo(faceExpressionInfo: BooleanArray?) {
        resetFaceExpression()
        if (faceExpressionInfo != null) {
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_HEAD_NORMAL.expressionCode]) {
                mBinding.ivFaceExpressionHeadNormal.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_head_normal_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_SIDE_FACE_LEFT.expressionCode]) {
                mBinding.ivFaceExpressionSideFaceLeft.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_side_face_left_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_SIDE_FACE_RIGHT.expressionCode]) {
                mBinding.ivFaceExpressionSideFaceRight.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_side_face_right_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_TILTED_FACE_LEFT.expressionCode]) {
                mBinding.ivFaceExpressionTiltedFaceLeft.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_tilted_face_left_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_TILTED_FACE_RIGHT.expressionCode]) {
                mBinding.ivFaceExpressionTiltedFaceRight.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_tilted_face_right_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_HEAD_RISE.expressionCode]) {
                mBinding.ivFaceExpressionHeadRise.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_head_rise_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_HEAD_LOWER.expressionCode]) {
                mBinding.ivFaceExpressionHeadLower.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_head_lower_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_TWO_EYE_OPEN.expressionCode]) {
                mBinding.ivFaceExpressionTwoEyeOpen.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_two_eye_open_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_TWO_EYE_CLOSE.expressionCode]) {
                mBinding.ivFaceExpressionTwoEyeClose.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_two_eye_close_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_LEFTEYE_CLOSE_RIGHTEYE_OPEN.expressionCode]) {
                mBinding.ivFaceExpressionLefteyeCloseRighteyeOpen.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_lefteye_close_righteye_open_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_LEFTEYE_OPEN_RIGHTEYE_CLOSE.expressionCode]) {
                mBinding.ivFaceExpressionLefteyeOpenRighteyeClose.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_lefteye_open_righteye_close_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_MOUTH_OPEN.expressionCode]) {
                mBinding.ivFaceExpressionMouthOpen.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_mouth_open_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_MOUTH_CLOSE.expressionCode]) {
                mBinding.ivFaceExpressionMouthClose.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_mouth_close_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_FACE_LIPS_POUTED.expressionCode]) {
                mBinding.ivFaceExpressionFaceLipsPouted.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_face_lips_pouted_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_FACE_LIPS_UPWARD.expressionCode]) {
                mBinding.ivFaceExpressionFaceLipsUpward.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_face_lips_upward_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_FACE_LIPS_CURL_LEFT.expressionCode]) {
                mBinding.ivFaceExpressionLipsCurlLeft.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_lips_curl_left_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_FACE_LIPS_CURL_RIGHT.expressionCode]) {
                mBinding.ivFaceExpressionLipsCurlRight.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_lips_curl_right_selected)
                )
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun resetHandActionInfo() {
        mBinding.ivPalm.setBackgroundColor(Color.parseColor("#00000000"))
        mBinding.ivPalm.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_palm)
        )
        mBinding.ivThumb.setBackgroundColor(Color.parseColor("#00000000"))
        mBinding.ivThumb.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_thumb)
        )
        mBinding.ivOk.setBackgroundColor(Color.parseColor("#00000000"))
        mBinding.ivOk.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_ok)
        )
        mBinding.ivPistol.setBackgroundColor(Color.parseColor("#00000000"))
        mBinding.ivPistol.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_pistol)
        )
        mBinding.ivOneFinger.setBackgroundColor(Color.parseColor("#00000000"))
        mBinding.ivOneFinger.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_one_finger)
        )
        mBinding.ivFingerHeart.setBackgroundColor(Color.parseColor("#00000000"))
        mBinding.ivFingerHeart.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_finger_heart)
        )
        mBinding.ivHeartHand.setBackgroundColor(Color.parseColor("#00000000"))
        mBinding.ivHeartHand.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_heart_hand)
        )
        mBinding.ivScissor.setBackgroundColor(Color.parseColor("#00000000"))
        mBinding.ivScissor.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_scissor)
        )
        mBinding.ivCongratulate.setBackgroundColor(Color.parseColor("#00000000"))
        mBinding.ivCongratulate.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_congratulate)
        )
        mBinding.ivPalmUp.setBackgroundColor(Color.parseColor("#00000000"))
        mBinding.ivPalmUp.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_palm_up)
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun showHandActionInfo(action: Long) {
        val mColorBlue = Color.parseColor("#0a8dff")
        resetHandActionInfo()
        if (action and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_PALM > 0) {
            mBinding.ivPalm.setBackgroundColor(mColorBlue)
            mBinding.ivPalm.setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_palm_selected)
            )
        } else if (action and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_GOOD > 0) {
            mBinding.ivThumb.setBackgroundColor(mColorBlue)
            mBinding.ivThumb.setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_thumb_selected)
            )
        } else if (action and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_OK > 0) {
            mBinding.ivOk.setBackgroundColor(mColorBlue)
            mBinding.ivOk.setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_ok_selected)
            )
        } else if (action and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_PISTOL > 0) {
            mBinding.ivPistol.setBackgroundColor(mColorBlue)
            mBinding.ivPistol.setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_pistol_selected)
            )
        } else if (action and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_FINGER_INDEX > 0) {
            mBinding.ivOneFinger.setBackgroundColor(mColorBlue)
            (mBinding.ivOneFinger as ImageView).setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_one_finger_selected)
            )
        } else if (action and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_FINGER_HEART > 0) {
            mBinding.ivFingerHeart.setBackgroundColor(mColorBlue)
            mBinding.ivFingerHeart.setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_finger_heart_selected)
            )
        } else if (action and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_LOVE > 0) {
            mBinding.ivHeartHand.setBackgroundColor(mColorBlue)
            mBinding.ivHeartHand.setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_heart_hand_selected)
            )
        } else if (action and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_SCISSOR > 0) {
            mBinding.ivScissor.setBackgroundColor(mColorBlue)
            (mBinding.ivScissor as ImageView).setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_scissor_selected)
            )
        } else if (action and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_CONGRATULATE > 0) {
            mBinding.ivCongratulate.setBackgroundColor(mColorBlue)
            mBinding.ivCongratulate.setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_congratulate_selected)
            )
        } else if (action and STMobileHandInfo.STMobileHandActionType.ST_HAND_ACTION_TYPE_HOLDUP > 0) {
            mBinding.ivPalmUp.setBackgroundColor(mColorBlue)
            mBinding.ivPalmUp.setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_palm_up_selected)
            )
        }
    }

    fun showDebug(show: Boolean) {
        mBinding.rlTestLayout.visibility = View.VISIBLE
        mBinding.llFaceExpression.visibility = View.VISIBLE
        mBinding.llHandActionInfo.visibility = View.VISIBLE
        mBinding.testSwitch0.visibility = VISIBLE
        mBinding.testSwitch1.visibility = VISIBLE
        mBinding.testSwitch2.visibility = VISIBLE
    }
}