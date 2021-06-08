package com.example.capstoneproject.main.news.notification

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstoneproject.databinding.FragmentRvNotificationBinding
import com.example.capstoneproject.detailnotification.DetailNotificationActivity
import com.example.capstoneproject.viewmodel.NewsViewModel
import com.example.core.domain.model.News
import com.example.core.presentation.NewsAdapter
import com.example.core.valueobject.Status
import org.koin.android.viewmodel.ext.android.viewModel


class NotificationFragment : Fragment() {
    private lateinit var binding : FragmentRvNotificationBinding
    private lateinit var rvAdapter: NewsAdapter
    private val newsViewModel: NewsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRvNotificationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)

        rvAdapter = NewsAdapter()

        initialiseRecyclerView()
        generateDataShow()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rvAdapter.setOnItemClickCallback(object : NewsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: News) {
                val intent = Intent(requireActivity(), DetailNotificationActivity::class.java)
                intent.putExtra(DetailNotificationActivity.DATE_NOTIF, data.date)
                intent.putExtra(DetailNotificationActivity.TITLE_NOTIF, data.title)
                intent.putExtra(DetailNotificationActivity.DESC_NOTIF, data.description)
                intent.putExtra(DetailNotificationActivity.CLUSTER_ID_NOTIF, data.clusterId)
                println("SEND "+ data.clusterId)
                startActivity(intent)
            }
        })
    }
    private fun initialiseRecyclerView() {
        with(binding.rvNotifications) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = rvAdapter
        }
        generateDataShow()
    }

    private fun generateDataShow(){
        newsViewModel.getAllNews().observe(requireActivity(), { list ->
            if (list != null) {
                when (list.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> if (list.data != null) {
                        rvAdapter.setData(list.data)
                        rvAdapter.notifyDataSetChanged()
                        showLoading(false)
                    }
                    Status.ERROR -> {
                        showLoading(false)
                    }
                }
            } else {
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.INVISIBLE
    }
}

