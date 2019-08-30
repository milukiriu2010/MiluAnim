package milu.kiriu2010.myapplication.a0x.a04

import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.text.TextPaint
import android.view.*
import milu.kiriu2010.gui.basic.MyPointF

import milu.kiriu2010.myapplication.R

// ----------------------------------------
// saveLayer
// ----------------------------------------
class A04Fragment : Fragment()
    , SurfaceHolder.Callback {

    // 描画に使うサーフェースビュー
    private lateinit var surfaceViewCanvas: SurfaceView

    // サーフェースビューの幅・高さ
    private var sw: Float = 0f
    private var sh: Float = 0f

    private val marginW = 0f
    private val marginH = 0f

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

    enum class Mode {
        PH1,
        PH2,
        PH3,
        PH4,
        PH5,
        PH6
    }

    // 現在のモード
    private var modeNow = Mode.PH1

    // 領域分割
    private val splitW  = 5f
    private val splitWN = splitW.toInt()
    private val splitH  = 7.5f
    private val splitHN = (splitH.toInt()+1)

    // ---------------------------------
    // 国旗
    // ---------------------------------
    //private val flagW = side/splitW
    private val flagW = 300f
    private val flagWX = flagW*0.5f
    private val flagW1 = 270f
    private val flagW2 = flagW1*0.5f
    private val flagW6 = flagW1/6f
    //private val flagH = side/splitH
    private val flagH = 210f
    private val flagHX = flagH*0.5f
    private val flagH1 = 180f
    private val flagH2 = flagH1*0.5f

    // 境界
    private val bw = 36f

    // 国旗の移動比率
    private val ratioMax = 1f
    private val ratioMin = 0f
    private var ratioNow = ratioMin
    private val ratioDv = 0.1f
    private val angle = 90f

    // パスの初期化を実施したかどうか
    private var isInitialized = false

    // カメルーン国旗の四角形リスト
    private val square0Lst = mutableListOf<Square>()

    // セネガル国旗の四角形リスト
    private val square1Lst = mutableListOf<Square>()

    // ---------------------------------
    // ペイント赤
    // ---------------------------------
    private val redPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    // -------------------------------
    // ペイント黄色
    // -------------------------------
    private val yellowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
        style = Paint.Style.FILL
    }

    // ---------------------------------
    // ペイント緑
    // ---------------------------------
    private val greenPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xff008800.toInt()
        style = Paint.Style.FILL
    }

    // ---------------------------------
    // ペイント黒
    // ---------------------------------
    private val blackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
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
            // パスの初期化
            createPath()
            // 移動する
            movePath()
            drawBitmap()
            drawCanvas()
        }
        handler.post(runnable)

        return view
    }

    // -------------------------------
    // パスの初期化
    // -------------------------------
    private fun createPath() {
        if ( (ratioNow > ratioMin) and (ratioNow < ratioMax) ) return

        ratioNow = ratioMin

        // モードを設定
        if (isInitialized) {
            modeNow = when (modeNow) {
                Mode.PH1 -> Mode.PH2
                Mode.PH2 -> Mode.PH3
                Mode.PH3 -> Mode.PH4
                Mode.PH4 -> Mode.PH5
                Mode.PH5 -> Mode.PH6
                Mode.PH6 -> Mode.PH1
            }
        }

        // パス生成
        when (modeNow) {
            Mode.PH1 -> createPathPH1()
            Mode.PH2 -> createPathPH2()
            Mode.PH3 -> createPathPH3()
            Mode.PH4 -> createPathPH4()
            Mode.PH5 -> createPathPH5()
            Mode.PH6 -> createPathPH6()
        }


        // パスの初期化を実施したかどうか
        isInitialized = true
    }

    // パス生成(PH1)
    private fun createPathPH1() {
        square0Lst.clear()
        square1Lst.clear()

        // 右下
        val square0 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW2,0f))
            square.ps.add(MyPointF(flagW2,flagH2))
            square.ps.add(MyPointF(flagW6,flagH2))
            square.paint = greenPaint
        }
        square0Lst.add(square0)
        square1Lst.add(square0.copy().also {
            it.paint = redPaint
        })

        // 右上
        val square1 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW6,-flagH2))
            square.ps.add(MyPointF(flagW2,-flagH2))
            square.ps.add(MyPointF(flagW2,0f))
            square.paint = greenPaint
        }
        square0Lst.add(square1)
        square1Lst.add(square1.copy().also {
            it.paint = redPaint
        })

        // 左上
        val square2 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,-flagH2))
            square.ps.add(MyPointF(-flagW2,-flagH2))
            square.ps.add(MyPointF(-flagW2,0f))
            square.paint = redPaint
        }
        square0Lst.add(square2)
        square1Lst.add(square2.copy().also {
            it.paint = greenPaint
        })

        // 左下
        val square3 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,flagH2))
            square.ps.add(MyPointF(-flagW2,flagH2))
            square.ps.add(MyPointF(-flagW2,0f))
            square.paint = redPaint
        }
        square0Lst.add(square3)
        square1Lst.add(square3.copy().also {
            it.paint = greenPaint
        })
    }

    // パス生成(PH2)
    private fun createPathPH2() {
        square0Lst.clear()
        square1Lst.clear()

        // 右上
        val square0 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW6,-flagH2))
            square.ps.add(MyPointF(flagW2,-flagH2))
            square.ps.add(MyPointF(flagW2,0f))
            square.paint = greenPaint
        }
        square0Lst.add(square0)
        square1Lst.add(square0.copy().also {
            it.paint = redPaint
        })

        // 上
        val square1 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW6,-flagH2))
            square.ps.add(MyPointF(-flagW6,-flagH2))
            square.paint = greenPaint
        }
        square0Lst.add(square1)
        square1Lst.add(square1.copy().also {
            it.paint = redPaint
        })

        // 左下
        val square2 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,flagH2))
            square.ps.add(MyPointF(-flagW2,flagH2))
            square.ps.add(MyPointF(-flagW2,0f))
            square.paint = redPaint
        }
        square0Lst.add(square2)
        square1Lst.add(square2.copy().also {
            it.paint = greenPaint
        })


        // 下
        val square3 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,flagH2))
            square.ps.add(MyPointF(flagW6,flagH2))
            square.paint = redPaint
        }
        square0Lst.add(square3)
        square1Lst.add(square3.copy().also {
            it.paint = greenPaint
        })
    }

    // パス生成(PH3)
    private fun createPathPH3() {
        square0Lst.clear()
        square1Lst.clear()

        // 上
        val square0 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW6,-flagH2))
            square.ps.add(MyPointF(-flagW6,-flagH2))
            square.paint = greenPaint
        }
        square0Lst.add(square0)
        square1Lst.add(square0.copy().also {
            it.paint = redPaint
        })

        // 左上
        val square1 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,-flagH2))
            square.ps.add(MyPointF(-flagW2,-flagH2))
            square.ps.add(MyPointF(-flagW2,0f))
            square.paint = greenPaint
        }
        square0Lst.add(square1)
        square1Lst.add(square1.copy().also {
            it.paint = redPaint
        })

        // 下
        val square2 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,flagH2))
            square.ps.add(MyPointF(flagW6,flagH2))
            square.paint = redPaint
        }
        square0Lst.add(square2)
        square1Lst.add(square2.copy().also {
            it.paint = greenPaint
        })

        // 右下
        val square3 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW6,flagH2))
            square.ps.add(MyPointF(flagW2,flagH2))
            square.ps.add(MyPointF(flagW2,0f))
            square.paint = redPaint
        }
        square0Lst.add(square3)
        square1Lst.add(square3.copy().also {
            it.paint = greenPaint
        })

    }

    // パス生成(PH4)
    private fun createPathPH4() {
        square0Lst.clear()
        square1Lst.clear()

        // 左上
        val square0 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,-flagH2))
            square.ps.add(MyPointF(-flagW2,-flagH2))
            square.ps.add(MyPointF(-flagW2,0f))
            square.paint = greenPaint
        }
        square0Lst.add(square0)
        square1Lst.add(square0.copy().also {
            it.paint = redPaint
        })

        // 左下
        val square1 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW2,0f))
            square.ps.add(MyPointF(-flagW2,flagH2))
            square.ps.add(MyPointF(-flagW6,flagH2))
            square.paint = greenPaint
        }
        square0Lst.add(square1)
        square1Lst.add(square1.copy().also {
            it.paint = redPaint
        })

        // 右下
        val square2 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW6,flagH2))
            square.ps.add(MyPointF(flagW2,flagH2))
            square.ps.add(MyPointF(flagW2,0f))
            square.paint = redPaint
        }
        square0Lst.add(square2)
        square1Lst.add(square2.copy().also {
            it.paint = greenPaint
        })

        // 右上
        val square3 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW2,0f))
            square.ps.add(MyPointF(flagW2,-flagH2))
            square.ps.add(MyPointF(flagW6,-flagH2))
            square.paint = redPaint
        }
        square0Lst.add(square3)
        square1Lst.add(square3.copy().also {
            it.paint = greenPaint
        })

    }

    // パス生成(PH5)
    private fun createPathPH5() {
        square0Lst.clear()
        square1Lst.clear()

        // 左下
        val square0 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW2,0f))
            square.ps.add(MyPointF(-flagW2,flagH2))
            square.ps.add(MyPointF(-flagW6,flagH2))
            square.paint = greenPaint
        }
        square0Lst.add(square0)
        square1Lst.add(square0.copy().also {
            it.paint = redPaint
        })

        // 下
        val square1 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,flagH2))
            square.ps.add(MyPointF(flagW6,flagH2))
            square.paint = greenPaint
        }
        square0Lst.add(square1)
        square1Lst.add(square1.copy().also {
            it.paint = redPaint
        })

        // 右上
        val square2 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW2,0f))
            square.ps.add(MyPointF(flagW2,-flagH2))
            square.ps.add(MyPointF(flagW6,-flagH2))
            square.paint = redPaint
        }
        square0Lst.add(square2)
        square1Lst.add(square2.copy().also {
            it.paint = greenPaint
        })

        // 上
        val square3 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW6,-flagH2))
            square.ps.add(MyPointF(-flagW6,-flagH2))
            square.paint = redPaint
        }
        square0Lst.add(square3)
        square1Lst.add(square3.copy().also {
            it.paint = greenPaint
        })

    }

    // パス生成(PH6)
    private fun createPathPH6() {
        square0Lst.clear()
        square1Lst.clear()

        // 下
        val square0 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,flagH2))
            square.ps.add(MyPointF(flagW6,flagH2))
            square.paint = greenPaint
        }
        square0Lst.add(square0)
        square1Lst.add(square0.copy().also {
            it.paint = redPaint
        })

        // 右下
        val square1 = Square().also { square ->
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW6,flagH2))
            square.ps.add(MyPointF(flagW2,flagH2))
            square.ps.add(MyPointF(flagW2,0f))
            square.paint = greenPaint
        }
        square0Lst.add(square1)
        square1Lst.add(square1.copy().also {
            it.paint = redPaint
        })

        // 上
        val square2 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(flagW6,0f))
            square.ps.add(MyPointF(flagW6,-flagH2))
            square.ps.add(MyPointF(-flagW6,-flagH2))
            square.paint = redPaint
        }
        square0Lst.add(square2)
        square1Lst.add(square2.copy().also {
            it.paint = greenPaint
        })

        // 左上
        val square3 = Square().also { square ->
            square.ps.add(MyPointF(-flagW6,0f))
            square.ps.add(MyPointF(-flagW6,-flagH2))
            square.ps.add(MyPointF(-flagW2,-flagH2))
            square.ps.add(MyPointF(-flagW2,0f))
            square.paint = redPaint
        }
        square0Lst.add(square3)
        square1Lst.add(square3.copy().also {
            it.paint = greenPaint
        })


    }

    // -------------------------------
    // 国旗を移動する
    // -------------------------------
    private fun movePath() {
        ratioNow += ratioDv
    }

    // -------------------------------
    // ビットマップに描画
    // -------------------------------
    private fun drawBitmap() {
        val tmpBitmap = Bitmap.createBitmap(sw.toInt(),sh.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(tmpBitmap)

        // バックグランドを描画
        canvas.drawRect(RectF(0f,0f,sw,sh),yellowPaint)

        // 原点(0,0)の位置
        // = (マージン,マージン)
        val x0 = marginH
        val y0 = marginW

        canvas.save()
        canvas.translate(x0,y0)


        canvas.saveLayer(0f,0f,flagW,flagH,null)

        canvas.translate(flagW2,flagH2)

        (0..3).forEach { i ->
            val square0 = square0Lst[i]
            val path0 = Path()
            var p0 = square0.ps[0]
            square0.ps.forEachIndexed { id, p ->
                if (id == 0) {
                    path0.moveTo(p.x,p.y)
                }
                else {
                    val p1 = p.copy()
                    p1.rotate(-ratioNow*angle,p0)
                    path0.lineTo(p1.x,p1.y)
                }
            }
            path0.close()
            canvas.drawPath(path0,square0.paint)
        }

        canvas.restore()

        canvas.saveLayer(flagW,0f,2f*flagW,flagH,null)

        canvas.translate(flagW+flagW2,flagH2)

        (0..3).forEach { i ->
            val square0 = square1Lst[i]
            val path0 = Path()
            var p0 = square0.ps[0]
            square0.ps.forEachIndexed { id, p ->
                if (id == 0) {
                    path0.moveTo(p.x,p.y)
                }
                else {
                    val p1 = p.copy()
                    p1.rotate(-ratioNow*angle,p0)
                    path0.lineTo(p1.x,p1.y)
                }
            }
            path0.close()
            canvas.drawPath(path0,square0.paint)
        }

        canvas.restore()


        canvas.restore()


        // これまでの描画はテンポラリ領域に実施していたので、実際の表示に使うビットマップにコピーする
        val matrix = Matrix()
        matrix.setScale(1f,1f)
        imageBitmap = Bitmap.createBitmap(tmpBitmap,0,0,sw.toInt(),sh.toInt(),matrix,true)
    }

    // 描画
    private fun drawCanvas() {
        val canvas = surfaceViewCanvas.holder.lockCanvas()
        if (canvas == null) return

        // 描画用ビットマップがインスタンス化されていなければ描画はスキップする
        if ( this::imageBitmap.isInitialized ) {
            canvas.drawBitmap(imageBitmap,0f,0f,null)
        }

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
            A04Fragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private data class Square(
        // 頂点リスト
        val ps: MutableList<MyPointF> = mutableListOf(),
        // ペイント
        var paint: Paint = Paint()
    )
}
