package com.nandra.myschool.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ale.infra.contact.IRainbowContact
import com.ale.infra.contact.RainbowPresence
import com.ale.infra.list.IItemListChangeListener
import com.ale.infra.proxy.conversation.IRainbowConversation
import com.ale.rainbowsdk.RainbowSdk
import com.nandra.myschool.R
import com.nandra.myschool.adapter.ConversationListAdapter
import com.nandra.myschool.ui.MainActivityViewModel
import com.nandra.myschool.utils.Utility
import kotlinx.android.synthetic.main.chat_conversation_fragment.*

class ChatConversationFragment : Fragment(), IRainbowContact.IContactListener {

    private lateinit var conversationListAdapter: ConversationListAdapter
    private val mainViewModel: MainActivityViewModel by activityViewModels()
    private val chatViewModel: ChatViewModel by activityViewModels()
    private var conversationList = listOf<IRainbowConversation>()
    private var contactList = listOf<IRainbowContact>()
    private val changeListener = IItemListChangeListener(::getConversationList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        RainbowSdk.instance().conversations().allConversations.registerChangeListener(changeListener)
        return inflater.inflate(R.layout.chat_conversation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.loadingState.observe(viewLifecycleOwner, Observer {
            handleLoadingState(it)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        conversationListAdapter = ConversationListAdapter(::clickCallback)
        fragment_chat_conversation_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = conversationListAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterListeners()
        RainbowSdk.instance().contacts().rainbowContacts.unregisterChangeListener(changeListener)
    }

    private fun clickCallback(conversation: IRainbowConversation) {
        //TODO: Implement This
    }

    private fun handleLoadingState(state: Utility.LoadingState) {
        when(state) {
            Utility.LoadingState.LOADING -> { }
            Utility.LoadingState.SUCCESS -> { getConversationList() }
            else -> { }
        }
    }

    private fun getConversationList() {

        unregisterListeners()
        conversationList = chatViewModel.getConversationList()
        getContactFromConversation(conversationList)
        registerListeners()

        activity?.runOnUiThread {
            conversationListAdapter.submitList(conversationList)
            conversationListAdapter.notifyDataSetChanged()
        }
    }

    // Unregister IRainbowContact.IContactListener to all contacts
    private fun unregisterListeners() {
        for (contact in contactList) {
            contact.unregisterChangeListener(this)
        }
    }

    // Register IRainbowContact.IContactListener for all contacts
    private fun registerListeners() {
        for (contact in contactList) {
            contact.registerChangeListener(this)
        }
    }

    private fun getContactFromConversation(conversations: List<IRainbowConversation>) {

        val newContact = mutableListOf<IRainbowContact>()

        //TODO: ADD ROOM TOO
        for (conversation in conversations) {
            if (!conversation.isRoomType) {
                newContact.add(conversation.contact)
            }
        }

        contactList = newContact
    }

    override fun onCompanyChanged(p0: String?) {
        Log.d(Utility.LOG_DEBUG_TAG,"OnPresenceChanged")
    }

    override fun onPresenceChanged(p0: IRainbowContact?, p1: RainbowPresence?) {
        Log.d(Utility.LOG_DEBUG_TAG,"OnPresenceChanged")
    }

    override fun onActionInProgress(p0: Boolean) {
        Log.d(Utility.LOG_DEBUG_TAG,"OnActionInProgress")
    }

    override fun contactUpdated(p0: IRainbowContact?) {
        getConversationList()
    }
}