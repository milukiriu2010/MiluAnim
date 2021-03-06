package milu.kiriu2010.anim.a0x.a01

import android.graphics.*
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.*

import milu.kiriu2010.anim.R

// ----------------------------------------
// PathEffect
// ----------------------------------------
class A01Fragment : Fragment()
    , SurfaceHolder.Callback {

    // 描画に使うサーフェースビュー
    private lateinit var surfaceViewCanvas: SurfaceView

    // サーフェースビューの幅・高さ
    private var sw: Float = 0f
    private var sh: Float = 0f

    private val marginW = 50f
    private val marginH = 50f

    // 描画に使うハンドラ
    val handler = Handler()
    // 描画に使うスレッド
    private lateinit var runnable: Runnable

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_a01, container, false)

        // サーフェースビューを取得
        surfaceViewCanvas = view.findViewById(R.id.surfaceViewCanvas)

        val holder = surfaceViewCanvas.holder
        holder.addCallback(this)

        runnable = Runnable {
            drawCanvas()
        }
        handler.post(runnable)

        return view
    }

    // 描画
    private fun drawCanvas() {
        val canvas = surfaceViewCanvas.holder.lockCanvas()
        if (canvas == null) return

        // バックグラウンドを描画
        canvas.drawColor(Color.WHITE)


        // --------------------------------------------------------------------
        // PathDashPathEffectで使うパス
        // --------------------------------------------------------------------

        // パス円
        val pathCircle = Path()
        // CCW => 反時計回り
        pathCircle.addCircle(10f,10f,10f, Path.Direction.CCW)

        // 円弧
        val pathArc = Path()
        pathArc.addArc(RectF(0f,0f,20f,20f),0f,180f)

        // 四角
        val pathRect = Path()
        pathRect.addRect(RectF(0f,0f,20f,20f), Path.Direction.CCW)

        // --------------------------------------------------------------------
        // 通常の実線
        // --------------------------------------------------------------------
        var n = 1f
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        // --------------------------------------------------------------------
        // DashPathEffectのサンプル
        // --------------------------------------------------------------------

        // 100,50,位相0点線
        val dashPath1 = DashPathEffect(floatArrayOf(100f,50f),0f)
        linePaint.setPathEffect(dashPath1)
        n = 2f
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        // 100,50,位相20点線(左に20ずれる)
        val dashPath2 = DashPathEffect(floatArrayOf(100f,50f),20f)
        linePaint.setPathEffect(dashPath2)
        n = 3f
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        // 100,50,位相-20点線(右に20ずれる)
        val dashPath3 = DashPathEffect(floatArrayOf(100f,50f),-20f)
        linePaint.setPathEffect(dashPath3)
        n = 4f
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        // 100,50,位相150点線(左に150ずれる)
        val dashPath4 = DashPathEffect(floatArrayOf(100f,50f),150f)
        linePaint.setPathEffect(dashPath4)
        n = 5f
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        // --------------------------------------------------------------------
        // PathDashPathEffectのサンプル
        // --------------------------------------------------------------------

        // 50ごと,位相0 円線
        val pathDashPathEffect1 = PathDashPathEffect(pathCircle,50f,0f, PathDashPathEffect.Style.ROTATE)
        linePaint.setPathEffect(pathDashPathEffect1)
        n = 6f
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        // 100ごと,位相0 円線
        val pathDashPathEffect2 = PathDashPathEffect(pathCircle,100f,0f, PathDashPathEffect.Style.ROTATE)
        linePaint.setPathEffect(pathDashPathEffect2)
        n = 7f
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        // 100ごと,位相-10 円線(右に10ずれる)
        val pathDashPathEffect3 = PathDashPathEffect(pathCircle,100f,-10f, PathDashPathEffect.Style.ROTATE)
        linePaint.setPathEffect(pathDashPathEffect3)
        n = 8f
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        // 100ごと,位相-10 円弧線(右に10ずれる)
        val pathDashPathEffect4 = PathDashPathEffect(pathArc,100f,-10f, PathDashPathEffect.Style.ROTATE)
        linePaint.setPathEffect(pathDashPathEffect4)
        n = 9f
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        // 100ごと,位相-10 四角線(右に10ずれる)
        val pathDashPathEffect5 = PathDashPathEffect(pathRect,100f,-10f, PathDashPathEffect.Style.ROTATE)
        linePaint.setPathEffect(pathDashPathEffect5)
        n = 10f
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        // --------------------------------------------------------------------
        // DiscretePathEffectのサンプル
        // --------------------------------------------------------------------

        val discretePathEffect1 = DiscretePathEffect(0f,0f)
        linePaint.setPathEffect(discretePathEffect1)
        n = 11f
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        val discretePathEffect2 = DiscretePathEffect(50f,0f)
        linePaint.setPathEffect(discretePathEffect2)
        n = 12f
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        val discretePathEffect3 = DiscretePathEffect(50f,25f)
        linePaint.setPathEffect(discretePathEffect3)
        n = 13f
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        val discretePathEffect4 = DiscretePathEffect(50f,50f)
        linePaint.setPathEffect(discretePathEffect4)
        n = 14f
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        val discretePathEffect5 = DiscretePathEffect(50f,100f)
        linePaint.setPathEffect(discretePathEffect5)
        n = 15f
        canvas.drawLine(marginW,n*marginH,sw-marginW,n*marginH,linePaint)

        // --------------------------------------------------------------------
        // 円弧を120度ずつ描く(stroke,useCenter=true)
        // --------------------------------------------------------------------
        linePaint.pathEffect = null
        n = 16f
        canvas.save()
        canvas.translate(0f,n*marginH)
        (0..9).forEach { i ->
            val ii = i.toFloat()
            canvas.drawArc(0f,0f,marginW,marginH,ii*60f,120f,true,linePaint)
            canvas.translate(marginW,0f)
        }
        canvas.restore()

        // --------------------------------------------------------------------
        // 円弧を120度ずつ描く(stroke,useCenter=false)
        // --------------------------------------------------------------------
        n = 17f
        canvas.save()
        canvas.translate(0f,n*marginH)
        (0..9).forEach { i ->
            val ii = i.toFloat()
            canvas.drawArc(0f,0f,marginW,marginH,ii*60f,120f,false,linePaint)
            canvas.translate(marginW,0f)
        }
        canvas.restore()

        // --------------------------------------------------------------------
        // 円弧を120度ずつ描く(fill,useCenter=true)
        // --------------------------------------------------------------------
        n = 18f
        canvas.save()
        canvas.translate(0f,n*marginH)
        (0..9).forEach { i ->
            val ii = i.toFloat()
            canvas.drawArc(0f,0f,marginW,marginH,ii*60f,120f,true,fillPaint)
            canvas.translate(marginW,0f)
        }
        canvas.restore()

        // --------------------------------------------------------------------
        // 円弧を120度ずつ描く(fill,useCenter=false)
        // --------------------------------------------------------------------
        n = 19f
        canvas.save()
        canvas.translate(0f,n*marginH)
        (0..9).forEach { i ->
            val ii = i.toFloat()
            canvas.drawArc(0f,0f,marginW,marginH,ii*60f,120f,false,fillPaint)
            canvas.translate(marginW,0f)
        }
        canvas.restore()

        surfaceViewCanvas.holder.unlockCanvasAndPost(canvas)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        // サーフェースビューの幅・高さを取得
        sw = width.toFloat()
        sh = height.toFloat()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            A01Fragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
