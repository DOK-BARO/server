package kr.kro.dokbaro.server.core.bookquiz.application.service.auth

import kr.kro.dokbaro.server.common.annotation.AuthorityCheckService
import kr.kro.dokbaro.server.common.exception.http.status4xx.default.DefaultForbiddenException
import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope
import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz
import kr.kro.dokbaro.server.core.member.domain.Role
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindAllStudyGroupMembersUseCase
import kr.kro.dokbaro.server.security.details.DokbaroUser

@AuthorityCheckService
class BookQuizAuthorityCheckService(
	private val findAllStudyGroupMembersUseCase: FindAllStudyGroupMembersUseCase,
) {
	/**
	 * 스터디 그룹용 퀴즈일 때, 맴버가 스터디 그룹에 속해있지 않으면 생성을 제한합니다.
	 */
	fun checkCreateBookQuiz(
		user: DokbaroUser,
		studyGroupId: Long?,
	) {
		studyGroupId?.let { groupId ->
			if (findAllStudyGroupMembersUseCase.findAllStudyGroupMembers(groupId).none { it.memberId == user.id }) {
				throw DefaultForbiddenException()
			}
		}
	}

	/**
	 * 다음인 경우 퀴즈 수정이 가능합니다.
	 * 1. 퀴즈 수정의 권한이 전체인 경우
	 * 2. ADMIN 권한인 경우
	 * 3. 본인인 경우
	 * 4. 퀴즈 수정 scope가 스터디 그룹이면서, 스터디 그룹원인 경우.
	 */
	fun checkUpdateBookQuiz(
		user: DokbaroUser,
		bookQuiz: BookQuiz,
	) {
		if (bookQuiz.editScope == AccessScope.EVERYONE) {
			return
		}

		if (user.hasRole(Role.ADMIN)) {
			return
		}

		if (user.id == bookQuiz.creatorId) {
			return
		}

		if (bookQuiz.studyGroupId?.let { studyGroupId ->
				bookQuiz.editScope == AccessScope.STUDY_GROUP &&
					findAllStudyGroupMembersUseCase
						.findAllStudyGroupMembers(studyGroupId)
						.any { it.memberId == user.id }
			} == true
		) {
			return
		}

		throw DefaultForbiddenException()
	}

	/**
	 * 본인이 직접 만든 퀴즈이거나, ADMIN만 퀴즈 삭제가 가능합니다.
	 */
	fun checkDeleteBookQuiz(
		user: DokbaroUser,
		bookQuiz: BookQuiz,
	) {
		if (!user.hasRole(Role.ADMIN) && user.id != bookQuiz.creatorId) {
			throw DefaultForbiddenException()
		}
	}
}