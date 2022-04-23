package com.example.budgetapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class Avatar: AppCompatActivity(){
    private lateinit var avatarList: GridView
    private lateinit var homepageButton: ImageButton
    private lateinit var adjustExpenseButton: ImageButton
    private lateinit var viewHistoryButton: ImageButton
    private lateinit var upcomingBillButton: ImageButton

    private lateinit var avatarAdapter: AvatarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar)
        adjustExpenseButton = findViewById(R.id.adjust_expense_button)
        viewHistoryButton = findViewById(R.id.view_history_button)
        upcomingBillButton = findViewById(R.id.upcoming_bill_button)
        homepageButton = findViewById(R.id.add_entry_button)

        avatarList = findViewById(R.id.avatar_list)

        homepageButton.setOnClickListener {
            val intent = Intent(this@Avatar, MainActivity::class.java) //
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
        }

        adjustExpenseButton.setOnClickListener {
            // start new activity
            val intent = Intent(this@Avatar, AdjustExpense::class.java) //
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
        }

        viewHistoryButton.setOnClickListener {
            // start new activity
            val intent = Intent(this@Avatar, ExpenseViewer::class.java) //
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
        }

        upcomingBillButton.setOnClickListener {
            val intent = Intent(this@Avatar, RecurringViewer::class.java) //
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
        }
        avatarAdapter = AvatarAdapter(this)
        avatarList.adapter = avatarAdapter
    }
}

data class AvatarItem(val id: Int?, val src: String, val level: Int, var isChoosen: Boolean, var isActivated: Boolean)

class AvatarAdapter(var context: Context): BaseAdapter() {
    private var db: EntriesDB = EntriesDB(context)
    private var arraylist: ArrayList<AvatarItem> = this.updateAvatarList()

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

        levelText.text = "Unlock at Level ${avatarItem.level}"

        if(!avatarItem.isActivated){
            chooseButton.isEnabled = false
            chooseButton.text = "Locked"
            chooseButton.setTextColor(Color.DKGRAY)
        }

        else if(avatarItem.isChoosen) {
            chooseButton.isEnabled = false
            chooseButton.text = "Chosen"
        }
        else{
            chooseButton.setOnClickListener{
                //choose the avatar
                db.setDefault_isChosen()
                db.update_isChosen(p0, true)
                arraylist = this.updateAvatarList()
                this.notifyDataSetChanged()
                db.updateAvatar(avatarItem.src)
            }
        }

        return view
    }

    private fun updateAvatarList(): ArrayList<AvatarItem>{
        val ids = listOf(0,1,2,3)
        db.update_isActivated()
        val avatarItemList = ids.map{AvatarItem(it, db.get_AvatarName(it), db.get_AvatarLevel(it), db.get_isChosen(it), db.get_isActivated(it))}
        return ArrayList(avatarItemList)
    }
}
