package lee.simplenote

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.EditText
import org.litepal.crud.DataSupport

class DetailActivity : AppCompatActivity() {

    var id=0
    var aNote:Note?=null
    var eTitle: EditText? = null
    var eNote: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_layout)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        eTitle=findViewById(R.id.edit_title) as EditText
        eNote=findViewById(R.id.edit_note) as EditText
        id=intent.getIntExtra("id",id)
        aNote=getNoteById(id)
        if (aNote!=null){
            eTitle!!.setText(aNote!!.mTitle)
            eNote!!.setText(aNote!!.mNote)
        }
    }

    fun getNoteById(id :Int ):Note?= DataSupport.findAll(Note::class.java).filter { it.mId==id }.firstOrNull()

    fun isChanged():Boolean=eTitle!!.text.toString()!=aNote?.mTitle||eNote!!.text.toString()!=aNote?.mNote

    override fun onDestroy() {
        super.onDestroy()
        if (isChanged()){
            val note=Note(aNote!!.mId,eTitle!!.text.toString(),eNote!!.text.toString())
            note.updateAll("mId=?",note.mId.toString())
        }
    }
}
