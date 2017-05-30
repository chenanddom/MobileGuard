package main.mobileguard.com.mobileguard;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.util.concurrent.Service;
import android.support.test.runner.AndroidJUnit4;

import com.mobileguard.domain.Contact;
import com.mobileguard.service.ContactsService;
import com.mobileguard.service.impl.ContactsServiceImpl;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        SQLiteDatabase db = SQLiteDatabase
                .openDatabase(appContext.getFilesDir().getAbsolutePath() +
                                "/antivirus.db",
                        null, SQLiteDatabase.OPEN_READONLY);

        assertEquals("main.mobileguard.com.mobileguard", appContext.getPackageName());
    }
   // @Test
    public void testDB(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        ContactsService service = new ContactsServiceImpl(appContext);
        Contact contact = new Contact();
        contact.setName("zhangsan2");
        contact.setTelephonenumber("1232435456533");
        try {
            boolean flag  = service.addContact(contact);
            System.out.println("----------------------------------------------------flag:"+flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void testDB2() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        ContactsService service = new ContactsServiceImpl(appContext);
           List<Contact> contacts = service.findContact();
        System.out.println("----------------------------------------------------contacts:"+contacts.toString());
    }


}
