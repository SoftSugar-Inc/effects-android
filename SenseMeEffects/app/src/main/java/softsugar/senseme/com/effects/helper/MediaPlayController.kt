package softsugar.senseme.com.effects.helper

import android.media.MediaPlayer
import android.util.Log
import android.view.Surface
import java.io.IOException

open class MediaPlayController(playerCallback: PlayerCallback) {

    private var mediaPlayer: MediaPlayer? = null
    private var renderFirstFrame: Boolean = false
    private var playerCallback: PlayerCallback? = null

    init {
        this.playerCallback = playerCallback
    }

    fun initPlayer(filePath: String, surface: Surface) {
        if (mediaPlayer != null) {
            releasePlayer()
        }
        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setDataSource(filePath)
            mediaPlayer!!.setSurface(surface)
            setListener()
            mediaPlayer!!.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }

    fun pausePlay() {
        if (mediaPlayer == null)
            return
        if (mediaPlayer!!.isPlaying) {
            mediaPlayer!!.pause()
        }
    }

    fun resumePlay() {
        if (mediaPlayer == null)
            return;
        if (mediaPlayer!!.isPlaying) {

        }
    }

    private fun setListener() {
        mediaPlayer!!.setOnPreparedListener {
            mediaPlayer!!.start()
        }
        mediaPlayer!!.setOnInfoListener { mp: MediaPlayer?, what: Int, extra: Int ->
            (
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        if (!renderFirstFrame) {
                            renderFirstFrame = true
                            playerCallback?.onRenderFirstFrame()
                        }
                    })
            return@setOnInfoListener true
        }
        mediaPlayer!!.setOnErrorListener(object:MediaPlayer.OnErrorListener {
            override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
                Log.d("GreenSeg", "onError() called with: p0 = $p0, p1 = $p1, p2 = $p2")
                return false
            }

        })


    }

    fun releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }
}