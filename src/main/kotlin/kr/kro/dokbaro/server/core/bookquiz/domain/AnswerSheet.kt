package kr.kro.dokbaro.server.core.bookquiz.domain

import kr.kro.dokbaro.server.core.bookquiz.domain.exception.EmptyAnswerSheetException

data class AnswerSheet(
	val answer: Collection<String>,
) {
	init {
		if (answer.isEmpty()) {
			throw EmptyAnswerSheetException()
		}
	}
}