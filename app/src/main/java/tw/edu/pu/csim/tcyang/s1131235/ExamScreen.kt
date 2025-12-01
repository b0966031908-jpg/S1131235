package tw.edu.pu.csim.tcyang.s1131235

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * 讀取螢幕寬高並更新 ViewModel 中的狀態 (in pixels, px)。
 */
@Composable
fun ReadScreenDimensions(viewModel: ExamViewModel) {
    val context = LocalContext.current

    // 使用 Context.WINDOW_SERVICE 修正錯誤
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val metrics = DisplayMetrics()

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
        // API 30+ 獲取尺寸的方法
        val windowMetrics = windowManager.currentWindowMetrics
        viewModel.updateScreenDimensions(
            width = windowMetrics.bounds.width(),
            height = windowMetrics.bounds.height()
        )
    } else {
        // API < 30 獲取尺寸的方法
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(metrics)
        viewModel.updateScreenDimensions(
            width = metrics.widthPixels,
            height = metrics.heightPixels
        )
    }
}


@Composable
fun ExamScreen(
    viewModel: ExamViewModel = viewModel()
) {
    ReadScreenDimensions(viewModel)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFF00)) // 黃色背景
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // 圖片與文字置中
        verticalArrangement = Arrangement.Center // 垂直居中對齊
    ) {
        // 圖片：happy.png
        Image(
            painter = painterResource(id = R.drawable.happy), // 圖片引用
            contentDescription = "服務生命是一件快樂的事",
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Fit
        )

        // 間隔 10dp
        Spacer(modifier = Modifier.height(10.dp))

        // 您的系級與姓名
        Text(
            text = viewModel.studentInfo,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        // 間隔 10dp
        Spacer(modifier = Modifier.height(10.dp))

        // 讀取到的螢幕尺寸 (px)
        Text(
            text = "螢幕大小: ${viewModel.screenWidthPx} * ${viewModel.screenHeightPx}",
            fontSize = 16.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 成績顯示
        Text(
            text = "成績: 0分",
            fontSize = 16.sp,
            color = Color.DarkGray
        )
    }
}