package com.example.roomdatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Toast
import com.example.roomdatabase.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var appDatabase: AppDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDatabase=AppDatabase.getDatabase(this)

        binding.btnWriteData.setOnClickListener{
            writeData()
        }

        binding.btnReadData.setOnClickListener {
            readData()
        }

        binding.btnDeleteAll.setOnClickListener {
            GlobalScope.launch {
                appDatabase.studentDao().deleteall()
            }
        }



    }


    private fun writeData(){
        val firstName=binding.etFirstName.text.toString()
        val lastName=binding.etLastName.text.toString()
        val rollNo=binding.etRollNo.text.toString()

        if (firstName.isNotEmpty()&&lastName.isNotEmpty()&&rollNo.isNotEmpty()){

            val student=Student(
                null,firstName,lastName,rollNo.toInt()
            )
            GlobalScope.launch(Dispatchers.IO){
                appDatabase.studentDao().instert(student)
            }

            binding.etFirstName.text.clear()
            binding.etLastName.text.clear()
            binding.etRollNo.text.clear()

            Toast.makeText(this,"Succesfully Written",Toast.LENGTH_SHORT).show()

        }else Toast.makeText(this,"Please Enter Data",Toast.LENGTH_SHORT).show()


    }

    private suspend fun displayData(student: Student){
        withContext(Dispatchers.Main){
            binding.tvFirstName.text=student.firstname
            binding.tvLastName.text=student.lastname
            binding.tvRollNo.text=student.rollNo.toString()
        }
    }

    private fun readData(){

        val rollNo=binding.etRollNoRead.text.toString()

        if(rollNo.isNotEmpty()){
            lateinit var student: Student

            GlobalScope.launch {
                student=appDatabase.studentDao().findbyRoll(rollNo.toInt())
                displayData(student)
            }
        }else Toast.makeText(this,"Please Entry other roll number",Toast.LENGTH_SHORT).show()

    }



}