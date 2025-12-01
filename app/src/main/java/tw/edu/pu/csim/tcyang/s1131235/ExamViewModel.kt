package tw.edu.pu.csim.tcyang.s1131235

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

// ViewModel 負責儲存和管理應用程式的資料
class ExamViewModel : ViewModel() {

    // --- 螢幕尺寸資訊 ---
    var screenWidthPx by mutableIntStateOf(0)
        private set
    var screenHeightPx by mutableIntStateOf(0)
        private set

    // 更新螢幕尺寸，從 Composable 傳入
    fun updateScreenDimensions(width: Int, height: Int) {
        screenWidthPx = width
        screenHeightPx = height
    }

    // 學生資訊，用於畫面顯示
    val studentInfo = """
    瑪利亞基金會服務大考驗
    作者:資管二B 楊承智
""".trimIndent()

    // --- 服務圖示的狀態與邏輯 ---

    // 遊戲是否正在運行
    var gameRunning by mutableStateOf(false)
        private set

    // 【★ 關鍵修改區域：更新下墜圖示列表 ★】
    // 服務圖示所有可能的圖片資源 ID 列表
    private val serviceIconResources = listOf(
        R.drawable.service0, // 極早期療育
        R.drawable.service1, // 離島服務
        R.drawable.service2, // 極重多障
        R.drawable.service3  // 輔具服務
    )

    // 圖示的 X 座標 (水平位置，單位：像素)
    var serviceIconX by mutableIntStateOf(0)
        private set
    // 圖示的 Y 座標 (垂直位置，單位：像素)
    var serviceIconY by mutableIntStateOf(0)
        private set
    // 當前正在使用的圖示資源 ID
    var currentServiceIconId by mutableIntStateOf(serviceIconResources.first())
        private set

    // 圖示的尺寸大小 (單位：像素)，從 Composable 傳入
    private var serviceIconSizePx = 70

    // 儲存圖示的實際尺寸
    fun updateServiceIconSizePx(size: Int) {
        if (serviceIconSizePx != size) {
            serviceIconSizePx = size
        }
    }

    /**
     * 【隨機產生並重置位置】
     * 隨機選一個圖示，並將它放回螢幕上方水平中間。
     */
    fun resetServiceIconPosition() {
        if (screenWidthPx == 0) return

        // 1. 隨機選一個圖示
        currentServiceIconId = serviceIconResources[Random.nextInt(serviceIconResources.size)]

        // 2. 設定水平位置置中: (螢幕寬度 / 2) - (圖示尺寸 / 2)
        serviceIconX = (screenWidthPx / 2) - (serviceIconSizePx / 2)

        // 3. 設定垂直位置在螢幕最上方
        serviceIconY = 0
    }

    /**
     * 【圖示下墜和碰撞檢查】
     * 讓圖示往下掉 20px，如果碰到螢幕底部就重置位置。
     */
    fun moveIconDown() {
        if (screenHeightPx == 0) return

        // 圖示往下掉 20 像素
        val fallSpeed = 20
        serviceIconY += fallSpeed

        // 檢查是否碰撞螢幕下方: 圖示底部的 Y 座標 > 螢幕高度
        if (serviceIconY + serviceIconSizePx > screenHeightPx) {
            // 碰撞底部，重新產生圖示並放回上方
            resetServiceIconPosition()
        }
    }

    /**
     * 【水平拖曳】
     * 根據拖曳的位移量 (xOffset) 來移動圖示，並確保不超出左右邊界。
     */
    fun moveIconHorizontal(xOffset: Int) {
        val newX = serviceIconX + xOffset
        val minX = 0
        // 最大 X 座標 = 螢幕寬度 - 圖示尺寸
        val maxX = screenWidthPx - serviceIconSizePx

        // 限制圖示的 X 座標在 [minX, maxX] 範圍內
        serviceIconX = newX.coerceIn(minX, maxX)
    }

    /**
     * 啟動遊戲主循環：定時器
     */
    fun StartGame() {
        if (gameRunning) return // 如果已經在跑，就不要再啟動一次

        gameRunning = true
        resetServiceIconPosition() // 遊戲開始時，先產生第一個圖示

        // 使用 Coroutine 啟動一個背景任務來當作定時器
        viewModelScope.launch {
            while (gameRunning) {
                delay(100) // 等待 100 毫秒 (即 0.1 秒)
                moveIconDown() // 呼叫圖示下墜函數
            }
        }
    }

    fun StopGame() {
        gameRunning = false
    }
}