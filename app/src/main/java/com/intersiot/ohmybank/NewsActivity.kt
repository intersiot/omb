package com.intersiot.ohmybank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.intersiot.ohmybank.adapter.NewsAdapter
import com.intersiot.ohmybank.databinding.ActivityNewsBinding
import com.intersiot.ohmybank.model.NewsDTO
import org.jetbrains.anko.doAsync
import org.jsoup.Jsoup
import java.lang.Exception

class NewsActivity : AppCompatActivity(), NewsAdapter.setActivityMove {
    // layout view
    private lateinit var binding: ActivityNewsBinding
    // firebase
    private var mAuth = FirebaseAuth.getInstance()

    var adapter = NewsAdapter(this)
    var queue: RequestQueue? = null

    var tag = "NewsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.newsContents.setHasFixedSize(true)
        binding.newsContents.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        queue = Volley.newRequestQueue(this)
        //        queue.add(stringRequest);
        getNews()

        // bottom navigation click event
        // mybank click
        binding.bottomNavMybank.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            Log.d(tag, "내통장으로 이동")
            startActivity(intent)
        }
        // transaction click
        binding.bottomNavTrans.setOnClickListener {
            val intent = Intent(this, TransactionActivity::class.java)
            Log.d(tag, "거래내역으로 이동")
            startActivity(intent)
        }
    } // end onCreate()

    private fun getNews() {
        var data : MutableList<NewsDTO> = mutableListOf()
        doAsync {
            val url = "https://news.daum.net/breakingnews/economic"
            try {
                // 여기서 스크래핑 한다.
                val doc = Jsoup.connect(url).get()
                // 첫번째 1st_detail_t1의 데이터만 가져오는 듯 하다. 다 가져 올려면 루프문을 돌려야 할듯
                val newsData = doc.select("ul[class=list_news2 list_allnews]").select("li")

                newsData.forEachIndexed { index, element ->
                    // 실제 페이지의 html 문서의 소스를 보고
                    // 어떤 데이터가 어떤 태그를 사용하고 있는지 분석해야 한다.
                    val newsTitle = element.select("div.cont_thumb strong.tit_thumb a").text()
                    val thumbnail = element.select("a.link_thumb img").attr("src")
                    val info = element.select("div.desc_thumb span.link_txt").text()
//                    val url = "http://m.inven.co.kr${element.select("a[class=subject]").attr("href")}"
                    val url = element.select("div.cont_thumb strong.tit_thumb a").attr("href")
                    data.add(NewsDTO(newsTitle, thumbnail, info, url))

                    runOnUiThread {
                        // recyclerView.adapter = MyAdapter(movies)
                        adapter.addData(data)
                        binding.newsContents.adapter = adapter
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun logout(view: View?) {
        mAuth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
        startActivity(intent)
    }

    override fun activityMove(intent: Intent) {
        startActivity(intent)
    }

    fun moveHome(view: View) {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

} // end Activity