package com.semenchenko.foodfriend.view

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.semenchenko.foodfriend.MainActivity
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.adapter.SearchDishAdapter
import com.semenchenko.foodfriend.databinding.FragmentSearchBinding
import com.semenchenko.foodfriend.repository.SupabaseManager
import com.semenchenko.foodfriend.viewmodel.RecipeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    private val supabaseManager: SupabaseManager = SupabaseManager()
    private val recipeViewModel: RecipeViewModel by activityViewModels()

    private val handler = Handler()
    private val delay: Long = 1000 // 1000 = 1 second
    private lateinit var searchQuery: String

    private val searchRunnable = Runnable {
        // Виконується після затримки
        GlobalScope.launch(Dispatchers.Main) {
            binding.searchDishRecyclerView.adapter = SearchDishAdapter(supabaseManager.searchDishesByName(
                searchQuery), recipeViewModel, activity)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        binding.searchView.requestFocus()

        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchView, InputMethodManager.SHOW_IMPLICIT)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
            (activity as MainActivity).setBottomNavVisibility(true)
        }

        (activity as MainActivity).setBottomNavVisibility(false)

        binding.searchCancel.setOnClickListener {
            findNavController().navigateUp()
            (activity as MainActivity).setBottomNavVisibility(true)
        }

        binding.searchDishRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }

        binding.searchView.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                GlobalScope.launch(Dispatchers.Main) {
//                    binding.searchDishRecyclerView.adapter = SearchDishAdapter(supabaseManager.searchDishesByName(s.toString()))
//                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                GlobalScope.launch(Dispatchers.Main) {
//                    binding.searchDishRecyclerView.adapter = SearchDishAdapter(supabaseManager.searchDishesByName(s.toString()))
//                }
            }
            override fun afterTextChanged(s: Editable?) {
                handler.removeCallbacks(searchRunnable)
                if (s.toString().isNotEmpty()){
                    searchQuery = s?.toString()!!
                    handler.postDelayed(searchRunnable, delay)
                }
            }
        })

    }
}