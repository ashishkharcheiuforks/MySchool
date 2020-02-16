package com.nandra.myschool.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ale.infra.manager.channel.ChannelItem
import com.ale.infra.manager.fileserver.RainbowFileDescriptor
import com.ale.rainbowsdk.RainbowSdk
import com.bumptech.glide.Glide
import com.nandra.myschool.R
import com.nandra.myschool.utils.Utility
import com.nandra.myschool.utils.Utility.convertToString
import kotlinx.android.synthetic.main.channel_detail_item.view.*

class ChannelItemListAdapter : ListAdapter<ChannelItem, ChannelItemListAdapter.HomeViewHolder>(homeDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.channel_detail_item, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(channelItem: ChannelItem) {
            val contactName = Utility.nameBuilder(RainbowSdk.instance().contacts().getContactFromId(channelItem.contact.id))
            val channel = RainbowSdk.instance().channels().getChannel(channelItem.channelId)
            itemView.channel_detail_item_channel_name.text = channel.name
            itemView.channel_detail_item_publisher_name.text = contactName
            if (!channelItem.title.isNullOrEmpty()) {
                itemView.channel_detail_item_content_title.visibility = View.VISIBLE
                itemView.channel_detail_item_content_title.text = channelItem.title
            } else {
                itemView.channel_detail_item_content_title.visibility = View.GONE
            }
            if (!channelItem.message.isNullOrEmpty()) {
                itemView.channel_detail_item_content_body.visibility = View.VISIBLE
                itemView.channel_detail_item_content_body.text = Html.fromHtml(channelItem.message).toString()
            } else {
                itemView.channel_detail_item_content_body.visibility = View.GONE
            }
            itemView.channel_detail_item_publish_date.text = channelItem.creationDate.convertToString()

            if(channelItem.imageList.size > 0) {
                itemView.channel_detail_item_content_photo.visibility = View.VISIBLE
                //TODO: FIX THIS
                Glide.with(itemView.context)
                    .load(channelItem.imageList[0].image)
                    .into(itemView.channel_detail_item_content_photo)
            } else {
                itemView.channel_detail_item_content_photo.visibility = View.GONE
            }

            Glide.with(itemView.context)
                .load(channel.channelAvatar)
                .into(itemView.channel_detail_item_photo)
        }
    }


    companion object {
        private val homeDiffCallback = object : DiffUtil.ItemCallback<ChannelItem>() {
            override fun areItemsTheSame(oldItem: ChannelItem, newItem: ChannelItem): Boolean {
                return oldItem == newItem
            }

            //TODO: Optimize?
            override fun areContentsTheSame(oldItem: ChannelItem, newItem: ChannelItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}