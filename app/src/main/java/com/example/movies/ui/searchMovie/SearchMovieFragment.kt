package com.example.movies.ui.searchMovie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movies.R
import com.example.movies.constants.ErrorConstants
import com.example.movies.databinding.FragmentSearchMovieBinding
import com.example.movies.repository.MovieRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchMovieFragment : Fragment() {

    @Inject
    lateinit var repository: MovieRepository
    private var _binding: FragmentSearchMovieBinding? = null
    private val binding: FragmentSearchMovieBinding
        get() = _binding ?: throw Exception(ErrorConstants.fragmentError)

    companion object {
        private const val NEW_TEXT_SEARCH = "newText"

        fun newInstance(newText: String): SearchMovieFragment {
            val args = Bundle().apply {
                putString(NEW_TEXT_SEARCH, newText)
            }
            return SearchMovieFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchMovieBinding.inflate(inflater, container, false)
        return binding.root
    }
}