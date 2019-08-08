package milu.kiriu2010.myapplication.a0x.a02

import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.text.TextPaint
import android.view.*

import milu.kiriu2010.myapplication.R

// ----------------------------------------
// PorterDuff
// ----------------------------------------
// https://tech.recruit-mp.co.jp/mobile/remember_canvas2/
// https://vividcode.hatenablog.com/entry/android-app/sdk/canvas-savelayer-and-xfermode
// ----------------------------------------
class A02Fragment : Fragment()
    , SurfaceHolder.Callback {

    // 描画に使うサーフェースビュー
    private lateinit var surfaceViewCanvas: SurfaceView

    // サーフェースビューの幅・高さ
    private var sw: Float = 0f
    private var sh: Float = 0f

    private val marginW = 50f
    private val marginH = 50f

    // ---------------------------------------------------------------------
    // 描画領域として使うビットマップ
    // ---------------------------------------------------------------------
    // 画面にタッチするとdrawが呼び出されるようなのでビットマップに描画する
    // ---------------------------------------------------------------------
    private lateinit var imageBitmap: Bitmap
    //private val tmpBitmap = Bitmap.createBitmap(intrinsicWidth,intrinsicHeight, Bitmap.Config.ARGB_8888)

    // 描画に使うハンドラ
    val handler = Handler()
    // 描画に使うスレッド
    private lateinit var runnable: Runnable

    // 十字架
    private val crossPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    // 円
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    // ダミー
    private val dummyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        textSize = 50f
        val typeFaceCur = typeface
        val typeFaceBold = Typeface.create(typeFaceCur,Typeface.BOLD)
        typeface = typeFaceBold
    }

    private val textPaint = TextPaint().apply {
        color = Color.BLACK
        textAlign = Paint.Align.LEFT
        textSize = 50f
        val typeFaceCur = typeface
        val typeFaceBold = Typeface.create(typeFaceCur,Typeface.BOLD)
        typeface = typeFaceBold
    }

    // テキストの位置を補正
    // https://blog.danlew.net/2013/10/03/centering_single_line_text_in_a_canvas/
    private val textHeight = textPaint.descent() - textPaint.ascent()
    //private val textOffset = textHeight/2 - textPaint.descent()
    private val textOffset = textHeight

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

        // ---------------------------------------
        // 十字架のビットマップを生成(プログラム)
        // ---------------------------------------
        val bmpCross1 = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888)
        val cvsCross = Canvas(bmpCross1)
        // 透明色でクリア
        cvsCross.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR)
        cvsCross.drawRect(RectF(40f, 0f, 60f,100f),crossPaint)
        cvsCross.drawRect(RectF( 0f,40f,100f, 60f),crossPaint)

        // ---------------------------------------
        // 円のビットマップを生成(プログラム)
        // ---------------------------------------
        val bmpCircle1 = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888)
        val cvsCircle = Canvas(bmpCircle1)
        // 透明色でクリア
        cvsCircle.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR)
        cvsCircle.drawCircle(50f,50f,40f,circlePaint)

        // ---------------------------------------
        // 十字架のビットマップを生成(リソース)
        // ---------------------------------------
        val bmpCross2 = BitmapFactory.decodeResource(resources,R.drawable.cross)

        // ---------------------------------------
        // 円のビットマップを生成(リソース)
        // ---------------------------------------
        val bmpCircle2 = BitmapFactory.decodeResource(resources,R.drawable.circle)

        // ---------------------------------------

        // バックグラウンドを描画
        canvas.drawColor(0xffcccccc.toInt())

        // ---------------------------------------
        // １行目
        // ---------------------------------------
        canvas.drawBitmap(bmpCross1,0f,0f,dummyPaint)
        canvas.drawBitmap(bmpCircle1,100f,0f,dummyPaint)

        // ---------------------------------------
        // ２行目(SRC)
        // ---------------------------------------
        canvas.saveLayer(0f,100f,sw,200f,dummyPaint)
        canvas.translate(100f,100f)

        // DST
        canvas.drawBitmap(bmpCross1,0f,0f,dummyPaint)
        dummyPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)

        // SRC
        canvas.drawBitmap(bmpCircle1,0f,0f,dummyPaint)
        dummyPaint.xfermode = null

        canvas.translate(200f,0f)
        canvas.drawText("SRC",0f,textOffset,textPaint)

        canvas.restore()

        // ---------------------------------------
        // ３行目(DST)
        // ---------------------------------------
        canvas.saveLayer(0f,200f,sw,300f,dummyPaint)
        canvas.translate(100f,200f)

        // DST
        canvas.drawBitmap(bmpCross1,0f,0f,dummyPaint)
        dummyPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST)

        // SRC
        canvas.drawBitmap(bmpCircle1,0f,0f,dummyPaint)
        dummyPaint.xfermode = null

        canvas.translate(200f,0f)
        canvas.drawText("DST",0f,textOffset,textPaint)

        canvas.restore()

        // ---------------------------------------
        // ４行目(SRC_OVER)
        // ---------------------------------------
        canvas.saveLayer(0f,300f,sw,400f,dummyPaint)
        canvas.translate(100f,300f)

        // DST
        canvas.drawBitmap(bmpCross1,0f,0f,dummyPaint)
        dummyPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)

        // SRC
        canvas.drawBitmap(bmpCircle1,0f,0f,dummyPaint)
        dummyPaint.xfermode = null

        canvas.translate(200f,0f)
        canvas.drawText("SRC_OVER",0f,textOffset,textPaint)

        canvas.restore()

        // ---------------------------------------
        // ５行目(DST_OVER)
        // ---------------------------------------
        canvas.saveLayer(0f,400f,sw,500f,dummyPaint)
        canvas.translate(100f,400f)

        // DST
        canvas.drawBitmap(bmpCross1,0f,0f,dummyPaint)
        dummyPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)

        // SRC
        canvas.drawBitmap(bmpCircle1,0f,0f,dummyPaint)
        dummyPaint.xfermode = null

        canvas.translate(200f,0f)
        canvas.drawText("DST_OVER",0f,textOffset,textPaint)

        canvas.restore()











        /*
        // ---------------------------------------
        // DST_OVER
        // ---------------------------------------
        canvas.translate(100f,0f)
        // DST
        canvas.drawBitmap(bmpCross1,0f,0f,dummyPaint)
        dummyPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)

        // SRC
        canvas.drawBitmap(bmpCircle1,0f,0f,dummyPaint)
        dummyPaint.xfermode = null

        // ---------------------------------------
        // SRC_IN
        // ---------------------------------------
        canvas.translate(100f,0f)
        // DST
        canvas.drawBitmap(bmpCross1,0f,0f,dummyPaint)
        dummyPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        // SRC
        canvas.drawBitmap(bmpCircle1,0f,0f,dummyPaint)
        dummyPaint.xfermode = null

        canvas.restore()
        */


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
            A02Fragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
