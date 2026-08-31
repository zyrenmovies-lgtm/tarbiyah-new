package com.tarbiyah.ailearn.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tarbiyah.ailearn.R

class QuizResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val score = arguments?.getInt("SCORE") ?: 0
        val totalQuestions = arguments?.getInt("TOTAL_QUESTIONS") ?: 5
        
        // Calculate score out of 100
        val finalScore = (score.toFloat() / totalQuestions * 100).toInt()

        view.findViewById<TextView>(R.id.tv_score).text = finalScore.toString()
        view.findViewById<TextView>(R.id.tv_correct).text = "$score/$totalQuestions"

        view.findViewById<Button>(R.id.btn_finish).setOnClickListener {
            findNavController().navigate(R.id.action_quizResultFragment_to_homeFragment)
        }

        view.findViewById<ImageButton>(R.id.btn_close).setOnClickListener {
            findNavController().navigate(R.id.action_quizResultFragment_to_homeFragment)
        }
    }
}
