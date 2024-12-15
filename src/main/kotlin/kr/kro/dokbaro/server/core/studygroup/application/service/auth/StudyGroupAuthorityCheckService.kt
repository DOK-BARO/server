package kr.kro.dokbaro.server.core.studygroup.application.service.auth

import kr.kro.dokbaro.server.common.annotation.AuthorityCheckService
import kr.kro.dokbaro.server.common.exception.http.status4xx.default.DefaultForbiddenException
import kr.kro.dokbaro.server.core.member.domain.Role
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindAllStudyGroupMembersUseCase
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMemberRole
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupMemberResult
import kr.kro.dokbaro.server.security.details.DokbaroUser

@AuthorityCheckService
class StudyGroupAuthorityCheckService(
	private val findAllStudyGroupMembersUseCase: FindAllStudyGroupMembersUseCase,
) {
	/**
	 * study 그룹은 리더 혹은 ADMIN만 가능합니다.
	 */
	fun checkDeleteStudyGroup(
		user: DokbaroUser,
		studyGroupId: Long,
	) {
		val members: Collection<StudyGroupMemberResult> =
			findAllStudyGroupMembersUseCase.findAllStudyGroupMembers(
				studyGroupId,
			)

		if (!user.hasRole(Role.ADMIN) &&
			members.none {
				it.role == StudyMemberRole.LEADER &&
					it.memberId == user.id
			}
		) {
			throw DefaultForbiddenException()
		}
	}
}