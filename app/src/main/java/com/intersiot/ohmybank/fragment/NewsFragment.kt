package com.intersiot.ohmybank.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.intersiot.ohmybank.R
import com.intersiot.ohmybank.adapter.NewsAdapter
import com.intersiot.ohmybank.databinding.FragmentNewsBinding
import com.intersiot.ohmybank.model.NewsDTO
import org.jsoup.Jsoup
import java.lang.Exception

class NewsFragment : Fragment(), NewsAdapter.setActivityMove {
    // firebase 인증
    private var mAuth = FirebaseAuth.getInstance()

    var adapter = NewsAdapter(this)



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // layout binding
        val binding = FragmentNewsBinding.inflate(inflater, container, false)


        return binding.root
    }

    private fun getNews() {
        var data: MutableList<NewsDTO> = mutableListOf()
        doAsync {
            val url = "https://news.naver.com/main/list.nhn?mode=LS2D&mid=shm&sid1=101&sid2=259"
            try {
                // 여기서 스크래핑 한다
                val doc = Jsoup.connect(url).get()
                val newsData = doc.select("ul[class=list]").select("li")

                newsData.forEachIndexed { index, element ->
                    /**
                     * 실제 페이지의 html 문서의 소스를 보고
                     * 어떤 데이터가 어떤 태그를 사용하고 있는지 분석해야 함.
                     */
                    val newsTitle = element.select("").text()

                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun activityMove(intent: Intent?) {
        startActivity(intent)
    }


}