package com.example.firebasestorage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.firebasestorage.databinding.ActivityMainBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var fireBaseStorage: FirebaseStorage
    lateinit var reference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fireBaseStorage = FirebaseStorage.getInstance()
        reference = fireBaseStorage.getReference("my_photos")
        binding.image.setOnClickListener {
            getImageContent.launch("image/*")

            //agar video bo'sa
            //getImageContent.launch("video/*")

        }
    }

    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

            if (uri != null) {
                val task = reference.child("uy").putFile(uri)
                binding.progressBar.visibility = View.VISIBLE
                task.addOnSuccessListener {
                    binding.progressBar.visibility = View.GONE
                    binding.image.setImageURI(uri)

                    //agar video bo'sa
                    // binding.image.setVideoUri(uri)

                    it.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                        println(it)
                        Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()
                    }
                }
                task.addOnFailureListener {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Yuklab bo'lmadi", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Rasm tanlanmadi", Toast.LENGTH_SHORT).show()
            }

        }
}