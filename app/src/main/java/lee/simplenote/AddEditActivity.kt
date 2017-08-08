package lee.simplenote

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import org.litepal.crud.DataSupport

class AddEditActivity : AppCompatActivity() {

    var eTitle: EditText? = null
    var eNote: EditText? = null
    //todo not check in puns
    val puns= listOf('.',',','!','?','。','，','！','？')
    var mNote=" "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_layout)
        //Note note=new Note();
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        eTitle =findViewById(R.id.edit_title) as EditText
        eNote =findViewById(R.id.edit_note) as EditText
    }
    // generate id
    public fun start():Statistics?=DataSupport.findAll(Statistics::class.java).maxBy { it.numsOfNote }

    //todo can be used in search
    fun getPattern1()="""\w*"""
    fun Note.countWords()=this.mNote?.matches(getPattern1().toRegex())

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        var nums=start()
        var mNums=Statistics(0,0)
        var id=0

        if (eNote?.text!!.toString().isNotEmpty()) {
            var note = Note(id, eTitle!!.text.toString(), eNote!!.text.toString())
            if (nums!=null) {
                //val a = Integer.parseInt(outputData)
                id=nums.numsOfNote++
                nums.allWordsOfNote += note.mNote!!.length
                nums.save()
                Log.d("edit", note.toString())
                Log.d("Statistics:", nums.allWordsOfNote.toString() + " " + nums.numsOfNote)
            }else{
                id=mNums.numsOfNote++
                mNums.allWordsOfNote+= note.mNote!!.length
                mNums.save()
            }
            note.mId=id
            note.save()
        }else{
            Toast.makeText(this@AddEditActivity,"Note can't be empty:(",Toast.LENGTH_SHORT).show()
        }
    }
}
