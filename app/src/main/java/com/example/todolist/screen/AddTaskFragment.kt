package com.example.todo.screen

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.todo.database.AppDataBase
import com.example.todo.entities.Task
import com.example.todolist.R
import com.example.todolist.databinding.FragmentAddTaskBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


class AddTaskFragment : Fragment() {
    lateinit var binding: FragmentAddTaskBinding
    private var param1 : String?  = null
    private var param2 : String?  = null
    private lateinit var currentFilePath: String
    private lateinit var img: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        img = binding.photo
        binding.btnSave.setOnClickListener {


            var task = Task(
                title = binding.taskInfoTitle.text.toString(),
                text = binding.taskInfoText.text.toString(),
                filePath = currentFilePath
            )

            val appDataBase = AppDataBase.getDataBsae(requireContext())
            appDataBase.getTaskDao().addTask(task)

            parentFragmentManager.beginTransaction()
                .replace(R.id.main_screen, HomeFragment()).commit()
        }
            binding.gallery.setOnClickListener {
                takePhotoResult.launch("image/*")
            }

            binding.camera.setOnClickListener {
                dispatchTakePictureIntent()
        }
        return binding.root
    }
        val takePhotoResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri == null) return@registerForActivityResult
            img.setImageURI(uri)
            //rasmni alohida xotiraga saqlash

            val openInputStream = requireActivity().contentResolver?.openInputStream(uri)
            val file = File(requireActivity().filesDir, "${System.currentTimeMillis()}.jpg")
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            currentFilePath = file.absolutePath
            openInputStream?.close()


        }

        @Throws(IOException::class)
        private fun createImageFile(): File {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                currentFilePath = absolutePath
            }
        }
        private fun dispatchTakePictureIntent() {
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                null
            }

            photoFile?.let {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    "package com.example.todo",
                    it
                )
                takePhotoResultCamera.launch(photoURI)
            }
        }
        val takePhotoResultCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                img.setImageURI(Uri.fromFile(File(currentFilePath)))
            }
        }
}