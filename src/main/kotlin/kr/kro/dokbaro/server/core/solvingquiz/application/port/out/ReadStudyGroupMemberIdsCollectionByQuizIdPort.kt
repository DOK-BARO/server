package kr.kro.dokbaro.server.core.solvingquiz.application.port.out

fun interface ReadStudyGroupMemberIdsCollectionByQuizIdPort {
	fun findAllGroupMemberIdsByQuizId(quizId: Long): Collection<Long>
}