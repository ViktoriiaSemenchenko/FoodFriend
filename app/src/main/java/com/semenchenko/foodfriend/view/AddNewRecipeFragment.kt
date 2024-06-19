package com.semenchenko.foodfriend.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.semenchenko.foodfriend.MainActivity
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.databinding.FragmentAddNewRecipeBinding
import com.semenchenko.foodfriend.repository.PhotoManager
import com.semenchenko.foodfriend.viewmodel.AddNewRecipeViewModel

class AddNewRecipeFragment : Fragment(R.layout.fragment_add_new_recipe) {

    private lateinit var binding: FragmentAddNewRecipeBinding
    private val addNewRecipeViewModel: AddNewRecipeViewModel by activityViewModels()
    private lateinit var getContent: ActivityResultLauncher<Intent>
    private val photoManager: PhotoManager = PhotoManager()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddNewRecipeBinding.bind(view)

        (activity as MainActivity).setBottomNavVisibility(false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_addNewRecipe_to_homePage)
            (activity as MainActivity).setBottomNavVisibility(true)
        }

        binding.addRecipeCancel.setOnClickListener {
            addNewRecipeViewModel.clearData()
            findNavController().navigate(R.id.action_addNewRecipe_to_homePage)
            (activity as MainActivity).setBottomNavVisibility(true)
        }

        val testImageResource: Drawable? =
            ContextCompat.getDrawable(requireContext(), R.drawable.background_for_photo)

        addNewRecipeViewModel.dishName.observe(viewLifecycleOwner) { dishName ->
            binding.dishName.setText(dishName)
        }

        addNewRecipeViewModel.dishDescription.observe(viewLifecycleOwner) { dishDescription ->
            binding.dishDescription.setText(dishDescription)
        }

        addNewRecipeViewModel.image.observe(viewLifecycleOwner) { image ->
            if (image.isNullOrEmpty()) {
                binding.recipePhoto.setImageDrawable(testImageResource)
            } else {
                binding.recipePhoto.setImageBitmap(image.let { photoManager.base64ToBitmap(it) })
            }
        }

        view.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            view.getWindowVisibleDisplayFrame(r)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - r.bottom

            if (keypadHeight > screenHeight * 0.15) {
                binding.next.visibility = View.GONE
            } else {
                binding.next.visibility = View.VISIBLE
            }
        }

        binding.next.setOnClickListener {
            addNewRecipeViewModel.dishName.value = binding.dishName.text.toString()
            addNewRecipeViewModel.dishDescription.value = binding.dishDescription.text.toString()

            if (checkRequiredFields(
                    addNewRecipeViewModel.dishName.value!!,
                    addNewRecipeViewModel.dishDescription.value!!
                )
            ) {
                findNavController().navigate(R.id.action_addNewRecipe_to_addIngredientToNewRecipe)
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Required fields are not filled in or overflow")
                    .setPositiveButton("OK") { _, _ ->
                    }
                    .show()
            }
        }

        binding.recipePhoto.setOnClickListener {
            println("recipePhoto clicked")
            checkPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, fragment = this
            ) {

                val takePhotoIntent = Intent(Intent.ACTION_PICK)
                takePhotoIntent.type = "image/*"
                getContent.launch(takePhotoIntent)
            }
        }

        val photoManager = PhotoManager()
        getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let { uri ->
                        val photo = photoManager.getBitmapFromUri(uri, requireContext())
                        binding.recipePhoto.setImageBitmap(photo)
                        addNewRecipeViewModel.image.value = photoManager.bitmapToBase64(photo)
                        println("addNewRecipeViewModel.image.value: ${addNewRecipeViewModel.image.value}")
                    }
                }
            }
    }

    private fun checkPermission(
        vararg permissions: String,
        fragment: Fragment,
        res: (Boolean) -> (Unit)
    ) {
        var checkPermission = 0
        permissions.forEach { permission ->
            if (ActivityCompat.checkSelfPermission(
                    fragment.requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    fragment.requireActivity(),
                    permissions, 1
                )
            } else {
                checkPermission++
            }
        }
        if (permissions.size == checkPermission) {
            res(true)
        }
    }

    private fun checkRequiredFields(name: String, description: String): Boolean {
        if (name != "" && description != "") {
            return name.length <= 55 && description.length <= 2500
        }
        return false
    }
}