package com.example.financeapp.presentation.fragments.finances_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.databinding.FragmentFinancesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinancesFragment: Fragment(), AccountsAdapter.OnAccountClickListener {

    private lateinit var binding:FragmentFinancesBinding
    private lateinit var accountsAdapter: AccountsAdapter
    private lateinit var operationsAdapter: OperationsAdapter
    private val financesViewModel:FinancesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentFinancesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountsAdapter= AccountsAdapter(this,requireContext())
        operationsAdapter= OperationsAdapter(requireContext())
        binding.fabAdd.setOnClickListener{
            financesViewModel.onFabAddClick()
        }
        setupRecyclerViews()
        observeAccounts()
        observeOperations()
        observeOverallMoney()
        collectEvents()
    }

    private fun observeOverallMoney() {

    }

    private fun collectEvents() {
        lifecycleScope.launchWhenStarted {
            financesViewModel.event.collect {
                when(it){
                    is FinancesViewModel.Event.NavigateToEditAccountScreen->{
                        val action=FinancesFragmentDirections.actionFinancesFragmentToAddEditAccountFragment(it.account)
                        findNavController().navigate(action)
                    }
                    is FinancesViewModel.Event.NavigateToAddAccountScreen->{
                        val action=FinancesFragmentDirections.actionFinancesFragmentToAddEditAccountFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun observeOperations() {
        financesViewModel.operations.observe(viewLifecycleOwner){
            operationsAdapter.differ.submitList(it)
        }
    }

    private fun observeAccounts() {
        financesViewModel.accounts.observe(viewLifecycleOwner){
            accountsAdapter.differ.submitList(it)
        }
    }

    private fun setupRecyclerViews() {
        binding.apply {
            recyclerViewAccounts.adapter=accountsAdapter
            recyclerViewAccounts.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            recyclerViewAccounts.setHasFixedSize(true)

            recyclerViewOperations.adapter=operationsAdapter
            recyclerViewOperations.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            recyclerViewOperations.setHasFixedSize(true)
        }
    }

    override fun onAccountItemClick(position: Int) {
        accountsAdapter.apply {
            selectedPosition = position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
            if(lastSelectedPosition==selectedPosition){
                financesViewModel.getAllOperations()
            }else{
                financesViewModel.getOperationsByAccId(differ.currentList[position].accId)
            }
            lastSelectedPosition = selectedPosition
        }
    }

    override fun onAccountItemLongClick(account: Account) {
        financesViewModel.onAccountLongClick(account)
    }
}