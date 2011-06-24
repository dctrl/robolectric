package com.xtremelabs.robolectric;

import android.content.Context;
import android.view.View;
import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.util.TestOnClickListener;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultRequestDirector;
import org.apache.http.protocol.HttpContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

@RunWith(WithTestDefaultsRunner.class)
public class RobolectricTest {

    private PrintStream originalSystemOut;
    private ByteArrayOutputStream buff;
    private String defaultLineSeparator;

    @Before
    public void setUp() {
        originalSystemOut = System.out;
        defaultLineSeparator = System.getProperty("line.separator");

        System.setProperty("line.separator", "\n");
        buff = new ByteArrayOutputStream();
        PrintStream testOut = new PrintStream(buff);
        System.setOut(testOut);
    }

    @After
    public void tearDown() throws Exception {
        System.setProperty("line.separator", defaultLineSeparator);
        System.setOut(originalSystemOut);
    }

    @Test
    public void shouldLogMissingInvokedShadowMethodsWhenRequested() throws Exception {
        Robolectric.bindShadowClass(TestShadowView.class);
        Robolectric.logMissingInvokedShadowMethods();


        View aView = new View(null);
        // There's a shadow method for this
        aView.getContext();
        String output = buff.toString();
        assertEquals("No Shadow method found for View.<init>(android.content.Context)\n", output);
        buff.reset();

        aView.findViewById(27);
        // No shadow here... should be logged
        output = buff.toString();
        assertEquals("No Shadow method found for View.findViewById(int)\n", output);
    }

    @Test // This is nasty because it depends on the test above having run first in order to fail
    public void shouldNotLogMissingInvokedShadowMethodsByDefault() throws Exception {
        View aView = new View(null);
        aView.findViewById(27);
        String output = buff.toString();

        assertEquals("", output);
    }

    @Test(expected = RuntimeException.class)
    public void clickOn_shouldThrowIfViewIsDisabled() throws Exception {
        View view = new View(null);
        view.setEnabled(false);
        Robolectric.clickOn(view);
    }

    @Test
    public void shouldResetBackgroundSchedulerBeforeTests() throws Exception {
        assertThat(Robolectric.getBackgroundScheduler().isPaused(), equalTo(false));
        Robolectric.getBackgroundScheduler().pause();
    }

    @Test
    public void shouldResetBackgroundSchedulerAfterTests() throws Exception {
        assertThat(Robolectric.getBackgroundScheduler().isPaused(), equalTo(false));
        Robolectric.getBackgroundScheduler().pause();
    }

    @Test
    public void httpRequestWasSent_ReturnsTrueIfRequestWasSent() throws IOException, HttpException {
        Robolectric.addPendingHttpResponse(200, "a happy response body");

        ConnectionKeepAliveStrategy connectionKeepAliveStrategy = new ConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
                return 0;
            }

        };
        DefaultRequestDirector requestDirector = new DefaultRequestDirector(null, null, null, connectionKeepAliveStrategy, null, null, null, null, null, null, null, null);
        requestDirector.execute(null, new HttpGet("http://example.com"), null);

        assertTrue(Robolectric.httpRequestWasMade());
    }

    @Test
    public void httpRequestWasMade_ReturnsFalseIfNoRequestWasMade() {
        assertFalse(Robolectric.httpRequestWasMade());
    }

    public void clickOn_shouldCallClickListener() throws Exception {
        View view = new View(null);
        TestOnClickListener testOnClickListener = new TestOnClickListener();
        view.setOnClickListener(testOnClickListener);
        Robolectric.clickOn(view);
        assertTrue(testOnClickListener.clicked);
    }

    @Implements(View.class)
    public static class TestShadowView {
        @SuppressWarnings({"UnusedDeclaration"})
        @Implementation
        public Context getContext() {
            return null;
        }
    }
}
