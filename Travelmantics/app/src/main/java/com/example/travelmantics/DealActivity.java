package com.example.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DealActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    EditText txtTitle;
    EditText txtDescription;
    EditText txtPrice;
    private TravelDeal deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        FirebaseUtil.openFbReference("traveldeals");
        mFirebaseDatabase=FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference=FirebaseUtil.mDatabaseReference;
        txtTitle=findViewById(R.id.txtTitle);
        txtPrice=findViewById(R.id.txtPrice);
        txtDescription=findViewById(R.id.txtDescription);
        Intent intent=getIntent();
        TravelDeal deal= (TravelDeal) intent.getSerializableExtra("Deal");
        if(deal==null){
            deal=new TravelDeal();
        }
        this.deal=deal;
        txtTitle.setText(deal.getTitle());
        txtPrice.setText(deal.getPrice());
        txtDescription.setText(deal.getDescription());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu:
                saveDeal();
                Toast.makeText(this,"Deal saved", Toast.LENGTH_LONG).show();
                clean();
                backToList();
                return true;
            case R.id.delete_menu:
                deleteDeal();
                Toast.makeText(this,"Deal deleted", Toast.LENGTH_LONG).show();
                backToList();
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void clean() {
        txtTitle.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtTitle.requestFocus();
    }

    private void saveDeal() {
        deal.setTitle(txtTitle.getText().toString());
        deal.setDescription(txtDescription.getText().toString());
        deal.setPrice(txtPrice.getText().toString());
        if(deal.getId()==null) {
            mDatabaseReference.push().setValue(deal);
        } else{
            mDatabaseReference.child(deal.getId()).setValue(deal);
        }
    }
    private void deleteDeal(){
        if(deal==null){
            Toast.makeText(this,"Please save deal before deleting",Toast.LENGTH_LONG).show();
            return;
        }
        mDatabaseReference.child(deal.getId()).removeValue();
    }
    private  void backToList(){
        Intent intent=new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true;
    }
}
