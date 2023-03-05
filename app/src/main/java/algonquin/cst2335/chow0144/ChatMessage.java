package algonquin.cst2335.chow0144;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

    public ChatMessage() {
    }

    ChatMessage(String m, String t, boolean sent)
    {
        message = m;
        timeSent = t;
        isSentButton = sent;
    }

    public String getMessage(){
        return message;
    }

    public String getTimeSent(){
        return timeSent;
    }

    public boolean getIsSentButton(){
        return isSentButton;
    }
}
