package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;

public class MediaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        // Gmail
        LinearLayout emailLayout = findViewById(R.id.email);
        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGmail();
            }
        });

        // WhatsApp
        LinearLayout waLayout = findViewById(R.id.wa);
        waLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });

        // Facebook
        LinearLayout fbLayout = findViewById(R.id.fb);
        fbLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacebook();
            }
        });

        // Instagram
        LinearLayout igLayout = findViewById(R.id.ig);
        igLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstagram();
            }
        });

        // Phone
        LinearLayout tlpLayout = findViewById(R.id.tlp);
        tlpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhone();
            }
        });

        // Location
        LinearLayout locationLayout = findViewById(R.id.location);
        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLocation();
            }
        });
    }

    public void openGmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","sn301620@gmail.com", null));
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    public void openWhatsApp() {
        String phoneNumber = "081276725792";
        Intent waIntent = new Intent(Intent.ACTION_VIEW);
        waIntent.setData(Uri.parse("https://wa.me/" + phoneNumber));
        startActivity(waIntent);
    }

    public void openFacebook() {
        Intent fbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/pnp.pelalawan?mibextid=AwY8MHCEufnrIXm5"));
        startActivity(fbIntent);
    }

    public void openInstagram() {
        Intent igIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/pnp.pelalawan?igsh=dHh4MTFqaXQ5d3dj"));
        startActivity(igIntent);
    }

    public void openPhone() {
        String phoneNumber = "081276725792";
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        startActivity(phoneIntent);
    }

    public void openLocation() {
        String geoUri = "https://www.google.com/maps/place/Politeknik+Negeri+Padang+Kampus+Pelalawan/@0.4003044,101.8592242,17z/data=!3m1!4b1!4m6!3m5!1s0x31d5c4e3b407ca9f:0xef44d9959e8f060d!8m2!3d0.4003044!4d101.8592242!16s%2Fg%2F11cr_g1f56?entry=ttu";
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        startActivity(mapIntent);
    }
}