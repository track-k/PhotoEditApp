package com.example.photoeditapp

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    // 用于显示图片的ImageView
    private lateinit var imageView: ImageView

    // 注册一个Activity结果监听器，用于接收从相册返回的图片
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri: Uri? = data?.data
            selectedImageUri?.let {
                // 将选中的图片设置到ImageView中
                imageView.setImageURI(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 找到布局中的Button和ImageView
        val btnSelectImage: Button = findViewById(R.id.btnSelectImage)
        imageView = findViewById(R.id.imageView)

        // 为按钮设置点击事件监听器
        btnSelectImage.setOnClickListener {
            // 检查是否有读取图片的权限
            if (hasReadImagesPermission()) {
                // 有权限，直接打开相册
                openGallery()
            } else {
                // 没有权限，请求权限
                requestReadImagesPermission()
            }
        }
    }

    /**
     * 检查是否拥有读取图片的权限
     */
    private fun hasReadImagesPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13及以上，使用READ_MEDIA_IMAGES权限
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // Android 12及以下，使用旧的READ_EXTERNAL_STORAGE权限
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * 请求读取图片的权限
     */
    private fun requestReadImagesPermission() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_READ_IMAGES)
    }

    /**
     * 打开系统相册
     */
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    /**
     * 权限请求结果回调
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_READ_IMAGES) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户授予了权限，打开相册
                openGallery()
            } else {
                // 用户拒绝了权限，显示提示信息
                Toast.makeText(
                    this,
                    "需要授予读取图片权限才能从相册选择图片",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        // 权限请求码
        private const val REQUEST_CODE_READ_IMAGES = 1001
    }
}