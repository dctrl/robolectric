package com.xtremelabs.robolectric.matchers;

import android.view.View;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

public class ViewVisibilityMatcher<T extends View> extends TypeSafeMatcher<T> {
    private final int expectedVisibility;
    private int actualVisibility;

    public ViewVisibilityMatcher(int expectedVisibility) {
        this.expectedVisibility = expectedVisibility;
    }

    @Override
    public boolean matchesSafely(T t) {
        actualVisibility = t.getVisibility();
        return expectedVisibility == actualVisibility;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("[" + actualVisibility + "]");
        description.appendText(" visibility to be ");
        description.appendText("[" + expectedVisibility + "]");
    }

    @Factory
    public static <T extends View> Matcher<T> isVisible() {
        return new ViewVisibilityMatcher<T>(View.VISIBLE);
    }

    @Factory
    public static <T extends View> Matcher<T> isInvisible() {
        return new ViewVisibilityMatcher<T>(View.INVISIBLE);
    }

    @Factory
    public static <T extends View> Matcher<T> isGone() {
        return new ViewVisibilityMatcher<T>(View.GONE);
    }
}