package softsugar.senseme.com.effects.entity

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 2021/8/20 1:55 下午
 */
data class BasicEffectEntity(
    val des: String,
    val type: Int,
    val strength: Float,
    val mode: Int
) {
    override fun toString(): String {
        return "BasicEffectEntity(des='$des', type=$type, strength=$strength, mode=$mode)"
    }
}