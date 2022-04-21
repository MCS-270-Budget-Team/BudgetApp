package com.example.budgetapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class Avatar: AppCompatActivity(){
    private lateinit var homeButton: ImageButton
    private lateinit var avatarList: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar)
        homeButton = findViewById(R.id.home_button)
        avatarList = findViewById(R.id.avatar_list)

        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}

data class AvatarItem(val id: Int?, val src: String, val level: Int, var isChoosen: Boolean, var isActivated: Boolean)

class AvatarAdapter(var context: Context, val arraylist: ArrayList<AvatarItem>): BaseAdapter() {
    override fun getCount(): Int {
        return arraylist.size
    }

    override fun getItem(p0: Int): Any {
        return arraylist[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("SetTextI18n", "ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.activity_avatar_bubble, null)
        val avatarImage: ImageView = view.findViewById(R.id.avatar_image)
        val levelText: TextView = view.findViewById(R.id.level_text)
        val chooseButton: Button = view.findViewById(R.id.choose_button)

        val avatarItem = arraylist[p0]
        //set up the avatar image
        val drawableId = context.resources.getIdentifier(avatarItem.src, "drawable", context.packageName)
        avatarImage.setImageResource(drawableId)

        levelText.setText("Unlock at Level ${avatarItem.level}")

        if(!avatarItem.isActivated){
            chooseButton.isEnabled = false
            chooseButton.setText("Locked")
        }

        else if(avatarItem.isChoosen) {
            chooseButton.isEnabled = false
        }
        else{
            chooseButton.setOnClickListener{
                //choose the avatar
                arraylist[p0].isChoosen = true
                this.notifyDataSetChanged()
            }
        }

        return view
    }
}
