package milu.kiriu2010.anim.a0x.a03

import android.graphics.*
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.text.TextPaint
import android.view.*

import milu.kiriu2010.anim.R

// ----------------------------------------
// PorterDuff
// ----------------------------------------
// https://tech.recruit-mp.co.jp/mobile/remember_canvas2/
// https://vividcode.hatenablog.com/entry/android-app/sdk/canvas-savelayer-and-xfermode
// http://axolotlhp.web.fc2.com/kobanasi/kobanasi07.html
// ----------------------------------------
class A03Fragment : Fragment()
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
        //val bmpCross2 = BitmapFactory.decodeResource(resources,R.drawable.cross)

        // ---------------------------------------
        // 円のビットマップを生成(リソース)
        // ---------------------------------------
        //val bmpCircle2 = BitmapFactory.decodeResource(resources,R.drawable.circle)

        // ---------------------------------------

        // バックグラウンドを描画
        canvas.drawColor(0xffcccccc.toInt())

        // ---------------------------------------
        // １行目
        // ---------------------------------------
        canvas.drawBitmap(bmpCross1,0f,0f,dummyPaint)
        canvas.drawBitmap(bmpCircle1,100f,0f,dummyPaint)

        // ---------------------------------------
        // XOR(in save)
        // ---------------------------------------
        canvas.save()
        canvas.translate(200f,0f)

        // DST
        canvas.drawBitmap(bmpCross1,0f,0f,dummyPaint)
        dummyPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)

        // SRC
        canvas.drawBitmap(bmpCircle1,0f,0f,dummyPaint)
        dummyPaint.xfermode = null

        canvas.restore()


        // ---------------------------------------
        // XOR(in saveLayer)
        // ---------------------------------------
        canvas.saveLayer(300f,0f,400f,100f,dummyPaint)
        canvas.translate(300f,0f)

        // DST
        canvas.drawBitmap(bmpCross1,0f,0f,null)
        dummyPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)

        // SRC
        canvas.drawBitmap(bmpCircle1,0f,0f,dummyPaint)
        dummyPaint.xfermode = null

        canvas.restore()

        // ---------------------------------------
        // XOR(in save=>saveLayer)
        // ---------------------------------------
        canvas.save()
        canvas.saveLayer(400f,0f,500f,100f,dummyPaint)
        canvas.translate(400f,0f)

        // DST
        canvas.drawBitmap(bmpCross1,0f,0f,null)
        dummyPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)

        // SRC
        canvas.drawBitmap(bmpCircle1,0f,0f,dummyPaint)
        dummyPaint.xfermode = null

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
            A03Fragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
