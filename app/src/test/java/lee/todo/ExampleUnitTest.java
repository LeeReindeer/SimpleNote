package lee.todo;

import org.junit.Test;

import lee.simplenote.Note;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        Note note=new Note(1,"test","this is test");
        assertEquals(4, 2+2);
    }
}