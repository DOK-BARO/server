package kr.kro.dokbaro.server.core.bookquiz.application.port.out.dto

import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType

data class BookQuizDetailQuestions(
	val id: Long,
	val title: String,
	val description: String,
	val bookId: Long,
	val questions: Collection<Question>,
	val studyGroupId: Long?,
	val timeLimitSecond: Int?,
	val viewScope: AccessScope,
	val editScope: AccessScope,
) {
	data class Question(
		val id: Long,
		val content: String,
		val answerExplanationContent: String,
		val answerType: QuizType,
	)
}