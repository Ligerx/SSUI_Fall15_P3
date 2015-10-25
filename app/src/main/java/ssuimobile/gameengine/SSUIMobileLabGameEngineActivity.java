package ssuimobile.gameengine;

import android.app.Activity;
import android.os.Bundle;

public class SSUIMobileLabGameEngineActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        GameEngineBase gb = new GameEngineBase(R.raw.basic_fsmspec, this);
        
        
        setContentView(gb.finalInit());
        
        
    }
}