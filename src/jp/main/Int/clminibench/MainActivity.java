package jp.main.Int.clminibench;

import com.example.clminibench.R;

import java.util.ArrayList;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView;

public class MainActivity extends FragmentActivity {

    LinearLayout hor, left, right;
    ScrollView lefts, rights;
    ListView tests;
    TextView right_text;

    CLminibench cl;
    LayoutParams lp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	this.hor = new LinearLayout(this);
	this.lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                                   LayoutParams.MATCH_PARENT);
	this.hor.setLayoutParams(lp);
	this.hor.setOrientation(LinearLayout.HORIZONTAL);

	this.setContentView(hor);

	//this.lefts = new ScrollView(this);
        //this.left = new LinearLayout(this);
	this.right = new LinearLayout(this);

	this.lp = new LayoutParams(420,
                                   LayoutParams.MATCH_PARENT);

	//this.lefts.setBackgroundColor(0xffffcccc);
	//this.left.setOrientation(LinearLayout.VERTICAL);
	this.right.setOrientation(LinearLayout.VERTICAL);

        int have = CLminibench.init0();
        if (have) {
            CLminibench cl = new CLminibench();
            cl.init();
            this.cl = cl;

            this.rights = new ScrollView(this);
            this.rights.addView(right);

            this.right_text = new TextView(this);
            this.right_text.setText("right");
            right.addView(this.right_text);
            this.right_text.setTextSize(20);

            this.setDevice(0);
        } else {
            
        }
    }

    public void setDevice(int dev) {
        if (this.tests != null) {
            hor.removeView(this.tests);
            hor.removeView(rights);
            this.tests = null;
        }

        cl.seldev(dev);
        ArrayList<String> al = new ArrayList<String>();
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);
        this.tests = new ListView(this);

        tests.setLayoutParams(this.lp);
        tests.setAdapter(aa);
        tests.setBackgroundColor(0xffffcccc);

	//lefts.addView(left);
	hor.addView(this.tests);
	hor.addView(rights);

        aa.add(cl.cur_platform_name);
        aa.add(cl.cur_dev_name);

        for (int i=0; i<cl.num_bench; i++) {
            aa.add(cl.bench_name_list[i]);
        }

        this.tests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    switch (pos) {
                    case 0:
                        right_text.setText(cl.cur_dev_name);
                        break;
                    case 1:
                        right_text.setText(cl.cur_dev_config);
                        break;
                    default: {
                        int bench_id = pos - 2;
                        BenchResult r = cl.run(bench_id);

                        if (r.code == BenchResult.BENCH_OK) {
                            String rs;
                            if (cl.result_type_list[bench_id] == CLminibench.RESULT_TYPE_INT) {
                                rs = cl.bench_desc_list[bench_id] + "\n" +
                                    r.ival + cl.bench_unit_list[bench_id] + "\n\n" +
                                    cl.bench_cl_code_list[bench_id];
                            } else {
                                rs = cl.bench_desc_list[bench_id] + "\n" +
                                    r.fval + cl.bench_unit_list[bench_id] + "\n\n" +
                                    cl.bench_cl_code_list[bench_id];
                            }
                            //Log.d("cl", rs);
                            right_text.setText(rs);
                        }
                    }
                        break;
                    }
                }
            }

            );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_settings: {
            SelectDeviceDialogFragment newFragment = new SelectDeviceDialogFragment(this,cl.dev_names);
            ((DialogFragment)newFragment).show(getSupportFragmentManager(), "seldev");
        }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

}
