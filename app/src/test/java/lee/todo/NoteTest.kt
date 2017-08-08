package lee.todo

import lee.simplenote.Note
import org.junit.Test
import org.junit.Assert.assertEquals

/**
 *@Author lee
 *@Time 8/8/17.
 */
class NoteTest {

    val puns= listOf(".",",","!","?","。","，","！","？"," ")

    @Test fun testLengthOfwords(){
        val note=Note(1,"test1","..hi")
        assertEquals(2,note.mNote?.toByteArray())
    }
    public fun Note.getLengthOfWords(): Int{
        var count=this.mNote!!.count { it.equals(puns.any()) }
        return count
    }



}