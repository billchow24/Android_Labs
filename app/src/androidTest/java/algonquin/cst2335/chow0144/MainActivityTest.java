package algonquin.cst2335.chow0144;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Test case to check if the password requirement for at least one upper case is met
     */
    @Test
    public void testFindMissingUpperCase(){
        ViewInteraction appCompatEditText = onView(withId(R.id.pwEditText));

        appCompatEditText.perform(replaceText("password123#$*"));

        ViewInteraction materialButton = onView(withId(R.id.pwButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.pwMsgTextView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case to check if the password requirement for at least one lower case is met
     */
    @Test
    public void testFindMissingLowerCase(){
        ViewInteraction appCompatEditText = onView(withId(R.id.pwEditText));

        appCompatEditText.perform(replaceText("PASSWORD123#$*"));

        ViewInteraction materialButton = onView(withId(R.id.pwButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.pwMsgTextView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case to check if the password requirement for at least one special character is met
     */
    @Test
    public void testFindMissingSpecialCharacter(){
        ViewInteraction appCompatEditText = onView(withId(R.id.pwEditText));

        appCompatEditText.perform(replaceText("pAssword123"));

        ViewInteraction materialButton = onView(withId(R.id.pwButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.pwMsgTextView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case to check if the password requirement for at least one digit is met
     */
    @Test
    public void testFindMissingDigit(){
        ViewInteraction appCompatEditText = onView(withId(R.id.pwEditText));

        appCompatEditText.perform(replaceText("pAssword#$*"));

        ViewInteraction materialButton = onView(withId(R.id.pwButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.pwMsgTextView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case to check if all password requirements are met
     */
    @Test
    public void testFindAll(){
        ViewInteraction appCompatEditText = onView(withId(R.id.pwEditText));

        appCompatEditText.perform(replaceText("pAssword123#$*"));

        ViewInteraction materialButton = onView(withId(R.id.pwButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.pwMsgTextView));
        textView.check(matches(withText("Your password meets the requirements")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
