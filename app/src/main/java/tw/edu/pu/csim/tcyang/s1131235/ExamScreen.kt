package tw.edu.pu.csim.tcyang.s1131235

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

// ReadScreenDimensions 函數保持不變
@Composable
fun ReadScreenDimensions(viewModel: ExamViewModel) {
    val context = LocalContext.current
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val metrics = DisplayMetrics()

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
        val windowMetrics = windowManager.currentWindowMetrics
        viewModel.updateScreenDimensions(
            width = windowMetrics.bounds.width(),
            height = windowMetrics.bounds.height()
        )
    } else {
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

    val roleIconSizePx = 300
    val serviceIconSizeDp = 70.dp

    val screenWidth = viewModel.screenWidthPx
    val screenHeight = viewModel.screenHeightPx

    val density = LocalDensity.current

    with(density) {

        val roleIconSizeDp = roleIconSizePx.toDp()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFF00)) // 黃色背景
        ) {

            // --- 邊緣的角色圖片定位邏輯 (保持不變) ---

            val yHalfScreen = screenHeight / 2
            val yBottomScreen = screenHeight

            // 1. 嬰幼兒 (role0)
            Image(
                painter = painterResource(id = R.drawable.role0),
                contentDescription = "嬰幼兒",
                modifier = Modifier
                    .size(roleIconSizeDp)
                    .offset { IntOffset(x = 0, y = yHalfScreen - roleIconSizePx) },
                contentScale = ContentScale.Fit
            )

            // 2. 兒童 (role1)
            Image(
                painter = painterResource(id = R.drawable.role1),
                contentDescription = "兒童",
                modifier = Modifier
                    .size(roleIconSizeDp)
                    .offset { IntOffset(x = screenWidth - roleIconSizePx, y = yHalfScreen - roleIconSizePx) },
                contentScale = ContentScale.Fit
            )

            // 3. 成人 (role2)
            Image(
                painter = painterResource(id = R.drawable.role2),
                contentDescription = "成人",
                modifier = Modifier
                    .size(roleIconSizeDp)
                    .offset { IntOffset(x = 0, y = yBottomScreen - roleIconSizePx) },
                contentScale = ContentScale.Fit
            )

            // 4. 一般民眾 (role3)
            Image(
                painter = painterResource(id = R.drawable.role3),
                contentDescription = "一般民眾",
                modifier = Modifier
                    .size(roleIconSizeDp)
                    .offset { IntOffset(x = screenWidth - roleIconSizePx, y = yBottomScreen - roleIconSizePx) },
                contentScale = ContentScale.Fit
            )

            // --- 畫面中央內容：服務圖示與文字資訊 ---

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.happy),
                        contentDescription = "服務生命是一件快樂的事",
                        modifier = Modifier.size(200.dp)

                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // 您的系級與姓名
                Text(
                    text = viewModel.studentInfo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 讀取到的螢幕尺寸 (px)
                Text(
                    text = "螢幕大小: ${viewModel.screenWidthPx} * ${viewModel.screenHeightPx} (px)",
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 成績顯示
                Text(
                    text = "成績: 0分", // 【已修改】分數固定顯示 0分
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}