package com.example.shakyabroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.shakyabroadcast.databinding.ActivityMainBinding;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private boolean flag = false;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String time = String.format(
                    Locale.getDefault(),
                    "%02d:%02d:%02d",
                    bundle.getInt("hour"),
                    bundle.getInt("minute"),
                    bundle.getInt("second")
            );
            binding.tvClock.setText(time);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        registerReceiver(receiver, new IntentFilter("MyMessage"), Context.RECEIVER_EXPORTED);

        flag = MyService.flag;

        if (flag) {
            binding.btnStart.setText("暫停");
        } else {
            binding.btnStart.setText("開始");
        }

        binding.btnStart.setOnClickListener(view -> {
            flag = !flag;

            if (flag) {
                binding.btnStart.setText("暫停");
                Toast.makeText(this, "計時開始", Toast.LENGTH_SHORT).show();
            } else {
                binding.btnStart.setText("開始");
                Toast.makeText(this, "計時暫停", Toast.LENGTH_SHORT).show();
            }

            startService(new Intent(this, MyService.class).putExtra("flag", flag));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
