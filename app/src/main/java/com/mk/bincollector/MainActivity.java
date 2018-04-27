package com.mk.bincollector;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Map;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BinParser bp = new BinParser();
        AsyncTask runner = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                bp.init();
                return null;
            }

            @Override
            protected void onPostExecute(Object result) {

                TextView tv = (TextView)(findViewById(R.id.tvBlackBin));
                tv.setText(bp.getBinDay(BinParser.Bin.BLACK_BIN));

                tv = (TextView)(findViewById(R.id.tvBlueBin));
                tv.setText(bp.getBinDay(BinParser.Bin.BLUE_BIN));

                tv = (TextView)(findViewById(R.id.tvBrownBin));
                tv.setText(bp.getBinDay(BinParser.Bin.BROWN_BIN));
            }

        };

        runner.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
