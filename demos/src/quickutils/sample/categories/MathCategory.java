package quickutils.sample.categories;

import java.util.ArrayList;
import java.util.Arrays;

import quickutils.core.QuickUtils;
import quickutils.sample.ParentActivity;
import quickutils.sample.R;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug.FlagToString;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MathCategory extends ParentActivity {

	private ListView mainListView;
	private ArrayAdapter<String> listAdapter;
	private Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = this.getApplicationContext();

		// Find the ListView resource.
		mainListView = (ListView) findViewById(R.id.list);

		ArrayList<String> categoriesArray = new ArrayList<String>();

		Resources res = getResources();
		String[] categoriesStringArray = res.getStringArray(R.array.math_array);
		categoriesArray.addAll(Arrays.asList(categoriesStringArray));

		// Create ArrayAdapter using the category list.
		listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, categoriesArray);

		// Set the ArrayAdapter as the ListView's adapter.
		mainListView.setAdapter(listAdapter);

		mainListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
				Object listItem = mainListView.getItemAtPosition(position);

				String optionString = listItem.toString();

				int minValue = 0;
				int intValue = 15;
				float minFloatValue = (float) 0.3;
				float floatValue = (float) 2.2;
				
				String resultString = null;

				// isOdd
				if (optionString.equals("isOdd")) {
					resultString = "(" + intValue + ") = " + QuickUtils.math.isOdd(intValue);
				} else
				// isEven
				if (optionString.equals("isEven")) {
					resultString = "(" + intValue + ") = " + QuickUtils.math.isEven(intValue);
				}
				// degreesToRadians
				if (optionString.equals("degreesToRadians")) {
					resultString = "(" + intValue + ") = " + QuickUtils.math.degreesToRadians(intValue);
				}
				// radiansToDegrees
				if (optionString.equals("radiansToDegrees")) {
					resultString = "(" + intValue + ") = " + QuickUtils.math.radiansToDegrees(intValue);
				}
				// exponencial
				if (optionString.equals("exponencial")) {
					resultString = "(" + intValue + ") = " + QuickUtils.math.exponencial(intValue);
				}
				// logarithm
				if (optionString.equals("logarithm")) {
					resultString = "(" + intValue + ") = " + QuickUtils.math.logarithm(intValue);
				}
				// logarithm
				if (optionString.equals("getRandomNumber")) {
					resultString = "(" + minValue + "," + intValue + ") = " + QuickUtils.math.getRandomNumber(minValue, intValue);
				}
				// max
				if (optionString.equals("max")) {
					resultString = "(" + minValue + "," + intValue + ") = " + QuickUtils.math.max(minValue, intValue);
				}
				// min
				if (optionString.equals("min")) {
					resultString = "(" + minValue + "," + intValue + ") = " + QuickUtils.math.min(minValue, intValue);
				}	
				// abs
				if (optionString.equals("abs")) {
					resultString = "(" + floatValue + ") = " + QuickUtils.math.abs(floatValue);
				}
				// acos
				if (optionString.equals("acos")) {
					resultString = "(" + floatValue + ") = " + QuickUtils.math.acos(floatValue);
				}
				// asin
				if (optionString.equals("asin")) {
					resultString = "(" + floatValue + ") = " + QuickUtils.math.asin(floatValue);
				}
				// tan
				if (optionString.equals("tan")) {
					resultString = "(" + floatValue + ") = " + QuickUtils.math.tan(floatValue);
				}
				// atan
				if (optionString.equals("atan")) {
					resultString = "(" + floatValue + ") = " + QuickUtils.math.atan(floatValue);
				}
				// atan2
				if (optionString.equals("atan2")) {
					resultString = "(" + minFloatValue + "," + floatValue + ") = " + QuickUtils.math.atan2(minFloatValue,floatValue);
				}

				// Output the result
				QuickUtils.misc.toast(mContext, optionString + " " + resultString);
			}
		});
	}
}
