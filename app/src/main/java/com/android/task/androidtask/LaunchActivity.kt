package com.android.task.androidtask

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LaunchActivity : AppCompatActivity() {


    lateinit var getuserdetails: GetUserDetails
    var TAG: String = "LaunchActivity";
    var getUserDetailsCollections: ArrayList<GetUserDetails> = ArrayList()
    lateinit var getusermodel:GetUserDetails
    lateinit var context: Context;
    val GetContext: Activity = this;
    private var PAGE_START = 1
    internal var isLastPage: Boolean = false
    var singlepage_count: Int = 0;
    var isloding: Boolean = false;
    private var TOTAL_PAGES = 0
    private var currentPage = PAGE_START
    lateinit var adapter: UserListAdapter;
    var position_hit: Int = 0;
    var dummy_url : String="https://reqres.in/";
    var USER_FIRSTNAME = "first_name"
    var USER_LASTNAME = "last_name"
    var USER_IMAGEURL = "avatar"
    var USER_ID = "id"
    var TOTAL_PAGE_FIElD = "total_pages"
    var PER_PAGEITEMCOUNT = "per_page"
    var CURRENT_PAGE = "page"
    var getFullDisplay: DisplayMetrics = DisplayMetrics();
    val layoutManagaer: LinearLayoutManager = LinearLayoutManager(GetContext, LinearLayoutManager.VERTICAL, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        getWindowManager().getDefaultDisplay().getMetrics(getFullDisplay);
        var rUser: RecyclerView
        rUser = findViewById(R.id.rv_userList)
        rUser.layoutManager = layoutManagaer;
        rUser.setItemAnimator(DefaultItemAnimator())
        adapter = UserListAdapter(GetContext, getFullDisplay.heightPixels);
        rUser.adapter = adapter;



        rUser.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


                val visibleItemCount = layoutManagaer.childCount
                val totalItemCount = layoutManagaer.itemCount
                val firstVisibleItemPosition = layoutManagaer.findFirstVisibleItemPosition()
                position_hit = visibleItemCount + firstVisibleItemPosition





                if (!isloding && !isLastPage) {
                    if (position_hit >= totalItemCount && firstVisibleItemPosition >= 0) {
                        isloding = true


                        Handler().postDelayed({
                            currentPage += 1
                            GetUserNextData("" + currentPage);
                        }, 1000)
                    }
                }

            }
        })






        GetUserData("1");
    }

    fun GetUserData(pCount: String) {
        try {
            val retrofit = Retrofit.Builder()
                    .baseUrl(dummy_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(buildClient())
                    .build()


            val apiinterface = retrofit.create(APIInterface::class.java)
            val getrightswipecall = apiinterface.GetDetails(pCount)
            getrightswipecall.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(code: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                    try {
                        val result: String

                        if (response.isSuccessful) {

                            result = response.body().string().toString();
                            var response: JSONObject = JSONObject(result);
                            var userArray: JSONArray = response.getJSONArray("data")
                            currentPage = response.getInt("" + CURRENT_PAGE)
                            singlepage_count = response.getInt("" + PER_PAGEITEMCOUNT)
                            TOTAL_PAGES = response.getInt("" + TOTAL_PAGE_FIElD)
                            if (userArray != null && userArray.length() > 0) {
                                if (getUserDetailsCollections == null) {
                                    getUserDetailsCollections = ArrayList();
                                } else {
                                    getUserDetailsCollections.clear();
                                }

                                for (i in 0 until userArray.length()) {
                                    getusermodel = GetUserDetails();
                                    var userobj: JSONObject = JSONObject(userArray.getString(i));
                                    getusermodel.FName = userobj.getString("" + USER_FIRSTNAME);
                                    getusermodel.LName = userobj.getString("" + USER_LASTNAME);
                                    getusermodel.Avatarurl = userobj.getString("" + USER_IMAGEURL);
                                    getusermodel.Uid = userobj.getString("" + USER_ID);
                                    getUserDetailsCollections.add(getusermodel)
                                }

                                adapter.addAll(getUserDetailsCollections, getUserDetailsCollections.size)
                                if (currentPage <= TOTAL_PAGES) {
                                    adapter.addLoadingFooter()
                                } else {
                                    //   isLastPage = true
                                }


                            }
                        } else {

                            result = response.errorBody().string().toString().trim { it <= ' ' }

                        }


                    } catch (e: Exception) {
                        e.printStackTrace();

                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) { }
            })

        } catch (e: Exception) { }


    }


    fun GetUserNextData(pCount: String) {
        try {
            val retrofit = Retrofit.Builder()
                    .baseUrl(dummy_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(buildClient())
                    .build()


            val apiinterface = retrofit.create(APIInterface::class.java)
            val getrightswipecall = apiinterface.GetDetails(pCount)
            getrightswipecall.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(code: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                    try {
                        val result: String

                        if (response.isSuccessful) {
                            result = response.body().string().toString();
                            var response: JSONObject = JSONObject(result);
                            var userArray: JSONArray = response.getJSONArray("data")
                            currentPage = response.getInt("" +CURRENT_PAGE)
                            singlepage_count = response.getInt("" +PER_PAGEITEMCOUNT)
                            TOTAL_PAGES = response.getInt("" +TOTAL_PAGE_FIElD)
                            if (userArray != null && userArray.length() > 0) {
                                if (getUserDetailsCollections == null) {
                                    getUserDetailsCollections = ArrayList();
                                } else {
                                    getUserDetailsCollections.clear();
                                }

                                for (i in 0 until userArray.length()) {
                                    getusermodel = GetUserDetails();
                                    var userobj: JSONObject = JSONObject(userArray.getString(i));
                                    getusermodel.FName = userobj.getString("" +USER_FIRSTNAME);
                                    getusermodel.LName = userobj.getString("" +USER_LASTNAME);
                                    getusermodel.Avatarurl = userobj.getString("" +USER_IMAGEURL);
                                    getusermodel.Uid = userobj.getString("" +USER_ID);
                                    getUserDetailsCollections.add(getusermodel)
                                }

                            }

                            if (getUserDetailsCollections != null && getUserDetailsCollections.size > 0) {

                                adapter.removeLoadingFooter()
                                isloding = false

                                adapter.addAll(getUserDetailsCollections, getUserDetailsCollections.size)


                                if (currentPage != TOTAL_PAGES) {
                                    if (adapter.getItemCount() > 5)
                                        adapter.addLoadingFooter()
                                } else {
                                    isLastPage = true
                                }

                            }
                        } else {

                            result = response.errorBody().string().toString().trim { it <= ' ' }

                        }


                    } catch (e: Exception) {
                        e.printStackTrace();

                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {




                }
            })

        } catch (e: Exception) {}


    }

    private fun buildClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }



}
