package com.agarcia.myapplication.activities

import android.graphics.Movie
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.agarcia.myapplication.R
import com.agarcia.myapplication.adapters.MovieAdapter
import com.agarcia.myapplication.network.NetworkUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.MalformedURLException

class MainActivity : AppCompatActivity() {

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var viewManager:RecyclerView.LayoutManager
    private var movieList: ArrayList<Movie> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
        initSearchButton()
    }


    fun initRecyclerView(){
        viewManager= LinearLayoutManager(this)

        movieAdapter= MovieAdapter(movieList) { movieItem: com.agarcia.myapplication.pojos.Movie -> movieItemClicked(movieItem)}

        movie_list_rv.apply {
            setHasFixedSize(true)
            layoutManager=viewManager
            adapter=movieAdapter
        }
    }

    fun initSearchButton()=add_movie_btn.setOnClickListener {
        if(!movie_name_et.text.toString().isEmpty()){
            FetchMovie().execute(movie_name_et.text.toString())
        }
    }
    private fun  movieItemClicked(m: com.agarcia.myapplication.pojos.Movie){

    }
fun addMovieToList(movie: Movie){
    movieList.add(movie)
    movieAdapter.changeList(movieList)
    Log.d("Number",movieList.size.toString())
}

    private inner class FetchMovie: AsyncTask<String,Void,String>(){
        override fun doInBackground(vararg params: String?): String {
            if(params.isNullOrEmpty())return ""
            val movieName=params[0]
            val movieUrl= NetworkUtils().buildURL(movieName.toString())
            return try {
                NetworkUtils().getResponseFromHttpUrl(movieUrl!!)!!
            }catch (e: MalformedURLException){
                ""
            }
        }
        override fun onPostExecute(s: String?) {
super.onPostExecute(s)
            if (s != null) {
                if (!s.isEmpty()) {
                    val moviejson=JSONObject(s)
                    if(moviejson.getString("Response")=="True"){
                        val movie = Gson().fromJson<Movie>(s,Movie::class.java)
                        addMovieToList(movie)

                    }
                    else{
                        Snackbar.make(main_ll,"no hay",Snackbar.LENGTH_SHORT).show()
                    }
                    // get JSONObject from JSON file


                }
            }
        }





    }


}
