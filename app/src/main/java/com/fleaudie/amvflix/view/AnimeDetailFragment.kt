package com.fleaudie.amvflix.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fleaudie.amvflix.glide.GradientRoundedTransformation
import com.fleaudie.amvflix.R
import com.fleaudie.amvflix.adapters.ImagePagerAdapter
import com.fleaudie.amvflix.adapters.ListSelectionAdapter
import com.fleaudie.amvflix.databinding.FragmentAnimeDetailBinding
import com.fleaudie.amvflix.databinding.PopupAddToListBinding
import com.fleaudie.amvflix.viewmodel.AnimeDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class AnimeDetailFragment : Fragment() {

    private val viewModel: AnimeDetailViewModel by viewModels()
    private lateinit var binding: FragmentAnimeDetailBinding
    private lateinit var imagePagerAdapter: ImagePagerAdapter
    private var timer: Timer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var customView: View? = null
    private var customViewCallback: WebChromeClient.CustomViewCallback? = null
    private var originalOrientation: Int = 0
    private var originalSystemUiVisibility: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnimeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animeName = AnimeDetailFragmentArgs.fromBundle(requireArguments()).animeName

        viewModel.getAnimeDetailByName(animeName)
        viewModel.animeDetail.observe(viewLifecycleOwner) { animeDetail ->
            binding.apply {
                txtAnimeName.text = animeDetail.animeName
                txtAuthor.text = "${getString(R.string.author)}: ${animeDetail.author}"
                txtCharacters.text = "${getString(R.string.characters)}: ${animeDetail.characters.joinToString(", ")} "
                txtTags.text = "${getString(R.string.genre)}: ${animeDetail.tags.joinToString(", ")} "
                txtReleaseDate.text = "${getString(R.string.release_date)}: ${animeDetail.year} "
                txtDescription.text = animeDetail.description

                val requestOptions = RequestOptions().transform(GradientRoundedTransformation(50f))
                context?.let {
                    Glide.with(it)
                        .load(animeDetail.coverPhoto)
                        .apply(requestOptions)
                        .into(imgDetailCoverPhoto)
                }

                val embedId = animeDetail.embedId
                val webView = webView
                webView.settings.javaScriptEnabled = true
                webView.settings.loadWithOverviewMode = true
                webView.webChromeClient = MyWebChromeClient()
                webView.webViewClient = WebViewClient()

                val html = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/$embedId\" frameborder=\"0\" allowfullscreen></iframe>"
                webView.loadData(html, "text/html", "utf-8")

                context?.let {
                    Glide.with(it)
                        .load(animeDetail.animeLogo)
                        .into(imgAnimeLogo)
                }

                imagePagerAdapter = ImagePagerAdapter(animeDetail.detailPhotos, requireContext())
                viewPagerAnimeScene.adapter = imagePagerAdapter

                val indicatorLayout = binding.indicatorLayout
                val indicatorParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                indicatorParams.setMargins(8, 0, 8, 0)

                for (i in 0 until imagePagerAdapter.getItemCount()) {
                    val indicator = ImageView(context)
                    indicator.setImageDrawable(context?.let { ContextCompat.getDrawable(it, R.drawable.indicator_circle) })
                    indicator.layoutParams = indicatorParams
                    indicatorLayout.addView(indicator)
                }

                binding.viewPagerAnimeScene.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

                    override fun onPageSelected(position: Int) {
                        updateIndicators(position % imagePagerAdapter.getItemCount())
                    }

                    override fun onPageScrollStateChanged(state: Int) {}
                })
                startAutoScroll()

                btnAddToList.setOnClickListener {
                    showAddToListPopup(animeDetail.animeName, animeDetail.animeLogo)
                }
            }
        }
    }

    private fun showAddToListPopup(animeName: String, animeLogo: String) {
        val inflater = LayoutInflater.from(context)
        val popupBinding = PopupAddToListBinding.inflate(inflater, null, false)
        val view = popupBinding.root

        val popupWindow = PopupWindow(
            view,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        val recyclerView = popupBinding.rcyListSelection
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        val listAdapter = ListSelectionAdapter(viewModel.watchLists.value ?: emptyList()) { listName ->
            viewModel.addAnimeToList(animeName, animeLogo,listName) { success, error ->
                if (success) {
                    popupWindow.dismiss()
                } else {
                    Toast.makeText(context, "Anime could not be added to the list: $error", Toast.LENGTH_SHORT).show()
                }
            }
        }
        recyclerView.adapter = listAdapter

        popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
    }

    private fun startAutoScroll() {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                handler.post {
                    var nextPage = binding.viewPagerAnimeScene.currentItem + 1
                    if (nextPage == imagePagerAdapter.count) {
                        nextPage = 0
                    }
                    binding.viewPagerAnimeScene.setCurrentItem(nextPage, true)
                }
            }
        }, 5000, 5000)
    }

    private fun updateIndicators(position: Int) {
        for (i in 0 until binding.indicatorLayout.childCount) {
            val indicator = binding.indicatorLayout.getChildAt(i) as ImageView
            if (i == position) {
                indicator.setImageDrawable(context?.let { ContextCompat.getDrawable(it, R.drawable.indicator_circle_click) })
            } else {
                indicator.setImageDrawable(context?.let { ContextCompat.getDrawable(it, R.drawable.indicator_circle) })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
    }

    private inner class MyWebChromeClient : WebChromeClient() {
        @RequiresApi(Build.VERSION_CODES.R)
        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
            if (customView != null) {
                callback?.onCustomViewHidden()
                return
            }
            customView = view
            customViewCallback = callback
            originalSystemUiVisibility = requireActivity().window.decorView.systemUiVisibility
            originalOrientation = requireActivity().requestedOrientation
            (requireActivity().window.decorView as FrameLayout).addView(
                customView,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )

            requireActivity().window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.systemBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        @RequiresApi(Build.VERSION_CODES.R)
        override fun onHideCustomView() {
            (requireActivity().window.decorView as FrameLayout).removeView(customView)
            customView = null

            requireActivity().window.insetsController?.show(WindowInsets.Type.systemBars())

            requireActivity().window.decorView.systemUiVisibility = originalSystemUiVisibility
            requireActivity().requestedOrientation = originalOrientation
            customViewCallback?.onCustomViewHidden()
            customViewCallback = null
        }
    }
}