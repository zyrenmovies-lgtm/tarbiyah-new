package com.tarbiyah.ailearn.ui.quiz

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tarbiyah.ailearn.R

class QuizFragment : Fragment() {

    private lateinit var tvQuestion: TextView
    private lateinit var tvProgressText: TextView
    private lateinit var progressQuiz: ProgressBar
    private lateinit var btnOptionA: Button
    private lateinit var btnOptionB: Button
    private lateinit var btnOptionC: Button
    private lateinit var btnOptionD: Button
    private lateinit var btnNext: Button
    private lateinit var btnBack: ImageButton

    private lateinit var options: List<Button>

    private var currentQuestionIndex = 0
    private var score = 0
    private var isAnswered = false

    // Dummy data for now
    private val questions = listOf(
        QuizQuestion(
            "Siapakah nama malaikat yang bertugas menurunkan wahyu kepada Nabi Muhammad SAW?",
            listOf("Malaikat Mikail", "Malaikat Jibril", "Malaikat Israfil", "Malaikat Izrail"),
            1 // Index of correct answer (Jibril)
        ),
        QuizQuestion(
            "Surah apakah yang diturunkan pertama kali kepada Nabi Muhammad SAW?",
            listOf("Al-Fatihah", "Al-Baqarah", "Al-'Alaq", "Yasin"),
            2 // Al-'Alaq
        ),
        QuizQuestion(
            "Rukun Islam yang ke-3 adalah...",
            listOf("Sholat", "Zakat", "Puasa", "Haji"),
            1 // Zakat
        ),
        QuizQuestion(
            "Berapa jumlah rakaat sholat wajib dalam sehari semalam?",
            listOf("15", "17", "20", "12"),
            1 // 17
        ),
        QuizQuestion(
            "Siapakah sahabat Nabi yang mendapat julukan Al-Faruq?",
            listOf("Abu Bakar Ash-Shiddiq", "Umar bin Khattab", "Utsman bin Affan", "Ali bin Abi Thalib"),
            1 // Umar bin Khattab
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupListeners()
        loadQuestion()
    }

    private fun initViews(view: View) {
        tvQuestion = view.findViewById(R.id.tv_question)
        tvProgressText = view.findViewById(R.id.tv_progress_text)
        progressQuiz = view.findViewById(R.id.progress_quiz)
        progressQuiz.max = questions.size

        btnOptionA = view.findViewById(R.id.btn_option_a)
        btnOptionB = view.findViewById(R.id.btn_option_b)
        btnOptionC = view.findViewById(R.id.btn_option_c)
        btnOptionD = view.findViewById(R.id.btn_option_d)
        options = listOf(btnOptionA, btnOptionB, btnOptionC, btnOptionD)

        btnNext = view.findViewById(R.id.btn_next)
        btnBack = view.findViewById(R.id.btn_back)
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        for (i in options.indices) {
            options[i].setOnClickListener {
                if (!isAnswered) {
                    checkAnswer(i)
                }
            }
        }

        btnNext.setOnClickListener {
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                loadQuestion()
            } else {
                // Finish quiz
                val bundle = Bundle().apply {
                    putInt("SCORE", score)
                    putInt("TOTAL_QUESTIONS", questions.size)
                }
                findNavController().navigate(R.id.action_quizFragment_to_quizResultFragment, bundle)
            }
        }
    }

    private fun loadQuestion() {
        isAnswered = false
        btnNext.visibility = View.INVISIBLE

        val currentQuestion = questions[currentQuestionIndex]
        tvQuestion.text = currentQuestion.question

        // Assuming options list in layout always has 4 buttons, and data always has 4 strings
        val labels = listOf("A. ", "B. ", "C. ", "D. ")
        for (i in options.indices) {
            options[i].text = labels[i] + currentQuestion.options[i]
            // Reset background and text color to default
            options[i].backgroundTintList = null
            options[i].setBackgroundResource(R.drawable.bg_input_modern)
            options[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
        }

        tvProgressText.text = "${currentQuestionIndex + 1}/${questions.size}"
        progressQuiz.progress = currentQuestionIndex + 1
    }

    private fun checkAnswer(selectedIndex: Int) {
        isAnswered = true
        btnNext.visibility = View.VISIBLE

        val currentQuestion = questions[currentQuestionIndex]

        val correctColor = Color.parseColor("#4CAF50") // Green
        val wrongColor = Color.parseColor("#F44336") // Red

        if (selectedIndex == currentQuestion.correctAnswerIndex) {
            score++
            // Set selected option to green
            options[selectedIndex].backgroundTintList = ColorStateList.valueOf(correctColor)
            options[selectedIndex].setTextColor(Color.WHITE)
        } else {
            // Set selected option to red
            options[selectedIndex].backgroundTintList = ColorStateList.valueOf(wrongColor)
            options[selectedIndex].setTextColor(Color.WHITE)
            
            // Show correct option in green
            options[currentQuestion.correctAnswerIndex].backgroundTintList = ColorStateList.valueOf(correctColor)
            options[currentQuestion.correctAnswerIndex].setTextColor(Color.WHITE)
        }
    }
}
