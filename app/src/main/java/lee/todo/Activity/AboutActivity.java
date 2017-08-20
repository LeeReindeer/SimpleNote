package lee.todo.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import lee.todo.R;
import us.feras.mdv.MarkdownView;

public class AboutActivity extends AppCompatActivity {

    private final static String README="https://raw.githubusercontent.com/LeeReindeer/SimpleNote/master/README.md";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MarkdownView markdownView=new MarkdownView(this);
        setContentView(markdownView);
        markdownView.loadMarkdownFile(README);
    }
}
