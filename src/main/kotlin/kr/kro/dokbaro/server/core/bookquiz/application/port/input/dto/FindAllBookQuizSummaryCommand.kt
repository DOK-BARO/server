package kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto

data class FindAllBookQuizSummaryCommand(
	val bookId: Long? = null,
	val studyGroup: StudyGroup = StudyGroup(),
) {
	data class StudyGroup(
		val all: Boolean = true,
		val id: Long? = null,
	)
}