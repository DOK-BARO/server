package kr.kro.dokbaro.server.core.solvingquiz.application.service.auth

import kr.kro.dokbaro.server.common.annotation.AuthorityCheckService
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.ReadStudyGroupMemberIdsCollectionByQuizIdPort
import kr.kro.dokbaro.server.core.solvingquiz.application.service.exception.StudyQuizForbiddenException

@AuthorityCheckService
class SolvingQuizAuthorityCheckService(
	private val readStudyGroupMemberIdsCollectionByQuizIdPort: ReadStudyGroupMemberIdsCollectionByQuizIdPort,
) {
	fun checkSolvingQuiz(
		playerId: Long,
		quizId: Long,
	) {
		val members: Collection<Long> =
			readStudyGroupMemberIdsCollectionByQuizIdPort.findAllGroupMemberIdsByQuizId(quizId)

		if (members.none { it == playerId }) {
			throw StudyQuizForbiddenException()
		}
	}
}