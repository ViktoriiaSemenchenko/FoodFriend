package com.semenchenko.foodfriend.view

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.semenchenko.foodfriend.MainActivity
import com.semenchenko.foodfriend.R
import com.semenchenko.foodfriend.adapter.SearchIngredientAdapter
import com.semenchenko.foodfriend.databinding.FragmentSearchIngredientBinding
import com.semenchenko.foodfriend.repository.SupabaseManager
import com.semenchenko.foodfriend.viewmodel.AddUnitViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchIngredientFragment : Fragment(R.layout.fragment_search_ingredient) {
    private lateinit var binding: FragmentSearchIngredientBinding
    private val supabaseManager: SupabaseManager = SupabaseManager()
    private val addUnitViewModel: AddUnitViewModel by activityViewModels()

    private val handler = Handler()
    private val delay: Long = 700 // 1000 = 1 second
    private lateinit var searchQuery: String

    private val searchRunnable = Runnable {
        // Виконується після затримки
        GlobalScope.launch(Dispatchers.Main) {
            binding.searchDishRecyclerView.adapter = SearchIngredientAdapter(supabaseManager.searchIngredientsByName(
                searchQuery), addUnitViewModel, activity)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchIngredientBinding.bind(view)

        binding.searchView.requestFocus()

        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchView, InputMethodManager.SHOW_IMPLICIT)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        (activity as MainActivity).setBottomNavVisibility(false)

        binding.searchCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.searchDishRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }

        binding.searchView.doAfterTextChanged {
            handler.removeCallbacks(searchRunnable)
            if (it.toString().isNotEmpty()){
                searchQuery = it?.toString()!!
                handler.postDelayed(searchRunnable, delay)
            }
        }
    }
}