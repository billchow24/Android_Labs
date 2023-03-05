package algonquin.cst2335.chow0144;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * ChatMessage is an entity class that represents a chat message.
 */
@Entity
public class ChatMessage {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;

    @ColumnInfo(name="Message")
    protected String message;

    @ColumnInfo(name="TimeSent")
    protected String timeSent;

    @ColumnInfo(name="SendOrReceive")
    protected boolean isSentButton;

    /**
     * ChatMessage(): default constructor
     */
    public ChatMessage() {
    }

    /**
     * Constructor that initializes the message, timestamp and isSentButton properties
     * @param m, the message text
     * @param t, the message timestamp
     * @param sent, whether the message was sent or received
     */
    ChatMessage(String m, String t, boolean sent)
    {
        message = m;
        timeSent = t;
        isSentButton = sent;
    }

    /**
     * Returns the message text
     * @return message, the message text
     */
    public String getMessage(){
        return message;
    }

    /**
     * Returns the message timestamp
     * @return timeSent, the message timestamp
     */
    public String getTimeSent(){
        return timeSent;
    }

    /**
     * Returns whether the message was sent or received
     * @return isSentButton, whether the message was sent or received
     */
    public boolean getIsSentButton(){
        return isSentButton;
    }
}
