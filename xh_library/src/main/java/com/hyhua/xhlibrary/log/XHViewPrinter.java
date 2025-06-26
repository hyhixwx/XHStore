package com.hyhua.xhlibrary.log;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyhua.xhlibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 将log打印到界面上
 */
public class XHViewPrinter implements IXHLogPrinter {
    private final RecyclerView recyclerView;
    private final LogAdapter adapter;
    private final XHViewPrinterProvider viewProvider;

    public XHViewPrinter(Activity activity) {
        FrameLayout rootView = activity.findViewById(android.R.id.content);
        recyclerView = new RecyclerView(activity);
        adapter = new LogAdapter(LayoutInflater.from(recyclerView.getContext()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        viewProvider = new XHViewPrinterProvider(rootView, recyclerView, adapter);
    }

    /**
     * 获取ViewProvider，通过其控制log视图展示和隐藏
     */
    public XHViewPrinterProvider getViewProvider() {
        return viewProvider;
    }

    @Override
    public void print(@NonNull XHLogConfig config,@XHLogType.TYPE int level, String tag, @NonNull String printString) {
        adapter.addItem(new XHLogModel(System.currentTimeMillis(), level, tag, printString));
        // 滚动到对应的位置
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    public static class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {
        private final LayoutInflater inflater;
        private final List<XHLogModel> logs = new ArrayList<>();

        public LogAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        void addItem(XHLogModel logModel) {
            logs.add(logModel);
            notifyItemInserted(logs.size() - 1);
        }

        @SuppressLint("NotifyDataSetChanged")
        void clear() {
            logs.clear();
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.xhlog_item, parent, false);
            return new LogViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
            XHLogModel logItem = logs.get(position);
            int color = getHighLightColor(logItem.level);
            holder.tagView.setTextColor(color);
            holder.messageView.setTextColor(color);

            holder.tagView.setText(logItem.getFlattenedPrefix());
            holder.messageView.setText(logItem.log);
        }

        /**
         * 根据log级别获取不同的高亮颜色
         */
        private int getHighLightColor(int logLevel) {
            int highLight;
            switch (logLevel) {
                case XHLogType.V:
                    highLight = 0xffbbbbbb;
                    break;
                case XHLogType.D:
                    highLight = 0xffffffff;
                    break;
                case XHLogType.I:
                    highLight = 0xff6a8759;
                    break;
                case XHLogType.W:
                    highLight = 0xffbbb529;
                    break;
                case XHLogType.E:
                    highLight = 0xffff6b68;
                    break;
                default:
                    highLight = 0xffffff00;
                    break;
            }
            return highLight;
        }

        @Override
        public int getItemCount() {
            return logs.size();
        }
    }

    private static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView tagView;
        TextView messageView;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            tagView = itemView.findViewById(R.id.tag);
            messageView = itemView.findViewById(R.id.message);
        }
    }
}
