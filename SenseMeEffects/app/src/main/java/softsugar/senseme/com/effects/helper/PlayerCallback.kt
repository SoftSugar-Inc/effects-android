package softsugar.senseme.com.effects.helper

interface PlayerCallback {
    fun onPrepare()

    fun onError(what: Int, extra: Int): Boolean

    fun onRenderFirstFrame()

    fun onCompleteListener()
}