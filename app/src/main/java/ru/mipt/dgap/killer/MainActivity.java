package ru.mipt.dgap.killer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.vk.sdk.VKUIHelper;

import java.io.InputStream;


public class MainActivity extends ActionBarActivity {
    Core core;
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        core = new Core(this);

        TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec spec = tabs.newTabSpec("tag1");

        spec.setContent(R.id.tab1);
        spec.setIndicator("Киллер");
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Жертва");
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tag3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Новости");
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        View.OnClickListener oclBtnLogin = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                core.logIn();
            }
        };
        Button btnLogin = (Button) findViewById(R.id.login_button);
        btnLogin.setOnClickListener(oclBtnLogin);

        View.OnClickListener oclBtnVictim = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                core.getVictim();
            }
        };

        Button btnVictim = (Button) findViewById(R.id.refresh_victim_btn);
        btnVictim.setOnClickListener(oclBtnVictim);

        View.OnClickListener oclBtnKill = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Важное сообщение!")
                        .setMessage("Жизнь бесценна, одумайтесь! Вы действительно желаете ее отнять?")
                        //.setIcon(R.drawable.ic_android_cat)
                        .setCancelable(false)
                        .setNegativeButton("Да",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        kill();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        };

        Button btnKill = (Button) findViewById(R.id.kill_victim_btn);
        btnKill.setOnClickListener(oclBtnKill);

    }
    public void kill(){
        EditText agentVictimName = (EditText) findViewById(R.id.victim_agent_name);
        String name = agentVictimName.getText().toString();
        core.kill(name);

    }
    public void refreshKiller(String... strings){
        Log.d("fuck", "fock");
        TextView agentName = (TextView) this.findViewById(R.id.agent_name);
        TextView userName = (TextView) this.findViewById(R.id.killer_name);
        agentName.setText("Агент " + strings[1]);
        userName.setText(strings[2]);
        new DownloadImageTask((ImageView) findViewById(R.id.login_image))
                .execute(strings[0]);
    }
    public void refreshVictim(String... strings){
        Log.d("fuck", "fock");
        TextView victimName = (TextView) this.findViewById(R.id.victim_name);
        TextView victimId = (TextView) this.findViewById(R.id.victim_id);
        victimName.setText(strings[1]);
        victimId.setText(strings[2]);
        victimId.setClickable(true);
        new DownloadImageTask((ImageView) findViewById(R.id.victim_avatar))
                .execute(strings[0]);
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

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }
}
