package tw.edu.pu.csim.tcyang.s1131235

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import tw.edu.pu.csim.tcyang.s1131235.ui.theme.S1131235Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 隱藏上方狀態列及下方巡覽列
        hideSystemUI()

        enableEdgeToEdge()
        setContent {
            S1131235Theme {
                // 使用 ExamScreen 作為主內容
                ExamScreen()
            }
        }
    }

    /**
     * 隱藏系統 UI（狀態列和導覽列），實現沉浸式全螢幕。
     */
    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11 (API 30) 及更高版本
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let { controller ->
                // 隱藏狀態列和導覽列
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
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

@Preview(showBackground = true)
@Composable
fun ExamScreenPreview() {
    S1131235Theme {
        ExamScreen()
    }
}