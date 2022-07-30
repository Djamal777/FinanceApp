package com.example.financeapp.presentation.fragments.icons_dialog_fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class IconsDialogFragment : DialogFragment(), IconsAdapter.OnIconClickListener {

    private var mRecyclerView: RecyclerView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mRecyclerView = RecyclerView(requireContext())
        mRecyclerView?.layoutManager = GridLayoutManager(requireContext(), 3)
        mRecyclerView?.adapter = IconsAdapter(this, requireContext())
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Иконки")
            .setView(mRecyclerView)
            .create()
    }

    override fun onIconItemClick(tag:String) {
        setFragmentResult(
            "icon_request",
            bundleOf("icon_result" to tag)
        )
        findNavController().popBackStack()
    }

}