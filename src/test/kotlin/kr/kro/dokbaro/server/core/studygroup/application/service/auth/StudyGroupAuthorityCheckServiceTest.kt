package kr.kro.dokbaro.server.core.studygroup.application.service.auth

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.common.exception.http.status4xx.default.DefaultForbiddenException
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindAllStudyGroupMembersUseCase
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMember
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMemberRole
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupMemberResult
import kr.kro.dokbaro.server.fixture.domain.dokbaroAdminFixture
import kr.kro.dokbaro.server.fixture.domain.dokbaroUserFixture
import kr.kro.dokbaro.server.fixture.domain.studyGroupFixture

class StudyGroupAuthorityCheckServiceTest :
	StringSpec({
		val findAllStudyGroupMembersUseCase: FindAllStudyGroupMembersUseCase = mockk()

		val studyGroupAuthorityCheckService = StudyGroupAuthorityCheckService(findAllStudyGroupMembersUseCase)

		"스터디 그룹 리더일 경우 그룹 삭제가 가능하다" {
			val leaderId = 100L
			val memberId = 200L

			every { findAllStudyGroupMembersUseCase.findAllStudyGroupMembers(any()) } returns
				listOf(
					StudyGroupMemberResult(
						groupId = 1,
						groupMemberId = 3,
						memberId = leaderId,
						nickname = "hello",
						role = StudyMemberRole.LEADER,
					),
					StudyGroupMemberResult(
						groupId = 1,
						groupMemberId = 3,
						memberId = memberId,
						nickname = "hello",
						role = StudyMemberRole.MEMBER,
					),
				)

			shouldNotThrow<DefaultForbiddenException> {
				studyGroupAuthorityCheckService.checkDeleteStudyGroup(dokbaroUserFixture(id = leaderId), 1)
			}

			shouldThrow<DefaultForbiddenException> {
				studyGroupAuthorityCheckService.checkDeleteStudyGroup(dokbaroUserFixture(id = memberId), 1)
			}

			shouldThrow<DefaultForbiddenException> {
				studyGroupAuthorityCheckService.checkDeleteStudyGroup(dokbaroUserFixture(id = 700), 1)
			}
		}

		"admin인 경우 그룹 삭제가 가능하다" {
			every { findAllStudyGroupMembersUseCase.findAllStudyGroupMembers(any()) } returns
				listOf(
					StudyGroupMemberResult(
						groupId = 1,
						groupMemberId = 3,
						memberId = 100,
						nickname = "hello",
						role = StudyMemberRole.MEMBER,
					),
				)

			shouldThrow<DefaultForbiddenException> {
				studyGroupAuthorityCheckService.checkDeleteStudyGroup(dokbaroUserFixture(), 1)
			}

			shouldNotThrow<DefaultForbiddenException> {
				studyGroupAuthorityCheckService.checkDeleteStudyGroup(dokbaroAdminFixture(), 1)
			}
		}

		"스터디에 전반적인 수적은 leader만 가능하다" {
			val studyGroup =
				studyGroupFixture(
					studyMembers =
						mutableSetOf(
							StudyMember(
								memberId = 1,
								role = StudyMemberRole.LEADER,
							),
							StudyMember(
								memberId = 2,
								role = StudyMemberRole.MEMBER,
							),
						),
				)

			shouldThrow<DefaultForbiddenException> {
				studyGroupAuthorityCheckService.checkUpdateStudyGroup(dokbaroUserFixture(id = 3), studyGroup)
			}
			shouldThrow<DefaultForbiddenException> {
				studyGroupAuthorityCheckService.checkUpdateStudyGroup(dokbaroUserFixture(id = 2), studyGroup)
			}
			shouldNotThrow<DefaultForbiddenException> {
				studyGroupAuthorityCheckService.checkUpdateStudyGroup(dokbaroUserFixture(id = 1), studyGroup)
			}
		}

		"스터디 탈퇴는 본인 혹은 리더만 가능하다" {
			val studyGroup =
				studyGroupFixture(
					studyMembers =
						mutableSetOf(
							StudyMember(
								memberId = 1,
								role = StudyMemberRole.LEADER,
							),
							StudyMember(
								memberId = 2,
								role = StudyMemberRole.MEMBER,
							),
						),
				)

			shouldThrow<DefaultForbiddenException> {
				studyGroupAuthorityCheckService.checkWithdrawMember(
					dokbaroUserFixture(id = 3),
					studyGroup,
					targetMemberId = 2,
				)
			}
			shouldThrow<DefaultForbiddenException> {
				studyGroupAuthorityCheckService.checkWithdrawMember(
					dokbaroUserFixture(id = 2),
					studyGroup,
					targetMemberId = 1,
				)
			}
			shouldNotThrow<DefaultForbiddenException> {
				studyGroupAuthorityCheckService.checkWithdrawMember(
					dokbaroUserFixture(id = 2),
					studyGroup,
					targetMemberId = 2,
				)
			}
			shouldNotThrow<DefaultForbiddenException> {
				studyGroupAuthorityCheckService.checkWithdrawMember(
					dokbaroUserFixture(id = 1),
					studyGroup,
					targetMemberId = 2,
				)
			}
		}
	})