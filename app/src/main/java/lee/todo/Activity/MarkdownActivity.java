package lee.todo.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import lee.todo.R;
import us.feras.mdv.MarkdownView;

public class MarkdownActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.markdown_layout);
        MarkdownView markdownView=new MarkdownView(this);
        setContentView(markdownView);
        Intent mark=getIntent();
        markdownView.loadMarkdown("# "+mark.getStringExtra("title")+"\n"+mark.getStringExtra("note"));
    }
}
