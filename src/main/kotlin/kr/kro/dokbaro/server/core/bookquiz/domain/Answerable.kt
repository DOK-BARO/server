package kr.kro.dokbaro.server.core.bookquiz.domain

interface Answerable {
	fun isCorrect(sheet: AnswerSheet): Boolean

	fun getType(): QuizType
}