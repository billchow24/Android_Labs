package algonquin.cst2335.chow0144;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import algonquin.cst2335.chow0144.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.chow0144.databinding.ReceiveMessageBinding;
import algonquin.cst2335.chow0144.databinding.SentMessageBinding;

import android.app.Instrumentation;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * ChatRoom class is an Activity class that represents a chatroom view that has a RecyclerView to display
 * chat messages, and two buttons (send and receive) to allow users to add messages to the chat. This class
 * also has a ChatRoomViewModel to store chat messages and a ChatMessageDAO to handle database operations.
 */
public class ChatRoom extends AppCompatActivity {
    Instrumentation instrumentation = new Instrumentation();
    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    ChatMessageDAO mDAO;
    private RecyclerView.Adapter myAdapter;

    int position;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch( item.getItemId() )
        {
            case R.id.item_1:

                //put your ChatMessage deletion code here. If you select this item, you should show the alert dialog
                //asking if the user wants to delete this message.

                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setTitle("Question:")
                        .setMessage("Do you want to delete the message?")
                        .setNegativeButton("No",(dialog, cl) ->{})
                        .setPositiveButton("Yes",(dialog, cl) -> {
                            Executor thread = Executors.newSingleThreadExecutor();
                            ChatMessage m = messages.get(position);
                            thread.execute(() ->
                            {
                                mDAO.deleteMessage(m);
                            });
                            messages.remove(position);
                            myAdapter.notifyItemRemoved(position);
                            Snackbar.make(findViewById(R.id.message),"You deleted message #"+ position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo",click ->{
                                        messages.add(position, m);
                                        myAdapter.notifyItemInserted(position);
                                    })
                                    .show();
                        })
                        .create().show();
                break;

            case R.id.about:
                Toast.makeText(getApplicationContext(), "Version 1.0, created by Shing Kwan Chow", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    /** Called when the activity is starting. This method initializes view objects, reads chat messages from
     * the database, sets up click listeners for buttons, and configures the RecyclerView.
     * @param savedInstanceState Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "ChatMessage").build();
        mDAO = db.cmDAO();
        setContentView(binding.getRoot());

        setSupportActionBar(binding.myToolbar);


        if(messages == null)
        {
            chatModel.messages.setValue(messages = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database

                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }

        binding.sendButton.setOnClickListener(click->{
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage message = new ChatMessage(binding.textInput.getText().toString(), currentDateandTime, true);
            messages.add(message);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                mDAO.insertMessage(message);
            });
        });

        binding.receiveButton.setOnClickListener(click->{
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage message = new ChatMessage(binding.textInput.getText().toString(), currentDateandTime, false);
            messages.add(message);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                mDAO.insertMessage(message);
            });
        });

        /**
         * MyRowHolder is an inner class that extends RecyclerView.ViewHolder and provides a reference to the UI
         * elements that represent a chat message in the RecyclerView.
         * Properties:
         * messageText: TextView that displays the message text
         * timeText: TextView that displays the message timestamp
         * Methods:
         * MyRowHolder(View itemView): constructor that initializes the messageText and timeText properties
         * and sets up a click listener to handle the deletion of the message
         */
        class MyRowHolder extends RecyclerView.ViewHolder {
            TextView messageText;
            TextView timeText;

            public MyRowHolder(@NonNull View itemView) {

                super(itemView);


                itemView.setOnClickListener(clk->{
                    position = getAdapterPosition();
                    clk.setTag(position);
                    ChatMessage selected = messages.get(position);

                    chatModel.selectedMessage.postValue(selected);

                });

                messageText = itemView.findViewById(R.id.message);
                timeText = itemView.findViewById(R.id.time);

            }

        }

        chatModel.selectedMessage.observe(this, (newMessageValue) -> {
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            MessageDetailsFragment chatFragment = new MessageDetailsFragment( newMessageValue );
            tx.replace(R.id.fragmentLocation, chatFragment);
            tx.addToBackStack("");
            tx.commit();

        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if(viewType == 0){
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }else{
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder  holder, int position) {
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            public int getItemViewType(int position){
                ChatMessage obj = messages.get(position);
                if(obj.getIsSentButton()){
                    return 0;
                } else {
                    return 1;
                }
            }
        });


    }
}