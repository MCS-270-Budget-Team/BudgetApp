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
    private lateinit var avatarList: ExpandableHeightGridView
    private lateinit var themeList: ExpandableHeightGridView

    private lateinit var homepageButton: ImageButton
    private lateinit var adjustExpenseButton: ImageButton
    private lateinit var viewHistoryButton: ImageButton
    private lateinit var upcomingBillButton: ImageButton

    private lateinit var avatarAdapter: AvatarAdapter
    private lateinit var themeAdapter: ThemeAdapter
    private var db: EntriesDB = EntriesDB(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setTheme()
        setContentView(R.layout.activity_avatar)
        adjustExpenseButton = findViewById(R.id.adjust_expense_button)
        viewHistoryButton = findViewById(R.id.view_history_button)
        upcomingBillButton = findViewById(R.id.upcoming_bill_button)
        homepageButton = findViewById(R.id.add_entry_button)

        avatarList = findViewById(R.id.avatar_list)
        themeList = findViewById(R.id.theme_list)
        avatarList.isExpanded = true
        themeList.isExpanded = true

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

        themeAdapter = ThemeAdapter(this)
        themeList.adapter = themeAdapter
    }
    private fun setTheme(){
        when (db.getThemeID()) {
            0 -> {
                setTheme(R.style.Theme_BudgetApp)
            }
            1 -> {
                setTheme(R.style.Forest)
            }
            2 -> {
                setTheme(R.style.Eggplant)
            }
            3 -> {
                setTheme(R.style.Pumpkin)
            }
            else -> {
                setTheme(R.style.Vintage)
            }
        }
    }
}

data class AvatarItem(val id: Int?, val src: String, val level: Int, var isChosen: Boolean, var isActivated: Boolean)

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

        else if(avatarItem.isChosen) {
            chooseButton.isEnabled = false
            chooseButton.text = "Chosen"
            chooseButton.setTextColor(Color.WHITE)
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
        val ids = listOf(0,1,2,3, 4)
        db.update_isActivated()
        val avatarItemList = ids.map{AvatarItem(it, db.get_AvatarName(it), db.get_AvatarLevel(it), db.get_isChosen(it), db.get_isActivated(it))}
        return ArrayList(avatarItemList)
    }
}

data class ThemeItem(val id: Int,
                     val src: String,
                     val level: Int,
                     val background: String,
                     val button: String,
                     val text: String,
                     val progress: String,
                     val title: String)

class ThemeAdapter(var context: Context): BaseAdapter() {
    private var db: EntriesDB = EntriesDB(context)
    private var arraylist: ArrayList<ThemeItem> = arrayListOf(
        ThemeItem(0, "dark_blue", 2, "#0D1013", "#212930", "#2E3943", "#3B4956", "#256D66"),
        ThemeItem(1, "forest", 4, "#080809", "#292825", "#1B2413", "#433F3E", "#5F6556"),
        ThemeItem(2, "eggplant", 8, "#694F5D","#68A691", "#BFD3C1", "#FFE5D4", "#EFC7C2"),
        ThemeItem(3, "pumpkin", 10, "#233D4D", "#619B8A", "#A1C181", "#FCCA46", "#FE7F2D"),
        ThemeItem(4, "vintage", 14, "#313E50", "#53131E", "#5A464C", "#3A435E", "#455561")
    )

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
        val themeImage: ImageView = view.findViewById(R.id.avatar_image)
        val levelText: TextView = view.findViewById(R.id.level_text)
        val chooseButton: Button = view.findViewById(R.id.choose_button)

        val theme = arraylist[p0]
        //set up the avatar image
        val drawableId = context.resources.getIdentifier(theme.src, "drawable", context.packageName)
        themeImage.setImageResource(drawableId)

        levelText.text = "Unlock at Level ${theme.level}"

        when {
            db.getLevel() < theme.level -> {
                chooseButton.isEnabled = false
                chooseButton.text = "Locked"
                chooseButton.setTextColor(Color.DKGRAY)
            }
            db.getThemeID() == theme.id -> {
                chooseButton.isEnabled = false
                chooseButton.text = "Chosen"
                chooseButton.setTextColor(Color.WHITE)
            }
            else -> {
                chooseButton.setOnClickListener{
                    //change the entry in PersonalInfo table in the database
                    db.updateThemeID(theme.id)
                    val intent = Intent(context, Avatar::class.java) //
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    context.startActivity(intent)
                }
                this.notifyDataSetChanged()
            }
        }

        return view
    }
}

