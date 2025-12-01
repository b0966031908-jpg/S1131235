package tw.edu.pu.csim.tcyang.s1131235

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


// 讀取螢幕尺寸的函數 (保持不變)
@Composable
fun ReadScreenDimensions(viewModel: ExamViewModel) {
    val context = LocalContext.current
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val metrics = DisplayMetrics()

    // 根據 Android 版本不同，獲取螢幕尺寸的方式不同
    if (android.os.Build.VERSION_CODES.R <= android.os.Build.VERSION.SDK_INT) {
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
    viewModel: ExamViewModel = viewModel() // 獲取 ViewModel 實例
) {
    ReadScreenDimensions(viewModel) // 讀取螢幕尺寸

    val roleIconSizePx = 300 // 邊緣角色圖示的像素尺寸
    val serviceIconSizeDp = 70.dp // 服務圖示的 DP 尺寸 (較小)

    val screenWidth = viewModel.screenWidthPx
    val screenHeight = viewModel.screenHeightPx

    val density = LocalDensity.current

    // 使用 with(density) 可以在內部直接使用 toDp() 或 toPx() 轉換
    with(density) {

        val roleIconSizeDp = roleIconSizePx.toDp()

        // 將服務圖示的 DP 尺寸轉為 PX，傳給 ViewModel
        val serviceIconSizePx = serviceIconSizeDp.toPx().toInt()
        viewModel.updateServiceIconSizePx(serviceIconSizePx)


        // --- 啟動遊戲邏輯 ---
        // 確保在螢幕尺寸讀取完畢後，只執行一次 StartGame
        LaunchedEffect(screenWidth, screenHeight) {
            if (screenWidth > 0 && screenHeight > 0) {
                viewModel.StartGame()
            }
        }

        // 將 ViewModel 中的 PX 座標轉為 DP (Compose 顯示單位)
        val xOffsetDp = viewModel.serviceIconX.toDp()
        val yOffsetDp = viewModel.serviceIconY.toDp()


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFF00)) // 黃色背景
        ) {

            // --- 邊緣的角色圖片定位邏輯 (不變) ---
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


            // --- 【新增：可移動、可拖曳的服務圖示】 ---

            Image(
                // 使用 ViewModel 告訴我們現在該顯示哪個圖示
                painter = painterResource(id = viewModel.currentServiceIconId),
                contentDescription = "服務圖示",
                modifier = Modifier
                    .size(serviceIconSizeDp)

                    // 1. 使用 ViewModel 的 (X, Y) 座標來定位圖示
                    .offset(x = xOffsetDp, y = yOffsetDp)

                    // 2. 處理拖曳手勢
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume() // 告訴系統我們處理了這個手勢

                            // *** 修正的程式碼在這裡 ***
                            // dragAmount.x 已經是像素值 (Float)，直接轉換為 Int
                            val dragAmountPx = dragAmount.x.toInt()

                            // 呼叫 ViewModel 來更新圖示的 X 座標
                            viewModel.moveIconHorizontal(dragAmountPx)
                        }
                    },
                contentScale = ContentScale.Fit
            )


            // --- 畫面中央內容：文字資訊 (不變) ---

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.happy),
                    contentDescription = "服務生命是一件快樂的事",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = viewModel.studentInfo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "螢幕大小: ${viewModel.screenWidthPx} * ${viewModel.screenHeightPx} (px)",
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "成績: 0分",
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}