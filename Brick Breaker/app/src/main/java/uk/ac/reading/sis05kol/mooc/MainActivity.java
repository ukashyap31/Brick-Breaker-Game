
package uk.ac.reading.sis05kol.mooc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private static final int MENU_RESUME = 1;
    private static final int MENU_START = 2;
    private static final int MENU_STOP = 3;

    private GameThread mGameThread;
    private GameView mGameView;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        mGameView = (GameView)findViewById(R.id.gamearea);
        mGameView.setStatusView((TextView)findViewById(R.id.text));
        mGameView.setScoreView((TextView)findViewById(R.id.score));

        this.startGame(mGameView, null, savedInstanceState);

        // Hides nav/status bar from screen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    private void startGame(GameView gView, GameThread gThread, Bundle savedInstanceState) {

        //Set up a new game, we don't care about previous states
        mGameThread = new TheGame(mGameView);
        mGameView.setThread(mGameThread);
        mGameThread.setState(GameThread.STATE_READY);
        mGameView.startSensor((SensorManager)getSystemService(Context.SENSOR_SERVICE));
    }

    /*
     * Activity state functions
     */

    @Override
    protected void onPause() {
        super.onPause();

        if(mGameThread.getMode() == GameThread.STATE_RUNNING) {
            mGameThread.setState(GameThread.STATE_PAUSE);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mGameView.cleanup();
        mGameView.removeSensor((SensorManager)getSystemService(Context.SENSOR_SERVICE));
        mGameThread = null;
        mGameView = null;
    }



    public void mainMenu(View view) {
        startActivity(new Intent(getApplicationContext(), Menu.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, MENU_START, 0, R.string.menu_start);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_START:
                mGameThread.doStart();
                return true;
        }
        return false;
    }
}

// This file is part of the course "Begin Programming: Build your first mobile game" from futurelearn.com
// Copyright: University of Reading and Karsten Lundqvist
// It is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// It is is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
//
// You should have received a copy of the GNU General Public License
// along with it.  If not, see <http://www.gnu.org/licenses/>.