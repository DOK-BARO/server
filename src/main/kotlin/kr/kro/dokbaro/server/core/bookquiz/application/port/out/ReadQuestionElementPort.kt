package kr.kro.dokbaro.server.core.bookquiz.application.port.out

interface ReadQuestionElementPort {
	fun findSelectOptionBy(ids: Collection<Long>): Map<Long, Collection<String>>

	fun findAnswerExplanationImageBy(ids: Collection<Long>): Map<Long, Collection<String>>

	fun findAnswersBy(ids: Collection<Long>): Map<Long, Collection<String>>
}