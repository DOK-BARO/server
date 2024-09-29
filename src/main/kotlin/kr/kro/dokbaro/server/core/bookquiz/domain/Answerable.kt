package kr.kro.dokbaro.server.core.bookquiz.domain

interface Answerable {
	fun match(sheet: AnswerSheet): Boolean

	fun getType(): QuizType

	fun getAnswers(): Collection<String>
}