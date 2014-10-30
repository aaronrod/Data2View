package san.data2view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * see res/raw/main.json
 */
public class MainActivity extends ActionBarActivity {
    private static final int DEFAULT_LAYOUT = R.layout.template_0;
    private Template template;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(DEFAULT_LAYOUT);
        initLayout();

    }

    @Override
    public void setContentView(int layoutResID){
        template = getTemplate();
        Integer templateId = template.getId();
        if(null != templateId){
            //change default template to new template
            layoutResID = template.getResLayoutID();
        }
        super.setContentView(layoutResID);
    }

    /**
     * TODO needs a lot of work with the dynamic logic
     * TODO casting will be an issue, though possible other approach is preferable.
     *
     * @see <a href="http://stackoverflow.com/questions/2127318/java-how-can-i-do-dynamic-casting-of-a-variable-from-one-type-to-another">
     *     Stack Overflow: casting-of-a-variable-from-one-type-to-another</a>
     */
    private void initLayout() {
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);

        //Can add actions blindly as they come
//        for(int i=0; i<viewGroup.getChildCount(); ++i) {
//            View nextChild = viewGroup.getChildAt(i);
//        }

        Map<String, String> actionMap = template.getActions();

        TextView childA = (TextView)viewGroup.getChildAt(0);
        childA.setText(actionMap.get("title_text"));

        TextView childB = (TextView)viewGroup.getChildAt(1);
        childB.setText(actionMap.get("paragraph_text"));

//        MyButton childC = (MyButton)viewGroup.getChildAt(2);
//        String action = actionMap.get("next_button_destination_id");
//        Intent intent = template.getButtonIntent(action);
//        childC.onClick(intent);
    }

    /**
     * @return template id or null
     */
    private Template getTemplate() {
        //TODO Incomplete Method
        //TODO integrate JSON
        return new Template(this, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static class Template {
        private Context context;
        private Integer id;
        private int resLayoutID = R.layout.template_0;

        public Template(Context context, JSONObject jObject){
            this.context = context;
            //TODO parse JSON
        }

        /**
         *
         * TODO temp workaround to interpreting JSON data
         * TODO actions in String form seem a little weak
         *
         * "title_text": "Welcome to the Main Page",
         * "paragraph_text": "Sed ut perspiciatis, unde omnis iste natus error sit voluptatem accusantium doloremque laudantium",
         * "next_button_destination_id": "200"
         *
         * @return
         */
        public Map<String, String> getActions(){
            Map<String, String> actions = new HashMap<String, String>();
            actions.put("title_text", "Welcome to the Main Page");
            actions.put("paragraph_text", "Sed ut perspiciatis, unde omnis iste natus error sit voluptatem accusantium doloremque laudantium");
            actions.put("next_button_destination_id", "200");
            return actions;
        }

        /**
         * Experimental if xml should come from server
         * @return
         *
         * @see <a href="http://stackoverflow.com/questions/17379168/android-using-xml-layout-from-internal-storage">
         *     Stack Overflow: android-using-xml-layout-from-internal-storage</a>
         */
        private XmlParser xmlParser(){
            return new XmlParser();
        }

        /**
         * @return May return null
         */
        public Integer getId(){
            return id;
        }

        public int getResLayoutID(){
            return resLayoutID;
        }

        public interface ButtonSetup{
            Intent getIntent(String destinationId);
        }

        public Intent getButtonIntent(String destinationId){
            Class<? extends Activity> destinationClass = SecondActivity.class;
            switch (Integer.parseInt(destinationId)){
                case 100:
                    destinationClass = MainActivity.class;
                    break;
                case 200:
                    destinationClass = SecondActivity.class;
                    break;
            }
            return new Intent(context, destinationClass);
        }

    }

    public static class MyButton extends Button{

        public MyButton(Context context) {
            super(context);

        }

        public void onClick(final Intent destination){
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    getContext().startActivity(destination);
                }
            });
        }
    }
}
