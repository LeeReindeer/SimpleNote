package lee.simplenote

import org.litepal.crud.DataSupport

/**
 * @Author lee
 * *
 * @Time 8/7/17.
 */

data class Statistics(var allWordsOfNote: Int, var numsOfNote: Int) : DataSupport()
