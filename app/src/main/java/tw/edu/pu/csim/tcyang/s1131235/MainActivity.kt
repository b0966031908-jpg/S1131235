package tw.edu.pu.csim.tcyang.s1131235

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tw.edu.pu.csim.tcyang.s1131235.ui.theme.S1131235Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 實作隱藏上方狀態列及下方巡覽列的功能
        hideSystemUI()

        enableEdgeToEdge() // 保持 EdgeToEdge 以便內容延伸到螢幕邊緣
        setContent {
            S1131235Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 這裡應替換為您的角色和服務網格介面 (MainScreen)
                    Greeting(
                        name = "楊承智", // 更改為 App 名稱
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    /**
     * 隱藏系統 UI（狀態列和導覽列），實現沉浸式全螢幕。
     * 此方法適用於 View-based 或 Compose 專案，用於控制 Activity 的 Window。
     */
    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11 (API 30) 及更高版本
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let { controller ->
                // 隱藏狀態列和導覽列
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                // 設置為沉浸模式，用戶滑動邊緣時 UI 會短暫出現
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // 舊版本 Android (API < 30)
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // 隱藏導覽列
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)    // 隱藏狀態列
        }
    }
}

// 保持原有的 Greeting 函數 (作為基礎 UI 佔位符)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name! (已啟用全螢幕)",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    S1131235Theme {
        Greeting("Android")
    }
}