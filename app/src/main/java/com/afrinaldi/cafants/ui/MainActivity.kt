package com.afrinaldi.cafants.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.afrinaldi.cafants.R
import com.afrinaldi.cafants.adapter.SliderAdapter
import com.afrinaldi.cafants.databinding.ActivityMainBinding
import com.afrinaldi.cafants.helper.Constant
import com.afrinaldi.cafants.helper.LoadingDialog
import com.afrinaldi.cafants.helper.PrefHelper
import com.afrinaldi.cafants.model.PlantsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private val dataPlant = ArrayList<PlantsModel>()
    private var sliderAdapter : SliderAdapter? = null
    private lateinit var prefHelper: PrefHelper
    private val loading = LoadingDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = Firebase.database("https://cafants-default-rtdb.asia-southeast1.firebasedatabase.app")
        prefHelper = PrefHelper(this)

        dataPlant.addAll(dataPlants)
        sliderAdapter = SliderAdapter()

        readData()
        addSlider()

        binding.cvLogout.setOnClickListener(this)
        binding.tvVerified.setOnClickListener(this)
    }

    private fun addSlider() {
        sliderAdapter?.addData(dataPlant)
        binding.vgSlider.adapter = sliderAdapter

        binding.tvWater.text = dataPlant[0].water
        binding.tvHumedity.text = dataPlant[0].humedity

        binding.vgSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tvWater.text = dataPlant[position].water
                binding.tvHumedity.text = dataPlant[position].humedity
            }
        })
    }

    private fun readData() {
        val profRef = db.reference.child("Profile")
        val token = prefHelper.getString(Constant.PREF_TOKEN)
        val nameRef = profRef.child(token.toString())

        nameRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.tvName.text = snapshot.child("name").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private val dataPlants: ArrayList<PlantsModel>
        get() {
            val image = resources.obtainTypedArray(R.array.plant_image)
            val water = resources.getStringArray(R.array.plant_water)
            val humedity = resources.getStringArray(R.array.plant_humedity)
            val dataPlant = ArrayList<PlantsModel>()
            for (i in water.indices) {
                val tourism = PlantsModel(image.getResourceId(i, -1), water[i], humedity[i])
                dataPlant.add(tourism)
            }
            return dataPlant
        }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.cv_logout -> {
                auth.signOut()
                prefHelper.clear()
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }

            R.id.tv_verified -> {
                verifiedEmail()
            }
        }
    }

    private fun verifiedEmail() {
        loading.startLoading()
        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener { verified ->
            if (verified.isSuccessful){
                loading.isDismiss()
                Toast.makeText(this, "Silahkan cek email dan login kembali", Toast.LENGTH_SHORT).show()
                auth.signOut()
                prefHelper.clear()
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            } else {
                loading.isDismiss()
                Toast.makeText(this, "Gagal, silahkan coba lagi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser?.isEmailVerified!!){
            binding.clHidden.visibility = View.GONE
        }
    }
}