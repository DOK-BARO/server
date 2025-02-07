package kr.kro.dokbaro.server.core.bookquiz.adapter.input.web.dto

import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.UpdateBookQuizCommand
import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope

data class UpdateBookQuizRequest(
	val title: String,
	val description: String,
	val bookId: Long,
	val timeLimitSecond: Int?,
	val viewScope: AccessScope,
	val editScope: AccessScope,
	val studyGroupId: Long?,
	val questions: Collection<UpdateBookQuizCommand.Question>,
	val temporary: Boolean,
)