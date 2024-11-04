package kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto

import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import java.util.UUID

data class UpdateBookQuizCommand(
	val id: Long,
	val title: String,
	val description: String,
	val bookId: Long,
	val timeLimitSecond: Int?,
	val viewScope: AccessScope,
	val editScope: AccessScope,
	val studyGroupId: Long?,
	val questions: Collection<UpdateQuizQuestionCommand>,
	val modifierAuthId: UUID,
)

data class UpdateQuizQuestionCommand(
	val id: Long? = null,
	val content: String,
	val selectOptions: Collection<String> = emptyList(),
	val answerExplanationContent: String,
	val answerExplanationImages: Collection<String> = emptyList(),
	val answerType: QuizType,
	val answers: Collection<String> = emptyList(),
)