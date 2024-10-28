package kr.kro.dokbaro.server.core.bookquiz.domain

interface Gradable {
	fun isCorrect(sheet: AnswerSheet): Boolean

	fun getType(): QuizType

	fun getAnswers(): Collection<String>
}