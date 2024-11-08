package kr.kro.dokbaro.server.core.solvingquiz.query

import java.time.LocalDateTime

data class StudyGroupSolveSummary(
	val id: Long,
	val solvedAt: LocalDateTime,
	val book: BookSummary,
	val quiz: QuizSummary,
)

data class BookSummary(
	val id: Long,
	val title: String,
	val imageUrl: String,
)

data class QuizSummary(
	val id: Long,
	val title: String,
	val creator: QuizCreator,
	val createdAt: LocalDateTime,
	val contributors: Collection<QuizContributor>,
)

data class QuizCreator(
	val id: Long,
	val nickname: String,
	val profileImageUrl: String?,
)

data class QuizContributor(
	val id: Long,
	val nickname: String,
	val profileImageUrl: String?,
)