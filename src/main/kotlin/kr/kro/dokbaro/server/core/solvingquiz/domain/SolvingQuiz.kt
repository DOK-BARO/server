package kr.kro.dokbaro.server.core.solvingquiz.domain

import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet

class SolvingQuiz(
	private val sheets: MutableMap<Long, AnswerSheet> = mutableMapOf(),
) {
	fun addSheet(
		questionId: Long,
		sheet: AnswerSheet,
	) {
		sheets[questionId] = sheet
	}

	fun getSheets() = sheets.toMap()
}