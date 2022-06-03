package com.example.financeapp.presentation.fragments.finances_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.financeapp.R
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.databinding.FragmentFinancesBinding
import com.example.financeapp.presentation.MainActivity
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinancesFragment : Fragment(), AccountsAdapter.OnAccountClickListener {

    private lateinit var binding: FragmentFinancesBinding
    private lateinit var accountsAdapter: AccountsAdapter
    private lateinit var operationsAdapter: OperationsAdapter
    private val financesViewModel: FinancesViewModel by viewModels()
    private lateinit var layoutManager: LinearLayoutManager
    private var collapsed=false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFinancesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountsAdapter = AccountsAdapter(this, requireContext())
        operationsAdapter = OperationsAdapter(requireContext())
        binding.fabAdd.setOnClickListener {
            financesViewModel.onFabAddClick()
        }
        setupRecyclerViews()
        observeAccounts()
        observeOperations()
        observeOverallMoney()
        collectEvents()
        addOperationsOnListener()
        addAppBarOnListener()
    }

    private fun addAppBarOnListener() {
        binding.appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                (activity as MainActivity).binding.bottomNavigationView.apply {
                    if (binding.toolbarLayout.height + verticalOffset < 2 * ViewCompat.getMinimumHeight(binding.toolbarLayout)){
                        collapsed=true
                    } else {
                        clearAnimation();
                        animate().translationY(0F).duration = 200
                        collapsed=false
                    }
                }
            }
        })
    }

    private fun observeOverallMoney() {
        financesViewModel.overallMoney.observe(viewLifecycleOwner) {
            binding.toolbarLayout.title = resources.getString(R.string.money, it)
        }
    }

    private fun collectEvents() {
        lifecycleScope.launchWhenStarted {
            financesViewModel.event.collect {
                when (it) {
                    is FinancesViewModel.Event.NavigateToEditAccountScreen -> {
                        val action =
                            FinancesFragmentDirections.actionFinancesFragmentToAddEditAccountFragment(
                                it.account
                            )
                        findNavController().navigate(action)
                    }
                    is FinancesViewModel.Event.NavigateToAddAccountScreen -> {
                        val action =
                            FinancesFragmentDirections.actionFinancesFragmentToAddEditAccountFragment()
                        findNavController().navigate(action)
                    }
                }
                accountsAdapter.apply {
                    if (selectedPosition != -1) {
                        differ.currentList[selectedPosition].selected = false
                        financesViewModel.setAccountId(-1)
                        notifyItemChanged(selectedPosition)
                    }
                }
            }
        }
    }

    private fun observeOperations() {
        financesViewModel.operations.observe(viewLifecycleOwner) {
            operationsAdapter.differ.submitList(it)
        }
    }

    private fun observeAccounts() {
        financesViewModel.accounts.observe(viewLifecycleOwner) {
            accountsAdapter.differ.submitList(it)
        }
    }

    private fun setupRecyclerViews() {
        binding.apply {
            recyclerViewAccounts.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            (recyclerViewAccounts.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            recyclerViewAccounts.adapter = accountsAdapter
            recyclerViewAccounts.setHasFixedSize(true)

            layoutManager=LinearLayoutManager(requireContext())
            recyclerViewOperations.layoutManager = layoutManager
            (recyclerViewOperations.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            recyclerViewOperations.adapter = operationsAdapter
            recyclerViewOperations.setHasFixedSize(true)
        }
    }

    private fun addOperationsOnListener(){
        binding.recyclerViewOperations.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(layoutManager.findLastVisibleItemPosition()==operationsAdapter.differ.currentList.size-1 &&
                    recyclerView.scrollState!=RecyclerView.SCROLL_STATE_DRAGGING &&
                    collapsed){
                    (activity as MainActivity).binding.bottomNavigationView.apply{
                        clearAnimation()
                        animate().translationY((height*2).toFloat()).duration = 200
                    }
                }else if(recyclerView.scrollState!=RecyclerView.SCROLL_STATE_DRAGGING){
                    (activity as MainActivity).binding.bottomNavigationView.apply{
                        clearAnimation()
                        animate().translationY(0F).duration = 100
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(layoutManager.findLastVisibleItemPosition()==operationsAdapter.differ.currentList.size-1 &&
                    newState!=RecyclerView.SCROLL_STATE_DRAGGING &&
                    collapsed){
                    (activity as MainActivity).binding.bottomNavigationView.apply{
                        clearAnimation()
                        animate().translationY((height*2).toFloat()).duration = 200
                    }
                }
            }
        })
    }

    override fun onAccountItemClick(position: Int) {
        accountsAdapter.apply {
            if (selectedPosition == position && differ.currentList[position].selected) {
                differ.currentList[position].selected = false
                financesViewModel.setAccountId(-1)
            } else {
                differ.currentList[position].selected = true
                financesViewModel.setAccountId(differ.currentList[position].accId)
                if (selectedPosition != -1 && differ.currentList[selectedPosition].selected && position != selectedPosition) {
                    differ.currentList[selectedPosition].selected = false
                }
            }
            notifyItemChanged(selectedPosition)
            selectedPosition = position
            notifyItemChanged(selectedPosition)
        }
    }

    override fun onAccountItemLongClick(account: Account) {
        financesViewModel.onAccountLongClick(account)
    }
}