package in.informationworks.learnaptcomic.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.informationworks.learnaptcomic.Adapters.HomeAdapter;
import in.informationworks.learnaptcomic.Models.ComicCardPreviewItem;
import in.informationworks.learnaptcomic.Models.CommonRecyclerItem;
import in.informationworks.learnaptcomic.Models.CoverItem;
import in.informationworks.learnaptcomic.Models.SingleItemModel;
import in.informationworks.learnaptcomic.R;

public class ComicDetailsActivity extends AppCompatActivity {
    int temp;
    String comicname,comicImageURL,comicpages,s,comicsize;
    Intent intent;
    int comicID;
    ImageView imageView4;
    TextView nameLable;
    TextView comicName;
    TextView sizeLabel;
    TextView comicSize;
    TextView pagesLabel;
    TextView comicPages;
    Button readNowButton;
    android.support.v7.widget.RecyclerView previewRecyclerView;
    Context context;
    HomeAdapter homeAdapter;
    ArrayList<CommonRecyclerItem> recyclerItems;
    JsonObject jsonObject,comicObject;
    List<ComicCardPreviewItem> receivedComicImages;
    JsonArray comicImagePreviewArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_details);
        bindViews();
        readIntent();
        prepareRecyclerView();
        getData(comicID);
        startFetchingData();
    }


    private void bindViews(){
        previewRecyclerView = (android.support.v7.widget.RecyclerView) findViewById(R.id.preview_recycler_view);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        nameLable = (TextView) findViewById(R.id.nameLable);
        comicName = (TextView) findViewById(R.id.comicName);
        sizeLabel = (TextView) findViewById(R.id.sizeLabel);
        comicSize = (TextView) findViewById(R.id.comicSize);
        pagesLabel = (TextView) findViewById(R.id.pagesLabel);
        comicPages = (TextView) findViewById(R.id.comicPages);
        readNowButton = (Button) findViewById(R.id.readNowButton);
    }

    private void readIntent()
    {
     comicID=getIntent().getIntExtra(SingleItemModel.EXTRA_ID,-1);
        Toast.makeText(this, "comic id:"+comicID+" : "+comicname, Toast.LENGTH_SHORT).show();
    }

    private void prepareRecyclerView() {
        recyclerItems = new ArrayList<>();
        previewRecyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
    }

    private void getData(int comicID) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.2.30:3000/api/mobile/v1/comics/"+comicID+".json";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        jsonObject = (new JsonParser()).parse(response).getAsJsonObject();
                        comicObject = jsonObject.get("comic").getAsJsonObject();
                        comicname=comicObject.get("name").getAsString();
                        comicpages=comicObject.get("pages_count").getAsString();
                        comicsize=comicObject.get("size").getAsString();
                        JsonObject coverImageObject = comicObject.get("cover_image").getAsJsonObject();
                        comicImageURL = coverImageObject.get("compressed_image_url").getAsString();
                        Picasso.with(getApplicationContext()).load(comicImageURL).error(R.drawable.ic_menu_manage).into(imageView4);
                        //Toast.makeText(getApplicationContext(),comicname+":"+comicpages+":"+comicImageURL,Toast.LENGTH_LONG).show();
                        comicName.setText(comicname);
                        comicPages.setText(comicpages);
                        comicSize.setText(comicsize);
                        comicImagePreviewArray = comicObject.getAsJsonArray("comic_images");
                        Type listType = new TypeToken<ArrayList<ComicCardPreviewItem>>(){}.getType();
                        receivedComicImages = new Gson().fromJson(comicImagePreviewArray,listType);
                        setDataInRecyclerView();



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  mTextView.setText("That didn't work!");
            }
        });

        queue.add(stringRequest);
    }

    public void onReadNow(View view)
    {
        Intent comicDetailsIntent = new Intent(this,ComicImagePlayerActivity.class);
        comicDetailsIntent.putExtra("comicID",comicID);
        startActivity(comicDetailsIntent);
    }

    private void startFetchingData()
    {
        homeAdapter = new HomeAdapter(this,recyclerItems);
        previewRecyclerView.setAdapter(homeAdapter);
    }

    private void setDataInRecyclerView()
    {
        for (ComicCardPreviewItem comicCardPreviewItem : receivedComicImages) {
            recyclerItems.add(new CommonRecyclerItem(CommonRecyclerItem.TYPE_SINGLE_PREVIEW_IMAGE,comicCardPreviewItem));
        }

    }

}
