package quickutils.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Toast;

public abstract class QuickUtils {

	private static String TAG = "DESIRED_TAG";

	private static final int VERBOSE = android.util.Log.VERBOSE;
	private static final int DEBUG = android.util.Log.DEBUG;
	private static final int INFO = android.util.Log.INFO;
	private static final int WARN = android.util.Log.WARN;
	private static final int ERROR = android.util.Log.ERROR;

	private static final int REQUEST_CODE = 1234;
	private static final int RESULT_OK = 1234;

	/**
	 * Developer mode for Debugging purposes
	 */
	public static final boolean DEVELOPER_MODE = true;

	/**
	 * Production mode for Debugging purposes
	 */
	public static final boolean PRODUCTION_MODE = false;

	private static boolean DEBUG_MODE = QuickUtils.DEVELOPER_MODE;

	/**
	 * private constructor
	 */
	private QuickUtils() {
	}

	/**
	 * Set debug mode (default: QuickUtils.DEBUG_MODE). Set
	 * QuickUtils.PRODUCTION_MODE when you go to production
	 * 
	 * @param debugMode
	 *            the new developer mode value
	 */
	public static void setDebugMode(boolean debugMode) {
		DEBUG_MODE = debugMode;
	}

	/**
	 * Set the log TAG for debug purposes
	 * 
	 * @param TAG
	 *            Desired tag (e.g.: Aplication_X)
	 */
	public static void setTAG(String tag) {
		TAG = tag;
	}

	/**
	 * Log Utils
	 * 
	 * @author cesar
	 * 
	 */
	public static class log {

		/**
		 * private constructor
		 */
		private log() {
		}

		/**
		 * Sends an ERROR log message
		 * 
		 * @param message
		 *            The message you would like logged.
		 */
		public static int e(String message) {
			return logger(QuickUtils.ERROR, message);
		}

		/**
		 * Sends an ERROR log message
		 * 
		 * @param message
		 *            The message you would like logged.
		 * @param throwable
		 *            An exception to log
		 */
		public static int e(String message, Throwable throwable) {
			return logger(QuickUtils.ERROR, message, throwable);
		}

		/**
		 * Sends an INFO log message.
		 * 
		 * @param message
		 *            The message you would like logged.
		 * 
		 */
		public static int i(String message) {
			return logger(QuickUtils.INFO, message);
		}

		/**
		 * Sends an INFO log message.
		 * 
		 * @param message
		 *            The message you would like logged.
		 * @param throwable
		 *            An exception to log
		 */
		public static int i(String message, Throwable throwable) {
			return logger(QuickUtils.INFO, message, throwable);
		}

		/**
		 * Sends a VERBBOSE log message.
		 * 
		 * @param message
		 *            The message you would like logged.
		 */
		public static int v(String message) {
			return logger(QuickUtils.VERBOSE, message);
		}

		/**
		 * Sends a VERBBOSE log message.
		 * 
		 * @param message
		 *            The message you would like logged.
		 * @param throwable
		 *            An exception to log
		 */
		public static int v(String message, Throwable throwable) {
			return logger(QuickUtils.VERBOSE, message, throwable);
		}

		/**
		 * Sends a WARNING log message.
		 * 
		 * @param message
		 *            The message you would like logged.
		 */
		public static int w(String message) {
			return logger(QuickUtils.WARN, message);
		}

		/**
		 * Sends a WARNING log message.
		 * 
		 * @param message
		 *            The message you would like logged.
		 * @param throwable
		 *            An exception to log
		 */
		public static int w(String message, Throwable throwable) {
			return logger(QuickUtils.WARN, message, throwable);
		}

		/**
		 * Sends a DEBUG log message.
		 * 
		 * @param message
		 *            The message you would like logged.
		 */
		public static int d(String message) {
			return logger(QuickUtils.DEBUG, message);
		}

		/**
		 * Sends a DEBUG log message and log the exception.
		 * 
		 * @param message
		 *            The message you would like logged.
		 * @param throwable
		 *            An exception to log
		 */
		public static int d(String message, Throwable throwable) {
			return logger(QuickUtils.DEBUG, message, throwable);

		}

		/**
		 * Private Logger function to handle Log calls
		 * 
		 * @param level
		 *            level of the log message
		 * @param message
		 *            log output
		 * @param throwable
		 * 
		 */
		private static int logger(int level, String message, Throwable throwable) {

			if (DEBUG_MODE) {
				switch (level) {

				case QuickUtils.DEBUG:
					return Log.d(TAG, message, throwable);
				case QuickUtils.VERBOSE:
					return Log.v(TAG, message, throwable);
				case QuickUtils.INFO:
					return Log.i(TAG, message, throwable);
				case QuickUtils.WARN:
					return Log.w(TAG, message, throwable);
				case QuickUtils.ERROR:
					return Log.e(TAG, message, throwable);
				default:
					break;
				}
			}

			return -1;
		}

		/**
		 * Private Logger function to handle Log calls
		 * 
		 * @param level
		 *            level of the log message
		 * @param message
		 *            log output
		 * @param throwable
		 * 
		 */
		private static int logger(int level, String message) {

			if (DEBUG_MODE) {
				switch (level) {

				case QuickUtils.DEBUG:
					return Log.d(TAG, message);
				case QuickUtils.VERBOSE:
					return Log.v(TAG, message);
				case QuickUtils.INFO:
					return Log.i(TAG, message);
				case QuickUtils.WARN:
					return Log.w(TAG, message);
				case QuickUtils.ERROR:
					return Log.e(TAG, message);
				default:
					break;
				}
			}

			return -1;
		}
	}

	/**
	 * Miscelaneous android App Utils
	 * 
	 * @author cesar
	 * 
	 */
	public static class misc {

		/**
		 * private constructor
		 */
		private misc() {
		}

		/**
		 * Navigate to an activity programmatically by providing the package +
		 * activity name
		 * 
		 * @param fromActivity
		 *            Activity where I am coming from
		 * @param className
		 *            Full path to the desired Activity (e.g.
		 *            "com.sample.MainActivity")
		 */
		public static void goToActivityByClassName(Activity fromActivity, String className) {
			Class<?> c = null;
			if (className != null) {
				try {
					c = Class.forName(className);
				} catch (ClassNotFoundException e) {
					QuickUtils.log.d("ClassNotFound", e);
				}
			}

			goToActivity(fromActivity, c);
		}

		/**
		 * Navigate to a activity
		 * 
		 * @param A
		 *            From Activity
		 * @param B
		 *            Destination Activity
		 * 
		 */
		public static void goToActivity(Activity A, Class<?> B) {
			Intent myIntent = new Intent(A, B);
			A.startActivity(myIntent);
		}

		/**
		 * 
		 * @param A
		 *            From Activity
		 * @param B
		 *            Destination Activity
		 * @param extras
		 *            Extras to be included in the new Activity
		 */
		public static void goToActivity(Activity A, Class<?> B, Bundle extras) {
			Intent myIntent = new Intent(A, B);
			myIntent.putExtras(extras);
			A.startActivity(myIntent);
		}

		/**
		 * Start google activity of speechRecognition (needed on
		 * onActivityResult(int requestCode, int resultCode, Intent data) to
		 * call getSpeechRecognitionResults() to get the results)
		 * 
		 * @param activity
		 *            - activity
		 * @param maxResults
		 *            - Max number of results that you want to get
		 * @param text
		 *            - what will ask to user when activity start
		 */
		public static void speechRecognition(final Activity activity, int maxResults, String text) {
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT, text);
			intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, maxResults);
			activity.startActivityForResult(intent, REQUEST_CODE);
		}

		/**
		 * Get all results from the Google Speech Recognition activity (to be
		 * called onActivityResult())
		 * 
		 * @param requestCode
		 *            - onActivityResult request code
		 * @param resultCode
		 *            - onActivityResult result code
		 * @param data
		 *            - onActivityResult Intent data
		 * @return ArrayList<String> with all results or null if was not
		 *         possible to get any results
		 */
		public static ArrayList<String> getSpeechRecognitionResults(int requestCode, int resultCode, Intent data) {
			ArrayList<String> matches = null;
			if (requestCode == 0 && resultCode == RESULT_OK) {
				matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				StringBuilder sb = new StringBuilder();
				for (String match : matches) {
					sb.append(match + ", ");
				}
			}
			return matches;
		}

		/**
		 * Get the first result that matches the Result List from Google Speech
		 * Recognition activity (to be called onActivityResult()) and the
		 * Dictionary given
		 * 
		 * @param requestCode
		 *            - onActivityResult request code
		 * @param resultCode
		 *            - onActivityResult result code
		 * @param data
		 *            - onActivityResult Intent data
		 * @param array
		 *            - Dictionary with all keywords
		 * 
		 * @return String with the first result matched or null if was not
		 *         possible to get any result
		 */
		public static String getSpeechRecognitionResultFromDicionary(int requestCode, int resultCode, Intent data, ArrayList<String> array) {
			ArrayList<String> matches = null;
			if (requestCode == 0 && resultCode == RESULT_OK) {
				matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				StringBuilder sb = new StringBuilder();
				for (String match : matches) {
					for (String arrayString : array) {
						if (arrayString.equals(match)) {
							return match;
						}
					}

				}
			}
			return null;
		}

		/**
		 * Get first result from the Google Speech Recognition activity (to be
		 * called onActivityResult())
		 * 
		 * @param requestCode
		 *            - onActivityResult request code
		 * @param resultCode
		 *            - onActivityResult result code
		 * @param data
		 *            - onActivityResult Intent data
		 * @return string containing the first result of what was recognized
		 */
		public static String getSpeechRecognitionFirstResult(int requestCode, int resultCode, Intent data) {
			if (requestCode == 0 && resultCode == RESULT_OK) {
				List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				if (results != null && results.size() > 0) {
					return results.get(0);
				}
			}
			return null;
		}

		/**
		 * Checks if the app has connectivity to the Internet
		 * 
		 * @param context
		 *            application context
		 * @return true if has connection to the Internet and false if it
		 *         doesn't
		 */
		public static boolean hasInternetConnection(Context context) {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm == null)
				return false;
			NetworkInfo info = cm.getActiveNetworkInfo();

			// 3G
			State mobile = cm.getNetworkInfo(0).getState();

			// wifi
			State wifi = cm.getNetworkInfo(1).getState();

			if (info == null)
				return false;

			if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {

				return info.isConnectedOrConnecting();
			} else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {

				return info.isConnectedOrConnecting();
			}
			return info.isConnectedOrConnecting();
		}

		/**
		 * Make the smartphone vibrate for a giving time.you need to put the
		 * vibration permission in the manifest as follows: <uses-permission
		 * android:name="android.permission.VIBRATE"/>
		 * 
		 * 
		 * @param context
		 *            context in which the smartphone will vibrate
		 * @param duration
		 *            duration of the vibration in miliseconds
		 * 
		 */
		public static void vibrate(Context context, int duration) {
			Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(duration);
		}

		/**
		 * Quick toast method with short duration
		 * 
		 * @param context
		 *            context in which will be displayed
		 * @param message
		 *            toast content
		 */
		public static void toast(Context context, String message) {

			Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			toast.show();
		}

		/**
		 * Quick toast method
		 * 
		 * @param context
		 *            context in which will be displayed
		 * @param message
		 *            The text to show. Can be formatted text.
		 * @param duration
		 *            How long to display the message. Either Toast.LENGTH_SHORT
		 *            or Toast.LENGTH_LONG
		 */
		public static void toast(Context context, String message, int duration) {

			Toast toast = Toast.makeText(context, message, duration);
			toast.show();
		}

		/**
		 * Sleep
		 * 
		 * @param milliseconds
		 *            seconds that the app will sleep
		 */
		public static void sleep(int milliseconds) {

			try {
				QuickUtils.log.i("delaying for " + milliseconds / 1000 + " seconds");
				Thread.sleep(milliseconds);
			} catch (InterruptedException e) {
				QuickUtils.log.e("Interrupted exception", e);
			}
		}

	}

	/**
	 * Math library
	 * 
	 * @author cesar
	 * 
	 */
	public static class math {

		/**
		 * private constructor
		 */
		private math() {
		}

		private static final float DEG_TO_RAD = 3.1415926f / 180.0f;
		private static final float RAD_TO_DEG = 180.0f / 3.1415926f;

		/**
		 * Degrees to radians
		 * 
		 * @param degrees
		 * @return the converted value
		 */
		public static float degreesToRadians(float degrees) {
			return degrees * DEG_TO_RAD;
		}

		/**
		 * Radians to degrees
		 * 
		 * @param degrees
		 * @return the converted value
		 */
		public static float radiansToDegrees(float radians) {
			return radians * RAD_TO_DEG;
		}

		/**
		 * Arc cosine
		 * 
		 * @param value
		 * @return Returns the closest double approximation of the arc cosine of
		 *         the argument within the range [0..pi]. The returned result is
		 *         within 1 ulp (unit in the last place) of the real result.
		 */
		public static float acos(float value) {
			return (float) Math.acos(value);
		}

		/**
		 * Arc sine
		 * 
		 * @param value
		 * @return Returns the closest double approximation of the arc sine of
		 *         the argument within the range [-pi/2..pi/2]. The returned
		 *         result is within 1 ulp (unit in the last place) of the real
		 *         result.
		 */
		public static float asin(float value) {
			return (float) Math.asin(value);
		}

		/**
		 * Arc tangent
		 * 
		 * @param value
		 * @return Returns the closest double approximation of the arc tangent
		 *         of the argument within the range [-pi/2..pi/2]. The returned
		 *         result is within 1 ulp (unit in the last place) of the real
		 *         result.
		 */
		public static float atan(float value) {
			return (float) Math.atan(value);
		}

		/**
		 * Arc tangent of y/x within the range [-pi..pi]
		 * 
		 * @param a
		 * @param b
		 * @return Returns the closest double approximation of the arc tangent
		 *         of y/x within the range [-pi..pi]. This is the angle of the
		 *         polar representation of the rectangular coordinates (x,y).
		 *         The returned result is within 2 ulps (units in the last
		 *         place) of the real result.
		 */
		public static float atan2(float a, float b) {
			return (float) Math.atan2(a, b);
		}

		/**
		 * Tangent of an angle
		 * 
		 * @param angle
		 *            angle
		 * @return the tangent
		 */
		public static float tan(float angle) {
			return (float) Math.tan(angle);
		}

		/**
		 * Absolute value
		 * 
		 * @param v
		 *            value
		 * @return returns the absolute value
		 */
		public static float abs(float v) {
			return v > 0 ? v : -v;
		}

		/**
		 * Number's logarithm <br>
		 * Special cases:
		 * 
		 * <li>log(+0.0) = -infinity</li> <li>log(-0.0) = -infinity</li><li>
		 * log((anything < 0) = NaN</li> <li>log(+infinity) = +infinity</li><li>
		 * log(-infinity) = NaN</li><li>log(NaN) = NaN</li>
		 * 
		 * 
		 * @param number
		 * @return Returns the closest double approximation of the natural
		 *         logarithm of the argument. The returned result is within 1
		 *         ulp (unit in the last place) of the real result.
		 */
		public static float logarithm(float number) {
			return (float) Math.log(number);
		}

		/**
		 * Number's Exponencial
		 * 
		 * @param number
		 *            float number
		 * @return Returns the closest double approximation of the natural
		 *         logarithm of the argument. The returned result is within 1
		 *         ulp (unit in the last place) of the real result.
		 */
		public static float exponencial(float number) {
			return (float) Math.exp(number);
		}

		/**
		 * Gets the higher number
		 * 
		 * @param a
		 *            float number
		 * @param b
		 *            float number
		 * @return the higher number between a and b
		 */
		public static float max(float a, float b) {
			return a > b ? a : b;
		}

		/**
		 * Gets the higher number
		 * 
		 * @param a
		 *            int number
		 * @param b
		 *            int number
		 * @return the higher number between a and b
		 */
		public static int max(int a, int b) {
			return a > b ? a : b;
		}

		/**
		 * Gets the lower number
		 * 
		 * @param a
		 *            float number
		 * @param b
		 *            float number
		 * @return the lower number between a and b
		 */
		public static float min(float a, float b) {
			return a < b ? a : b;
		}

		/**
		 * Gets the lower number
		 * 
		 * @param a
		 *            float number
		 * @param b
		 *            float number
		 * @return the lower number between a and b
		 */
		public static int min(int a, int b) {
			return a < b ? a : b;
		}

		/**
		 * Check if a number is Odd
		 * 
		 * @param num
		 *            int number
		 * @return true if the num is odd and false if it's even
		 */
		public static boolean isOdd(int num) {
			return !isEven(num);
		}

		/**
		 * Check if a number is Even
		 * 
		 * @param num
		 *            int number
		 * @return true if the num is even and false if it's odd
		 */
		public static boolean isEven(int num) {
			return (num % 2 == 0);
		}

		/**
		 * Returns a random number between MIN inclusive and MAX exclusive.
		 * 
		 * @param min
		 *            value inclusive
		 * @param max
		 *            value exclusive
		 * @return an int between MIN inclusive and MAX exclusive.
		 */
		public static int getRandomNumber(int min, int max) {
			Random r = new Random();
			return r.nextInt(max - min + 1) + min;
		}

	}

	/**
	 * SDCard Utils
	 * 
	 * @author cesar
	 * 
	 */
	public static class date {
		/**
		 * private constructor
		 */
		private date() {
		}

		public static final int YESTERDAY = -1;
		public static final int TODAY = 0;
		public static final int TOMORROW = 1;

		/**
		 * Gets the current year
		 * 
		 * @return current year
		 */
		public static int getCurrentYear() {
			Calendar c = Calendar.getInstance();
			return c.get(Calendar.YEAR);
		}

		/**
		 * Gets the current month
		 * 
		 * @return current month
		 */
		public static int getCurrentMonth() {
			Calendar c = Calendar.getInstance();
			return c.get(Calendar.MONTH);
		}

		/**
		 * Gets the current day
		 * 
		 * @return current day
		 */
		public static int getCurrentDay() {
			Calendar c = Calendar.getInstance();
			return c.get(Calendar.DAY_OF_MONTH);
		}

		/**
		 * Miliseconds since midnight
		 * 
		 * @return the number of miliseconds since midnight
		 */
		public static long getTimeSinceMidnight() {
			Calendar c = Calendar.getInstance();
			long now = c.getTimeInMillis();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return now - c.getTimeInMillis();
		}

		/**
		 * Gets a date with a desired format as a String
		 * 
		 * @param day
		 *            Can be: <li>QuickUtils.date.YESTERDAY</li><li>
		 *            QuickUtils.date.TODAY</li><li>QuickUtils.date.TOMORROW</li>
		 * @param format
		 *            desired format (e.g. "yyyy-MM-dd HH:mm:ss")
		 * @return returns a day with the given format
		 */
		public static String getDayAsString(int day, String format) {
			SimpleDateFormat simpleFormat = new SimpleDateFormat(format);
			return simpleFormat.format(getDayAsDate(day));
		}

		/**
		 * Gets a date with a desired format as a String
		 * 
		 * @param date
		 *            date to be formated
		 * 
		 * @param format
		 *            desired format (e.g. "yyyy-MM-dd HH:mm:ss")
		 * @return returns a date with the given format
		 */
		public static String formatDate(long date, String format) {
			return formatDateBase(date, format, null);
		}

		/**
		 * Gets a date with a desired format as a String
		 * 
		 * @param date
		 *            date to be formated
		 * 
		 * @param format
		 *            desired format (e.g. "yyyy-MM-dd HH:mm:ss")
		 * 
		 * @param timeZone
		 *            specify the intended timezone (e.g. "GMT", "UTC", etc.)
		 * @return returns a date with the given format
		 */
		public static String formatDate(long date, String format, String timeZone) {
			return formatDateBase(date, format, timeZone);
		}

		private static String formatDateBase(long date, String format, String timeZone) {
			DateFormat simpleFormat = new SimpleDateFormat(format);
			if (timeZone != null) {
				simpleFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
			} else {
				simpleFormat.setTimeZone(TimeZone.getDefault());
			}
			return simpleFormat.format(date);
		}

		/**
		 * Gets the desired day as a Date
		 * 
		 * @param day
		 *            Can be: <li>QuickUtils.date.YESTERDAY</li><li>
		 *            QuickUtils.date.TODAY</li><li>QuickUtils.date.TOMORROW</li>
		 * @return returns a Date for that day
		 * 
		 */
		public static Date getDayAsDate(int day) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, day);
			return cal.getTime();
		}

		/**
		 * Parse a data string into a real Date
		 * 
		 * Note: (e.g. "yyyy-MM-dd HH:mm:ss")
		 * 
		 * @param dateString
		 *            date in String format
		 * @param dateFormat
		 *            desired format (e.g. "yyyy-MM-dd HH:mm:ss")
		 * 
		 * @return
		 */
		public static Date parseDate(String dateString, String dateFormat) {
			Date newDate = null;

			try {
				newDate = new SimpleDateFormat(dateFormat, Locale.ENGLISH).parse(dateString);
			} catch (ParseException e) {
				QuickUtils.log.d("parse error", e);
			}
			return newDate;
		}

		/**
		 * get Current time in milliseconds
		 * 
		 * @return current time in milliseconds
		 */
		public static long getCurrentTimeInMiliseconds() {
			return TimeUnit.MILLISECONDS.toMillis(Calendar.getInstance().getTimeInMillis());
		}

		/**
		 * get Current time in seconds
		 * 
		 * @return current time in seconds
		 */
		public static long getCurrentTimeInSeconds() {
			return TimeUnit.SECONDS.toSeconds(Calendar.getInstance().getTimeInMillis());

		}

	}

	/**
	 * Internet utils
	 * 
	 * @author cesar
	 * 
	 */
	public static class web {
		/**
		 * private constructor
		 */
		private web() {
		}

		/**
		 * Checks if the app has connectivity to the Internet
		 * 
		 * @param context
		 *            application context
		 * @return true if has connection to the Internet and false if it
		 *         doesn't
		 */
		public static boolean hasInternetConnection(Context context) {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm == null)
				return false;
			NetworkInfo info = cm.getActiveNetworkInfo();

			// 3G
			State mobile = cm.getNetworkInfo(0).getState();

			// wifi
			State wifi = cm.getNetworkInfo(1).getState();

			if (info == null)
				return false;

			if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {

				return info.isConnectedOrConnecting();
			} else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {

				return info.isConnectedOrConnecting();
			}
			return info.isConnectedOrConnecting();
		}

		/**
		 * Does a GET request to a given url
		 * 
		 * Note: Please use this method on an AsyncTask in order not to freeze
		 * the application unnecessarely
		 * (http://developer.android.com/guide/practices/responsiveness.html)
		 * 
		 * @param url
		 *            given url
		 * @return the string output of the GET request or null if something
		 *         went wrong
		 */
		public static String HTTPGetRequest(String url) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			StringBuffer stringBuffer = new StringBuffer();

			HttpGet httpGet = new HttpGet(url);

			try {
				HttpResponse response = httpClient.execute(httpGet, localContext);
				InputStream instream = response.getEntity().getContent();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(instream));

				String buffer;
				while ((buffer = bufferedReader.readLine()) != null) {
					stringBuffer.append(buffer);
				}

			} catch (ClientProtocolException e) {
				QuickUtils.log.e("ClientProtocolException", e);
			} catch (IOException e) {
				QuickUtils.log.e("IOException", e);
			} catch (IllegalArgumentException e) {
				QuickUtils.log.e("IllegalArgumentException", e);
			}

			return stringBuffer == null ? null : stringBuffer.toString();
		}

		/**
		 * Set wireless connectivity On, also this method will need the
		 * permissions "android.permission.CHANGE_WIFI_STATE" and
		 * "android.permission.ACCESS_WIFI_STATE"
		 * 
		 * @param context
		 *            - application context
		 * @param state
		 *            - set enable or disable wireless connection
		 * @return true if was set successfully and false if it wasn't
		 */
		public static boolean changeWirelessState(Context context, boolean state) {
			try {
				WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				wifi.setWifiEnabled(state);
				return true;
			} catch (Exception e) {
				return false;
			}
		}

		/**
		 * Check if can connect to the server, also this method will need the
		 * permissions "android.permission.INTERNET"
		 * 
		 * @param u
		 *            - server url
		 * @return true if the connection returned a successful code
		 */
		public static boolean checkServerConnection(URL u) {
			boolean value = false;
			try {
				value = new RetreiveCheckServerConnection().execute(u).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return value;

		}

		/**
		 * AsyncTask that will run the code responsible to try to connect to the
		 * server url
		 * 
		 * @author Pereira
		 * 
		 */
		private static class RetreiveCheckServerConnection extends AsyncTask<URL, Void, Boolean> {

			private Exception exception;

			protected Boolean doInBackground(URL... url) {
				try {
					HttpURLConnection huc = (HttpURLConnection) url[0].openConnection();
					huc.setRequestMethod("GET");
					huc.connect();
					int code = huc.getResponseCode();
					if (code == 200) {
						return true;
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}

		}

		/**
		 * Check if can connect to the server, also this method will need the
		 * permissions "android.permission.INTERNET"
		 * 
		 * @param serverURL
		 *            - server url
		 * @return true if the connection returned a successful code
		 */
		public static boolean checkServerConnection(String serverURL) {
			boolean value = false;
			try {
				value = new RetreiveCheckServerConnectionString().execute(serverURL).get();
			} catch (InterruptedException e) {
				QuickUtils.log.e("InterruptedException", e);
			} catch (ExecutionException e) {
				QuickUtils.log.e("ExecutionException", e);
			}
			return value;
		}

		/**
		 * AsyncTask that will run the code responsible to try to connect to the
		 * server url
		 * 
		 * @author Pereira
		 * 
		 */
		private static class RetreiveCheckServerConnectionString extends AsyncTask<String, Void, Boolean> {

			private Exception exception;

			protected Boolean doInBackground(String... serverURL) {
				try {
					URL u = new URL(serverURL[0]);
					HttpURLConnection huc = (HttpURLConnection) u.openConnection();
					huc.setRequestMethod("GET"); // OR huc.setRequestMethod
													// ("HEAD");
					huc.connect();
					int code = huc.getResponseCode();
					if (code == 200) {
						return true;
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}

		}
	}

	public static class share {

		/**
		 * Share via Email
		 * 
		 * @param context
		 *            application context
		 * @param email
		 *            destination email (e.g. support@company.com)
		 * @param subject
		 *            email subject
		 * @param emailBody
		 *            email body
		 */
		public static void sendEmail(Context context, String email, String subject, String emailBody) {
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.setType("message/rfc822");
			emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
			emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

			emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			try {
				context.startActivity(emailIntent);
			} catch (android.content.ActivityNotFoundException ex) {
				QuickUtils.misc.toast(context, "There are no email clients installed.");
			}
		}

		/**
		 * Share message via Twitter
		 * 
		 * @param context
		 *            Application Context
		 * @param message
		 *            Message to be delivered
		 */
		public static void sendTweet(Context context, String message) {
			shareMethod(context, message, "com.twitter.android.PostActivity");
		}

		/**
		 * Share message via Facebook (for the time being, facebook api has a
		 * bug that prevents this from working, we hope facebook solves it fast
		 * enough!)
		 * 
		 * @param context
		 *            Application Context
		 * @param message
		 *            Message to be delivered
		 */
		@Deprecated
		public static void updateFacebookStatus(Context context, String message) {
			shareMethod(context, message, "facebook");
		}

		/**
		 * Private method that handles facebook and twitter sharing
		 * 
		 * @param context
		 *            Application Context
		 * @param message
		 *            Message to be delivered
		 * @param activityInfoName
		 */
		private static void shareMethod(Context context, String message, String activityInfoName) {
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
			PackageManager pm = context.getPackageManager();
			List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
			for (final ResolveInfo app : activityList) {
				if ((app.activityInfo.name).contains(activityInfoName)) {
					final ActivityInfo activity = app.activityInfo;
					final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
					shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
					shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					shareIntent.setComponent(name);
					context.startActivity(shareIntent);
					break;
				}
			}
		}

		/**
		 * Generic method for sharing that Deliver some data to someone else.
		 * Who the data is being delivered to is not specified; it is up to the
		 * receiver of this action to ask the user where the data should be
		 * sent.
		 * 
		 * @param context
		 *            Application Context
		 * @param message
		 *            Message to be delivered
		 */
		public static void genericSharing(Context context, String message) {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, message);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			try {
				context.startActivity(intent);
			} catch (android.content.ActivityNotFoundException ex) {
				QuickUtils.log.e("ActivityNotFound", ex);
			}
		}
	}

	/**
	 * File Utils
	 * 
	 * @author cesar
	 * 
	 */
	public static class sdcard {

		/**
		 * private constructor
		 */
		private sdcard() {
		}

		/**
		 * Check if the SD Card is Available
		 * 
		 * @return true if the sd card is available and false if it is not
		 */
		public static boolean isSDCardAvailable() {
			boolean mExternalStorageAvailable = false;

			String state = Environment.getExternalStorageState();

			if (Environment.MEDIA_MOUNTED.equals(state)) {
				// We can read and write the media
				mExternalStorageAvailable = true;
			} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
				// We can only read the media
				mExternalStorageAvailable = true;

			} else {
				// Something else is wrong. It may be one of many other states,
				// but all we need
				// to know is we can neither read nor write
				mExternalStorageAvailable = false;
			}

			return mExternalStorageAvailable;

		}

		/**
		 * Check if the SD Card is writable
		 * 
		 * @return true if the sd card is writable and false if it is not
		 */
		public static boolean isSDCardWritable() {

			boolean mExternalStorageWriteable = false;

			String state = Environment.getExternalStorageState();

			if (Environment.MEDIA_MOUNTED.equals(state)) {
				// We can read and write the media
				mExternalStorageWriteable = true;
			} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
				// We can only read the media

				mExternalStorageWriteable = false;
			} else {
				// Something else is wrong. It may be one of many other states,
				// but all we need
				// to know is we can neither read nor write
				mExternalStorageWriteable = false;
			}

			return mExternalStorageWriteable;

		}

		/**
		 * Read file from SDCard
		 */
		// public static void readFileFromSDCard() {
		// // TODO this is incomplete
		//
		// File directory = Environment.getExternalStorageDirectory();
		//
		// // Assumes that a file article.rss is available on the SD card
		// File file = new File(directory + "/article.rss");
		// if (!file.exists()) {
		// throw new RuntimeException("File not found");
		// }
		// BufferedReader reader = null;
		// try {
		// reader = new BufferedReader(new FileReader(file));
		// StringBuilder builder = new StringBuilder();
		// String line;
		// while ((line = reader.readLine()) != null) {
		// builder.append(line);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// if (reader != null) {
		// try {
		// reader.close();
		// } catch (IOException e) {
		// QuickUtils.log.e("IO Exception:", e);
		// }
		// }
		// }
		// }

		/**
		 * Creates the specified <code>toFile</code> as a byte for byte copy of
		 * the <code>fromFile</code>. If <code>toFile</code> already exists,
		 * then it will be replaced with a copy of <code>fromFile</code>. The
		 * name and path of <code>toFile</code> will be that of
		 * <code>toFile</code>.<br/>
		 * <br/>
		 * <i> Note: <code>fromFile</code> and <code>toFile</code> will be
		 * closed by this function.</i>
		 * 
		 * @param fromFile
		 *            - FileInputStream for the file to copy from.
		 * @param toFile
		 *            - FileOutpubStream for the file to copy to.
		 */
		public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
			FileChannel fromChannel = null;
			FileChannel toChannel = null;
			try {
				fromChannel = fromFile.getChannel();
				toChannel = toFile.getChannel();
				fromChannel.transferTo(0, fromChannel.size(), toChannel);
			} finally {
				try {
					if (fromChannel != null) {
						fromChannel.close();
					}
				} finally {
					if (toChannel != null) {
						toChannel.close();
					}
				}
			}
		}

		/**
		 * Creates the specified <code>toFile</code> as a byte for byte copy of
		 * the <code>fromFile</code>. If <code>toFile</code> already exists,
		 * then it will be replaced with a copy of <code>fromFile</code>. The
		 * name and path of <code>toFile</code> will be that of
		 * <code>toFile</code>.<br/>
		 * <br/>
		 * <i> Note: <code>fromFile</code> and <code>toFile</code> will be
		 * closed by this function.</i>
		 * 
		 * @param fromFile
		 *            - File to copy from.
		 * @param toFile
		 *            - File to copy to.
		 */
		public static void copyFile(File fromFile, File toFile) throws IOException {
			copyFile(new FileInputStream(fromFile), new FileOutputStream(toFile));
		}

		/**
		 * Get the SDCard Path
		 * 
		 * @return the complete path to the SDCard
		 */
		public static String getSDCardPath() {
			return Environment.getExternalStorageDirectory().toString() + "/";
		}

		/**
		 * Get the SDCard Path as a File
		 * 
		 * @return the complete path to the SDCard
		 */
		public static File getSDCardPathFile() {
			return Environment.getExternalStorageDirectory();
		}

		/**
		 * Check if file exists on SDCard or not
		 * 
		 * @param filePath
		 *            - its the path of the file after SDCardDirectory (no need
		 *            for getExternalStorageDirectory())
		 * @return boolean - if file exist on SDCard or not
		 */
		public static boolean checkIfFileExists(String filePath) {
			File file = new File(getSDCardPath(), filePath);
			return (file.exists() ? true : false);
		}

		/**
		 * Detailed log with a "yyyy/MM/dd HH:mm:ss.SSS" timestamp
		 * 
		 * @param text
		 *            text to append
		 * @param logFilePath
		 *            path to the file
		 */
		public static void appendTextToLog(String text, String logFilePath) {
			writeToFile(text, logFilePath, true);

		}

		/**
		 * Append a new line of text to a certain file provided by `logFilePath`
		 * 
		 * @param text
		 *            text to append
		 * @param logFilePath
		 *            path to the file
		 */
		public static void appendTextToFile(String text, String logFilePath) {
			writeToFile(text, logFilePath, false);
		}

		/**
		 * private write to file method
		 * 
		 * @param text
		 *            text to append
		 * @param logFilePath
		 *            path to the file
		 * @param isDetailed
		 *            if it should show the timestamp or not
		 */
		private static void writeToFile(String text, String logFilePath, boolean isDetailed) {
			if (isSDCardAvailable() && isSDCardWritable() && text != null) {

				try {
					File file = new File(logFilePath);
					OutputStream os = new FileOutputStream(file, true);
					if (isDetailed) {
						os.write(("---" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(Calendar.getInstance().getTime()) + "---\n")
								.getBytes());
					}
					os.write((text + "\n").getBytes());
					// os.write(("------\n").getBytes());
					os.close();
				} catch (Exception e) {
					QuickUtils.log.e("Exception", e);
				}
			} else {
				return;
			}
		}

	}

	/**
	 * Security Utils
	 * 
	 * @author cesar
	 * 
	 */
	public static class security {

		/**
		 * private constructor
		 */
		private security() {
		}

		/**
		 * Calculate the MD5 of a given String
		 * 
		 * @param string
		 *            String to be MD5'ed
		 * @return MD5'ed String
		 */
		public static String calculateMD5(String string) {
			byte[] hash;

			try {
				hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException("Huh, MD5 should be supported?", e);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("Huh, UTF-8 should be supported?", e);
			}

			StringBuilder hex = new StringBuilder(hash.length * 2);

			for (byte b : hash) {
				int i = (b & 0xFF);
				if (i < 0x10)
					hex.append('0');
				hex.append(Integer.toHexString(i));
			}

			return hex.toString();
		}

		/**
		 * Calculate the SHA-1 of a given String
		 * 
		 * @param string
		 *            String to be SHA1'ed
		 * @return SHA1'ed String
		 */
		public static String calculateSHA1(String string) {
			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("SHA-1");
			} catch (NoSuchAlgorithmException e) {
				QuickUtils.log.e("NoSuchAlgorithmException", e);
			}
			try {
				md.update(string.getBytes("iso-8859-1"), 0, string.length());
			} catch (UnsupportedEncodingException e) {
				QuickUtils.log.e("UnsupportedEncodingException", e);

			}
			byte[] sha1hash = md.digest();
			return convertToHex(sha1hash);
		}

		private static String convertToHex(byte[] data) {
			StringBuilder buf = new StringBuilder();
			for (byte b : data) {
				int halfbyte = (b >>> 4) & 0x0F;
				int two_halfs = 0;
				do {
					buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
					halfbyte = b & 0x0F;
				} while (two_halfs++ < 1);
			}
			return buf.toString();
		}
	}
}
