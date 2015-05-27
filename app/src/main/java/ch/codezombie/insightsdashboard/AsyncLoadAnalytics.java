package ch.codezombie.insightsdashboard;

import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.model.GaData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Asynchronously load information from Google Analytics
 */
class AsyncLoadAnalytics extends CommonAsyncTask {

    AsyncLoadAnalytics(MainActivity activity) {
        super(activity);
    }

    @Override
    protected void doInBackground() throws IOException {
        // Replace with a valid account identifier
        Analytics.Data.Ga.Get ga = this.client.data().ga().get("ga:XXXXXXXXXX", "30daysAgo", "yesterday", "ga:pageviews");

        ga.setDimensions("ga:keyword");
        ga.setSort("-ga:pageviews");

        GaData data = ga.execute();

        activity.resultList = printResults(data);
    }


    private List<String> printResults(GaData results) {
        List<String> result = new ArrayList<String>();

        // Parse the response from the Core Reporting API for
        // the profile name and number of sessions.
        if (results != null && !results.getRows().isEmpty()) {

            for (List<String> row : results.getRows()) {
                result.add(String.format("%s: %s", row.get(0), row.get(1)));
            }

        } else {
            result.add("No results found");
        }

        return result;
    }

    static void run(MainActivity activity) {
        new AsyncLoadAnalytics(activity).execute();
    }
}
