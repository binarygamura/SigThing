package de.fomad.sigthing.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.junit.BeforeClass;

/**
 *
 * @author binary
 */
public abstract class BasicJsonParseTest
{
    protected static Gson gson;
    
    protected static DateFormat dateFormat;
    
    @BeforeClass
    public static void init(){
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
