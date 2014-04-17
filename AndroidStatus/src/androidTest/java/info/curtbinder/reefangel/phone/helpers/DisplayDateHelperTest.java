package info.curtbinder.reefangel.phone.helpers;

import android.test.InstrumentationTestCase;

import junit.framework.Assert;

import java.util.Calendar;


public class DisplayDateHelperTest extends InstrumentationTestCase {


    public void testTimespanToString_OneYearInMilliSeconds_ShouldReturn1Year(){
        // Arrange
        //long timeSpan = 31556940;
        long timeSpan = DisplayDateHelper.YEAR;
        String result = DisplayDateHelper.timespanToString(timeSpan);

        Assert.assertEquals("1 year", result);
    }

    public void testTimespanToString_TwoYearsInMilliSeconds_ShouldReturn2Years(){
        // Arrange
        long timeSpan = DisplayDateHelper.YEAR * 2;
        // Act
        String result = DisplayDateHelper.timespanToString(timeSpan);
        // Assert
        Assert.assertEquals("2 years", result);
    }

    public void testTimespanToString_OneMonthInMilliSeconds_ShouldReturn1Month(){
        // Arrange
        long timeSpan = DisplayDateHelper.MONTH;
        // Act
        String result = DisplayDateHelper.timespanToString(timeSpan);
        // Assert
        Assert.assertEquals("1 month", result);
    }

    public void testTimespanToString_TwoMonthsInMilliSeconds_ShouldReturn2Months(){
        // Arrange
        long timeSpan = DisplayDateHelper.MONTH * 2;
        // Act
        String result = DisplayDateHelper.timespanToString(timeSpan);
        // Assert
        Assert.assertEquals("2 months", result);
    }

    public void testTimespanToString_OneDayInMilliSeconds_ShouldReturn1Day(){
        // Arrange
        long timeSpan = DisplayDateHelper.DAY;
        // Act
        String result = DisplayDateHelper.timespanToString(timeSpan);
        // Assert
        Assert.assertEquals("1 day", result);
    }

    public void testTimespanToString_TwoDaysInMilliSeconds_ShouldReturn2Days(){
        // Arrange
        long timeSpan = DisplayDateHelper.DAY * 2;
        // Act
        String result = DisplayDateHelper.timespanToString(timeSpan);
        // Assert
        Assert.assertEquals("2 days", result);
    }

    public void testTimespanToString_OneHourInMilliSeconds_ShouldReturn1Hour(){
        // Arrange
        long timeSpan = DisplayDateHelper.HOUR;
        // Act
        String result = DisplayDateHelper.timespanToString(timeSpan);
        // Assert
        Assert.assertEquals("1 hour", result);
    }

    public void testTimespanToString_TwoHoursInMilliSeconds_ShouldReturn2Hours(){
        // Arrange
        long timeSpan = DisplayDateHelper.HOUR * 2;
        // Act
        String result = DisplayDateHelper.timespanToString(timeSpan);
        // Assert
        Assert.assertEquals("2 hours", result);
    }

    public void testTimespanToString_OneMinuteInMilliSeconds_ShouldReturn1Minute(){
        // Arrange
        long timeSpan = DisplayDateHelper.MINUTE;
        // Act
        String result = DisplayDateHelper.timespanToString(timeSpan);
        // Assert
        Assert.assertEquals("1 minute", result);
    }

    public void testTimespanToString_TwoMinutesInMilliSeconds_ShouldReturn2Minutes(){
        // Arrange
        long timeSpan = DisplayDateHelper.MINUTE * 2;
        // Act
        String result = DisplayDateHelper.timespanToString(timeSpan);
        // Assert
        Assert.assertEquals("2 minutes", result);
    }

    public void testTimespanToString_OneSecondInMilliSeconds_ShouldReturn1Second(){
        // Arrange
        long timeSpan = DisplayDateHelper.SECOND;
        // Act
        String result = DisplayDateHelper.timespanToString(timeSpan);
        // Assert
        Assert.assertEquals("1 second", result);
    }

    public void testTimespanToString_TwoSecondsInMilliSeconds_ShouldReturn2Seconds(){
        // Arrange
        long timeSpan = DisplayDateHelper.SECOND * 2;
        // Act
        String result = DisplayDateHelper.timespanToString(timeSpan);
        // Assert
        Assert.assertEquals("2 seconds", result);
    }

    public void testTimespanToString_LessThanOneSecondInMilliSeconds_ShouldReturn1Second(){
        // Arrange
        long timeSpan = 999;
        // Act
        String result = DisplayDateHelper.timespanToString(timeSpan);
        // Assert
        Assert.assertEquals("1 second", result);
    }

}
