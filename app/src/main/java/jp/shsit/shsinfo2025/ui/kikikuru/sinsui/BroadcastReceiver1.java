package jp.shsit.shsinfo2025.ui.kikikuru.sinsui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class BroadcastReceiver1 extends BroadcastReceiver {

   Handler receiverHandler = new Handler();

   @Override
   public void onReceive(Context context, Intent intent) {

      Bundle bundle = intent.getExtras();
      String depth = bundle.getString("depth");
      String eleva = bundle.getString("eleva");
      String address = bundle.getString("address");
      System.out.println("koko2");



   //   Toast.makeText(context,"onReceive! " + eleva,Toast.LENGTH_LONG).show();

      try {
         SinsuiFragment.getInstace().updateTheTextView(depth,eleva,address);


      } catch (Exception e) {

      }


   }


}