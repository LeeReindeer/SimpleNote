package lee.simplenote

import org.litepal.crud.DataSupport

/**
 *@Author lee
 *@Time 8/7/17.
 * Note can be null
 */
 data class Note(var mId:Int, val mTitle:String, val mNote:String?): DataSupport()