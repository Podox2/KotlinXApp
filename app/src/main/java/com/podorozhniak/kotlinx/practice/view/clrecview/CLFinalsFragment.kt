package com.podorozhniak.kotlinx.practice.view.clrecview

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.google.android.material.snackbar.Snackbar
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentClFinalsBinding
import com.podorozhniak.kotlinx.practice.BaseFragment
import java.util.*

class CLFinalsFragment : BaseFragment<FragmentClFinalsBinding>(){

    lateinit var matchAdapter: MatchAdapter
    private val matches = ArrayList<Match>()

    override val layoutId: Int
        get() = R.layout.fragment_cl_finals

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateViewBinding(view: View) = FragmentClFinalsBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search, menu)
        val searchItem = menu.findItem(R.id.menu_item_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                matchAdapter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                matchAdapter.filter(newText)
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initList(){
        matches.add(Match("Marseille 1:0 Milan", "1992/93"))
        matches.add(Match("Milan 4:0 Barcelona", "1993/94"))
        matches.add(Match("Ajax 1:0 Milan", "1994/95"))
        matches.add(Match("Ajax 1 (2):(4) 1 Juventus", "1995/96"))
        matches.add(Match("Borussia Dortmund 3:1 Juventus", "1996/97"))
        matches.add(Match("Juventus 0:1 Real Madrid", "1997/98"))
        matches.add(Match("Manchester United 2:1 Bayern Munich", "1998/99"))
        matches.add(Match("Real Madrid 3:0 Valencia", "1999/00"))
        matches.add(Match("Bayern Munich 1 (5):(4) 1 Valencia", "2000/01"))
        matches.add(Match("Bayer Leverkusen 1:2 Real Madrid", "2001/02"))
        matches.add(Match("Juventus 0 (2):0 (3) Milan", "2002/03"))
        matches.add(Match("Monaco 0:3 Porto", "2003/04"))
        matches.add(Match("Milan 3 (2):(3) 3 Liverpool", "2004/05"))
        matches.add(Match("Barcelona 2:1 Arsenal", "2005/06"))
        matches.add(Match("Milan 2:1 Liverpool", "2006/07"))
        matches.add(Match("Man U 1 (6):(5) 1 Chelsea", "2007/08"))
        matches.add(Match("Barcelona 2:0 Man U", "2008/09"))
        matches.add(Match("Bayern Munich 0:2 Inter", "2009/10"))
        matches.add(
            Match(
                "Barcelona 3:1 Man U", "201" +
                        "0/11"
            )
        )
        matches.add(Match("Bayern Munich 1 (3):(4) 1 Chelsea", "2011/12"))
        matches.add(Match("Borussia D 1:2 Bayern Munich", "2012/13"))
        matches.add(Match("Real Madrid 4:1 Atlético M", "2013/14"))
        matches.add(Match("Juventus 1:3 Barcelona", "2014/15"))
        matches.add(Match("Real Madrid 1 (5):(3) 1 Atlético M", "2015/16"))
        matches.add(Match("Juventus 1:4 Real Madrid", "2016/17"))
        matches.add(Match("Real Madrid 3:1 Liverpool", "2017/18"))
        matches.add(Match("Tottenham 0:2 Liverpool", "2018/19"))
        matches.add(Match("Paris Saint-Germain 0:1 Bayern Munich", "2019/20"))
        matches.reverse()
    }

    private fun initRecyclerView() {
        val onMatchClickListener: MatchAdapter.OnMatchClickListener =
            MatchAdapter.OnMatchClickListener { match ->
                Toast.makeText(requireContext(), match?.result, Toast.LENGTH_SHORT).show()
                Snackbar.make(
                    binding.recView,
                    match.season,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        matchAdapter = MatchAdapter(onMatchClickListener)
        binding.recView.adapter = matchAdapter
        matchAdapter.setItems(matches)
    }
}



