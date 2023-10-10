package com.example.newsApp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.newsApp.R
import com.example.newsApp.data.Article
import com.example.newsApp.databinding.FragmentNewsDetailBinding
import com.example.newsApp.viewmodel.NewsViewModel
import com.example.newsApp.viewmodel.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewsDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class NewsDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var _binding: FragmentNewsDetailBinding
    private val viewModel: NewsViewModel by viewModels()

    @Inject
    lateinit var newsAdapter: NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        _binding = FragmentNewsDetailBinding.inflate(inflater)

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding.rvNews.adapter = newsAdapter
        newsAdapter.setItemClick(object : ClickInterface<Article> {
            override fun onClick(data: Article) {
                val bundle = Bundle()
                bundle.putString("url", data.url)
                Navigation.findNavController(view).navigate(R.id.action_newsDetailFragment_to_webViewFragment,bundle);
            }
        })

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.stateNewsHeadline.collect { news ->
                when (news) {
                    is State.LOADING -> {
                        _binding.progressbar.visibility = View.VISIBLE
                    }

                    is State.FAILURE -> {
                        _binding.progressbar.visibility = View.GONE

                    }

                    is State.SUCCESS -> {
                        _binding.progressbar.visibility = View.GONE
                        newsAdapter.updateNews(news.response.articles.filter { it.description!="[removed]" })

                    }

                    else -> {}
                }
            }


        }
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewsDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewsDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}