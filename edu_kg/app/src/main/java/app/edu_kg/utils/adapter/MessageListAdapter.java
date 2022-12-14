package app.edu_kg.utils.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.*;

import java.util.ArrayList;
import java.util.List;

import app.edu_kg.R;

public class MessageListAdapter extends Adapter<ViewHolder> {

    private static class ReceivedMessageHolder extends ViewHolder {
        TextView messageText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.message_card_left_text);
        }
        void bind(UserMessage message) {
            messageText.setText(message.message);
        }
    }

    private static class SentMessageHolder extends ViewHolder {
        TextView messageText;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.message_card_right_text);
        }
        void bind(UserMessage message) {
            messageText.setText(message.message);
        }
    }

    public static class UserMessage {
        String message;
        int type;
        static final int USER_MESSAGE = 0;
        static final int ROBOT_MESSAGE = 1;
        UserMessage (String message, int type){
            this.message = message;
            this.type = type;
        }
    }

    private List<UserMessage> messageList;

    public MessageListAdapter() {
        messageList = new ArrayList<UserMessage>();
    }

    public void addUserMessage(String message) {
        messageList.add(new UserMessage(message, UserMessage.USER_MESSAGE));
    }

    public void addRobotMessage(String message) {
        messageList.add(new UserMessage(message, UserMessage.ROBOT_MESSAGE));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        UserMessage message = messageList.get(position);
        if (message.type == UserMessage.USER_MESSAGE)
            return UserMessage.USER_MESSAGE;
        else
            return UserMessage.ROBOT_MESSAGE;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == UserMessage.USER_MESSAGE) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_card_right, parent, false);
            return new SentMessageHolder(view);
        }
        else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_card_left, parent, false);
            return new ReceivedMessageHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserMessage message = messageList.get(position);
        if (holder.getItemViewType() == UserMessage.USER_MESSAGE)
            ((SentMessageHolder) holder).bind(message);
        else
            ((ReceivedMessageHolder) holder).bind(message);
    }
}