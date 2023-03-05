package algonquin.cst2335.chow0144;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import algonquin.cst2335.chow0144.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.chow0144.databinding.ReceiveMessageBinding;
import algonquin.cst2335.chow0144.databinding.SentMessageBinding;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    ChatMessageDAO mDAO;
    private RecyclerView.Adapter myAdapter;

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
                    int position = getAdapterPosition();
                    AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                    builder.setTitle("Question:")
                    .setMessage("Do you want to delete the message: "+messageText.getText() )
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
                        Snackbar.make(messageText,"You deleted message #"+ position, Snackbar.LENGTH_LONG)
                                .setAction("Undo",click ->{
                                    messages.add(position, m);
                                    myAdapter.notifyItemInserted(position);
                                })
                                .show();
                    })
                    .create().show();
                });

                messageText = itemView.findViewById(R.id.message);
                timeText = itemView.findViewById(R.id.time);
            }
        }

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