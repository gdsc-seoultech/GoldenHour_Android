package com.gdsc.goldenhour.view.call

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gdsc.goldenhour.databinding.FragmentBtn110Binding
class btn110Fragment : Fragment() {

    lateinit var binding: FragmentBtn110Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBtn110Binding.inflate(inflater, container, false)

        val type = requireArguments().getString("type")

        binding.resBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+type))
            intent.putExtra("sms_body", arguments?.getString("key"))
            startActivity(intent)
        }

        return binding.root
    }
}