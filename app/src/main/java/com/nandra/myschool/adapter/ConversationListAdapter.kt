package com.nandra.myschool.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ale.infra.proxy.conversation.IRainbowConversation
import com.bumptech.glide.Glide
import com.nandra.myschool.R
import com.nandra.myschool.ui.ChatDetailActivity
import com.nandra.myschool.utils.Utility.EXTRA_JID
import com.nandra.myschool.utils.Utility.nameBuilder
import kotlinx.android.synthetic.main.chat_conversation_fragment_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class ConversationListAdapter(
    val clickCallback: (IRainbowConversation) -> Unit
) : ListAdapter<IRainbowConversation, ConversationListAdapter.ConversationViewHolder>(chatConversationDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_conversation_fragment_item, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ConversationViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(conversation: IRainbowConversation) {
            if(!conversation.isRoomType) {
                val contact = conversation.contact
                itemView.fragment_home_item_channel_name.text = nameBuilder(contact)

                Glide.with(itemView.context)
                    .load(conversation.contact.photo)
                    .into(itemView.fragment_home_item_photo)
            } else {
                val roomName = conversation.room.name
                itemView.fragment_home_item_channel_name.text = roomName

                Glide.with(itemView.context)
                    .load(conversation.room.photo)
                    .into(itemView.fragment_home_item_photo)
            }
            itemView.fragment_home_item_publisher_name.text = conversation.lastMessage.messageContent

            view.setOnClickListener {
                val jid = conversation.jid
                val intent = Intent(view.context, ChatDetailActivity::class.java).apply {
                    putExtra(EXTRA_JID, jid)
                }
                view.context.startActivity(intent)
            }

            //TODO: Optimize These Two
            val unreadMessageCount = conversation.unreadMsgNb
            if (unreadMessageCount != 0) {
                itemView.fragment_chat_conversation_unread_count_tv.visibility = View.VISIBLE
                itemView.fragment_chat_conversation_unread_count_tv.text = conversation.unreadMsgNb.toString()
            } else {
                itemView.fragment_chat_conversation_unread_count_tv.visibility = View.GONE
            }

            if (conversation.lastMessage.messageDate != null) {
                itemView.fragment_home_item_publish_date.text = conversation.lastMessage.messageDate.convertToString()
            } else {
                itemView.fragment_home_item_publish_date.visibility = View.GONE
            }
        }
    }

    private fun Date.convertToString() : String {
        val simpleDateFormat = SimpleDateFormat("hh:mm a")
        return simpleDateFormat.format(this)
    }

    companion object {
        val chatConversationDiffCallback = object : DiffUtil.ItemCallback<IRainbowConversation>() {
            override fun areItemsTheSame(oldItem: IRainbowConversation, newItem: IRainbowConversation): Boolean {
                return oldItem == newItem
            }

            //TODO: Optimize?
            override fun areContentsTheSame(oldItem: IRainbowConversation, newItem: IRainbowConversation): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}