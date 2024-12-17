package kr.kro.dokbaro.server.core.quizquestionreport.domain

import kr.kro.dokbaro.server.common.constant.Constants

data class QuizQuestionReport(
	val id: Long = Constants.UNSAVED_ID,
	val questionId: Long,
	val reporterId: Long,
	val content: String,
)