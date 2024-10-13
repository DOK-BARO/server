package kr.kro.dokbaro.server.core.solvingquiz.domain

import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet

class SolvingQuiz(
	val playerId: Long,
	val quizId: Long,
	private val sheets: MutableMap<Long, AnswerSheet> = mutableMapOf(),
	val id: Long = Constants.UNSAVED_ID,
) {
	fun addSheet(
		questionId: Long,
		sheet: AnswerSheet,
	) {
		sheets[questionId] = sheet
	}

	fun getSheets() = sheets.toMap()
}