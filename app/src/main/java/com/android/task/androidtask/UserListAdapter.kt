package com.android.task.androidtask

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


/**
 * Created by hai on 6/26/2018.
 */
class UserListAdapter(val context: Context, val cardviewht: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var TAG: String = "UserListAdapter";
    var userCollections: ArrayList<GetUserDetails> = ArrayList()
    internal var retryPageLoad: Boolean = false;
    internal var isLoadingAdded: Boolean = false;
    private val ITEM = 0
    private val LOADING = 1


    override fun getItemCount(): Int {
        return userCollections.size;

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

        val getitem = getItemViewType(position)



        when (getitem) {


            ITEM -> {

                var currentscreen = holder as UserView


                currentscreen.tName.text = "Hi, "+userCollections.get(position).FName + " " + userCollections.get(position).LName;

                Glide.with(context)
                        .load( userCollections.get(position).Avatarurl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(currentscreen.iUserImage)


                var continerheight: Int = cardviewht / 3;
                val getHt: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, continerheight)
                        currentscreen.lContainer.setLayoutParams(getHt);

            }

            LOADING -> {
                var Loadinscreen = holder as LoadingVH

            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

        var ViewUser: RecyclerView.ViewHolder? = null

        when (viewType) {
            ITEM -> {
                val viewItem = LayoutInflater.from(context).inflate(R.layout.showuser, parent, false)
                ViewUser = UserView(viewItem)
            }
            LOADING -> {
                val viewLoading = LayoutInflater.from(context).inflate(R.layout.progress, parent, false)
                ViewUser = LoadingVH(viewLoading)
            }
        }
        return ViewUser!!;



    }

    class UserView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var TAG: String = "UserListAdapter";
        var lContainer: LinearLayout = itemView.findViewById(R.id.container_ll);
        var tName: TextView = itemView.findViewById(R.id.name_tv);
        var iUserImage: ImageView = itemView.findViewById(R.id.image_iv);

    }

    protected inner class LoadingVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun LoadProgress(user: GetUserDetails, context: Context) {
        }

    }


    fun addLoadingFooter() {
        isLoadingAdded = true
        add(GetUserDetails())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = userCollections.size - 1
        val result = getItem(position)

        if (result != null) {
            userCollections.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun add(r: GetUserDetails) {

        userCollections.add(r)

        notifyItemInserted(userCollections.size - 1)

    }

    fun addAll(moveResults: ArrayList<GetUserDetails>, size: Int) { for (i in 0..moveResults.size - 1) {
            userCollections.add(moveResults.get(i))
            notifyItemInserted(userCollections.size - 1)
        }


    }


    fun getItem(position: Int): GetUserDetails {
        return userCollections.get(position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == userCollections.size - 1 && isLoadingAdded) LOADING else ITEM

    }
}