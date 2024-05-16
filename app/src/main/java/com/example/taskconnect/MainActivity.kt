package com.example.taskconnect

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskconnect.authentication.LoginActivity
import com.example.taskconnect.databinding.ActivityMainBinding
import com.example.taskconnect.model.User
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    private lateinit var userAdapter: UserAdapter

    private var name = ""
    private var currentStatus:String? = null

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        binding.navView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this,binding.drawerLayout,toolbar,R.string.open_nav,R.string.close_nav)

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // setting for the header data
        val inflater = LayoutInflater.from(applicationContext)
        val header = inflater.inflate(R.layout.nav_header,binding.navView,false)

        binding.navView.addHeaderView(header)

        val statusHeader = header.findViewById<TextView>(R.id.nav_status)
        val nameHeader = header.findViewById<TextView>(R.id.nav_user_name)
//        val imgHeader = header.findViewById<ShapeableImageView>(R.id.nav_img_header)

        nameHeader.text = FirebaseAuth.getInstance().currentUser?.displayName.toString()

        val userList = ArrayList<User>()

        val userCollection = FirebaseFirestore.getInstance().collection("users")
        userCollection.get()
            .addOnSuccessListener { documents->
                    Toast.makeText(applicationContext, "name getting", Toast.LENGTH_SHORT).show()
                    userList.clear()
                    for (document in documents){

                        name = document.getString("name").toString()
                        var status = document.getString("status")
                        Toast.makeText(applicationContext, name, Toast.LENGTH_SHORT).show()
                        Log.e("names",name)
                        if (status.isNullOrEmpty()){
                            status = "Student of Computer Science"
                        }
                        if (name == FirebaseAuth.getInstance().currentUser?.displayName.toString()){
                            currentStatus = status
                            statusHeader.text = currentStatus.toString()
                        }
                        userList.add(User(name,status,0))
                    }

                    binding.recyclerUsers.layoutManager = LinearLayoutManager(this)

                    userAdapter = UserAdapter(this,userList)

                    binding.recyclerUsers.adapter = userAdapter
                }

        binding.etSearchMentor.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val filteredData = userList.filter {
                    it.userName.contains(p0.toString(),true) || it.userDesc.contains(p0.toString(),true)
                }
                userAdapter.onSearch(filteredData)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


        Log.e("name",FirebaseAuth.getInstance().currentUser?.displayName.toString())
        Log.e("status",currentStatus.toString())


    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        else{
            onBackPressedDispatcher.onBackPressed()
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home-> Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
            R.id.nav_sign_out -> {
                openDialog()
            }
            R.id.nav_profile -> Toast.makeText(this, "Profile item", Toast.LENGTH_SHORT).show()
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    private fun openDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_sign_out)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        val yes = dialog.findViewById<CardView>(R.id.sign_out_yes)
        val no = dialog.findViewById<CardView>(R.id.sign_out_no)

        yes.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "signed out successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@MainActivity,LoginActivity::class.java))
            finish()
        }
        no.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }
}