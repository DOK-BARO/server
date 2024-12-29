package kr.kro.dokbaro.server.core.bookquiz.application.service.auth

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.common.exception.http.status4xx.default.DefaultForbiddenException
import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindAllStudyGroupMembersUseCase
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMemberRole
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupMemberResult
import kr.kro.dokbaro.server.fixture.domain.bookQuizFixture
import kr.kro.dokbaro.server.fixture.domain.dokbaroAdminFixture
import kr.kro.dokbaro.server.fixture.domain.dokbaroUserFixture

class BookQuizAuthorityCheckServiceTest :
	StringSpec({
		val findAllStudyGroupMembersUseCase: FindAllStudyGroupMembersUseCase = mockk()

		val bookQuizAuthorityCheckService = BookQuizAuthorityCheckService(findAllStudyGroupMembersUseCase)

		afterEach {
			clearAllMocks()
		}

		"스터디 그룹용 퀴즈일 때, 맴버가 스터디 그룹에 속해있지 않으면 생성을 제한합니다" {
			val groupId = 2L
			val memberId = 5L
			every { findAllStudyGroupMembersUseCase.findAllStudyGroupMembers(groupId) } returns
				listOf(
					StudyGroupMemberResult(
						groupId = groupId,
						groupMemberId = 3,
						memberId = memberId,
						nickname = "hello",
						role = StudyMemberRole.MEMBER,
					),
				)

			shouldThrow<DefaultForbiddenException> {
				bookQuizAuthorityCheckService.checkCreateBookQuiz(
					user = dokbaroUserFixture(id = 77),
					studyGroupId = groupId,
				)
			}

			shouldNotThrow<DefaultForbiddenException> {
				bookQuizAuthorityCheckService.checkCreateBookQuiz(
					user = dokbaroUserFixture(id = 77),
					studyGroupId = null,
				)
			}

			shouldNotThrow<DefaultForbiddenException> {
				bookQuizAuthorityCheckService.checkCreateBookQuiz(
					user = dokbaroUserFixture(id = memberId),
					studyGroupId = groupId,
				)
			}
		}

		"퀴즈 수정 시 ADMIN 권한인 경우 수정을 허용한다" {
			val memberId = 7L
			val quiz = bookQuizFixture(studyGroupId = null, creatorId = 1)

			shouldNotThrow<DefaultForbiddenException> {
				bookQuizAuthorityCheckService.checkUpdateBookQuiz(dokbaroAdminFixture(), quiz)
			}

			shouldThrow<DefaultForbiddenException> {
				bookQuizAuthorityCheckService.checkUpdateBookQuiz(dokbaroUserFixture(id = memberId), quiz)
			}
		}

		"퀴즈 수정 시 본인이 작성한 퀴즈인 경우 수정을 허용한다" {
			val memberId = 7L
			val quiz = bookQuizFixture(studyGroupId = null, creatorId = memberId)

			shouldNotThrow<DefaultForbiddenException> {
				bookQuizAuthorityCheckService.checkUpdateBookQuiz(dokbaroUserFixture(id = memberId), quiz)
			}

			shouldThrow<DefaultForbiddenException> {
				bookQuizAuthorityCheckService.checkUpdateBookQuiz(dokbaroUserFixture(id = 777), quiz)
			}
		}

		"퀴즈 수정 시 퀴즈 수정 scope가 스터디 그룹이면서, 스터디 그룹원인 경우 수정을 허용한다" {
			val memberId = 7L
			val groupId = 4L
			every { findAllStudyGroupMembersUseCase.findAllStudyGroupMembers(groupId) } returns
				listOf(
					StudyGroupMemberResult(
						groupId = groupId,
						groupMemberId = 3,
						memberId = memberId,
						nickname = "hello",
						role = StudyMemberRole.MEMBER,
					),
				)

			val quiz = bookQuizFixture(studyGroupId = groupId, creatorId = 777, editScope = AccessScope.STUDY_GROUP)

			shouldNotThrow<DefaultForbiddenException> {
				bookQuizAuthorityCheckService.checkUpdateBookQuiz(dokbaroUserFixture(id = memberId), quiz)
			}

			val creatorOnlyQuiz =
				bookQuizFixture(studyGroupId = groupId, creatorId = 777, editScope = AccessScope.CREATOR)
			shouldThrow<DefaultForbiddenException> {
				bookQuizAuthorityCheckService.checkUpdateBookQuiz(dokbaroUserFixture(id = memberId), creatorOnlyQuiz)
			}

			shouldThrow<DefaultForbiddenException> {
				bookQuizAuthorityCheckService.checkUpdateBookQuiz(dokbaroUserFixture(id = 7797), quiz)
			}
		}

		"퀴즈 수정 시 퀴즈 수정 scope가 전체인 경우 경우 수정을 허용한다" {
			val quiz = bookQuizFixture(creatorId = 777, editScope = AccessScope.EVERYONE)

			shouldNotThrow<DefaultForbiddenException> {
				bookQuizAuthorityCheckService.checkUpdateBookQuiz(dokbaroUserFixture(id = 1234), quiz)
			}
		}

		"본인이 직접 만든 퀴즈인 경우 삭제를 허용한다" {
			val memberId = 5L
			val quiz = bookQuizFixture(creatorId = memberId)

			shouldNotThrow<DefaultForbiddenException> {
				bookQuizAuthorityCheckService.checkDeleteBookQuiz(dokbaroUserFixture(id = memberId), quiz)
			}

			shouldThrow<DefaultForbiddenException> {
				bookQuizAuthorityCheckService.checkDeleteBookQuiz(dokbaroUserFixture(id = 1234), quiz)
			}
		}

		"admin인 경우 삭제를 허용한다" {
			val memberId = 5L
			val quiz = bookQuizFixture(creatorId = memberId)

			shouldNotThrow<DefaultForbiddenException> {
				bookQuizAuthorityCheckService.checkDeleteBookQuiz(dokbaroAdminFixture(), quiz)
			}

			shouldThrow<DefaultForbiddenException> {
				bookQuizAuthorityCheckService.checkDeleteBookQuiz(dokbaroUserFixture(id = 7777), quiz)
			}
		}
	})