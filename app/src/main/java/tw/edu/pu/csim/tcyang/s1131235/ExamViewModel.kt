package tw.edu.pu.csim.tcyang.s1131235

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ExamViewModel : ViewModel() {
    // 儲存螢幕寬度 (像素)
    var screenWidthPx by mutableStateOf(0)
        private set

    // 儲存螢幕高度 (像素)
    var screenHeightPx by mutableStateOf(0)
        private set

    /**
     * 更新螢幕尺寸資訊。
     */
    fun updateScreenDimensions(width: Int, height: Int) {
        screenWidthPx = width
        screenHeightPx = height
    }

    // 您的系級和姓名資訊 (請修改成您的資訊)
    val studentInfo = """
        瑪利亞基金會服務大考驗
        作者:資管二B 楊承智
    """
}